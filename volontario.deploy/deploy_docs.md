# Volontario - dokumentacja wdrożeniowa

## 1. Wdrożenie za pomocą Dockera (rekomendowane)

System Volontario był projektowany pod konteneryzację, a więc domyślną i najprostszą opcją wdrożenia jest Docker Compose.

### 1.1. Wymagania

Do wdrożenia Volontario potrzebny jest serwer lub maszyna wirtualna z systemem operacyjnym Linux, posiadająca 1GB ramu i 1 vCPU (przy zaciąganiu obrazów z zewnętrznego registry) lub 4GB ramu i 2 vCPU (przy budowaniu obrazów bezpośrednio na serwerze). Serwer powinien mieć zainstalowanego [Dockera](https://docs.docker.com/engine/install/) i [Docker Compose](https://docs.docker.com/compose/install/linux/). Powinien też mieć otwarte na ruch porty 80 i 443, a także 9001.

Do tego potrzebna jest również skrzynka mailowa, pod którą można podłączyć system, by mógł wysyłać maile do użytkowników.

Deweloperska instancje Volontario była wdrażana na serwerze z Dockerem w wersji 23.0.5, Docker Compose w wersji 2.17.3. Systemem na serwerze było Ubuntu 22 LTS i pod tą dystrybucję przygotowany jest playbook Ansible (pod ścieżką `./volontario.deploy/ansible/configure-volontario-vm.yaml`), który instaluje Dockera i Docker Compose, [Certbota](https://certbot.eff.org/) generującego certyfikaty SSL, a także tworzy folder na backupy pod ścieżką `/var/volontariodumps`.

### 1.2. Konfiguracja

Pliki wdrożeniowe znajdują się w [repozytorium](https://git.wmi.amu.edu.pl/s464956/volontario) w folderze `volontario.deploy`. 

Plik `docker-compose.yaml`, używany do uruchomienia aplikacji Volontario, zaciąga konfigurację z pliku `.env`. Aby go utworzyć należy utworzyć kopię pliku `.env.tpl` i wypełnić ją odpowiednimi wartościami. W szczególności należy zmienić:

```bash
# username i hasło dla bazy danych
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# folder na hoście, do którego mają być zapisywane backupy
POSTGRES_BACKUP_DIR=

# konfiguracja serwera mailowego
MAILSERVER_HOSTNAME=smtp.wmi.amu.edu.pl
MAILSERVER_PORT=587
MAILSERVER_SMTP_AUTH=true
MAILSERVER_TLS_ENABLE=true

# username i hasło skrzynki mailowej, która będzie używana przez system
MAILSERVER_USERNAME=
MAILSERVER_PASSWORD=

# adres mailowy, z którego będą wysyłane wiadomości
VOLONTARIO_EMAIL=

# ścieżka do certyfikatów ssl (/etc/letsencrypt w przypadku certyfikatów z certbota)
NGINX_CERT_PATH=/tmp

# default.conf.template dla wdrożenia bez https, default.conf.template.ssl dla obsługi https
NGINX_CONFIG_TEMPLATE=default.conf.template

# domena, pod którą będzie działać system
VOLONTARIO_DOMAIN=localhost

# http lub https, w zależności z czego będzie korzystać aplikacja
SCHEMA=http

# environment angularowy, z którego będzie korzystać obraz frontendu
# nie ma znaczenia przy budowie obrazu niezależnie od pliku docker-compose.yaml
# wartości 'localdev' dla localhosta i 'production' przy wdrożeniu
CLIENT_ENVIRONMENT=

# email, na który przychodzą maile z prośbą o weryfikację instytucji
VOLONTARIO_MODERATOR=example@example.pl

# adresy email (oddzielone przecinkami), na które przychodzą maile ze zgłoszeniami błędów w systemie
MAINTENANCE_EMAILS=example1@example.pl,example2@example.pl

# username i hasło dla MinIO
MINIO_ROOT_USER=volontario
MINIO_ROOT_PASSWORD=volontario
```

Przed budową obrazów Dockerowych, w szczególności obrazu frontendu, należy również zmienić url w pliku `volontario.client/volontario.client.application/volontario/src/environments/environment.prod.ts` na domenę podpiętą pod serwer, na którym będzie wdrażany system.

```js
export const environment = {
  production: true,
  apiUrl: 'https://dev.volontario.me/api', // url do zmiany
};
```

Dodatkowo, jeżeli system ma korzystać z https i certyfikatów SSL innych niż te z Certbota, należy zmienić ścieżki do certyfikatu w konfiguracji NGINXa w `volontario.deploy/nginx/templates/default.conf.template.ssl`, a w razie potrzeby także podpięcie wolumenu NGINXa z konfiguracją w `docker-compose.yaml`

Certyfikat za pomocą Certbota, można wygenerować za pomocą komendy `certbot certonly`.

### 1.3. Uruchomienie systemu

System można wdrożyć budując obrazy bezpośrednio na serwerze (wymaga minimum 4GB ramu i 2 vCPU), lub budując obrazy na innym komputerze i wypychając je do Dockerowego Registry, do którego serwer będzie miał dostęp (wymaga minimum 1GB ramu i 1 vCPU)

#### 1.3.1 Budowa obrazów bezpośrednio na serwerze

Po konfiguracji, Volontario można bardzo łatwo uruchomić na serwerze za pomocą poniższych komend:

```bash
# uruchomienie systemu z budową obrazów
# za pierwszym razem lub po zmianach w repozytorium
docker compose up -d --build

# uruchomienie systemu bez budowy obrazów
docker compose up -d
```

Wyłączyć system można za pomocą komend

```bash
# wyłączenie zostawiając wolumeny
docker compose down

# wyłączenie wraz z usunięciem wolumenów
docker compose down -v
```

#### 1.3.2 Budowa obrazów i wypchnięcie ich do registry

W tym przypadku, na komputerze budującym obrazy potrzebny jest tylko krok konfiguracyjny ze zmianą url w pliku `volontario.client/volontario.client.application/volontario/src/environments/environment.prod.ts`, natomiast reszta kroków jest do wykonania na serwerze, na którym system będzie wdrożony.

Obrazy Volontario można zbudować i wypchnąć do registry za pomocą następujących komend:

```bash
# budowa obrazów
cd volontario.server
docker build -t ${REGISTRY_NAME}/volontario-back:latest . 

cd ../volontario.client/volontario.client.application/volontario
docker build -t ${REGISTRY_NAME}/volontario-front:latest --build-arg CLIENT_ENVIRONMENT=production .

# wypchnięcie do registry
docker push ${REGISTRY_NAME}/volontario-back:latest
docker push ${REGISTRY_NAME}/volontario-front:latest
```

Przy budowie konkretnej wersji Volontario z konkretnego tagu, należy też zdefiniować wersję systemu jako argument przy budowaniu obrazu:

```bash
# budowa obrazów
cd volontario.server
docker build -t ${REGISTRY_NAME}/volontario-back:${VERSION} --build-arg VOLONTARIO_VERSION=${VERSION} . 

cd ../volontario.client/volontario.client.application/volontario
docker build -t ${REGISTRY_NAME}/volontario-front:${VERSION} --build-arg CLIENT_ENVIRONMENT=production --build-arg VOLONTARIO_VERSION=${VERSION} .

# wypchnięcie do registry
docker push ${REGISTRY_NAME}/volontario-back:${VERSION}
docker push ${REGISTRY_NAME}/volontario-front:${VERSION}
```

Następnie należy dopisać nazwę repozytorium i ewentualnie numer wersji do definicji obrazów w pliku `docker-compose.yaml`.

Jeżeli maszyna posiada dostęp do registry, na które zostały wypchnięte obrazy, to przy uruchamianiu systemu Docker sam je zaciągnie. Uruchomienie systemu i jego wyłączenie następuje za pomocą poniższych komend:

```bash
# uruchomienie systemu
docker compose up -d

# uruchomienie systemu na nowo z zaciągnięciem najnowszych obrazów
docker compose pull && docker compose up -d

# wyłączenie zostawiając wolumeny
docker compose down

# wyłączenie wraz z usunięciem wolumenów
docker compose down -v
```

### 1.4. Backupy

Backupy są w naszej aplikacji rozwiązane po stronie hosta. Baza danych podłącza podaną w pliku `.env` ścieżkę na hoście (domyślnie `/var/volontariodumps`) pod ścieżkę `/var/volontariodumps` w kontenerze.

```bash
docker compose -p volontario exec volontario-db sh -c "pg_dump --clean -U {nazwa_uzytkownika} volontario | gzip > /var/volontariodumps/{nazwa_backupu}.gz"
```

Backupy można też zautomatyzować za pomocą crona. Poniżej przykładowa konfiguracja crona, którego stworzyliśmy na potrzeby backupów.

```bash
# codzienne backupy
0 4 * * * docker compose -p volontario exec volontario-db sh -c "pg_dump --clean -U {nazwa_uzytkownika} volontario | gzip > /var/volontariodumps/volontario-backup-daily-$(date +"%d-%m-%Y").gz"
# cotygodniowe backupy
0 23 * * 5 docker compose -p volontario exec volontario-db sh -c "pg_dump --clean -U {nazwa_uzytkownika} volontario | gzip > /var/volontariodumps/volontario-backup-weekly-$(date +"%d-%m-%Y").gz"
# czyszczenie starych backupów
30 23 * * 5 docker compose -p volontario exec volontario-db sh -c "ls /var/volontariodumps/*daily* -t | tail -n+6 | xargs -I {} rm {} && ls /var/volontariodumps/*weekly* -t | tail -n+4 | xargs -I {} rm {}"
```

Wybrany backup możemy przywrócić za pomocą komendy:

```bash
docker compose -p volontario exec volontario-db sh -c "gunzip -c /var/volontariodumps/{nazwa_backupu}.gz | psql -U {nazwa_uzytkownika} volontario"
```

### 1.5. Troubleshooting

Stan kontenerów można podejrzeć za pomocą komendy:

```bash
docker compose ps
```

Logi aplikacji można podejrzeć za pomocą komend:

```bash
# wyświetlenie logów całej aplikacji
docker compose -p volontario logs

# śledzenie logów na bieżąco całej aplikacji
docker compose -p volontario logs -f

# wyświetlenie logów pojedynczego komponentu
docker compose -p volontario logs {nazwa_komponentu}
# np. docker compose -p volontario logs volontario-back

# śledzenie logów na bieżąco pojedynczego komponentu
docker compose -p volontario logs -f {nazwa_komponentu}
```

Podczas utrzymywania Volontario należy zwracać uwagę na miejsce na dysku, w szczególności w folderze z backupami oraz w `/var/lib/docker`, gdzie są przechowywane wszystkie zasoby Dockerowe, w tym dane przechowywane w wolumenach MinIO oraz bazy danych.

## 2. Wdrożenie za pomocą Kubernetesa

Biorąc pod uwagę różne opcje wdrożenia aplikacji, przygotowana została również opcja wdrożenia aplikacji na klaster Kubernetes za pomocą Helm charta

### 2.1. Wymagania

Do wdrożenia potrzebny jest działający klaster [Kubernetes](https://kubernetes.io/pl/).

Na komputerze, który będzie budować obrazy dockerowe, potrzebny jest [Docker](https://docs.docker.com/engine/install/) (do budowy obrazów), [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl) oraz [Helm CLI](https://helm.sh/docs/intro/install/), a także [skonfigurowany dostęp do klastra](https://kubernetes.io/docs/concepts/configuration/organize-cluster-access-kubeconfig/).

Do tego potrzebna jest również skrzynka mailowa, pod którą można podłączyć system, by mógł wysyłać maile do użytkowników.

### 2.2. Konfiguracja

Helm chart znajduje się w [repozytorium](https://git.wmi.amu.edu.pl/s464956/volontario) w folderze `volontario.deploy/chart/volontario`. 

Aby wdrożyć system za pomocą Helma, należy najpierw skonfigurować url w `volontario.client/volontario.client.application/volontario/src/environments/environment.prod.ts` (analogicznie jak w punkcie 1.2) oraz zbudować obrazy i wypchnąć do zewnętrznego repozytorium (analogicznie jak w punkcie 1.3.2)

Następnie należy stworzyć kopię pliku values.yaml i wypełnić ją odpowiednimi wartościami. W szczególności należy wypełnić:

```yaml
volontarioGateway:
...
  ingress:
    ...
    # url wykorzystywany przez ingress
    host: volontario.192.168.49.2.nip.io 
...
volontarioFront:
  image:
    # definicja obrazu
    repository: volontario-front
    pullPolicy: Always
    tag: "latest"
...
volontarioBack:
  image:
    # definicja obrazu
    repository: volontario-back
    pullPolicy: Always
    tag: "latest"
...
env:
  # username i hasło dla bazy danych
  dbUsername: postgres
  dbPassword: postgres

  # username i hasło dla MinIO
  minioUsername: volontario
  minioPassword: volontario

  # konfiguracja serwera mailowego
  mailserverUsername: volontario@example.com
  mailserverPassword: volontario
  mailserverHostname: smtp.example.com
  mailserverPort: 587
  mailserverSMTPAuth: "true"
  mailserverTLSEnable: "true"

  # adres mailowy, z którego będą wysyłane wiadomości
  volontarioEmailAddress: volontario@example.com

  # email, na który przychodzą maile z prośbą o weryfikację instytucji
  volontarioModerator: moderator@example.com

  # adresy email (oddzielone przecinkami), na które przychodzą maile ze zgłoszeniami błędów w systemie
  maintenanceEmails: "maintenance1@example.com,maintenance2@example.com"
```

### 2.3. Uruchomienie systemu

Po konfiguracji, Volontario można wdrożyć na klaster Kubernetes za pomocą poniższej komendy:

```bash
cd volontario.deploy/chart/volontario
helm upgrade -f {plik_z_wartosciami.yaml} --install volontario .
```

### 2.4. Troubleshooting

Stan kontenerów można podejrzeć za pomocą komendy:

```bash
kubectl get pod
```

Logi aplikacji można podejrzeć za pomocą komend:

```bash
# wyświetlenie logów pojedynczego komponentu
kubectl logs {nazwa_poda}

# śledzenie na bieżąco logów pojedynczego komponentu
kubectl logs -f {nazwa_poda}
```
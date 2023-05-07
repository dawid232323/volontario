# Volontario - Deployment

## Docker

### Full deploy

You can run the whole project with command `docker-compose up --build`

Before deploying, it may be necessary to set appropriate url in frontend, under `volontario.client/volontario.client.application/volontario/src/app/core/service/`

### Development environments

There are also several docker compose files, that are created for development purposes. These files deploy only parts of the whole project, lack nginx proxy, and have open ports (in contrary to the deployment environment, where the only container with open ports is nginx).

#### Volontario without frontend

For developing frontend, you can run `docker-compose -f docker-compose-dev-back-only.yaml up --build`

This compose contains only database, backend container and postfix.

#### Volontario without backend

For developing frontend, you can run `docker-compose -f docker-compose-dev-front-only.yaml up --build`

This compose contains only database, frontend container and postfix.

Note that it may be necessary to change url in volontario.client/volontario.client.application/volontario/src/environments/environment.prod.ts

#### Volontario DB and Postfix only

If needed, you can run `docker-compose -f docker-compose-db-only.yaml up --build`

This compose contains only database and postfix.

#### Volontario full dev

If needed, you can run `docker-compose -f docker-compose-dev.yaml up --build`

This compose contains all containers (including nginx), but they have open ports.

## Ansible

You can run an Ansible playbook in 'ansible' folder to quickly install Docker and Docker Compose on remote VM running Ubuntu 22.04 LTS.

`ansible-playbook -i hosts configure-docker-vm.yaml`
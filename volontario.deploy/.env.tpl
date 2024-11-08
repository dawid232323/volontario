POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_HOSTNAME=volontario-db
POSTGRES_BACKUP_DIR=/tmp # placeholder for local deployment, /var/volontariodumps on server configured with ansible

DEMO_DATA=true

MAILSERVER_HOSTNAME=smtp.wmi.amu.edu.pl # to change when we get volontario email from UAM
MAILSERVER_PORT=587
MAILSERVER_SMTP_AUTH=true
MAILSERVER_TLS_ENABLE=true

MAILSERVER_USERNAME=
MAILSERVER_PASSWORD=
VOLONTARIO_EMAIL=

NGINX_CERT_PATH=/tmp # placeholder for local deployment
NGINX_CONFIG_TEMPLATE=default.conf.template # default.conf.template.ssl for ssl
VOLONTARIO_DOMAIN=localhost
SCHEMA=http # either http or https

# necessary to set when deploying to dev/prod without prebuilt images, otherwise handled by default values in docker compose
CLIENT_ENVIRONMENT=

# insert your own email for testing purposes
VOLONTARIO_MODERATOR=s464956@wmi.amu.edu.pl

MAINTENANCE_EMAILS=micmus4@st.amu.edu.pl,pawros1@st.amu.edu.pl,mikmum@st.amu.edu.pl,dawpyl@st.amu.edu.pl

MINIO_HOST_NAME=http://volontario-minio:9000
MINIO_ROOT_USER=volontario
MINIO_ROOT_PASSWORD=volontario
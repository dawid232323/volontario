# Volontario - Deployment

## Docker

### Full deploy

You can run the whole project with command `docker-compose up`

Before deploying, it's probably necessary to set appropriate url in frontend, under `volontario.client/volontario.client.application/volontario/src/app/core/service/`

### Database

For development purposes, you can run database with command `docker-compose -f docker-compose-db-only.yaml up`

## Ansible

You can run an Ansible playbook in 'ansible' folder to quickly install Docker and Docker Compose on remote VM running Ubuntu 22.04 LTS.

`ansible-playbook -i hosts configure-vm.yaml`
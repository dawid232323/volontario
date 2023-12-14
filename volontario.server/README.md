# Volontario backend server

## Recommended IDE
We recommend using IntelliJ Idea during the development process, especially from version 2023.1 and higher.

## Requirements
To build and run this application you will need Docker daemon running in the background, **Maven** with version **3.6** or higher 
and **Java 17** installed on your system.

## Setup
First of all make sure that you have all environment variables set. If not please follow **deploy_docs.md** in **volontario.deploy**.

## Run server with docker

To start volontario backend on the docker environment move to the **volontario.deploy** directory and run
```bash
docker compose -f docker-compose-dev-back-only up -d --build
```

It will build docker images from scratch and start them. 
This step is simpler than running application from the IntelliJ Idea but is quite more time complex, 
because building the image takes much longer then starting the app from IntelliJ and may be too much time-consuming 
during standard development process 

## Run server with IntelliJ

On startup IntelliJ should detect run configuration from the **uam.volontario.VolontarioInitializer** class.
You need to modify this run configuration by adding all environment variables used by the spring boot application.
All the used variables are specified in **application.properties** file (e.g. POSTGRES_HOSTNAME).

To start database and amazon storage system move to the **volontario.deploy** directory and run
```bash
docker compose -f docker-compose-dev-back-only up -d volontario-db;
docker compose -f docker-compose-dev-back-only up -d volontario-minio;
```

Then check your docker desktop app or run 
```bash
docker ps
```

There should be two containers running: volontario-db and volontario-minio.
When this step is completed you can simply run previously prepared IntelliJ Idea configuration.
Application will be compiled and run for you.
If there are any issues during server startup please follow error messages. It's usually caused by incomplete environment variables configuration.
### Important note
Please keep in mind that with this configuration volontario backend runs outside docker network, so all environment variables
that store other services addresses should point to localhost not docker network address (so MINIO_HOST_NAME=http://volontario-minio:9000 should be replaced with MINIO_HOST_NAME=localhost:9000)
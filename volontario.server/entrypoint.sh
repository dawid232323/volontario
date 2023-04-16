#!/bin/bash

export PGPASSWORD="$POSTGRES_PASSWORD"

sleep 10 # give database some time to fully deploy

if ! psql -h volontario-db -U "$POSTGRES_USER" -l | grep -wq "volontario"; then
    psql -h volontario-db -U "$POSTGRES_USER" -c "CREATE DATABASE volontario;"
    psql -h volontario-db -U $POSTGRES_USER -d volontario -f /data/database.sql
    psql -h volontario-db -U $POSTGRES_USER -d volontario -f /data/basic_data.sql

    # if [ "${DEMO_DATA}" = "true" ]; then
    #     # fill database with example data
    #     # psql -h volontario-db -U $POSTGRES_USER -d volontario -f demodata.sql
    # fi
fi

java -jar /volontario-server.jar
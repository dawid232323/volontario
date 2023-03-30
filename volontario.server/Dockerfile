# build .jar file
FROM maven:3-eclipse-temurin-17-alpine AS MAVEN_BUILD
COPY ./ ./
# skipping tests because db connection test fails and prevents building .jar
RUN mvn clean package -Dmaven.test.skip

# using eclipse image, as the official openjdk images are deprecated https://hub.docker.com/_/openjdk#:~:text=Tags-,DEPRECATION%20NOTICE,-This%20image%20is
FROM eclipse-temurin:17-jdk-alpine
COPY --from=MAVEN_BUILD target/volontario.server-0.0.1.jar /volontario-server.jar
# CMD ["java", "-jar", "/volontario-server.jar"]
# temporary fix for database not initializing
RUN apk update && apk add --no-cache postgresql
CMD export PGPASSWORD=$POSTGRES_PASSWORD; psql -h volontario-db -U $POSTGRES_USER -c "CREATE DATABASE volontario;"; java -jar /volontario-server.jar
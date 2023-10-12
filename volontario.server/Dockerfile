# build .jar file
FROM maven:3-eclipse-temurin-17 AS MAVEN_BUILD
COPY ./ ./
# skipping tests because db connection test fails and prevents building .jar
RUN mvn clean package -Dmaven.test.skip

# using eclipse image, as the official openjdk images are deprecated https://hub.docker.com/_/openjdk#:~:text=Tags-,DEPRECATION%20NOTICE,-This%20image%20is
FROM eclipse-temurin:17-jdk-jammy
ARG VOLONTARIO_VERSION=0.0.1
ENV VOLONTARIO_VERSION=${VOLONTARIO_VERSION}
COPY --from=MAVEN_BUILD target/volontario.server-${VOLONTARIO_VERSION}.jar /volontario-server.jar

COPY ./entrypoint.sh /
COPY ./data/* /data/

RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/* && chmod 755 /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
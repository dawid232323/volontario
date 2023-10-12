#!/bin/bash
set -euo pipefail

# script for releasing that will be later replaced with pipeline when we set new Jenkins instance

VERSION=$1
REGISTRY=dev.volontario.me:5000

git checkout develop
git checkout -b "${VERSION}_prerelease"

cd volontario.server
mvn -B -N versions:set-property -Dproperty=revision -DnewVersion=$VERSION -DgenerateBackupPoms=false
docker build -t $REGISTRY/volontario-back:$VERSION --build-arg VOLONTARIO_VERSION=$VERSION . 
docker tag $REGISTRY/volontario-back:$VERSION $REGISTRY/volontario-back:latest

cd ../volontario.client/volontario.client.application/volontario
npm version $VERSION
docker build -t $REGISTRY/volontario-front:$VERSION --build-arg CLIENT_ENVIRONMENT=production --build-arg VOLONTARIO_VERSION=$VERSION .
docker tag $REGISTRY/volontario-front:$VERSION $REGISTRY/volontario-front:latest

cd ../../..
git add volontario.client/volontario.client.application/volontario/package-lock.json \
        volontario.client/volontario.client.application/volontario/package.json \
        volontario.server/pom.xml
git commit -m "Set version ${VERSION}"
git tag -a $VERSION -m "Volontario version ${VERSION}"
git push origin $VERSION
git checkout develop
git branch -D "${VERSION}_prerelease"

docker push $REGISTRY/volontario-back:$VERSION
docker push $REGISTRY/volontario-back:latest
docker push $REGISTRY/volontario-front:$VERSION
docker push $REGISTRY/volontario-front:latest
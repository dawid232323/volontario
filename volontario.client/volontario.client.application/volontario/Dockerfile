# build files
FROM node:19-alpine as node
WORKDIR /app
COPY ./ ./
RUN npm install
ARG CLIENT_ENVIRONMENT
ENV CLIENT_ENVIRONMENT=${CLIENT_ENVIRONMENT}
RUN npm run build -- --configuration ${CLIENT_ENVIRONMENT}

# move files to nginx image
FROM nginx:1.23-alpine
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=node /app/dist/volontario /usr/share/nginx/html

ARG VOLONTARIO_VERSION
ENV VOLONTARIO_VERSION=${VOLONTARIO_VERSION}
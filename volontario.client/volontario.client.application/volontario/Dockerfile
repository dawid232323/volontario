# build files
FROM node:19-alpine as node
WORKDIR /app
COPY ./ ./
RUN npm install
RUN npm run build --prod

# move files to nginx image
FROM nginx:1.23-alpine
COPY --from=node /app/dist/volontario /usr/share/nginx/html
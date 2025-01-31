# Stage 1: Build the Angular app
FROM node:21-alpine AS build
RUN npm install -g @angular/cli

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN ng build --configuration development

# Stage 2: Serve the Angular app using Nginx
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/frontend /usr/share/nginx/html
EXPOSE 80

CMD nginx -g "daemon off;"

# Stage 1: Build React SPA bundle using Node.js
FROM node:20-alpine AS builder
WORKDIR /app

# Copy package metadata and install dependencies
COPY package.json package-lock.json* ./
RUN npm install

# Copy source code and build Vite assets
COPY . .
RUN npm run build

# Stage 2: Serve static production build via Nginx
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Remove default static content and copy built React bundle
RUN rm -rf ./*
COPY --from=builder /app/dist .

# Custom Nginx configuration for React SPA routing and backend API proxying
RUN echo $'server {\n\
    listen 80;\n\
    location /api/ {\n\
        proxy_pass http://spring-boot-api-service:8080/;\n\
        proxy_set_header Host $host;\n\
        proxy_set_header X-Real-IP $remote_addr;\n\
    }\n\
    location / {\n\
        root /usr/share/nginx/html;\n\
        index index.html;\n\
        try_files $uri $uri/ /index.html;\n\
    }\n\
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

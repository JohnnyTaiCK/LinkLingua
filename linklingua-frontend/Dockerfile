# ============================================================
# LinkLingua frontend (React 19 + Vite) image
# Multi-stage build: Node build stage -> nginx static + reverse proxy
# ============================================================

# ---------- Stage 1: build the static assets ----------
FROM node:20-alpine AS build
WORKDIR /app

# Install dependencies from the lockfile for reproducible builds
COPY package.json package-lock.json ./
RUN npm ci

# API base URL baked in at build time.
# Defaults to "/api" so the app talks to the backend through the nginx
# reverse proxy on the same origin (no CORS needed).
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

COPY . .
RUN npm run build

# ---------- Stage 2: serve via nginx ----------
FROM nginx:alpine AS runtime

# SPA routing + reverse proxy to the backend
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Static build output
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

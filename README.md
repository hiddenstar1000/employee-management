# Employee Management System

A full-stack, enterprise-grade Employee Management Application consisting of a **Spring Boot REST API** backend and a modern **React + Vite** single-page application (SPA) frontend.

---

## Architecture & Deployment

![Employee Management System: Rancher & MongoDB Atlas Architecture](docs/images/deployment-architecture.jpg)

### Production Deployment Architecture
- **Cluster Platform**: Hosted on a **Rancher / Kubernetes Cluster** managing containerized workloads.
- **Ingress Routing**:
  - `/` routes browser traffic directly to the **React Frontend** pod.
  - `/api/*` proxies API requests to the **Spring Boot API** pod.
- **Inter-Service Communication**: React SPA uses relative `/api` paths, allowing seamless proxying by the Ingress Controller.
- **Cloud Data Persistence**: Spring Boot REST API connects to **MongoDB Atlas** (Fully Managed Cloud DB) with credentials securely supplied via Secret Injection.

### Local Development Flow

```mermaid
graph TD
    Client["Browser Client (React SPA)\nhttp://localhost:5173"] -->|"/api/* requests"| ViteProxy["Vite Reverse Proxy\n(Strips /api)"]
    ViteProxy -->|"Proxied to http://localhost:8080"| SpringAPI["Spring Boot REST API\n(Port 8080)"]
    
    subgraph Persistence Layer
        SpringAPI -->|"Runtime Profile (!test)"| MongoBD[("MongoDB Cloud Atlas / Local\n(MONGODB_URI)")]
        SpringAPI -->|"Test Profile (test)"| NitriteDB[("Nitrite Embedded NoSQL DB\n(org.dizitart:nitrite)")]
    end
```

---

## Projects in this Repository

| Component | Subdirectory | Description | Tech Stack |
| :--- | :--- | :--- | :--- |
| **Backend API** | [`employee-management-api`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/employee-management-api) | RESTful web services, business logic, MongoDB runtime & Nitrite test persistence | Java 25, Spring Boot 3.4.2, Spring Data MongoDB, Nitrite NoSQL DB |
| **Frontend UI** | [`employee-management-ui`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/employee-management-ui) | Modern glassmorphic SPA dashboard for team directory management | React 18, Vite 5.4, Tailwind CSS, Lucide React, Axios |

---

## Key Features

- **Full-Stack CRUD Operations**: Create, read, update, and delete employee records seamlessly from the frontend UI or directly via REST endpoints.
- **Spring Security & JWT Authentication**:
  - Stateless API authentication using Spring Security filter chain and signed JWT tokens (`Authorization: Bearer <token>`).
  - Account-level login toggle (`loginEnabled`) and password requirement validation.
  - UI authentication gate: Full system UI is locked behind a modern glassmorphic Login screen.
- **AES-256-GCM Password Encryption**: Strong symmetric encryption for stored employee passwords using AES-256-GCM with secret key configured via `ENCRYPTION_SECRET_KEY` in `.env`.
- **Dual Database Persistence Model**:
  - **Runtime Application**: Operates on **Spring Data MongoDB** using connection settings specified in `.env`.
  - **Automated Test Suite**: Executes 100% offline using **Nitrite Embedded NoSQL Database** (`org.dizitart:nitrite`).
- **Auto UUID Assignment**: Backend service automatically assigns a unique UUID string when creating new employees if an ID is omitted.
- **Vite Reverse Proxy**: Seamless development routing where frontend `/api/employees` calls are automatically rewritten and proxied to backend `http://localhost:8080/employees`.
- **Fault-Tolerant Exception Handling**: Global exception handler returning structured HTTP `503 Service Unavailable` JSON responses during database cluster connection timeouts.

---

## Repository Structure

```text
employee-management/
├── README.md                      # Workspace Root Documentation (This File)
├── .github/                       # GitHub Workflows & Automation
│   └── workflows/
│       └── publish-ghcr.yaml      # Automated Docker Build, GHCR Publish & K8s Deploy Pipeline
├── docs/                          # Project Architecture & Media Assets
│   └── images/
│       └── deployment-architecture.jpg  # Rancher & MongoDB Atlas Architecture Diagram
├── employee-management-api/       # Spring Boot Backend API Project
│   ├── Dockerfile                 # Multi-stage Docker Build (JDK 21 + Maven)
│   ├── em-api-deployment.yaml     # Kubernetes Deployment & Service Manifest
│   ├── .env                       # Local Environment Variables (MONGODB_URI, PORT, ENCRYPTION_SECRET_KEY)
│   ├── .env-example               # Example Environment Variables Template
│   ├── pom.xml                    # Maven Dependencies & Build Configuration
│   ├── README.md                  # Comprehensive API Documentation
│   └── src/                       # Security, Controllers, Services & Tests
└── employee-management-ui/        # React + Vite Frontend Project
    ├── Dockerfile                 # Multi-stage Docker Build (Node 20 + Nginx)
    ├── em-ui-deployment.yaml      # Kubernetes Deployment & Service Manifest
    ├── em-ingress.yaml            # Kubernetes Ingress Subdomain Routing Manifest
    ├── .env                       # Local Environment Variables (VITE_API_URL)
    ├── .env-example               # Example Environment Variables Template
    ├── package.json               # NPM Dependencies & Scripts
    ├── vite.config.js             # Vite Proxy & Server Settings
    ├── README.md                  # Comprehensive UI Documentation
    └── src/                       # Login, Layout, List & Form Modal Components
```

---

## Kubernetes Deployment & GitHub Actions CI/CD

The application is containerized and deployed to a **Kubernetes Cluster** using native Kubernetes deployment manifests and automated GitHub Actions CI/CD workflows.

### 1. Automated CI/CD Pipeline ([`.github/workflows/publish-ghcr.yaml`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/.github/workflows/publish-ghcr.yaml))

On push to `main` branch, GitHub Actions executes:
1. **API Build**: Compiles Spring Boot JAR with Maven (`mvn clean package -DskipTests`).
2. **Container Build & Push**: Builds and pushes Docker images to GitHub Container Registry (GHCR):
   - `ghcr.io/hiddenstar1000/employee-management-api:latest`
   - `ghcr.io/hiddenstar1000/employee-management-ui:latest`
3. **Automated Kubernetes Secret Sync**: Dynamically creates/updates `mongodb-atlas-secret` and `ghcr-secret` in namespace `app-em-test` using GitHub Environment Secrets (`PROD`).
4. **Automated Kubernetes Deployment**: Applies `em-api-deployment.yaml` and `em-ui-deployment.yaml` to the cluster via `kubectl`.

---

### 2. Manual Kubernetes Deployment (`kubectl`)

To deploy manually on your Kubernetes cluster:

#### Step 1: Create Namespace & Secrets
```bash
# Create namespace
kubectl create namespace app-em-test

# Create registry pull secret for GHCR
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=hiddenstar1000 \
  --docker-password="<YOUR_GH_PAT>" \
  --namespace=app-em-test

# Create MongoDB Atlas secret
kubectl create secret generic mongodb-atlas-secret \
  --from-literal=MONGODB_URI="<YOUR_MONGODB_ATLAS_URI>" \
  --from-literal=ENCRYPTION_SECRET_KEY="<YOUR_32_CHAR_SECRET_KEY>" \
  --namespace=app-em-test
```

#### Step 2: Apply Deployment & Ingress Manifests (with Let's Encrypt SSL/TLS)
```bash
# (Optional) Apply Let's Encrypt Production ClusterIssuer if cert-manager is installed
kubectl apply -f employee-management-ui/letsencrypt-clusterissuer.yaml

# Deploy Backend API & Service
kubectl apply -f employee-management-api/em-api-deployment.yaml -n app-em-test

# Deploy Frontend UI & Service
kubectl apply -f employee-management-ui/em-ui-deployment.yaml -n app-em-test

# Deploy Ingress with cert-manager annotations for TLS certificate provisioning
kubectl apply -f employee-management-ui/em-ingress.yaml -n app-em-test
```

#### Step 3: Verify Pods
```bash
kubectl get pods -n app-em-test -w
```

---

## Quick Start Guide

### Prerequisites

- **Java Development Kit (JDK)**: Java 21 or Java 25+
- **Apache Maven**: 3.8+
- **Node.js**: v18+ or v20+
- **MongoDB**: Active MongoDB Atlas cluster or local instance (`mongodb://localhost:27017`)

---

### Step 1: Start Backend REST API

```bash
cd employee-management-api

# Create local environment configuration from template
cp .env-example .env

# Compile the Spring Boot API
mvn clean compile

# Run the API server (Starts on http://localhost:8080)
mvn spring-boot:run
```

---

### Step 2: Start Frontend UI Dashboard

Open a separate terminal window:

```bash
cd employee-management-ui

# Create local environment configuration from template
cp .env-example .env

# Install dependencies
npm install

# Start Vite dev server (Launches on http://localhost:5173)
npm run dev
```

Open your browser and navigate to **`http://localhost:5173`**.

---

## Running Automated Tests

- **Automated Testing Suite**: 36 comprehensive test cases covering security filters, JWT token providers, controllers, services, global exception handling, and Nitrite embedded NoSQL repositories (`mvn clean test`).

```bash
cd employee-management-api
mvn clean test
```

---

## API Reference Overview

| Method | Endpoint | Access | Description | Request Body |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Public | API Status & Health Check | None |
| `POST` | `/auth/login` | Public | System Authentication & JWT Token Issuance | `{ "emailId", "password" }` |
| `GET` | `/employees` | Authenticated (`Bearer`) | Retrieve all employees | None |
| `POST` | `/employees` | Authenticated (`Bearer`) | Create a new employee (Auto UUID if `id` omitted) | Employee JSON |
| `GET` | `/employees/{id}` | Authenticated (`Bearer`) | Retrieve employee by ID | None |
| `PUT` | `/employees/{id}` | Authenticated (`Bearer`) | Update existing employee details | Employee JSON |
| `DELETE` | `/employees/{id}` | Authenticated (`Bearer`) | Delete employee by ID | None |


---

## Detailed Component Documentation

- For in-depth API architecture, endpoint schemas, and testing configuration, see **[`employee-management-api/README.md`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/employee-management-api/README.md)**.
- For frontend component hierarchy, proxy routing details, and styling tokens, see **[`employee-management-ui/README.md`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/employee-management-ui/README.md)**.

---

## License

This project is licensed under the MIT License.

# Employee Management UI

A modern, responsive React single-page application (SPA) built with Vite, Tailwind CSS, Lucide React, and Axios for managing employee records in real-time.

## Features

- **Modern Glassmorphic UI**: Vibrant, responsive user interface built with Tailwind CSS, custom color palettes, and glassmorphism elements.
- **Team Directory Management**:
  - **View Employees**: Interactive employee directory displaying team member names, email addresses, departments, and avatars.
  - **Add Team Member**: Modal form dialog for adding new employees.
  - **Edit Team Member**: Inline modal for editing existing employee details.
  - **Delete Team Member**: Confirmation prompt and deletion handling.
- **Automated Proxy Routing**: Pre-configured Vite reverse proxy forwarding frontend `/api` requests to backend REST API on `http://localhost:8080`.
- **Loading & Error Handling**: Animated loading spinners, friendly error banners, and non-blocking toast/alert notifications.

---

## Tech Stack

| Technology | Version / Tool | Purpose |
| :--- | :--- | :--- |
| **Framework** | React 18.3 | Core UI Component Framework |
| **Build Tool** | Vite 5.4 | Next-Generation Frontend Tooling & Dev Server |
| **Styling** | Tailwind CSS 3.4 & PostCSS | Utility-First Modern Styling System |
| **Icons** | Lucide React | Modern Vector Icon Library |
| **HTTP Client** | Axios | Promise-Based HTTP Client |
| **Linting** | ESLint 9 | Code Quality & Syntax Standards |

---

## Project Structure

```text
employee-management-ui/
├── index.html
├── package.json
├── postcss.config.js
├── tailwind.config.js
├── vite.config.js
└── src/
    ├── App.jsx                   # Main Application State & View Controller
    ├── App.css                   # Layout & Animation Styles
    ├── index.css                 # Base Tailwind Directives & Custom Utility Tokens
    ├── main.jsx                  # React DOM Root Entrypoint
    ├── components/
    │   ├── Layout.jsx            # Application Header, Navigation & Shell Container
    │   ├── EmployeeList.jsx      # Employee Table & Card Grid Component
    │   └── EmployeeFormModal.jsx # Add/Edit Form Modal Dialog
    └── services/
        └── api.js                # Centralized Axios Instance & Endpoint Methods
```

---

## API & Reverse Proxy Architecture

The frontend uses a centralized Axios client in [`src/services/api.js`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/employee-management-ui/src/services/api.js) configured with `baseURL: '/api'`.

In development, Vite proxies all `/api/*` requests to the Spring Boot backend (`http://localhost:8080`) and strips the `/api` prefix using the rewrite rule in [`vite.config.js`](file:///Users/dixon/Projects/Personal/Dixon%20AI/employee-management/employee-management-ui/vite.config.js):

| Frontend Request | Vite Proxy Target | Backend Endpoint Received |
| :--- | :--- | :--- |
| `GET /api/employees` | `http://localhost:8080` | `GET /employees` |
| `POST /api/employees` | `http://localhost:8080` | `POST /employees` |
| `PUT /api/employees/{id}` | `http://localhost:8080` | `PUT /employees/{id}` |
| `DELETE /api/employees/{id}` | `http://localhost:8080` | `DELETE /employees/{id}` |

---

## Getting Started

### Prerequisites

- **Node.js**: v18.0.0+ or v20+
- **npm**: v9+ or v10+

### Installation & Setup

1. **Navigate to the UI directory**:
   ```bash
   cd employee-management-ui
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start Development Server**:
   ```bash
   npm run dev
   ```

The UI dev server will launch locally at `http://localhost:5173`.

---

## Available NPM Scripts

- **`npm run dev`**: Starts Vite local development server with Hot Module Replacement (HMR) on port `5173`.
- **`npm run build`**: Builds production-ready optimized static bundle in `dist/`.
- **`npm run preview`**: Serves production build locally for verification.
- **`npm run lint`**: Runs ESLint across all JavaScript/JSX source files.

---

## License

This project is licensed under the MIT License.

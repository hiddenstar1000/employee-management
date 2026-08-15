# Kubernetes Namespace
resource "kubernetes_namespace" "app_ns" {
  metadata {
    name = var.namespace

    labels = {
      name        = var.namespace
      managed-by  = "terraform"
      environment = "production"
    }
  }
}

# Secret for MongoDB Atlas Credentials Injection
resource "kubernetes_secret" "mongodb_atlas" {
  metadata {
    name      = "mongodb-atlas-secret"
    namespace = kubernetes_namespace.app_ns.metadata[0].name

    labels = {
      app        = var.app_name
      managed-by = "terraform"
    }
  }

  type = "Opaque"

  data = {
    MONGODB_URI = var.mongodb_uri
  }
}

# Spring Boot API Deployment
resource "kubernetes_deployment" "spring_boot_api" {
  metadata {
    name      = "spring-boot-api"
    namespace = kubernetes_namespace.app_ns.metadata[0].name

    labels = {
      app       = var.app_name
      component = "api"
      tier      = "backend"
    }
  }

  spec {
    replicas = var.api_replicas

    selector {
      match_labels = {
        app       = var.app_name
        component = "api"
        tier      = "backend"
      }
    }

    template {
      metadata {
        labels = {
          app       = var.app_name
          component = "api"
          tier      = "backend"
        }
      }

      spec {
        container {
          name              = "spring-boot-api"
          image             = var.api_image
          image_pull_policy = "IfNotPresent"

          port {
            name           = "http"
            container_port = 8080
          }

          env {
            name = "MONGODB_URI"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.mongodb_atlas.metadata[0].name
                key  = "MONGODB_URI"
              }
            }
          }

          env {
            name = "SPRING_DATA_MONGODB_URI"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.mongodb_atlas.metadata[0].name
                key  = "MONGODB_URI"
              }
            }
          }

          liveness_probe {
            http_get {
              path = "/"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 10
          }

          readiness_probe {
            http_get {
              path = "/"
              port = 8080
            }
            initial_delay_seconds = 15
            period_seconds        = 5
          }

          resources {
            limits = {
              cpu    = "1000m"
              memory = "1Gi"
            }
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }
        }
      }
    }
  }
}

# Spring Boot API Service
resource "kubernetes_service" "spring_boot_api" {
  metadata {
    name      = "spring-boot-api-service"
    namespace = kubernetes_namespace.app_ns.metadata[0].name

    labels = {
      app       = var.app_name
      component = "api"
      tier      = "backend"
    }
  }

  spec {
    type = "ClusterIP"

    selector = {
      app       = var.app_name
      component = "api"
      tier      = "backend"
    }

    port {
      name        = "http"
      port        = 8080
      target_port = 8080
    }
  }
}

# React Frontend SPA Deployment
resource "kubernetes_deployment" "react_frontend" {
  metadata {
    name      = "react-frontend"
    namespace = kubernetes_namespace.app_ns.metadata[0].name

    labels = {
      app       = var.app_name
      component = "ui"
      tier      = "frontend"
    }
  }

  spec {
    replicas = var.ui_replicas

    selector {
      match_labels = {
        app       = var.app_name
        component = "ui"
        tier      = "frontend"
      }
    }

    template {
      metadata {
        labels = {
          app       = var.app_name
          component = "ui"
          tier      = "frontend"
        }
      }

      spec {
        container {
          name              = "react-frontend"
          image             = var.ui_image
          image_pull_policy = "IfNotPresent"

          port {
            name           = "http"
            container_port = 80
          }

          liveness_probe {
            http_get {
              path = "/"
              port = 80
            }
            initial_delay_seconds = 10
            period_seconds        = 10
          }

          readiness_probe {
            http_get {
              path = "/"
              port = 80
            }
            initial_delay_seconds = 5
            period_seconds        = 5
          }

          resources {
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
            requests = {
              cpu    = "100m"
              memory = "128Mi"
            }
          }
        }
      }
    }
  }
}

# React Frontend Service
resource "kubernetes_service" "react_frontend" {
  metadata {
    name      = "react-frontend-service"
    namespace = kubernetes_namespace.app_ns.metadata[0].name

    labels = {
      app       = var.app_name
      component = "ui"
      tier      = "frontend"
    }
  }

  spec {
    type = "ClusterIP"

    selector = {
      app       = var.app_name
      component = "ui"
      tier      = "frontend"
    }

    port {
      name        = "http"
      port        = 80
      target_port = 80
    }
  }
}

# Nginx Ingress Controller Resource
resource "kubernetes_ingress_v1" "app_ingress" {
  metadata {
    name      = "employee-management-ingress"
    namespace = kubernetes_namespace.app_ns.metadata[0].name

    annotations = {
      "kubernetes.io/ingress.class"                = var.ingress_class_name
      "nginx.ingress.kubernetes.io/ssl-redirect"  = "false"
      "nginx.ingress.kubernetes.io/use-regex"     = "true"
    }
  }

  spec {
    ingress_class_name = var.ingress_class_name

    rule {
      host = var.ingress_host != "" ? var.ingress_host : null

      http {
        # Route /api and /api/* to Spring Boot API Service
        path {
          path      = "/api(/|$)(.*)"
          path_type = "ImplementationSpecific"

          backend {
            service {
              name = kubernetes_service.spring_boot_api.metadata[0].name
              port {
                number = 8080
              }
            }
          }
        }

        # Route root / to React Frontend SPA Service
        path {
          path      = "/"
          path_type = "Prefix"

          backend {
            service {
              name = kubernetes_service.react_frontend.metadata[0].name
              port {
                number = 80
              }
            }
          }
        }
      }
    }
  }
}

output "namespace" {
  description = "The Kubernetes namespace created for the application."
  value       = kubernetes_namespace.app_ns.metadata[0].name
}

output "spring_boot_api_service" {
  description = "ClusterIP service name for the Spring Boot REST API."
  value       = kubernetes_service.spring_boot_api.metadata[0].name
}

output "react_frontend_service" {
  description = "ClusterIP service name for the React Frontend SPA."
  value       = kubernetes_service.react_frontend.metadata[0].name
}

output "ingress_name" {
  description = "Name of the Nginx Ingress Controller resource."
  value       = kubernetes_ingress_v1.app_ingress.metadata[0].name
}

output "ingress_routing_summary" {
  description = "Summary of configured Ingress routes matching Rancher architecture diagram."
  value = {
    frontend_route = "/ -> ${kubernetes_service.react_frontend.metadata[0].name}:80"
    api_route      = "/api/* -> ${kubernetes_service.spring_boot_api.metadata[0].name}:8080"
    database       = "MongoDB Atlas Cloud (via Secret: ${kubernetes_secret.mongodb_atlas.metadata[0].name})"
  }
}

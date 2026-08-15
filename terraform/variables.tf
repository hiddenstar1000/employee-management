variable "kubeconfig_path" {
  type        = string
  description = "Path to the kubeconfig file for Rancher / Kubernetes cluster access."
  default     = "~/.kube/config"
}

variable "kubeconfig_context" {
  type        = string
  description = "Kubeconfig context name to use for deployment (optional)."
  default     = ""
}

variable "namespace" {
  type        = string
  description = "Kubernetes namespace for the employee management system."
  default     = "employee-management"
}

variable "app_name" {
  type        = string
  description = "Application base name used for resource labeling."
  default     = "employee-management"
}

variable "api_image" {
  type        = string
  description = "Docker image for the Spring Boot REST API service."
  default     = "employee-management-api:latest"
}

variable "api_replicas" {
  type        = number
  description = "Number of Spring Boot REST API pod replicas."
  default     = 2
}

variable "ui_image" {
  type        = string
  description = "Docker image for the React SPA frontend service."
  default     = "employee-management-ui:latest"
}

variable "ui_replicas" {
  type        = number
  description = "Number of React Frontend pod replicas."
  default     = 2
}

variable "mongodb_uri" {
  type        = string
  description = "MongoDB Atlas connection URI string (stored securely in Kubernetes secret)."
  sensitive   = true
  default     = "mongodb+srv://dbuser:dbpassword@cluster0.mongodb.net/employeedb?retryWrites=true&w=majority"
}

variable "ingress_host" {
  type        = string
  description = "Hostname for the Ingress Controller (leave empty for wildcard/ip access)."
  default     = ""
}

variable "ingress_class_name" {
  type        = string
  description = "Ingress class name for Rancher/Kubernetes cluster."
  default     = "nginx"
}

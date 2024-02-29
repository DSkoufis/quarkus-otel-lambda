variable "lambda_memory_size" {
  description = "Amount of memory in MB"
  type        = number
  default     = 1024
}

variable "lambda_timeout" {
  description = "Max amount of time the lambda can run in seconds"
  type        = number
  default     = 15
}

variable "lambda_logs_retention" {
  description = "The number of days to retain log events in the log group. 0 retains the logs forever"
  type        = number
  default     = 1
}

variable "lambda_file_path" {
  description = "The path where the zip file is relatively to Terraform directory, i.e ../target/function.zip"
  default     = "../target/function.zip"
}

variable "lambda_architecture" {
  description = "The architecture to use"
  type        = string
  default     = "arm64"
  validation {
    condition     = contains(["x86_64", "arm64"], var.lambda_architecture)
    error_message = "Unknown architecture"
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Environment   = var.environment_tag
      ProductCode   = var.product_code_tag
      InventoryCode = var.inventory_code_tag
    }
  }
}

#Variables used internally, no need to change
variable "environment_tag" {
  default = "test"
}
variable "product_code_tag" {
  default = "test"
}
variable "inventory_code_tag" {
  default = "otel-test"
}

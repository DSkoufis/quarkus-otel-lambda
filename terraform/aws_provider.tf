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

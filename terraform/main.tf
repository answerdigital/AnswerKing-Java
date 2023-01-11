provider "aws" {
  region = "eu-west-2"
}

resource "aws_instance" "example" {
  count         = 1
  ami           = "ami-084e8c05825742534"
  instance_type = "t2.micro"

  tags {
    Name = "answer-king-example"
  }

}
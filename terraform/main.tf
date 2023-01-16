provider "aws" {
  region = "eu-west-2"
}

resource "aws_instance" "answer_king_java" {
  count         = 1
  ami           = "ami-084e8c05825742534"
  instance_type = "t2.micro"

  user_data = <<-EOF
                #!/bin/bash
                sudo yum install docker -y
                sudo service docker start
                docker pull ghcr.io/answerconsulting/answerking-java/answer-king-rest-api_app:latest
                docker-compose build
                docker-compose up
              EOF

  user_data_replace_on_change = true

}

resource "aws_security_group" "instance" {
  name = "answer-king-instance"

  ingress {
    from_port   = 8080
    protocol    = "tcp"
    to_port     = 8080
    cidr_blocks = ["0.0.0.0/0"]
  }
}
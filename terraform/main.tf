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
              EOF

  user_data_replace_on_change = true
}
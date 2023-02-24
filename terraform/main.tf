provider "aws" {
  region = "eu-west-2"
}

data "aws_availability_zones" "available" {
  state = "available"
}

resource "aws_vpc" "answer_king_vpc" {
  cidr_block           = var.vpc_cidr_block
  enable_dns_hostnames = true

  tags = {
    Name = "answer_king_vpc"
  }
}

resource "aws_internet_gateway" "answer_king_igw" {
  vpc_id = aws_vpc.answer_king_vpc.id

  tags = {
    Name = "answer_king_igw"
  }
}

resource "aws_subnet" "answer_king_public_subnet" {
  count             = var.subnet_count.public
  cidr_block        = var.public_subnet_cidr_blocks[count.index]
  vpc_id            = aws_vpc.answer_king_vpc.id
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name = "answer_king_public_subnet_${count.index}"
  }
}

resource "aws_subnet" "answer_king_private_subnet" {
  count             = var.subnet_count.private
  vpc_id            = aws_vpc.answer_king_vpc.id
  cidr_block        = var.private_subnet_cidr_blocks[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name = "tutorial_private_subnet_${count.index}"
  }
}

resource "aws_route_table" "answer_king_public_rt" {
  vpc_id = aws_vpc.answer_king_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.answer_king_igw.id
  }
}

resource "aws_route_table_association" "public" {
  count          = var.subnet_count.public
  route_table_id = aws_route_table.answer_king_public_rt.id
  subnet_id      = aws_subnet.answer_king_public_subnet[count.index].id
}

resource "aws_route_table" "answer_king_private_rt" {
  vpc_id = aws_vpc.answer_king_vpc.id
}

resource "aws_route_table_association" "private" {
  count          = var.subnet_count.private
  route_table_id = aws_route_table.answer_king_private_rt.id
  subnet_id      = aws_subnet.answer_king_private_subnet[count.index].id
}

resource "aws_eip" "answer_king_api_eip" {
  count    = 1
  instance = aws_instance.answer_king_java[count.index].id
  vpc      = true
  tags = {
    Name = "answer_king_api_eip_${count.index}"
  }
}

resource "aws_instance" "answer_king_java" {
  count                  = 1
  ami                    = "ami-084e8c05825742534"
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.answer_king_public_subnet[count.index].id
  vpc_security_group_ids = [aws_security_group.answer_king_api_sg.id]
  key_name               = aws_key_pair.generated_key.key_name


  user_data = <<-EOF
                #!/bin/bash

                set -e
                # Output all log
                exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1
                # Make sure we have all the latest updates when we launch this instance
                yum update -y && yum upgrade -y
                # Install components
                yum install -y docker
                # Add credential helper to pull from ECR
                mkdir -p ~/.docker && chmod 0700 ~/.docker
                # Start docker now and enable auto start on boot
                service docker start && chkconfig docker on
                # Allow the ec2-user to run docker commands without sudo
                usermod -a -G docker ec2-user
                # Run application at start
                docker container run --name answer-king-rest-api-container  --restart=always --name answer-king-rest-api-container -e "RDS_USERNAME=${var.db_username}" -e "RDS_PASSWORD=${var.db_password}" -e "RDS_HOSTNAME=${aws_db_instance.answer_king_database.address}" -e "RDS_PORT=${var.db_port}" -e "RDS_DB_NAME=${var.db_name}" -e "SPRING_PROFILES_ACTIVE=${var.spring_profile}" -e "MYSQLDB_PASSWORD=${var.db_password}" -e "MYSQLDB_USER=${var.db_username}" -e "MYSQL_URL=jdbc:mysql://${aws_db_instance.answer_king_database.address}:${var.db_port}/${var.db_name}" -p ${var.http_server_port}:${var.http_server_port} -d ghcr.io/answerdigital/answerking-java/answer-king-rest-api_app:latest
                EOF

  user_data_replace_on_change = true
  
  tags = {
    Name = "answer-king-java"
  }
}

resource "aws_db_instance" "answer_king_database" {
  instance_class         = "db.t2.micro"
  identifier_prefix      = "answer-king-db"
  allocated_storage      = 5
  engine                 = "mysql"
  engine_version         = "8.0.27"
  skip_final_snapshot    = true
  db_name                = var.db_name
  db_subnet_group_name   = aws_db_subnet_group.answer_king_db_subnet_group.id
  vpc_security_group_ids = [aws_security_group.answer_king_db_sg.id]

  username = var.db_username
  password = var.db_password
}

resource "aws_security_group" "answer_king_api_sg" {
  name        = "answer_king_api_sg"
  description = "Security group for tutorial web servers"
  vpc_id      = aws_vpc.answer_king_vpc.id

  ingress {
    from_port   = var.http_server_port
    protocol    = "tcp"
    to_port     = var.http_server_port
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = var.ssh_server_port
    to_port     = var.ssh_server_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "answer_king_api_sg"
  }
}

resource "aws_security_group" "answer_king_db_sg" {
  name        = "answer_king_db_sg"
  description = "Security group for answer king databases"

  vpc_id = aws_vpc.answer_king_vpc.id

  ingress {
    description     = "Allow MySQL traffic from only the web sg"
    from_port       = "3306"
    protocol        = "tcp"
    to_port         = "3306"
    security_groups = [aws_security_group.answer_king_api_sg.id]
  }

  tags = {
    Name = "answer_king_db_sg"
  }

}

resource "aws_db_subnet_group" "answer_king_db_subnet_group" {
  name        = "answer_king_db_subnet_group"
  description = "DB subnet group for answer king"
  subnet_ids  = [for subnet in aws_subnet.answer_king_private_subnet : subnet.id]
}

resource "tls_private_key" "example" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "generated_key" {
  key_name   = var.ec2_key_name
  public_key = tls_private_key.example.public_key_openssh
}

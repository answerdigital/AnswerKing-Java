output "public_ip" {
  description = "The public IP address of the web server"
  value       = aws_eip.answer_king_api_eip[0].public_ip
  depends_on  = [aws_eip.answer_king_api_eip]
}

output "web_public_dns" {
  description = "The public DNS address of the web server"
  value       = aws_eip.answer_king_api_eip[0].public_dns
  depends_on  = [aws_eip.answer_king_api_eip]
}

output "database_endpoint" {
  description = "The endpoint of the database"
  value       = aws_db_instance.answer_king_database.address
}

output "database_port" {
  description = "The port of the database"
  value       = aws_db_instance.answer_king_database.port
}

output "private_pem" {
  value     = tls_private_key.example.private_key_pem
  sensitive = true
}
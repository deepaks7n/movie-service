output "aws_sqs_queue_url" {
  value = aws_sqs_queue.this.url
}

output "aws_s3_bucket_name" {
  value = aws_s3_bucket.this.bucket_domain_name
}
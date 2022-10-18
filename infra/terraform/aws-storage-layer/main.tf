resource "random_pet" "this" {
  prefix = var.name
  length = 3
}

resource "aws_s3_bucket" "this" {
  bucket = random_pet.this.id
}

resource "aws_s3_bucket_acl" "this" {
  bucket = aws_s3_bucket.this.id
  acl    = "private"
}

resource "aws_sqs_queue" "this" {
  name                       = "${random_pet.this.id}-sqs"
  delay_seconds              = 1
  max_message_size           = 262144
  visibility_timeout_seconds = 300
  message_retention_seconds  = 259200
  receive_wait_time_seconds  = 10
  sqs_managed_sse_enabled    = true
}

resource "aws_sqs_queue_policy" "this" {
  queue_url = aws_sqs_queue.this.id

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "sqspolicy",
  "Statement": [
    {
      "Sid": "First",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sqs:SendMessage",
      "Resource": "${aws_sqs_queue.this.arn}",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "${aws_s3_bucket.this.arn}"
        }
      }
    }
  ]
}
POLICY
}

resource "aws_s3_bucket_notification" "this" {
  bucket = aws_s3_bucket.this.id

  queue {
    queue_arn = aws_sqs_queue.this.arn
    events    = ["s3:ObjectCreated:*"]
  }
}

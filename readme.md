# Movie Data As A Service

## Intro

The product will

- listen to a S3 bucket for ingesting content ([Movie details](https://github.com/prust/wikipedia-movie-data))
- exposes RESTFUL APIs to query the ingested data.

## The Architecture

### Data Load flow

1. Providers upload payloads to a pre-provisioned S3 buckets
2. A SQS listens to S3 for any new files
3. A Spring Boot based App (Extractor) listens to the SQS for events
4. Once the extractor gets an event (containing the meta-data of S3 key):
    - it fetches the key from S3
    - persists its in a MongoDB

### Data Fetch flow

1. A spring boot based RESTful web service is backed the same DB populated by data load flow
    - the app
        - has caching enabled
        - exposes a few APIs to search and filter the data stored in the DB  

![Alt text](resources/images/DaaS.jpg?raw=true "DaaS")

## DevOps

- Both apps:
  - are written in Java using Spring Boot.
  - containerized using jib
  - has manifest to deploy to k8s

## Setting it up in local

### Prerequisite

1. AWS keys

```shell
export AWS_ACCESS_KEY_ID=""
export AWS_SECRET_ACCESS_KEY=""
```

### Steps

1. Apply the TF to provision and integrate S3 and SQS

```shell
cd movie-service/infra/terraform/aws-storage-layer
terraform apply --auto-approve
```

export the s3 bucket name and SQS URL from TF output

```shell
export S3_BUCKET_NAME="default-publicly-infinite-tortoise"
export SQS_URL="https://sqs.eu-north-2.amazonaws.com/456/default-publicly-infinite-tortoise-sqs"
```

2. Create the images

```shell
./gradlew extractor:jibDockerBuild
./gradlew webservice:jibDockerBuild
```

3. Run the containers

```shell
docker run -d -p 27017:27017 --name local-mongo mongo:latest
docker run -d -p 6379:6379 --name local-redis -d redis:latest

docker run --link local-mongo:mongo  --name movie-extractor -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY -e S3_BUCKET_NAME=$S3_BUCKET_NAME -e SQS_URL=$SQS_URL movie/extractor:0.0.1-SNAPSHOT

docker run -d -p 8080:8080 --name movie-webservice --link local-redis:redis --link local-mongo:mongo movie/webservice:0.0.1-SNAPSHOT

```

4. Upload blob to S3

```shell
aws s3api put-object --bucket $S3_BUCKET_NAME --key fewMovies.json --body fewMovies.json
```

5. Hit the API

```shell
curl http://localhost:8080/api/movie?title=Laughing%20Gas
curl http://localhost:8080/api/movie\?year\=1907
curl http://localhost:8080/api/movie\?year\=1907\&title\=Laughing%20Gas
curl http://localhost:8080/api/movie/search\?query\=Regustus 
```


## Setting in Minikube

1. Push the image to a registry

```shell
docker tag movie/webservice:0.0.1-SNAPSHOT deepaksisupal/webservice:latest
docker tag movie/extractor:0.0.1-SNAPSHOT deepaksisupal/extractor:latest
docker login
docker push deepaksisupal/webservice:latest
docker push deepaksisupal/extractor:latest
```

2. Start minikube
`minikube start`

3. Apply manifests

```shell
cd infra/k8sManifest
k apply -f mongo.yaml
k apply -f redis.yaml
k apply -f extractor.yaml
```

4. Test it out

```shell
minikube service webservice-nodeport-svc --url
curl http://127.0.0.1:<>/api/movie/search\?query\=Gas
```

### Troubleshooting

#### TF destroy fails when the S3 has contents

delete all in the S3
`aws s3 rm s3://$S3_BUCKET_NAME --recursive --include "*"`
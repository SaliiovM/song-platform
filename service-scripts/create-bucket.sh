#!/bin/bash

until $(curl --output /dev/null --silent --head --fail $AWS_ENDPOINT:$CLOUD_PORT); do
    printf '.'
    sleep 5
done

export AWS_ACCESS_KEY_ID=$ACCESS_KEY
export AWS_SECRET_ACCESS_KEY=$SECRET_KEY

if aws --endpoint-url=$AWS_ENDPOINT:$CLOUD_PORT s3api head-bucket --bucket $S3_AUDIO_BUCKET 2>/dev/null; then
    echo "Bucket '$S3_AUDIO_BUCKET' already exists."
else
    aws --endpoint-url=$AWS_ENDPOINT:$CLOUD_PORT s3 mb s3://$S3_AUDIO_BUCKET
    echo "Bucket '$S3_AUDIO_BUCKET' created."
fi
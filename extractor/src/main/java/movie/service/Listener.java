package movie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Event;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class Listener {

    @Autowired
    AmazonS3 s3Client;

    @SqsListener(value = "${cloud.aws.sqs.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void s3ObjectCreationListener(String message) {
        S3EventNotification notification = S3EventNotification.parseJson(message);
        notification.getRecords().forEach(
                s3Record -> {
                    log.info("Processing S3 event of type {}", s3Record.getEventNameAsEnum());

                    if (S3Event.ObjectCreatedByPut.equals(s3Record.getEventNameAsEnum())) {
                        String fileContent = readS3File(s3Record.getS3().getBucket().getName(), s3Record.getS3().getObject().getKey());
                        log.info("file content -> {}", fileContent);
                    }
                });
    }

    public String readS3File(String bucketName, String key) {
        log.info("Reading file with a key {} from S3 bucket {}", key, bucketName);
        String out = null;
        try {
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
            InputStream objectData = object.getObjectContent();
            out = IOUtils.toString(objectData);
            objectData.close();
        } catch (IOException e) {
            log.error("{} while reading file with a key {} from S3 bucket {}", e.getMessage(), key, bucketName);
        } catch (AmazonS3Exception e) {
            log.error("{} while reading file with a key {} from S3 bucket {}", e.getMessage(), key, bucketName);
        }
        return out;
    }



}

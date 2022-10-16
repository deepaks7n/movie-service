package movie.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class S3 {
    @Autowired
    AmazonS3 s3Client;

    public S3Object read(String bucketName, String key) {
        log.info("Reading file with a key {} from S3 bucket {}", key, bucketName);
        S3Object s3Object = null;
        try {
            s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        } catch (AmazonS3Exception e) {
            log.error("{} while reading file with a key {} from S3 bucket {}", e.getMessage(), key, bucketName);
        }
        return s3Object;
    }

}

package movie.service.aws;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.S3Event;
import lombok.extern.slf4j.Slf4j;
import movie.domain.SQSEventMetaData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SQSEventParser {
    public SQSEventMetaData execute(S3EventNotification.S3EventNotificationRecord s3NotificationRecord) {
        return Optional.of(s3NotificationRecord)
                .filter(r -> S3Event.ObjectCreatedByPut.equals(r.getEventNameAsEnum()))
                .map(s3Rec -> new SQSEventMetaData(s3Rec.getS3().getBucket().getName(), s3Rec.getS3().getObject().getKey()))
                .get();
    }
}


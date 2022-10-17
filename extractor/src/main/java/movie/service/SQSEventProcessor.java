package movie.service;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import movie.domain.Movie;
import movie.repository.MovieRepository;
import movie.service.aws.Mapper;
import movie.service.aws.S3;
import movie.service.aws.SQSEventParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SQSEventProcessor {

    @Autowired
    SQSEventParser sqsEventParser;

    @Autowired
    S3 s3;

    @Autowired
    Mapper mapper;

    @Autowired
    MovieRepository movieRepository;

    @SqsListener(value = "${cloud.aws.sqs.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void listen(String message) {
        log.info("parsing message : {}", message);
        S3EventNotification s3EventNotification = S3EventNotification.parseJson(message);
        if(null != s3EventNotification && !CollectionUtils.isNullOrEmpty(s3EventNotification.getRecords())){
            List<S3EventNotification.S3EventNotificationRecord> records = s3EventNotification.getRecords();
            List<Movie> upsertMovieList = records
                    .stream()
                    .filter(Objects::nonNull)
                    .map(s3NotificationRec -> sqsEventParser.execute(s3NotificationRec))
                    .map(sqsEventMetadata -> s3.read(sqsEventMetadata.getBucketName(), sqsEventMetadata.getBucketObjectKey()))
                    .map(s3Object -> mapper.parse(s3Object))
                    .flatMap(List::stream)
                    .map(movie -> movieRepository.update(movie))
                    .collect(Collectors.toList());
            log.info("DataUpsert: {}", upsertMovieList);
        }
    }

}

package movie.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class SQSEventMetaData {
    @NonNull
    private String bucketName;
    @NonNull
    private String bucketObjectKey;
}

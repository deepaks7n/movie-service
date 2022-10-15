package movie.config;

import com.amazonaws.auth.AWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Bean
    public AWSCredentials credentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }
}

package movie;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
                org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
                org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
        }
)
public class App {

    @Autowired
    AmazonS3 s3Client;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}

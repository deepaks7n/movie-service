package movie.service.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import movie.domain.Movie;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class Mapper {
    public List<Movie> parse(S3Object s3Object) {
        List<Movie> movies = null;
        try (InputStream objectData = s3Object.getObjectContent()) {
            ObjectMapper mapper = new ObjectMapper();
            movies = mapper.readValue(objectData, new TypeReference<List<Movie>>(){});
            log.info("DataReceived: {}", Arrays.toString(movies.toArray()));
        } catch (IOException e) {
            log.error("JSONMappingError: {}", e.getMessage());
        }
        return movies;
    }
}

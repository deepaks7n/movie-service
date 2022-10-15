package movie.repository;

import movie.domain.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    public List<Movie> findByTitleIgnoreCase(String title);
}

package movie.repository;

import movie.domain.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    
    List<Movie> findByTitleIgnoreCase(String title, Pageable pageable);
    
    List<Movie> findByYear(Integer year, Pageable pageable);
    
    @Query(value = "{ $text : { $search : ?0, $language : 'en' } }")
    List<Movie> textSearch(String query, Pageable pageable);
    
    @Query("{'$or':[ {'title': ?0}, {'year': ?1} ] }")
    List<Movie> findByTitleOrYear(String title, Integer year, Pageable pageable);
    
    @Query(value = "{  'cast' : { '$elemMatch' : { $eq: ?0 }}}")
    List<Movie> byActor(String cast);
    
    @Query(value = "{ $elemMatch { 'genres' : ?0 }}")
    List<Movie> byGenere(String genres);

}

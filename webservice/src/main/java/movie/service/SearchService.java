package movie.service;

import lombok.extern.slf4j.Slf4j;
import movie.domain.Movie;
import movie.domain.TMovie;
import movie.repository.MovieRepository;
import movie.utils.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchService {
    @Autowired
    MovieRepository repo;

    public List<Movie> execute(TMovie tMovie) {
        boolean isValid = Validators.isValidSearch(tMovie);
        log.info("Filtering for {} and is valid : {}", tMovie, isValid);
        List<Movie> movieList = new ArrayList<>();
        if (!isValid) return movieList;

        Pageable pageable =  PageRequest.of(tMovie.getPage(), tMovie.getSize());
        movieList = repo.textSearch(tMovie.getQuery(),pageable);
        return movieList;
    }
}

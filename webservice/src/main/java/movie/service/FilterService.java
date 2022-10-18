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
public class FilterService {

    @Autowired
    MovieRepository repo;

    public List<Movie> execute(TMovie tMovie) {
        boolean isValid = Validators.isValidFilter(tMovie);
        log.info("Filtering for {} and is valid : {}", tMovie, isValid);
        List<Movie> movieList = new ArrayList<>();
        if(!isValid) return movieList;

        Pageable pageable =  PageRequest.of(tMovie.getPage(), tMovie.getSize());
        //shame: make it a strategy
        if(tMovie.isYear() && tMovie.isTitle()){
            movieList = repo.findByTitleOrYear(tMovie.getTitle(), tMovie.getYear(), pageable);
        } else if (tMovie.isTitle()) {
            movieList = repo.findByTitleIgnoreCase(tMovie.getTitle(), pageable);
        } else if (tMovie.isYear()) {
            movieList = repo.findByYear(tMovie.getYear(), pageable);
        }
        return movieList;
    }
}

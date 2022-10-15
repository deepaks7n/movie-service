package movie.controller;

import lombok.extern.slf4j.Slf4j;
import movie.domain.Movie;
import movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    MovieRepository repo;

    @Cacheable(value = "movies", key = "#title")
    @GetMapping("/title/{title}")
    public List<Movie> greeting(@PathVariable String title) {
        log.info("returning movie for title " + title);
        return repo.findByTitleIgnoreCase(title);
    }
}


package movie.controller;

import lombok.extern.slf4j.Slf4j;
import movie.domain.Movie;
import movie.domain.TMovie;
import movie.service.FilterService;
import movie.service.SearchService;
import movie.utils.GenericBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/movie")
public class MovieController {
    @Autowired
    FilterService filterService;

    @Autowired
    SearchService searchService;

    @Cacheable(value = "movie", key = "#root.args")
    @GetMapping
    public List<Movie> getMovies(@RequestParam("title") Optional<String> optionalTitle,
                                 @RequestParam("year") Optional<Integer> optionalYear,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        TMovie tMovie = GenericBuilder.of(TMovie::new)
                .with(TMovie::setTitle, optionalTitle)
                .with(TMovie::setYear, optionalYear)
                .with(TMovie::setPage, page)
                .with(TMovie::setSize, size)
                .build();
        log.info("filter request : {}", tMovie);
        List<Movie> movieList = filterService.execute(tMovie);
        log.info("filter response : {}", movieList);
        return movieList;
    }

    @GetMapping("/search")
    @Cacheable(value = "movieSearch", key = "#root.args")
    public List<Movie> search(@RequestParam("query") String query,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        TMovie tMovie = GenericBuilder.of(TMovie::new)
                .with(TMovie::setPage, page)
                .with(TMovie::setSize, size)
                .with(TMovie::setQuery, query)
                .build();
        log.info("search request : {}", tMovie);
        List<Movie> movieList = searchService.execute(tMovie);
        log.info("search response : {}", movieList);
        return movieList;
    }
}


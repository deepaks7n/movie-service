package movie.controller;

import lombok.extern.slf4j.Slf4j;
import movie.domain.Movie;
import movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/movie")
public class MovieController {

    public static final int DEFAULT_PAGE_SIZE = 5;
    @Autowired
    MovieRepository repo;

    @Cacheable(value = "movie", key = "#root.args")
    @GetMapping
    public List<Movie> getMovies(@RequestParam("title") Optional<String> optionalTitle,
                                 @RequestParam("year") Optional<Integer> optionalYear,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        int s = size.orElse(DEFAULT_PAGE_SIZE);
        int p = page.orElse(0);
        boolean isYear = false;
        boolean isTitle = false;
        String title = optionalTitle.orElse(null);
        int year = optionalYear.orElse(0);
        log.info("year : {} page : {} and size : {} ", year, p, s);
        if(year >= 1900 && year <= 2018) {isYear = true;}
        if(StringUtils.hasText(title)) {isTitle = true;}
        Pageable pageable =  PageRequest.of(p, s);
        List<Movie> movieList = new ArrayList<>();
        if(isTitle && isYear){ //shame: move it to another layer
            movieList = repo.findByTitleOrYear(title, year, pageable);
        } else if (isTitle) {
            movieList = repo.findByTitleIgnoreCase(title, pageable);
        } else if (isYear) {
            movieList = repo.findByYear(year, pageable);
        }
        log.info("returning movie for title {} and Year {} : {}", title, year, movieList);

        return movieList;
    }

    @GetMapping("/search")
    @Cacheable(value = "movieSearch", key = "#root.args")
    public List<Movie> search(@RequestParam("query") String query,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int s = size.orElse(DEFAULT_PAGE_SIZE);
        int p = page.orElse(0);
        log.info("search query: {} page : {} and size : {} ", query, p, s);
        if(!StringUtils.hasText(query)) {return new ArrayList<>();}
        Pageable pageable =  PageRequest.of(p, s);
        List<Movie> searchList = repo.textSearch(query,pageable);

        log.info("search results for query: {} page : {} and size : {} are : {}", query, p, s, searchList);
        return searchList;
    }




    //    @GetMapping("/search")
//    public List<Movie> getByCast(@RequestParam("cast") Optional<String> optionalCast, @RequestParam("genre") Optional<String> optionalGenre) {
//        log.info("returning movie for cast {} and genre {} ", optionalCast, optionalGenre);
//        List<Movie> searchList = optionalCast.map(c -> repo.byActor(c)).orElse(new ArrayList<>());
//        log.info("Cast search results for movie: {} ", searchList);
//        List<Movie> genreSearch = optionalGenre.map(g -> repo.byGenere(g)).orElse(new ArrayList<>());
//        log.info("Genre search results for movie: {} ", genreSearch);
//        searchList.removeAll(genreSearch); //removes duplicates
//        searchList.addAll(genreSearch); //union
//        log.info("total search results for movie: {} ", searchList);
//        return searchList;
//    }
}


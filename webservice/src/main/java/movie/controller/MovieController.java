package movie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    @GetMapping("/greeting")
    public String greeting() {
        return "Hello";
    }
}


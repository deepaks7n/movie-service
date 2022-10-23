package movie.exception;

public class NoMovieNameFound extends RuntimeException {
    public NoMovieNameFound(String name) {
        super(String.format("Movie with Name %s is not found", name));
    }
}

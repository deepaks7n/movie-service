package movie.utils;

import movie.domain.TMovie;
import org.springframework.util.StringUtils;

public class Validators {
    public static boolean isValidFilter(TMovie tMovie){
        boolean isValid = true;
        if(tMovie.isYear() && (tMovie.getYear() < 1900 && tMovie.getYear() > 2018)){
            isValid = false;
        }
        if(tMovie.isTitle() && (!StringUtils.hasText(tMovie.getTitle()))){
            isValid = false;
        }
        return isValid;
    }

    public static boolean isValidSearch(TMovie tMovie) {
        return StringUtils.hasText(tMovie.getQuery());
    }
}

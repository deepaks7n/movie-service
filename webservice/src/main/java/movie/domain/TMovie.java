package movie.domain;

import lombok.Data;

import java.util.Optional;


@Data
public class TMovie {

    public static final int DEFAULT_PAGE_SIZE = 5;
    private String title;

    private String query;
    private Integer year;

    private Integer page;

    private Integer size;

    private boolean isYear = false;

    private boolean isTitle = false;
    public boolean isTitle() {
        return isTitle;
    }


    public void setTitle(Optional<String> optionalTitle) {
        this.title = optionalTitle.orElse(null);
        isTitle = optionalTitle.isPresent();
    }

    public void setYear(Optional<Integer> optionalYear) {
        this.year = optionalYear.orElse(null);
        isYear = optionalYear.isPresent();
    }

    public void setPage(Optional<Integer> page) {
        this.page = page.orElse(0);
    }

    public void setSize(Optional<Integer> size) {
        this.size = size.orElse(DEFAULT_PAGE_SIZE);
    }

    public boolean isYear() {
        return isYear;
    }
}

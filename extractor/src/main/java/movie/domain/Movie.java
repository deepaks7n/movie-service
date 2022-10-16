package movie.domain;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "movies")
@Data
@CompoundIndex(def = "{'title':1, 'cast':1, 'year':1}", name = "title_cast_year", unique = true)
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    @TextIndexed
    private String title;
    @Indexed
    private Integer year;
    @TextIndexed
    private List<String> cast = new ArrayList<>();
    @TextIndexed
    private List<String> genres = new ArrayList<>();

}


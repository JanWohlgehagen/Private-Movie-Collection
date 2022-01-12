package bll.util;

import be.Movie;
import javafx.scene.control.CheckBox;

import java.util.List;

public interface ISearcher {
    public List<Movie> search(List<Movie> searchBase, String query, boolean isTitleOn, List<CheckBox> checkBoxes, boolean isRatingOn);

    public boolean compareToMovieName(Movie movie, String query);

    public boolean compareToMovieCategory(List<CheckBox> checkBoxes);

    public boolean compareToMovieRating(Movie movie, String query);
}

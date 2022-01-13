package bll.util;

import be.Category;
import be.Movie;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher implements ISearcher{
    private boolean isTitleOn;
    private boolean isCatOn;
    private boolean isRatingOn;
    private List<Movie> searchResult = new ArrayList<>();


    @Override
    public List<Movie> search(List<Movie> searchBase, String query, boolean isTitleOn, boolean isCatOn, boolean isRatingOn) {
        this.isTitleOn = isTitleOn;
        this.isRatingOn = isRatingOn;
        this.isCatOn = isCatOn;

        searchResult.clear();
        for (Movie movie : searchBase) {
            if(compareToMovieName(movie, query) || compareToMovieCategory(movie, query) || compareToMovieRating(movie, query))
            {
                searchResult.add(movie);
            }
        }
        return searchResult;
    }

    @Override
    public boolean compareToMovieName(Movie movie, String query) {
        if(isTitleOn){
            return movie.getName().toLowerCase().contains(query.toLowerCase());
        }
        return false;
    }

    @Override
    public boolean compareToMovieCategory(Movie movie, String query) {
        if(isCatOn){
            return movie.getName().toLowerCase().contains(query.toLowerCase());
           }

        return false;
    }

    @Override
    public boolean compareToMovieRating(Movie movie, String query) {
        if(isRatingOn && !query.isEmpty()){
            return movie.getIMDBRating() >= Double.parseDouble(query);
        }
        return false;
    }
}

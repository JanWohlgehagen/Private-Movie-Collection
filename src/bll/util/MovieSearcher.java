package bll.util;

import be.DisplayMessage;
import be.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieSearcher implements ISearcher{
    private boolean isTitleOn;
    private boolean isRatingOn;
    private List<Movie> searchResult = new ArrayList<>();

    /**
     * Checks if the movie list contains a movie with the title that matches the query or a rating that is higher than the query.
     * @param searchBase a list of movies to search through
     * @param query a search query
     * @param isTitleOn a boolean that tracks whether the user is searching by title
     * @param isRatingOn a boolean that tracks whether the user is searching by rating
     * @return
     */
    @Override
    public List<Movie> search(List<Movie> searchBase, String query, boolean isTitleOn, boolean isRatingOn) {
        this.isTitleOn = isTitleOn;
        this.isRatingOn = isRatingOn;

        searchResult.clear();
        try{
            for (Movie movie : searchBase) {
                if(compareToMovieName(movie, query) || compareToMovieRating(movie, query))
                {
                    searchResult.add(movie);
                }
            }
        }catch (NumberFormatException nfe){
            DisplayMessage.displayErrorMessage(nfe, "A movie rating can only be a numeric value (0-10).");
        }
        return searchResult;
    }

    /**
     * If isTitleOn is true, it checks if the movie's name field contains the query and returns true if so.
     * @param movie a movie object
     * @param query a search query
     * @return
     */
    @Override
    public boolean compareToMovieName(Movie movie, String query) {
        if(isTitleOn){
            return movie.getNameProperty().get().toLowerCase().contains(query.toLowerCase());
        }
        return false;
    }

    /**
     * If isTitleOn is true, it checks if the IMDBRating field is higher than the query and returns true if so.
     * @param movie a movie object
     * @param query a search query
     * @return
     */
    @Override
    public boolean compareToMovieRating(Movie movie, String query) {
            if(isRatingOn && !query.isEmpty()){
                return movie.getIMDBRatingProperty().get() >= Double.parseDouble(query);
            }
        return false;
    }
}

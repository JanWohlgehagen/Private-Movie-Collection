package bll.util;

import be.Movie;

import java.util.List;

public interface ISearcher {
    public List<Movie> search(List<Movie> searchBase, String query, boolean isTitleOn, boolean isCatOn, boolean isRatingOn);

    public boolean compareToMovieName(Movie movie, String query);

    public boolean compareToMovieCategory(Movie movie, String query);

    public boolean compareToMovieRating(Movie movie, String query);
}

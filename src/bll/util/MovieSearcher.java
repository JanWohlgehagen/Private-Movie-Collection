package bll.util;

import be.Category;
import be.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher implements ISearcher{
    private boolean isTitleOn;
    private boolean isCatOn;
    private boolean isRatingOn;


    @Override
    public List<Movie> search(List<Movie> searchBase, String query, boolean isTitleOn, boolean isCatOn, boolean isRatingOn) {
        this.isTitleOn = isTitleOn;
        this.isCatOn = isCatOn;
        this.isRatingOn = isRatingOn;
        List<Movie> searchResult = new ArrayList<>();

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
            List<Category> CategoriesToCompare = movie.getCategories();
            for (Category cat: CategoriesToCompare) {
                if(cat.getName().toLowerCase().contains(query.toLowerCase())){
                    return true;
                }
            }
            return false;
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

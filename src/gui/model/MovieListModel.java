package gui.model;

import be.Category;
import be.CategoryException;
import be.Movie;
import be.MovieException;
import bll.MovieManager;
import bll.util.ISearcher;
import bll.util.MovieSearcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieListModel {
    private MovieManager movieManager;
    private ObservableList<Movie> movieList;
    private List<Movie> movieCache = new ArrayList<>();
    private List<Movie> filterResults = new ArrayList<>();
    private ISearcher movieSearcher;


    public MovieListModel() throws IOException, MovieException {
        movieManager = new MovieManager();
        movieList = FXCollections.observableArrayList(movieManager.getAllMovies());
        movieCache.addAll(movieList);
        movieSearcher = new MovieSearcher();
    }

    /**
     * Get all the movies that should be display in the view.
     * @return ObservableList of Songmodel
     */
    public ObservableList<Movie> getMovieList() {
        return movieList;
    }

    /**
     * Create new song to be display in main view
     * @param name
     * @param IMDBRating
     * @param pathToFile
     */
    public void createMovie(String name, double IMDBRating, String pathToFile) throws MovieException {
        Movie movie = movieManager.createMovie(name, IMDBRating, pathToFile);
        movieList.add(movie);
        movieCache.add(movie);
    }

    /**
     * Delete the song in mainview and database
     * @param movie
     */
    public void deleteMovie(Movie movie) throws MovieException {
        movieManager.deleteMovie(movie);
        movieList.remove(movie);
        movieCache.remove(movie);
    }

    /**
     * Update the song in mainview and database
     * @param movie
     * @param name
     * @param imdbRating
     * @param personalRating
     */
    public void updateMovie(Movie movie, String name, double imdbRating, double personalRating, ObservableList<Category> categories) throws MovieException {
        if(movieManager.checkUpdatedValues(imdbRating, personalRating)){
            movie.setNameProperty(name);
            movie.setIMDBRatingProperty(imdbRating);
            movie.setPersonalRatingProperty(personalRating);
            movie.setCategoryList(categories);
            movieManager.updateMovie(movie);
        }
    }


    /**
     * Searches through movie list, to find a song that matches the key word

     * @param query the key word, to search for
     * @return a list of songs that fit, the key word
     */
    public void searchMovie(String query, boolean isTitleOn, boolean isRatingOn) throws MovieException {
        if(query.isBlank()){
            movieList.clear();
            if(filterResults.isEmpty()){
                movieList.addAll(movieCache);
            } else {
                movieList.addAll(filterResults);
            }
        }else{
            List<Movie> tempList = new ArrayList<>();
            if (filterResults.isEmpty()){
                tempList.addAll(movieSearcher.search(movieCache, query, isTitleOn, isRatingOn));
            } else tempList.addAll(movieSearcher.search(filterResults, query, isTitleOn, isRatingOn));
            movieList.clear();
            movieList.addAll(tempList);
        }
    }

    public void filterCategories(List<String> selectedCategoreis) throws MovieException {
        filterResults = movieManager.filterCategories(selectedCategoreis);

        movieList.clear();
        if(selectedCategoreis.isEmpty()){
            movieList.addAll(movieCache);
            filterResults.addAll(movieCache);
        }else {
            movieList.addAll(filterResults);
        }
    }

    public ObservableList<Category> getCategoryList() throws CategoryException {
        return FXCollections.observableArrayList(movieManager.getAllCategories());
    }

    public void updateLastView(Movie movie) throws MovieException {
        movieManager.updateLastView(movie);
    }
}

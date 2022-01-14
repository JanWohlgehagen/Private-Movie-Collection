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
import java.util.Stack;

public class MovieListModel {
    private MovieManager movieManager;
    private ObservableList<Movie> movieList;
    private List<Movie> movieCache;
    private List<Movie> searchResults;
    private ISearcher movieSearcher;

    public MovieListModel() throws IOException, MovieException {
        movieManager = new MovieManager();
        movieList = FXCollections.observableArrayList(movieManager.getAllMovies());
        movieCache = new ArrayList<>();
        movieCache.addAll(movieList);
        searchResults = new ArrayList<>();
        searchResults.addAll(movieCache);
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
        List <Movie> tempList = new ArrayList<>();


        if(query.isBlank()){
            movieList.clear();
            movieList.addAll(movieCache);
        }else{
            tempList.addAll(movieSearcher.search(searchResults, query, isTitleOn, isRatingOn));
            movieList.clear();
            movieList.addAll(tempList);
        }
    }

    public void filterCategories(List<String> selectedCategoreis) throws MovieException {
        searchResults = movieManager.filterCategories(selectedCategoreis);

        movieList.clear();
        if(selectedCategoreis.isEmpty()){
            movieList.addAll(movieCache);
        }else {
            movieList.addAll(searchResults);
        }
    }

    public ObservableList<Category> getCategoryList() throws CategoryException {
        return FXCollections.observableArrayList(movieManager.getAllCategories());
    }

    public void updateLastView(Movie movie) throws MovieException {
        movieManager.updateLastView(movie);
    }
}

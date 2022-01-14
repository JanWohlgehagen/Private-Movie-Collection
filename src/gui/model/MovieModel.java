package gui.model;

import be.Category;
import be.CategoryException;
import be.Movie;
import be.MovieException;
import bll.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieModel {
    private MovieManager movieManager;
    private ObservableList<Movie> movieList;
    private List<Movie> movieCache;

    public MovieModel() throws IOException, MovieException {
        movieManager = new MovieManager();
        movieList = FXCollections.observableArrayList(movieManager.getAllMovies());
        movieCache.addAll(movieList);
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
     * Searches through song list, to find a Movie that matches the key word
     * @param query the key word, to search for
     */
    public void searchMovie(String query, boolean isTitleOn, boolean isCatON, boolean isRatingOn) throws MovieException {

        if(query.isBlank() && !isCatON){
            movieList.addAll(movieCache);
        }else if(query.isBlank() && isCatON){
            movieList.addAll(filterCategories();)
        }


        movieSearcher.search(moviesToSearch, query, isTitleOn, isCatOn, isRatingOn);

        movieList.clear();
        if(query.isBlank() && isCatON){
            movieList.addAll(movieCache);
            return;
        }
       movieList.addAll((searchResults));
    }

    /**
     * Searches through movie list, to find a song that matches the key word

     * @param query the key word, to search for
     * @return a list of songs that fit, the key word
     */
    public List<Movie> searchMovie(String query, List<Movie>  boolean isTitleOn, boolean isRatingOn) throws MovieException {
        List<Movie> moviesToSearch;
        boolean isCatOn;
        if(selectedCategories.isEmpty()){
            moviesToSearch = daoMovie.getAllMovies();
            isCatOn = false;
        }else{
            moviesToSearch = daoMovie.getMoviesWithSelectedCategories(selectedCategories);
            isCatOn = true;
        }
        return movieSearcher.search(moviesToSearch, query, isTitleOn, isCatOn, isRatingOn);
    }

    public void filterCategories(List<String> selectedCategoreis) throws MovieException {
        List<Movie> searchResults = movieManager.filterCategories(selectedCategoreis);

        movieList.clear();
        if(selectedCategoreis.isEmpty()){
            movieList.addAll(movieManager.getAllMovies());
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

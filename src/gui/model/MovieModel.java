package gui.model;

import be.Category;
import be.CategoryException;
import be.Movie;
import be.MovieException;
import bll.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class MovieModel {
    private MovieManager movieManager;
    private ObservableList<Movie> movieList;

    public MovieModel() throws IOException, MovieException {
        movieManager = new MovieManager();
        movieList = FXCollections.observableArrayList(movieManager.getAllMovies());
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
        movieList.add(movieManager.createMovie(name, IMDBRating, pathToFile));
    }

    /**
     * Delete the song in mainview and database
     * @param movie
     */
    public void deleteMovie(Movie movie) throws MovieException {
        movieManager.deleteMovie(movie);
        movieList.remove(movie);
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
    public void searchMovie(String query, boolean isTitleOn, List<String> selectedCategoreis, boolean isRatingOn) throws MovieException {
        List<Movie> searchResults = movieManager.searchMovie(query, isTitleOn, selectedCategoreis, isRatingOn);

        movieList.clear();
        if(query.isBlank()){
            movieList.addAll(movieManager.getAllMovies());
            return;
        }
       movieList.addAll((searchResults));
    }

    public ObservableList<Category> getCategoryList() throws CategoryException {
        return FXCollections.observableArrayList(movieManager.getAllCategories());
    }

    public void updateLastView(Movie movie) throws MovieException {
        movieManager.updateLastView(movie);
    }
}

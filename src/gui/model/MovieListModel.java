package gui.model;

import be.Category;
import be.CategoryException;
import be.Movie;
import be.MovieException;
import bll.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieListModel {
    private MovieManager movieManager;
    private ObservableList<MovieModel> movieList;

    public MovieListModel() throws IOException, MovieException {
        movieManager = new MovieManager();
        movieList = FXCollections.observableArrayList(movieManager.getAllMovies().stream().map(movie -> new MovieModel(movie)).toList());
    }

    /**
     * Get all the movies that should be display in the view.
     * @return ObservableList of Songmodel
     */
    public ObservableList<MovieModel> getMovieList() {
        return movieList;
    }

    /**
     * Create new song to be display in main view
     * @param name
     * @param IMDBRating
     * @param pathToFile
     */
    public void createMovie(String name, double IMDBRating, String pathToFile) throws MovieException {
        movieList.add(new MovieModel(movieManager.createMovie(name, IMDBRating, pathToFile)));
    }

    /**
     * Delete the song in mainview and database
     * @param movieModel
     */
    public void deleteMovie(MovieModel movieModel) throws MovieException {
        movieManager.deleteMovie(movieModel.convertToMovie());
        movieList.remove(movieModel);
    }

    /**
     * Update the song in mainview and database
     * @param movieModel
     * @param name
     * @param imdbRating
     * @param personalRating
     */
    public void updateMovie(MovieModel movieModel, String name, double imdbRating, double personalRating, ObservableList<CategoryModel> categories) throws MovieException {
        if(movieManager.checkUpdatedValues(imdbRating, personalRating)){
            movieModel.setNameProperty(name);
            movieModel.setIMDBRatingProperty(imdbRating);
            movieModel.setPersonalRatingProperty(personalRating);
            movieModel.setCategories(categories);
            movieManager.updateMovie(movieModel.convertToMovie());
        }
    }

    /**
     * Searches through song list, to find a Movie that matches the key word
     * @param query the key word, to search for
     */
    public void searchMovie(String query, boolean isTitleOn, List<String> selectedCategoreis, boolean isRatingOn) throws MovieException {
        List<MovieModel> searchResults = movieManager.searchMovie(query, isTitleOn, selectedCategoreis, isRatingOn).stream().map(movie ->
                new MovieModel(movie)).toList();


        movieList.clear();
        if(query.isBlank()){
            movieList.addAll(movieManager.getAllMovies().stream().map(movie -> new MovieModel(movie)).toList());
            return;
        }
       movieList.addAll(searchResults);
    }

    public void filterCategories(List<String> selectedCategoreis) throws MovieException {
        List<MovieModel> searchResults = movieManager.filterCategories(selectedCategoreis).stream().map(movie ->
                new MovieModel(movie)).toList();

        movieList.clear();
        if(selectedCategoreis.isEmpty()){
            movieList.addAll(movieManager.getAllMovies().stream().map(movie -> new MovieModel(movie)).toList());
        }else {
            movieList.addAll(searchResults);
        }
    }

    public ObservableList<CategoryModel> getCategoryList() throws CategoryException {
        return FXCollections.observableArrayList(movieManager.getAllCategories().stream().map(category -> new CategoryModel(category)).toList());
    }

    public void updateLastView(MovieModel movieModel) throws MovieException {
        movieManager.updateLastView(movieModel.convertToMovie());
    }
}

package bll;

import be.Category;
import be.CategoryException;
import be.Movie;
import be.MovieException;
import bll.util.ISearcher;
import bll.util.MovieSearcher;
import dal.db.DAOCategory;
import dal.db.DAOMovie;
import gui.model.CategoryModel;
import javafx.collections.ObservableList;


import java.io.IOException;
import java.util.List;

import static be.DisplayMessage.displayMessage;

public class MovieManager {
    private DAOMovie daoMovie;
    private DAOCategory daoCategory;
    private ISearcher movieSearcher;


    public MovieManager() throws IOException {
        movieSearcher = new MovieSearcher();
        daoMovie = new DAOMovie();
        daoCategory = new DAOCategory();
    }

    public List<Movie> getAllMovies() throws MovieException {
        return daoMovie.getAllMovies();
    }

    public Movie createMovie(String name, double imdbRating, String pathToFile) throws MovieException {
        return daoMovie.createMovie(name, imdbRating, pathToFile);
    }

    public void deleteMovie(Movie movie) throws MovieException {
        daoMovie.deleteMovie(movie);
    }

    public boolean checkUpdatedValues(double imdbRating, double personalRating){
        if (imdbRating <= 10.0 && imdbRating >= 0.0) {
            if (personalRating <= 10.0 && personalRating >= 0.0 || personalRating == -1.0) {
                return true;
            } else displayMessage("Personal rating must be a number between 0-10");
        } else displayMessage("IMDB rating must be a number between 0-10");
        return false;
    }

    public void updateMovie(Movie movie, ObservableList<CategoryModel> categories) throws MovieException {
        daoMovie.updateMovie(movie, categories);
    }


    /**
     * Searches through movie list, to find a song that matches the key word

     * @param query the key word, to search for
     * @return a list of songs that fit, the key word
     */
    public List<Movie> searchMovie(String query, boolean isTitleOn, boolean isCatOn, boolean isRatingOn) throws MovieException {
        List<Movie> allMovies = daoMovie.getAllMovies();
        List<Movie> searchResult = movieSearcher.search(allMovies, query, isTitleOn, isCatOn, isRatingOn);
        return searchResult;
    }

    public List<Category> getAllCategories() throws CategoryException {
        return daoCategory.getAllCategorys();
    }

    public CategoryModel addCategory(Category category, Movie movie) throws MovieException {
        daoMovie.addCategoryToMovie(category, movie);
        System.out.println("yoyoyo");
        return new CategoryModel(category);
    }
}

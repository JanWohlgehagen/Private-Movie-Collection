package dal.interfaces;

import be.Movie;
import be.MovieException;
import gui.model.CategoryModel;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;

public interface IMovieRepository {

    String ERROR_STRING = "Error: Cannot access database.";

    public List<Movie> getAllMovies() throws MovieException;

    public Movie createMovie(String name, double IMDBRating, String pathToFile) throws MovieException;

    public void updateMovie(Movie movie, ObservableList<CategoryModel> categories) throws MovieException;

    public void deleteMovie(Movie movie) throws MovieException;
}

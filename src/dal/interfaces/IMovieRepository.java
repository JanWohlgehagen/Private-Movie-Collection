package dal.interfaces;

import be.Movie;

import java.util.List;

public interface IMovieRepository {

    public List<Movie> getAllMovies();

    public Movie createMovie(String name, double IMDBRating, String pathToFile);

    public void updateMovie(Movie movie);

    public void deleteMovie(Movie movie);
}

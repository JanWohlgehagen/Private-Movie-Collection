package bll;

import be.*;
import bll.util.ISearcher;
import bll.util.MovieSearcher;
import dal.db.DAOCategory;
import dal.db.DAOMovie;


import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static be.DisplayMessage.displayMessage;

public class MovieManager {
    private DAOMovie daoMovie;
    private DAOCategory daoCategory;

    public MovieManager() throws IOException {
        daoMovie = new DAOMovie();
        daoCategory = new DAOCategory();
    }

    public List<Movie> getAllMovies(){
        List <Movie> allMovies = daoMovie.getAllMovies();
        List <Movie> oldMovies = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.minusYears(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (Movie movie:allMovies) {
            if(movie.getLastViewProperty().get() != null){
                if(movie.getLastViewProperty().get().before(date) && movie.getPersonalRatingProperty().get() < 6.0){ // check if the date of when the movie was last viewed is before the date object (2 years ago)
                    oldMovies.add(movie);
                }
            }
        }
        if(!oldMovies.isEmpty()){
            if(DisplayMessage.displayDeleteOldMoives(oldMovies)){ // prompt the user to delete the old movies
                for (Movie movie: oldMovies ) {
                    daoMovie.deleteMovie(movie);
                    allMovies.remove(movie);
                }
            }
        }
        return allMovies;
    }

    public Movie createMovie(String name, double imdbRating, String pathToFile) {
        return daoMovie.createMovie(name, imdbRating, pathToFile);
    }

    public void deleteMovie(Movie movie) {
        daoMovie.deleteMovie(movie);
    }

    public boolean checkUpdatedValues(double imdbRating, double personalRating){
        if (imdbRating <= 10.0 && imdbRating >= 0.0) {
            if (personalRating <= 10.0 && personalRating >= 0.0 || personalRating == -1.0) {
                return true;
            } else  DisplayMessage.displayMessage("Personal rating must be a number between 0-10");
        } else  DisplayMessage.displayMessage("IMDB rating must be a number between 0-10");
        return false;
    }

    public void updateMovie(Movie movie) {
        daoMovie.updateMovie(movie);
    }

    public List<Movie> filterCategories(List<String> selectedCategories) {
        List<Movie> movieList = daoMovie.getMoviesWithSelectedCategories(selectedCategories);
        //Creates a list of categories containing only categories that match the movie's list, if the compared list is empty it did not match any of the categories picked to search for,
        //and if the compared list is smaller than the selected categories it means that more categories are being requested than the movie contains.
        List<Movie> tempList = new ArrayList<>(movieList);
        for (Movie movie : tempList) {
            var comparedList = movie.getAllCategoryAsList().stream().filter(category -> selectedCategories.contains(category.getNameProperty().get())).toList();
            if (comparedList.isEmpty() || comparedList.size() < selectedCategories.size()) {
                movieList.remove(movie);
            }
        }
        return movieList;
    }

    public List<Category> getAllCategories() {
        return daoCategory.getAllCategories();
    }

    public void updateLastView(Movie movie) {
        daoMovie.updateLastview(movie);
    }
}

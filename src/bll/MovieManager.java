package bll;

import be.*;
import bll.util.ISearcher;
import bll.util.MovieSearcher;
import dal.db.DAOCategory;
import dal.db.DAOMovie;
import gui.model.CategoryModel;
import gui.model.MovieModel;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;


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
    private ISearcher movieSearcher;


    public MovieManager() throws IOException {
        movieSearcher = new MovieSearcher();
        daoMovie = new DAOMovie();
        daoCategory = new DAOCategory();
    }

    public List<Movie> getAllMovies() throws MovieException {
        List <Movie> allMovies = daoMovie.getAllMovies();
        List <Movie> oldMovies = new ArrayList<>();
        LocalDate currentData = LocalDate.now();
        Date date = Date.from(currentData.minusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (Movie movie:allMovies) {
            if(movie.getLastView() != null){
                if(movie.getLastView().before(date)){
                    oldMovies.add(movie);
                }
            }
        }
        if(DisplayMessage.displayDeleteOldMoives(oldMovies)){
            for (Movie movie: oldMovies ) {
                daoMovie.deleteMovie(movie);
                allMovies.remove(movie);
            }
        }
        return allMovies;
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

    public void updateMovie(Movie movie) throws MovieException {
        daoMovie.updateMovie(movie);
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

    public void updateLastView(Movie movie) throws MovieException {
        daoMovie.updateLastview(movie);
    }

    /*
    public void OldMovies() throws MovieException {
        List<Movie> allMovies = daoMovie.getAllMovies();
        List<String> moviesToDeleteString = new ArrayList<>();
        List<Movie> moviesToDelete = new ArrayList<>();
        LocalDate currentData = LocalDate.now();
        Date date = Date.from(currentData.minusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant());
        for (Movie movie:allMovies) {

            if(movie.getLastView() != null){
                System.out.println(movie);
                if(movie.getLastView().before(date)){
                    moviesToDelete.add(movie);

                    moviesToDeleteString.add(movie.getName());
                }
            }
        }

        if(!DisplayMessage.displayDeleteOldMoives(moviesToDeleteString)){
        for (Movie movie: moviesToDelete ) {
                daoMovie.deleteMovie(movie);
            }
        }


    } */
}

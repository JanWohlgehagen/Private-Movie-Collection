package dal.db;

import be.Category;
import be.Movie;
import be.MovieException;
import dal.interfaces.IMovieRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOMovie implements IMovieRepository {


    private MyConnection databaseConnector;

    public DAOMovie() throws IOException {
        databaseConnector = new MyConnection();
    }

    @Override
    public List<Movie> getAllMovies() throws MovieException {

        List<Movie> allMovies = new ArrayList<>();

        //Create a connection
        try(Connection connection = databaseConnector.getConnection()){
            String sql = "SELECT * FROM Movie;";
            String sql1 = "SELECT * FROM Category FULL JOIN CatMovie ON Category.title = CatMovie.catTitle WHERE CatMovie.movieId = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1); //Create statement

            //Extract data from DB
            if(preparedStatement.execute()){
                ResultSet resultSet = preparedStatement.getResultSet();
                while(resultSet.next()){
                    ObservableList<Category> categories = FXCollections.observableArrayList();
                    int movieId = resultSet.getInt("id");
                    String movieTitle = resultSet.getString("title");
                    double IMDBrating = resultSet.getDouble("IMDBrating");
                    double personalrating = resultSet.getDouble("personalrating");
                    String filepath = resultSet.getString("filepath");
                    Date lastview = (Date) resultSet.getObject("lastview");

                    Movie movie = new Movie(movieId, movieTitle, IMDBrating, filepath);
                    movie.setPersonalRatingProperty(personalrating);
                    movie.setLastViewProperty(lastview);

                    preparedStatement1.setInt(1,movieId);
                    if(preparedStatement1.execute()){
                        ResultSet resultSet1 = preparedStatement1.getResultSet();
                        while(resultSet1.next()) {
                            String catTitle = resultSet1.getString("title");

                            movie.addCategory(new Category(catTitle));
                        }
                    }
                    allMovies.add(movie);
                }
            }
        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
        return allMovies;
    }

    @Override
    public Movie createMovie(String name, double IMDBRating, String pathToFile) throws MovieException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Movie VALUES (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, IMDBRating);
            preparedStatement.setDouble(3, -1); // Personal Rating -default to -1 when it is created
            preparedStatement.setString(4, pathToFile);
            preparedStatement.setObject(5,null);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    return new Movie(id, name, IMDBRating, pathToFile);
                }
            }
        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws MovieException {

        try(Connection connection = databaseConnector.getConnection()) {

            String sql = "UPDATE Movie SET title = ?, filepath=?, IMDBrating=?, personalrating=? WHERE Id=?;";
            String sqlDel = "DELETE FROM CatMovie WHERE movieId = ?";
            PreparedStatement preparedStatementForDelete = connection.prepareStatement(sqlDel);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, movie.getNameProperty().get());
            preparedStatement.setString(2, movie.getPathToFileProperty().getName());
            preparedStatement.setDouble(3, movie.getIMDBRatingProperty().get());
            preparedStatement.setDouble(4, movie.getPersonalRatingProperty().get());
            preparedStatement.setDouble(5, movie.getIdProperty().get());

            preparedStatementForDelete.setInt(1, movie.getIdProperty().get());
            preparedStatementForDelete.executeUpdate();
            int affectedRows = preparedStatement.executeUpdate();
            for (Category cat: movie.getAllCategoryAsList()) {
                addCategoryToMovie(cat, movie);

            }
            if(affectedRows != 1) {
                throw new MovieException("Too many row affected");
            }
        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
    }

    public void updateLastview(Movie movie) throws MovieException {

        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Movie SET lastview = ? WHERE Id= ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, movie.getLastViewProperty().get());
            preparedStatement.setDouble(2, movie.getIdProperty().get());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows != 1) {
                throw new MovieException("Too many row affected");
            }
        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
    }

    public List<Movie> getMoviesWithSelectedCategories(List<String> selectedCategories) throws MovieException {

        try(Connection connection = databaseConnector.getConnection()) {
            List<Movie> movieList = new ArrayList<>();
            for (String category: selectedCategories) {
                String sql = "SELECT * FROM Movie FULL JOIN CatMovie ON Movie.id = CatMovie.movieId WHERE CatMovie.catTitle = ?";
                String sql1 = "SELECT * FROM Category FULL JOIN CatMovie ON Category.title = CatMovie.catTitle WHERE CatMovie.movieId = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1); //Create statement
                preparedStatement.setString(1, category);

                preparedStatement.execute();

                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    int movieId = resultSet.getInt("id");
                    String movieTitle = resultSet.getString("title");
                    double IMDBrating = resultSet.getDouble("IMDBrating");
                    double personalrating = resultSet.getDouble("personalrating");
                    String filepath = resultSet.getString("filepath");
                    Date lastview = (Date) resultSet.getObject("lastview");

                    Movie movie = new Movie(movieId, movieTitle, IMDBrating, filepath);
                    movie.setPersonalRatingProperty(personalrating);
                    movie.setLastViewProperty(lastview);

                    preparedStatement1.setInt(1, movieId);
                    if(preparedStatement1.execute()){
                        ResultSet resultSet1 = preparedStatement1.getResultSet();
                        while(resultSet1.next()) {
                            String catTitle = resultSet1.getString("title");

                            movie.addCategory(new Category(catTitle));
                        }
                    }

                    boolean isFound = true;
                    for (Movie movieFromList: movieList) {
                        if (movieFromList.getNameProperty().get().equals(movie.getNameProperty().get())) {
                            isFound = false;
                            break;
                        }
                    }
                    if(isFound){
                        movieList.add(movie);
                    }
                }
            }
            return movieList;
        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
    }

    public void addCategoryToMovie(Category category, Movie movie) throws MovieException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO CatMovie VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getNameProperty().get());
            preparedStatement.setInt(2, movie.getIdProperty().get());
            preparedStatement.executeUpdate();
        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
    }


    @Override
    public void deleteMovie(Movie movie) throws MovieException {

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE Movie WHERE id = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, movie.getIdProperty().get());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows != 1){
                throw new MovieException("Too many row affected");
            }

        } catch (SQLException SQLex) {
            throw new MovieException(ERROR_STRING, SQLex.fillInStackTrace());
        }
    }
}

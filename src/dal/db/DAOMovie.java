package dal.db;

import be.Category;
import be.DisplayMessage;
import be.Movie;
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

    /**
     * Constructer that establishes a connection to the database though MyConnetion.
     * @throws IOException
     */
    public DAOMovie() throws IOException {
        databaseConnector = new MyConnection();
    }

    /**
     * Queries the database for all movies in the Movie table.
     * @return a List of all Movie objects in the database
     */
    @Override
    public List<Movie> getAllMovies() {
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
            if(DisplayMessage.displayErrorSTOP(SQLex)){
                System.exit(0);
            }
        }
        return allMovies;
    }

    /**
     * Creates a movie by getting setting the name, IMDBRating and path file and fetches the ID that the database assigns it to create
     * a new Movie object to be returned to the BLL layer and then later to the GUI layer.
     * @param name the title of a movie
     * @param IMDBRating the IMDB Rating of a movie
     * @param pathToFile the path of the local file to the movie
     * @return
     */
    @Override
    public Movie createMovie(String name, double IMDBRating, String pathToFile) {
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
            if (DisplayMessage.displayErrorSTOP(SQLex)) {
                System.exit(0);
            }
        }
        return null;
    }

    /**
     * Overrides all rows in the Movie table referenced by ID with the fields of an 'updated' Movie object
     * @param movie with updated fields
     */
    @Override
    public void updateMovie(Movie movie){
        try(Connection connection = databaseConnector.getConnection()) {

            String sql = "UPDATE Movie SET title = ?, filepath=?, IMDBrating=?, personalrating=? WHERE Id=?;";
            String sqlDel = "DELETE FROM CatMovie WHERE movieId = ?";
            PreparedStatement preparedStatementForDelete = connection.prepareStatement(sqlDel);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, movie.getNameProperty().get());
            preparedStatement.setString(2, movie.getPathToFileProperty().get());
            preparedStatement.setDouble(3, movie.getIMDBRatingProperty().get());
            preparedStatement.setDouble(4, movie.getPersonalRatingProperty().get());
            preparedStatement.setDouble(5, movie.getIdProperty().get());
            Integer.parseInt("d");
            preparedStatementForDelete.setInt(1, movie.getIdProperty().get());
            preparedStatementForDelete.executeUpdate();
            for (Category cat: movie.getAllCategoryAsList()) {
                addCategoryToMovie(cat, movie);
            }
            DisplayMessage.displayMessage("The movie is update");
        } catch (Exception SQLex) {
            DisplayMessage.displayError(SQLex);
        }
    }

    /**
     * Updates the time the movie was last viewed, this happens when you press play on a movie in the view
     * @param movie
     */
    public void updateLastview(Movie movie) {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Movie SET lastview = ? WHERE Id= ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, movie.getLastViewProperty().get());
            preparedStatement.setDouble(2, movie.getIdProperty().get());

            preparedStatement.executeUpdate();
        } catch (SQLException SQLex) {
            DisplayMessage.displayError(SQLex);
        }
    }

    /**
     * Fetches all movies from a resultset from the Movie table joined with the CatMovie table where the title of the category is in the list of categories provided in the parameters.
     * Fetches all categories from a resultset from the Category table joined with the CatMovie table where the movieId matches the Movies was fetched with the first preparedstatement.
     * A loop then loops through the list of movies and deletes duplicates.
     * A list is then created by comparing every movie object's categories with the list that is passed to this method in its parameters and adding every category to that list.
     * If the list is empty no matches were found and the Movie is removed from the list that will be returned. If the list with category matches is smaller in size than that passed to this method.
     * It means the movie matches less categories than what was requested in the view and the movie is filtered out.
     *
     * The idea behind this is, that you can search for two categories e.g. horror and action and all movies that have both of those categories will show up, but a movie that does not have
     * them both would be filtered out.
     * @param selectedCategories a list of Strings that represents the categories a user has requested to filter.
     * @return a list of movie objects
     */
    public List<Movie> getMoviesWithSelectedCategories(List<String> selectedCategories) {
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

                    //Iterates over the list that is to be returned and only adds the movie to the list if it is not already in the lost (so it does not add duplicates).
                    boolean isFound = false;
                    for (Movie movieFromList: movieList) {
                        if (movieFromList.getNameProperty().get().equals(movie.getNameProperty().get())) {
                            isFound = true;
                            break;
                        }
                    }
                    if(!isFound){
                        movieList.add(movie);
                    }
                }
            }

            //Creates a list of categories containing only categories that match the movie's list, if the compared list is empty it did not match any of the categories picked to search for,
            //and if the compared list is smaller than the selected categories it means that more categories are being requested than the movie contains.
            List<Movie> tempList = new ArrayList<>(movieList);
            for (Movie movie : tempList) {
                List<Category> comparedList = movie.getAllCategoryAsList().stream().filter(category -> selectedCategories.contains(category.getNameProperty().get())).toList();
                if (comparedList.isEmpty() || comparedList.size() < selectedCategories.size()) {
                    movieList.remove(movie);
                }
            }
            
            return movieList;
        } catch (SQLException SQLex) {
            DisplayMessage.displayError(SQLex);
        }
        return null;
    }


    /**
     * Adds a category to the list by providing a movieId and adding a category to it.
     * @param category a category object
     * @param movie a movie object
     */
    public void addCategoryToMovie(Category category, Movie movie){
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO CatMovie VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getNameProperty().get());
            preparedStatement.setInt(2, movie.getIdProperty().get());
            preparedStatement.executeUpdate();
        } catch (SQLException SQLex) {
            DisplayMessage.displayError(SQLex);
        }
    }

    /**
     * Deletes a movie from the Movie table where the ID is that of the movie provided in the method's parameters.
     * @param movie
     */
    @Override
    public void deleteMovie(Movie movie) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE Movie WHERE id = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, movie.getIdProperty().get());

            preparedStatement.executeUpdate();

        } catch (SQLException SQLex) {
            DisplayMessage.displayError(SQLex);
        }
    }
}

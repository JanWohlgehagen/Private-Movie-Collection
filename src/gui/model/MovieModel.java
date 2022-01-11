package gui.model;

import be.Movie;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class MovieModel {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty IMDBRating = new SimpleDoubleProperty();
    private StringProperty pathToFile = new SimpleStringProperty();
    private ObjectProperty lastView = new SimpleObjectProperty<Date>();
    private DoubleProperty personalRating = new SimpleDoubleProperty();
    private ObservableList<CategoryModel> categorys;
    private StringProperty catInString = new SimpleStringProperty();

    public MovieModel(Movie movie){
        setIdProperty(movie.getId());
        setNameProperty(movie.getName());
        setIMDBRatingProperty(movie.getIMDBRating());
        setPathToFileProperty(movie.getPathToFile());
        setLastViewProperty(movie.getLastView());
        setPersonalRatingProperty(movie.getPersonalRating());
        categorys = FXCollections.observableArrayList(movie.getCategories().stream().map(cat -> new CategoryModel(cat)).toList());
    }

    /**
     * Used for setting the id of the Movie, only used when a movie is created.
     * @param id
     */
    private void setIdProperty(int id){
        getIdProperty().set(id);
    }

    /**
     * Used for getting the id of the movie.
     * @return
     */
    public IntegerProperty getIdProperty() {
        return id;
    }

    /**
     * Used for setting the name of the movie.
     * @param name
     */
    public void setNameProperty(String name){
        getNameProperty().set(name);
    }

    /**
     * Used for getting the name of the movie.
     * @return the name of the movie
     */
    public StringProperty getNameProperty(){
        return name;
    }

    /**
     * Used for getting the IMDB rating of the movie.
     * @param IMDBRating
     */
    public void setIMDBRatingProperty(double IMDBRating){
        getIMDBRatingProperty().set(IMDBRating);
    }

    /**
     * Used for getting the IMDB rating of the movie.
     * @return the IMDB rating of the movie
     */
    public DoubleProperty getIMDBRatingProperty() {
        return IMDBRating;
    }

    /**
     * Used for setting the path of the movie locally.
     * @param pathToFile
     */
    public void setPathToFileProperty(String pathToFile){
        getPathToFileProperty().set(pathToFile);
    }

    /**
     * Used for getting the path of the movie.
     * @return the file path of the movie
     */
    public StringProperty getPathToFileProperty() {
        return pathToFile;
    }

    /**
     * used for getting the Date of when the song was viewed last.
     * @return returns a ObjectProperty of type Date of when the movie was last viewed.
     */
    public ObjectProperty getLastViewProperty() {
        return lastView;
    }

    /**
     * sets the Date of when the movie was last viewed.
     * @param lastView
     */
    public void setLastViewProperty(Date lastView) {
        getLastViewProperty().set(lastView);
    }

    /**
     * sets the personal rating of when the movie.
     * @param personalRating a double
     */
    public void setPersonalRatingProperty(double personalRating) {
        getPersonalRatingProperty().set(personalRating);
    }

    /**
     * used for getting the personal rating of the movie.
     * @return returns a DoubleProperty of the movie.
     */
    public DoubleProperty getPersonalRatingProperty() {
        return personalRating;
    }

    public StringProperty getAllCategorysAsString(){
        StringBuilder newList = new StringBuilder();
        for (CategoryModel catModel: categorys) {
            if(catModel == categorys.get(categorys.size()-1)){
                newList.append(catModel);
            } else newList.append(catModel).append(", ");
        }
        catInString.set(newList.toString());
        return catInString;
    }


    public ObservableList<CategoryModel> getAllCategoryAsList(){
        return categorys;
    }


    public void addCatrgroyModel(CategoryModel categoryModel){
        categorys.add(categoryModel);
    }

    /**
     * Used for converting a movieModel into a movie object, mainly for storage in DB
     * @return a movie object with the same fields as the movieModel
     */
    public Movie convertToMovie(){
        return new Movie(id.get(), name.get(), IMDBRating.get(), pathToFile.get());

    }

    public void setCategories(ObservableList<CategoryModel> categories) {
        this.categorys = categories;
    }
}

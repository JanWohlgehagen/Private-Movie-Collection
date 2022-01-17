package be;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Date;

/**
 * A Movie object holds the essential data a movie must contain in the app.
 */
public class Movie {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty IMDBRating = new SimpleDoubleProperty();
    private StringProperty pathToFile = new SimpleStringProperty();
    private ObjectProperty<Date> lastView = new SimpleObjectProperty<>();
    private DoubleProperty personalRating = new SimpleDoubleProperty();
    private ObservableList<Category> categoryList;
    private StringProperty categoriesAsString = new SimpleStringProperty();

    /**
     *  constructor for Movie used for creating an instance of a Movie.
     * @param id the int id of the movie.
     * @param name the name of the movie.
     * @param IMDBRating the IMDBRating of the movie.
     * @param pathToFile the local location of the movie.
     */
    public Movie(int id, String name, double IMDBRating, String pathToFile) {
        this.id.set(id);
        this.name.set(name);
        this.IMDBRating.set(IMDBRating);
        this.pathToFile.set(pathToFile);
        this.lastView.set(null);
        this.personalRating.set(-1); //if the personal rating is -1 it has not been rated
        this.categoryList = FXCollections.observableArrayList();
    }

    /**
     * Used for getting the id of the movie.
     * @return
     */
    public IntegerProperty getIdProperty() {
        return this.id;
    }

    /**
     * Used for setting the name of the movie.
     * @param name
     */
    public void setNameProperty(String name){
        this.name.set(name);
    }

    /**
     * Used for getting the name of the movie.
     * @return the name of the movie
     */
    public StringProperty getNameProperty(){
        return this.name;
    }

    /**
     * Used for getting the IMDB rating of the movie.
     * @param IMDBRating
     */
    public void setIMDBRatingProperty(double IMDBRating){
        this.IMDBRating.set(IMDBRating);
    }

    /**
     * Used for getting the IMDB rating of the movie.
     * @return the IMDB rating of the movie
     */
    public DoubleProperty getIMDBRatingProperty() {
        return this.IMDBRating;
    }


    /**
     * Used for getting the path of the movie.
     * @return the file path of the movie
     */
    public StringProperty getPathToFileProperty() {
        return this.pathToFile;
    }

    /**
     * used for getting the Date of when the song was viewed last.
     * @return returns a ObjectProperty of type Date of when the movie was last viewed.
     */
    public ObjectProperty<Date> getLastViewProperty() {
        return this.lastView;
    }

    /**
     * sets the Date of when the movie was last viewed.
     * @param lastView
     */
    public void setLastViewProperty(Date lastView) {
        this.lastView.set(lastView);
    }

    /**
     * sets the personal rating of when the movie.
     * @param personalRating a double
     */
    public void setPersonalRatingProperty(double personalRating) {
        this.personalRating.set(personalRating);
    }

    /**
     * used for getting the personal rating of the movie.
     * @return returns a DoubleProperty of the movie.
     */
    public DoubleProperty getPersonalRatingProperty() {
        return this.personalRating;
    }

    /**
     * @return StringProperty category names concatenated in to one StringProperty
     */
    public StringProperty getAllCategoriesAsStringProperty(){
        updateCategoriesAsStringProperty(); // Updates the String that represent the list of categories the Movie object contains, used to show in the TableView in the main menu
        return categoriesAsString;
    }

    /**
     * @return ObservableList with category objects
     */
    public ObservableList<Category> getAllCategoryAsList(){
        return this.categoryList;
    }

    /**
     * Adds a category to the categoryList
     * @param category Object
     */
    public void addCategory(Category category){
        this.categoryList.add(category);
    }

    /**
     * Overrides the items that are currently in the list of Categories and set them to the list provided.
     * @param categoryList an Observable List of Category objects
     */
    public void setCategoryList(ObservableList<Category> categoryList) {
        this.categoryList.setAll(categoryList);
        updateCategoriesAsStringProperty(); // Updates the String that represent the list of categories the Movie object contains, used to show in the TableView in the main menu
    }

    /**
     * Updates the concatenated StringProperty by iterating over this objects list of categories
     */
    private void updateCategoriesAsStringProperty() {
        StringBuilder sb = new StringBuilder();
        for (Category cat: categoryList) {
            if(cat == categoryList.get(categoryList.size()-1)){
                sb.append(cat);
            } else sb.append(cat).append(", ");
        }
        categoriesAsString.set(sb.toString());
    }

    /**
     * Returns the String representation of the fields of this object
     * @return
     */
    @Override
    public String toString() {
        return id + " " +  name + " " + IMDBRating + " " +  pathToFile + " " +  lastView + " " +
                personalRating + " " + categoryList;
    }
}

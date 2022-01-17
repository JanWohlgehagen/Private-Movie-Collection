package be;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A Category is the object that represents a category of a movie.
 */
public class Category {

    private StringProperty name = new SimpleStringProperty();

    /**
     * Constructor for a Category
     * @param name
     */
    public Category(String name) {
        this.name.set(name);
    }

    /**
     * used for getting the name of a category
     * @return name a StringProperty
     */
    public StringProperty getNameProperty() {
        return this.name;
    }

    /**
     * @return String property of StringProperty
     */
    @Override
    public String toString() {
        return this.name.get();
    }
}

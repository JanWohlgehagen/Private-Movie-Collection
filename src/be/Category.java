package be;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class  Category {

    private StringProperty name = new SimpleStringProperty();

    /**
     * Constructor for a CategoryModel
     * @param name
     */
    public Category(String name){
        this.name.set(name);
    }

    /**
     * used for getting the name of a category
     * @return name a StringProperty
     */
    public StringProperty getNameProperty() {
        return this.name;
    }


    @Override
    public String toString() {
        return this.name.get();
    }
}

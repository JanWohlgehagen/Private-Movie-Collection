package gui.model;

import be.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoryModel {
    private StringProperty name = new SimpleStringProperty();

    /**
     * Constructor for a CategoryModel
     * @param category
     */
    public CategoryModel(Category category){
        setNameProperty(category.getName());
    }

    /**
     * used for getting the name of a category
     * @return name a StringProperty
     */
    public StringProperty getNameProperty() {
        return name;
    }

    /**
     * used for setting the name of a category
     * @param name a String
     */
    public void setNameProperty(String name) {
        getNameProperty().set(name);
    }


    @Override
    public String toString() {
        return name.get();
    }

    public Category convertToCategory(){
        return new Category(name.get());
    }
}

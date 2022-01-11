package gui.model;

import be.Category;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoryModel {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();

    /**
     * Constructor for a CategoryModel
     * @param category
     */
    public CategoryModel(Category category){
        setIdProperty(category.getId());
        setNameProperty(category.getName());
    }

    /**
     * used for getting the id of the category
     * @return id an IntegerProperty
     */
    public IntegerProperty getIdProperty() {
        return id;
    }

    /**
     * used for setting the id of the category
     * @param id an int
     */
    public void setIdProperty(int id) {
        getIdProperty().set(id);
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

    /**
     * used for converting a categoryModel to a category, mainly for storing it in DB
     * @return a new Category object with the same fields as this CategoryModel
     */
    public Category convertToCategory(){
        return new Category(this.id.get(), this.name.get());
    }

    @Override
    public String toString() {
        return name.get();
    }
}

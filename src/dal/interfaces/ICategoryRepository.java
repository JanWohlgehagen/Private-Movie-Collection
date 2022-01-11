package dal.interfaces;

import be.Category;
import be.CategoryException;

import java.util.List;

public interface ICategoryRepository {

    String ERROR_STRING = "Error: Cannot access database.";

    public List<Category> getAllCategorys() throws CategoryException;

   // public void updateCategory(Category category) throws CategoryException;

    public void deleteCategory(Category category) throws CategoryException;

}

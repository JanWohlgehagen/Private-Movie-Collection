package dal.interfaces;

import be.Category;

import java.util.List;

public interface ICategoryRepository {

    public List<Category> getAllCategories();

    public void deleteCategory(Category category);

}

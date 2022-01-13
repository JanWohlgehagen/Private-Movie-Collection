package dal.db;

import be.Category;
import be.CategoryException;
import dal.interfaces.ICategoryRepository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCategory implements ICategoryRepository {

    private MyConnection databaseConnector;

    public DAOCategory() throws IOException {
        this.databaseConnector = new MyConnection();
    }

    @Override
    public List<Category> getAllCategories() throws CategoryException {

        List<Category> allCategorys = new ArrayList<>();

        try(Connection connection = databaseConnector.getConnection()){
            String sql = "SELECT * FROM Category;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Extract data from DB
            if(preparedStatement.execute()){
                ResultSet resultSet = preparedStatement.getResultSet();
                while(resultSet.next()){
                    String title = resultSet.getString("title");
                    allCategorys.add(new Category(title));
                }
            }
        } catch (SQLException SQLex) {
            throw new CategoryException(ERROR_STRING, SQLex.fillInStackTrace());
        }
        return allCategorys;
    }

    @Override
    public void deleteCategory(Category category) throws CategoryException {

        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE Category WHERE title = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getNameProperty().get());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows != 1) {
                throw new CategoryException("Too many row affected");
            }
        } catch (SQLException SQLex) {
            throw new CategoryException(ERROR_STRING, SQLex.fillInStackTrace());
        }

    }
}

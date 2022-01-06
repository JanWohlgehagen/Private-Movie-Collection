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

    public DAOCategory(MyConnection databaseConnector) throws IOException {
        this.databaseConnector = new MyConnection();
    }


    @Override
    public List<Category> getAllCategorys() throws CategoryException {

        List<Category> allCategorys = new ArrayList<>();

        try(Connection connection = databaseConnector.getConnection()){
            String sql = "SELECT * FROM Category;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Extract data from DB
            if(preparedStatement.execute()){
                ResultSet resultSet = preparedStatement.getResultSet();
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    allCategorys.add(new Category(id, title));
                }
            }
        } catch (SQLException SQLex) {
            throw new CategoryException(ERROR_STRING, SQLex.fillInStackTrace());
        }
        return allCategorys;
    }

    @Override
    public void createCategory(Category category) throws CategoryException {

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Category VALUES (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getName());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new CategoryException("Too many row affected");
            }
        } catch (SQLException SQLex) {
            throw new CategoryException(ERROR_STRING, SQLex.fillInStackTrace());
        }
    }

    @Override
    public void updateCategory(Category category) throws CategoryException {

        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Category SET title = ? WHERE Id= ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, category.getName());
            preparedStatement.setDouble(2, category.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows != 1) {
                throw new CategoryException("Too many row affected");
            }
        } catch (SQLException SQLex) {
            throw new CategoryException(ERROR_STRING, SQLex.fillInStackTrace());
        }

    }

    @Override
    public void deleteCategory(Category category) throws CategoryException {

        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE Category WHERE Id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, category.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows != 1) {
                throw new CategoryException("Too many row affected");
            }
        } catch (SQLException SQLex) {
            throw new CategoryException(ERROR_STRING, SQLex.fillInStackTrace());
        }

    }
}

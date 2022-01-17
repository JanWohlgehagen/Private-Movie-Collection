package dal.db;

import be.Category;
import be.DisplayMessage;
import dal.interfaces.ICategoryRepository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCategory implements ICategoryRepository {

    private MyConnection databaseConnector;

    /**
     * Constructer that establishes a connection to the database though MyConnetion.
     * @throws IOException
     */
    public DAOCategory() throws IOException {
        this.databaseConnector = new MyConnection();
    }

    /**
     * Returns all the tables in the Category Table
     * @return a List of Category objects
     */
    @Override
    public List<Category> getAllCategories() {
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
            DisplayMessage.displayError(SQLex);
        }
        return allCategorys;
    }

    /**
     * Detes a category from the database by title.
     * @param category
     */
    @Override
    public void deleteCategory(Category category) {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE Category WHERE title = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getNameProperty().get());

            preparedStatement.executeUpdate();
        } catch (SQLException SQLex) {
            DisplayMessage.displayError(SQLex);
        }
    }
}

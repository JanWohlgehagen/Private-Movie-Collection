package gui.controller;

import be.Category;
import be.DisplayMessage;
import be.Movie;
import gui.model.MovieListModel;
import gui.util.SceneSwapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static be.DisplayMessage.displayMessage;

public class MainController implements Initializable {


    @FXML
    private ListView <Category> listViewCategories;
    @FXML
    private ComboBox <Category> comboBoxCategory;

    @FXML
    private TableView<Movie> tvMovies;
    @FXML
    private TableColumn<Movie, String> tcTitle;
    @FXML
    private TableColumn<Movie, String> tcCategory;
    @FXML
    private TableColumn<Movie, Double> tcRating;

    // Checkboxes in te main view
    @FXML
    private CheckBox cbTitle;
    @FXML
    private CheckBox cbCategory;
    @FXML
    private CheckBox cbRating;

    //Checkboxes in the category box
    @FXML
    private CheckBox CheckBoxAction;
    @FXML
    private CheckBox CheckBoxAnimation;
    @FXML
    private CheckBox CheckBoxAdventure;
    @FXML
    private CheckBox CheckBoxHorror;
    @FXML
    private CheckBox CheckBoxCrime;
    @FXML
    private CheckBox CheckBoxSci_Fi;
    @FXML
    private CheckBox CheckBoxSuperhero;
    @FXML
    private CheckBox CheckBoxComedy;
    @FXML
    private CheckBox CheckBoxAction_Comedy;
    @FXML
    private CheckBox CheckBoxThriller;
    @FXML
    private CheckBox CheckBoxDrama;
    @FXML
    private CheckBox CheckBoxFantasy;
    @FXML
    private CheckBox CheckBoxRomance;
    @FXML
    private CheckBox CheckBoxMystery;

    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtIMDBRating;
    @FXML
    private TextField txtPersonalRating;

    @FXML
    private HBox hBoxParrent; //parrent container

    @FXML
    private VBox vBoxControllMenu;
    @FXML
    private VBox vBoxCategories;

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnAddMovie;
    @FXML
    private Button btnDeleteMovie;
    @FXML
    private Button btnEditSave;
    @FXML
    private Button btnEditCancel;
    @FXML
    private Button btnRemoveCategory;

    private MovieListModel movieListModel;
    private SceneSwapper sceneSwapper;
    private ObservableList categoryCache;
    private final double MAX_WINDOW_SIZE = 950.0;



    public MainController() throws IOException {
        movieListModel = new MovieListModel();
        sceneSwapper = new SceneSwapper();
        categoryCache = FXCollections.observableArrayList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hBoxParrent.getChildren().remove(vBoxCategories);
        vBoxControllMenu.getChildren().remove(btnEditCancel);
        vBoxControllMenu.getChildren().remove(btnEditSave);

        tvMovies.setPlaceholder(new Label("No movies found in Database"));

        tvMovies.setItems(movieListModel.getMovieList());
        tcTitle.setCellValueFactory(addMovie -> addMovie.getValue().getNameProperty());
        tcCategory.setCellValueFactory(addMovie -> addMovie.getValue().getAllCategoriesAsStringProperty());
        tcRating.setCellValueFactory(addMovie -> addMovie.getValue().getIMDBRatingProperty().asObject());


        txtTitle.setDisable(true);
        txtIMDBRating.setDisable(true);
        txtPersonalRating.setDisable(true);
        comboBoxCategory.setDisable(true);

        // Listens for changes on the tableview that holds the Movies
        tvMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null){
                categoryCache.clear();
                categoryCache.addAll(newValue.getAllCategoryAsList());
                listViewCategories.setItems(categoryCache);
                txtTitle.setText(newValue.getNameProperty().get());
                txtIMDBRating.setText(String.valueOf(newValue.getIMDBRatingProperty().get()));
                if (newValue.getPersonalRatingProperty().get() == -1) {
                    txtPersonalRating.setText("Not rated yet..!");
                } else {
                    txtPersonalRating.setText(String.valueOf(newValue.getPersonalRatingProperty().get()));
                }
            }

        });

        // Search in all Movies
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                boolean isTitleOn = cbTitle.isSelected();
                boolean isRatingOn = cbRating.isSelected();
                if (!isTitleOn && !isRatingOn) {
                    cbTitle.setSelected(true);
                    isTitleOn = true;
                }
                movieListModel.searchMovie(newValue, isTitleOn, isRatingOn);
            } catch (Exception e) {
                DisplayMessage.displayError(e);
            }
        });
    }

    /**
     * Calls the movieListModel to filter categories
     * @param actionEvent
     */
    public void handleCheckBoxesCategories(ActionEvent actionEvent){
           movieListModel.filterCategories(isCheckBoxsON());
    }

    /**
     * Checks which one of the filters are on and adds them to a list of Strings.
     * @return List of Strings, reflected by which filters are chosen
     */
    public List<String> isCheckBoxsON(){
        List<String> selectedCheckBox = new ArrayList<>();
        if(cbCategory.isSelected()){

            if (CheckBoxAction.isSelected()) {
                selectedCheckBox.add("Action");
            }
            if (CheckBoxAction_Comedy.isSelected()) {
                selectedCheckBox.add("Action-Comedy");
            }
            if (CheckBoxAnimation.isSelected()) {
                selectedCheckBox.add("Animation");
            }
            if (CheckBoxCrime.isSelected()) {
                selectedCheckBox.add("Crime");
            }
            if (CheckBoxHorror.isSelected()) {
                selectedCheckBox.add("Horror");
            }
            if (CheckBoxComedy.isSelected()) {
                selectedCheckBox.add("Comedy");
            }
            if (CheckBoxRomance.isSelected()) {
                selectedCheckBox.add("Romance");
            }
            if (CheckBoxDrama.isSelected()) {
                selectedCheckBox.add("Drama");
            }
            if (CheckBoxFantasy.isSelected()) {
                selectedCheckBox.add("Fantasy");
            }
            if (CheckBoxMystery.isSelected()) {
                selectedCheckBox.add("Mystery");
            }
            if (CheckBoxSci_Fi.isSelected()) {
                selectedCheckBox.add("SCI-FI");
            }
            if (CheckBoxAdventure.isSelected()) {
                selectedCheckBox.add("Adventure");
            }
            if (CheckBoxSuperhero.isSelected()) {
                selectedCheckBox.add("Superhero");
            }
            if (CheckBoxThriller.isSelected()) {
                selectedCheckBox.add("Thriller");
            }
            return selectedCheckBox;
        }
        return selectedCheckBox;
    }

    /**
     * Sets the lastViewed date of the movie that is being played.
     * Changes scene to the MediaPlayer.
     * @param actionEvent
     */
    public void handlePlayMovie(ActionEvent actionEvent) {
        getSelectedMovie().setLastViewProperty(new Date());
        movieListModel.updateLastView(getSelectedMovie());
        sceneSwapper.sceneSwitch(new Stage(), "MediaPlayer.fxml");
    }

    /**
     * Switches to the addNewMovie window
     * @param actionEvent
     */
    public void handleAddMovie(ActionEvent actionEvent) {
        sceneSwapper.sceneSwitch(new Stage(), "addNewMovie.fxml");
    }

    /**
     * Used for editing a selected Movie adds a save and cancel button to the view and hides the delete and add movie from the view.
     * Also enables the fields of the selected movie for editing.
     * @param actionEvent
     */
    public void handleEditMovie(ActionEvent actionEvent) {
        try {
            if(comboBoxCategory.getItems().isEmpty())
            comboBoxCategory.getItems().addAll(movieListModel.getCategoryList());
        } catch (Exception e) {
            DisplayMessage.displayErrorMessage(e, "Failed to fetch categories from the database.");
            return;
        }
        if (tvMovies.getSelectionModel().selectedItemProperty().get() != null) {
            btnRemoveCategory.setDisable(false);
            vBoxControllMenu.getChildren().remove(btnAddMovie);
            vBoxControllMenu.getChildren().remove(btnDeleteMovie);
            vBoxControllMenu.getChildren().add(btnEditSave);
            vBoxControllMenu.getChildren().add(btnEditCancel);

            if (getSelectedMovie().getPersonalRatingProperty().get() == -1) {
                txtPersonalRating.clear();
            }
            enable_Disable_TextFields();
        }else {
            displayMessage("You must choose a movie..!");
        }
    }

    /**
     * Deletes the selected movie from the movieListModel. After a Warning comes up
     * @param actionEvent
     */
    public void handleDeleteMovie(ActionEvent actionEvent){
        if(DisplayMessage.displayWarning("Are you sure you want to delete this movie??\n" + getSelectedMovie().getNameProperty().get())){
            movieListModel.deleteMovie(getSelectedMovie());
        }
    }

    /**
     * Saves the changes made to the selected movie manipulates the view to disable editing of a movie.
     * @param actionEvent
     */
    public void handleEditSave(ActionEvent actionEvent) {
            ObservableList<Category> categories = listViewCategories.getItems();
            Movie movie = getSelectedMovie();
            try {
                double imdbRating = Double.parseDouble(txtIMDBRating.getText());
                double personalRating;
                if(txtPersonalRating.getText().isBlank()){
                    personalRating = -1.0;
                }else{
                    personalRating = Double.parseDouble(txtPersonalRating.getText());
                }
                movie.setCategoryList(categoryCache);
                movieListModel.updateMovie(movie, txtTitle.getText(), imdbRating, personalRating, categories);

                btnRemoveCategory.setDisable(true);
                vBoxControllMenu.getChildren().remove(btnEditSave);
                vBoxControllMenu.getChildren().remove(btnEditCancel);
                vBoxControllMenu.getChildren().add(btnAddMovie);
                vBoxControllMenu.getChildren().add(btnDeleteMovie);

                enable_Disable_TextFields();
            } catch (Exception e){
                DisplayMessage.displayErrorMessage(e, "You must provide a number between 0-10");
            }
    }

    /**
     * Cancels the editing of a movie by manipulating the view.
     * @param actionEvent
     */
    public void handleEditCancel(ActionEvent actionEvent) {
        vBoxControllMenu.getChildren().remove(btnEditSave);
        vBoxControllMenu.getChildren().remove(btnEditCancel);
        vBoxControllMenu.getChildren().add(btnAddMovie);
        vBoxControllMenu.getChildren().add(btnDeleteMovie);
        btnRemoveCategory.setDisable(true);
        enable_Disable_TextFields();
    }

    /**
     * Enables or disables controls in the view, works as a toggle
     */
    private void enable_Disable_TextFields() {
        txtTitle.setDisable(!txtTitle.isDisabled());
        txtIMDBRating.setDisable(!txtIMDBRating.isDisabled());
        txtPersonalRating.setDisable(!txtPersonalRating.isDisabled());
        comboBoxCategory.setDisable(!comboBoxCategory.isDisabled());
        btnEdit.setDisable(!btnEdit.isDisabled());
    }

    /**
     * Unselects the rating checkbox if the user wants to search by title instead of rating
     * @param actionEvent
     */
    public void handleCBTitle(ActionEvent actionEvent) {
        cbRating.setSelected(false);
    }

    /**
     * If the checkbox for categories is not already ticket it increases the size of the scene and adds a panel of checkboxes representing the apps categories.
     * If it is already ticket it removes the panel instead.
     * @param actionEvent
     */
    public void handleCBCategory(ActionEvent actionEvent) {
        if(cbCategory.isSelected()){
            hBoxParrent.getChildren().add(vBoxCategories);
            hBoxParrent.getScene().getWindow().setWidth(MAX_WINDOW_SIZE);
        } else {
            hBoxParrent.getChildren().remove(vBoxCategories);
            hBoxParrent.getScene().getWindow().setWidth(MAX_WINDOW_SIZE-vBoxCategories.getWidth());
        }
    }

    /**
     * Unselects the title checkbox if the user wants to search by rating instead of title
     * @param actionEvent
     */
    public void handleCBRating(ActionEvent actionEvent) {
        cbTitle.setSelected(false);
    }

    /**
     * Listens to events on the combobox that shows the apps categories and adds it to the listview that represents a movie's categories if it does not already exist i that list.
     * @param actionEvent
     */
    public void handleChooseCategory(ActionEvent actionEvent) {
        Category category = comboBoxCategory.getSelectionModel().getSelectedItem();
        boolean matchFound = false;
        for (Category cat : listViewCategories.getItems()) {
            if (cat.getNameProperty().get().equals(category.getNameProperty().get())){
                matchFound = true;
                break;
            }
        }
        if (!matchFound) {
            listViewCategories.getItems().add(category);
        }
    }

    /**
     * Removes a category from the selected Movie.
     * @param actionEvent
     */
    public void handleRemoveCategory(ActionEvent actionEvent) {
        Category category = listViewCategories.getSelectionModel().getSelectedItem();
        if(category != null){
            listViewCategories.getItems().remove(category);
        }
    }

    /**
     * Returns the movieList of the mainView used to add movies to it by the newMovieController.
     * @return
     */
    public MovieListModel getMovieListModel() {
        return movieListModel;
    }

    /**
     * Returns the selected movie from the TableView
     * @return
     */
    public Movie getSelectedMovie(){
        if (tvMovies.getSelectionModel().getSelectedItem() == null){
            tvMovies.getSelectionModel().selectFirst();
        }
        return tvMovies.getSelectionModel().getSelectedItem();
    }
}

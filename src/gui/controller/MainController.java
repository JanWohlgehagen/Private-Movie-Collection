package gui.controller;

import be.Category;
import be.DisplayMessage;
import be.MovieException;
import gui.model.CategoryModel;
import gui.model.MovieListModel;
import gui.model.MovieModel;
import gui.util.SceneSwapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static be.DisplayMessage.displayError;
import static be.DisplayMessage.displayMessage;

public class MainController implements Initializable {


    @FXML
    private ListView <CategoryModel> listViewCategories;
    @FXML
    private ComboBox <CategoryModel> comboBoxCategory;

    @FXML
    private TableView<MovieModel> tvMovies;
    @FXML
    private TableColumn<MovieModel, String> tcTitle;
    @FXML
    private TableColumn<MovieModel, String> tcCategory;
    @FXML
    private TableColumn<MovieModel, Double> tcRating;

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

    private MovieListModel movieListModel;
    private SceneSwapper sceneSwapper;
    private final double MAX_WINDOW_SIZE = 950.0;



    public MainController() throws IOException, MovieException {
        movieListModel = new MovieListModel();
        sceneSwapper = new SceneSwapper();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hBoxParrent.getChildren().remove(vBoxCategories);
        vBoxControllMenu.getChildren().remove(btnEditCancel);
        vBoxControllMenu.getChildren().remove(btnEditSave);

        tvMovies.setPlaceholder(new Label("No movies found in Database"));

        tvMovies.setItems(movieListModel.getMovieList());
        tcTitle.setCellValueFactory(addMovie -> addMovie.getValue().getNameProperty());
        tcCategory.setCellValueFactory(addMovie -> addMovie.getValue().getAllCategoriesStringProperty());
        tcRating.setCellValueFactory(addMovie -> addMovie.getValue().getIMDBRatingProperty().asObject());


        txtTitle.setDisable(true);
        txtIMDBRating.setDisable(true);
        txtPersonalRating.setDisable(true);
        comboBoxCategory.setDisable(true);

        tvMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null){
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
        List<String> selectedCategoreis = new ArrayList<>();
        selectedCategoreis.add("Action");
        selectedCategoreis.add("Action-Comedy");
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                List<String> checkBoxList = new ArrayList<>();
                boolean isTitleOn = cbTitle.isSelected();
                boolean isCatOn = cbCategory.isSelected();
                boolean isRatingOn = cbRating.isSelected();
                if (!isTitleOn && !isCatOn && !isRatingOn) {
                    cbTitle.setSelected(true);
                    isTitleOn = true;
                }
                movieListModel.searchMovie(newValue, isTitleOn,  isCheckBoxsON(), isRatingOn);
            } catch (MovieException e) {
                e.printStackTrace();
            }
        });
        //CheckBoxAction.selectedProperty().addListener();
    }

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


    public void handlePlayMovie(ActionEvent actionEvent) throws MovieException {
        getSelectedMovie().setLastViewProperty(new Date());
        movieListModel.updateLastView(getSelectedMovie());
        sceneSwapper.sceneSwitch(new Stage(), "MediaPlayer.fxml");
    }

    public void handleAddMovie(ActionEvent actionEvent) {
        sceneSwapper.sceneSwitch(new Stage(), "addNewMovie.fxml");
    }

    public void handleEditMovie(ActionEvent actionEvent) {
        try {
            comboBoxCategory.getItems().addAll(movieListModel.getCategoryList());

        } catch (Exception e) {
            displayMessage("Failed to fetch categories from the database.");
            displayError(e);
        }

        if (tvMovies.getSelectionModel().selectedItemProperty().get() != null) {
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

    public void handleDeleteMovie(ActionEvent actionEvent) throws MovieException {
        movieListModel.deleteMovie(tvMovies.getSelectionModel().selectedItemProperty().get());
    }

    public void handleEditSave(ActionEvent actionEvent) throws MovieException {
            ObservableList<CategoryModel> categories = listViewCategories.getItems();
            MovieModel movie = tvMovies.getSelectionModel().selectedItemProperty().get();
            try {

                double imdbRating = Double.parseDouble(txtIMDBRating.getText());
                double personalRating;
                if(txtPersonalRating.getText().isBlank()){
                    personalRating = -1.0;
                }else{
                    personalRating = Double.parseDouble(txtPersonalRating.getText());
                }
                movieListModel.updateMovie(movie, txtTitle.getText(), imdbRating, personalRating, categories);

                vBoxControllMenu.getChildren().remove(btnEditSave);
                vBoxControllMenu.getChildren().remove(btnEditCancel);
                vBoxControllMenu.getChildren().add(btnAddMovie);
                vBoxControllMenu.getChildren().add(btnDeleteMovie);


                        enable_Disable_TextFields();
            } catch (Exception e){
                displayMessage("You must provide a number between 0-10");
                e.printStackTrace();
                return;
            }
    }


    public void handleEditCancel(ActionEvent actionEvent) {
        vBoxControllMenu.getChildren().remove(btnEditSave);
        vBoxControllMenu.getChildren().remove(btnEditCancel);
        vBoxControllMenu.getChildren().add(btnAddMovie);
        vBoxControllMenu.getChildren().add(btnDeleteMovie);

        enable_Disable_TextFields();
    }

    private void enable_Disable_TextFields() {

        txtTitle.setDisable(!txtTitle.isDisabled());
        txtIMDBRating.setDisable(!txtIMDBRating.isDisabled());
        txtPersonalRating.setDisable(!txtPersonalRating.isDisabled());
        comboBoxCategory.setDisable(!comboBoxCategory.isDisabled());
        btnEdit.setDisable(!btnEdit.isDisabled());

    }

    public void handleCBTitle(ActionEvent actionEvent) {
        cbCategory.setSelected(false);
        cbRating.setSelected(false);
    }

    public void handleCBCategory(ActionEvent actionEvent) {
        cbTitle.setSelected(false);
        cbRating.setSelected(false);
        if(cbCategory.isSelected()){
            //txtSearch.setDisable(true);
            cbRating.setDisable(true);
            cbTitle.setDisable(true);
            hBoxParrent.getChildren().add(vBoxCategories);
            hBoxParrent.getScene().getWindow().setWidth(MAX_WINDOW_SIZE);

        } else {
            //txtSearch.setDisable(false);
            cbRating.setDisable(false);
            cbTitle.setDisable(false);
            hBoxParrent.getChildren().remove(vBoxCategories);
            hBoxParrent.getScene().getWindow().setWidth(MAX_WINDOW_SIZE-vBoxCategories.getWidth());
        }
    }

    public void handleCBRating(ActionEvent actionEvent) {
        cbTitle.setSelected(false);
        cbCategory.setSelected(false);
    }

    public MovieListModel getMovieListModel() {
        return movieListModel;
    }

    public void handleChooseCategory(ActionEvent actionEvent) {
        CategoryModel categoryModel = comboBoxCategory.getSelectionModel().getSelectedItem();
        boolean matchFound = false;
        for (CategoryModel c : listViewCategories.getItems()) {
            if (c.getNameProperty().get().equals(categoryModel.getNameProperty().get())){
                matchFound = true;
                break;
            }
        }
        if (!matchFound) {
            listViewCategories.getItems().add(categoryModel);
        }
    }

    public void handleRemoveCategory(ActionEvent actionEvent) {
        CategoryModel categoryModel = listViewCategories.getSelectionModel().getSelectedItem();
        if(categoryModel != null){
            listViewCategories.getItems().remove(categoryModel);
        }

    }

    public void handleClickMovieList(MouseEvent mouseEvent) {
        listViewCategories.setItems(getSelectedMovie().getAllCategoryAsList());
    }

    public MovieModel getSelectedMovie(){
        if (tvMovies.getSelectionModel().getSelectedItem() != null)
        return tvMovies.getSelectionModel().getSelectedItem();
        else return null;
    }
}

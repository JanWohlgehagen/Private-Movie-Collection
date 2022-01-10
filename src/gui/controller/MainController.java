package gui.controller;

import be.DisplayMessage;
import be.MovieException;
import gui.model.MovieListModel;
import gui.model.MovieModel;
import gui.util.SceneSwapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static be.DisplayMessage.displayMessage;

public class MainController implements Initializable {

    @FXML
    private TableView<MovieModel> tvMovies;
    @FXML
    private TableColumn<MovieModel, String> tcTitle;
    @FXML
    private TableColumn<MovieModel, String> tcCategory;
    @FXML
    private TableColumn<MovieModel, Double> tcRating;

    @FXML
    private CheckBox cbTitle;
    @FXML
    private CheckBox cbCategory;
    @FXML
    private CheckBox cbRating;

    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtIMDBRating;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtPersonalRating;

    @FXML
    private VBox vBoxControllMenu;

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
    private MediaPlayerController mediaPlayerController;

    public MainController() throws IOException, MovieException {
        movieListModel = new MovieListModel();
        sceneSwapper = new SceneSwapper();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vBoxControllMenu.getChildren().remove(btnEditCancel);
        vBoxControllMenu.getChildren().remove(btnEditSave);

        tvMovies.setPlaceholder(new Label("No movies found in Database"));

        tvMovies.setItems(movieListModel.getMovieList());
        tcTitle.setCellValueFactory(addMovie -> addMovie.getValue().getNameProperty());
        tcCategory.setCellValueFactory(addMovie -> addMovie.getValue().getAllCategorysAsString());
        tcRating.setCellValueFactory(addMovie -> addMovie.getValue().getIMDBRatingProperty().asObject());

        txtTitle.setDisable(true);
        txtIMDBRating.setDisable(true);
        txtCategory.setDisable(true);
        txtPersonalRating.setDisable(true);

        tvMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null){
                txtTitle.setText(newValue.getNameProperty().get());
                txtCategory.setText(newValue.getAllCategorysAsString().get());
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
                boolean isCatOn = cbCategory.isSelected();
                boolean isRatingOn = cbRating.isSelected();
                if (!isTitleOn && !isCatOn && !isRatingOn) {
                    cbTitle.setSelected(true);
                    isTitleOn = true;
                }
                movieListModel.searchMovie(newValue, isTitleOn, isCatOn, isRatingOn);

            } catch (MovieException e) {
                e.printStackTrace();
            }
        });
    }

    public void handlePlayMovie(ActionEvent actionEvent) {
        sceneSwapper.sceneSwitch(new Stage(), "MediaPlayer.fxml");
    }

    public void handleAddMovie(ActionEvent actionEvent) {
        sceneSwapper.sceneSwitch(new Stage(), "addNewMovie.fxml");
    }

    public void handleEditMovie(ActionEvent actionEvent) {
        if (tvMovies.getSelectionModel().selectedItemProperty().get() != null) {
            vBoxControllMenu.getChildren().remove(btnAddMovie);
            vBoxControllMenu.getChildren().remove(btnDeleteMovie);
            vBoxControllMenu.getChildren().add(btnEditSave);
            vBoxControllMenu.getChildren().add(btnEditCancel);

            if (tvMovies.getSelectionModel().getSelectedItem().getPersonalRatingProperty().get() == -1) {
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

            MovieModel movie = tvMovies.getSelectionModel().selectedItemProperty().get();
            try {
                double imdbRating = Double.parseDouble(txtIMDBRating.getText());
                double personalRating;
                if(txtPersonalRating.getText().isBlank()){
                    personalRating = -1.0;
                }else{
                    personalRating = Double.parseDouble(txtPersonalRating.getText());
                }

                movieListModel.updateMovie(movie, txtTitle.getText(), imdbRating, personalRating);

                vBoxControllMenu.getChildren().remove(btnEditSave);
                vBoxControllMenu.getChildren().remove(btnEditCancel);
                vBoxControllMenu.getChildren().add(btnAddMovie);
                vBoxControllMenu.getChildren().add(btnDeleteMovie);

                        enable_Disable_TextFields();
            } catch (Exception e){
                displayMessage("You must provide a number between 0-10");
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
        txtCategory.setDisable(!txtCategory.isDisabled());
        txtPersonalRating.setDisable(!txtPersonalRating.isDisabled());

        btnEdit.setDisable(!btnEdit.isDisabled());

    }

    public void handleCBTitle(ActionEvent actionEvent) {
        cbCategory.setSelected(false);
        cbRating.setSelected(false);
    }

    public void handleCBCategory(ActionEvent actionEvent) {
        cbTitle.setSelected(false);
        cbRating.setSelected(false);
    }

    public void handleCBRating(ActionEvent actionEvent) {
        cbTitle.setSelected(false);
        cbCategory.setSelected(false);
    }

    public MovieModel getMovieToPlay(){
        if (tvMovies.getSelectionModel().getSelectedItem() != null)
            return tvMovies.getSelectionModel().getSelectedItem();
        else return null;
    }
}

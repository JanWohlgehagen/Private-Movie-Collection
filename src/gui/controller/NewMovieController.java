package gui.controller;


import be.DisplayMessage;
import be.Movie;
import be.MovieException;
import gui.model.MovieListModel;
import gui.model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static be.DisplayMessage.displayMessage;

public class NewMovieController {

    final FileChooser fileChooser;

    @FXML
    public TextField txtTitle;
    @FXML
    private TextField txtImdb;
    @FXML
    private TextField txtChooseFile;

    @FXML
    private GridPane gridPaneId;

    MovieListModel movieListModel;

    public NewMovieController() {
    fileChooser = new FileChooser();
    }

    public void initialize() throws IOException, MovieException {
    movieListModel = new MovieListModel();
    }

    public void handleChooseBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) gridPaneId.getScene().getWindow();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movie", "*.mp4", "*.mpeg4"));
        File file = fileChooser.showOpenDialog(stage); // assigns the chosen file to the file
        if (file != null) { //DirectoryChooser returns null if the user closes the browse window
            txtChooseFile.setText(file.getAbsolutePath().replace("\\", "/"));
        }
    }

    public void handleSaveBtn()throws IOException, MovieException {
        if(!txtTitle.getText().isBlank() && !txtImdb.getText().isBlank() && !txtChooseFile.getText().isBlank()){
            String uploadTitle = txtTitle.getText();
            String pathToFile = txtChooseFile.getText();
            Double uploadImdb = Double.parseDouble(txtImdb.getText());
            movieListModel.createMovie(uploadTitle, uploadImdb, pathToFile);
            closeStage();
        } else displayMessage("All fields must be filled.");
    }

    public void handleCancelBtn(ActionEvent actionEvent) {
        closeStage();
    }

    /**
     * closes the stage
     */
    public void closeStage(){
        Stage stage = (Stage) gridPaneId.getScene().getWindow();
        stage.close();
    }
}


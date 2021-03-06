package gui.controller;


import gui.App;
import gui.model.MovieListModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private MovieListModel movieListModel;

    public NewMovieController() {
    fileChooser = new FileChooser();
    }

    public void initialize() {
        MainController mainController = new App().getController();
        movieListModel = mainController.getMovieListModel();
    }

    /**
     * Opens the filechooser in the default directory and shows mp4 and mpeg4 files and fetches the file path of the chosen file.
     * @param actionEvent
     */
    public void handleChooseBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) gridPaneId.getScene().getWindow();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movie", "*.mp4", "*.mpeg4"));
        File file = fileChooser.showOpenDialog(stage); // assigns the chosen file to the file
        if (file != null) { //DirectoryChooser returns null if the user closes the browse window
            txtChooseFile.setText(file.getAbsolutePath().replace("\\", "/"));
        }
    }

    /**
     * If the textfields in the view are not blank it calls the movielist to create a new movie and add it to the view.
     */
    public void handleSaveBtn() {
        if(!txtTitle.getText().isBlank() && !txtImdb.getText().isBlank() && !txtChooseFile.getText().isBlank()){
            String uploadTitle = txtTitle.getText();
            String pathToFile = txtChooseFile.getText();
            double uploadImdb = Double.parseDouble(txtImdb.getText());
            movieListModel.createMovie(uploadTitle, uploadImdb, pathToFile);
            closeStage();
        } else displayMessage("All fields must be filled.");
    }

    /**
     * Closes the stage.
     * @param actionEvent
     */
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


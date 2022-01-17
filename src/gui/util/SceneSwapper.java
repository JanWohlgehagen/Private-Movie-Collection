package gui.util;

import be.DisplayMessage;
import gui.controller.MediaPlayerController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class SceneSwapper {


    /**
     * switches the stage to a certain fxml file.
     * @param stage of stage wanted to be shown.
     * @param fxmlClassName the file that wanted to be shown
     */
    public void sceneSwitch(Stage stage, String fxmlClassName){
        try {
            URL url = new File("src/gui/view/" + fxmlClassName).toURI().toURL();
            Parent Scene = FXMLLoader.load(url);
            Scene ViewScene = new Scene(Scene);
            stage.setTitle("Private Movie Collection");
            Image image = new Image("/gui/images/film_icon2.png");
            stage.getIcons().add(image);
            stage.setScene(ViewScene);
            stage.show();
        }catch (IOException IOex){
            DisplayMessage.displayErrorMessage(IOex, "Something went wrong.\n Can not load scene");
        }
    }
}
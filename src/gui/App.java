package gui;

import gui.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {


    private static FXMLLoader mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainController = new FXMLLoader((getClass().getResource("view/MainMenu.fxml")));
        primaryStage.setTitle("Private Movie Collection");
        primaryStage.setScene(new Scene(mainController.load()));
        Image image = new Image("/gui/images/film_icon2.png");
        primaryStage.getIcons().add(image);
        primaryStage.show();
    }

    public MainController getController() {
        return mainController.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

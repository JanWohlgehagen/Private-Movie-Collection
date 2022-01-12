package be;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class DisplayMessage {

    /**
     * Displays errormessage to the user.
     *
     * @param ex The Exception
     */
    public static void displayError(Exception ex) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            addIicon(alert);
            alert.setTitle("Error: Something went wrong");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(String.valueOf(ex.getCause()));
            alert.showAndWait();
        });
    }

    /**
     * displays error to the user.
     * @param ex the exception
     *  @return true if user have clicked ok. false if user clicked cansel
     */
    public static boolean displayErrorSTOP(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        addIicon(alert);
        alert.setTitle("Error: Something went wrong");
        alert.setHeaderText(ex.getMessage());
        alert.setContentText(String.valueOf(ex.getCause()));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean displayErrorSTOPtest() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        addIicon(alert);
        alert.setTitle("Error: Something went wrong");
        alert.setHeaderText("");
        //alert.setContentText();
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *  used for getting confirmation from user thet they want to perform a certain action.
     * @param message the exception message.
     * @return true if user have clicked ok. false if user clicked cansel
     */
    public static boolean displayWarning (String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        addIicon(alert);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText("Press OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *  displays a error message
     * @param message the exception message
     */
    public static void displayMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            addIicon(alert);
            alert.setTitle("Message");
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

    /**
     * adds a image as a icon in error message.
     * @param alert the alert that we want to add image to.
     */
    private static void addIicon(Alert alert){
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image image = new Image("/gui/images/film_icon2.png");
        stage.getIcons().add(image);
    }
}

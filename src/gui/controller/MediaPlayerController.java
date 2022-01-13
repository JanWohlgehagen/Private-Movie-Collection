package gui.controller;

import gui.App;
import javafx.animation.PauseTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class MediaPlayerController implements Initializable {


    @FXML
    private AnchorPane anchorpaneParrent;

    @FXML
    private MediaView mvMovie;
    private MediaPlayer mpMovie;
    private Media mediaVideo;

    @FXML
    private Slider sliderTime;
    @FXML
    private Slider sliderVolume;

    @FXML
    private VBox hBoxControls;
    @FXML
    private HBox hBoxVolume;

    @FXML
    private Label labelVolume;
    @FXML
    private Label labelCurrentTime;
    @FXML
    private Label labelTotalTime;
    @FXML
    private Label labelSpeed;
    @FXML
    private Label labelFullScreen;

    @FXML
    private Button buttonPlayPauseReplay;

    private boolean atEndOfVideo = false;
    private boolean isPlaying = true;
    private boolean isMuted = false;
    private BooleanProperty mouseMoving = new SimpleBooleanProperty();
    private final long MIN_STATIONARY_TIME = 2000 ; // milliseconds that the cursor have to be stationary to be considered idle

    /*Images for the different labels in the view*/
    private ImageView ivPlay;
    private ImageView ivPause;
    private ImageView ivRestart;
    private ImageView ivVolume;
    private ImageView ivFullScreen;
    private ImageView ivMute;
    private ImageView ivExitFullscreen;

    private String filePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController mainController = new App().getController();
        filePath = mainController.getSelectedMovie().getPathToFileProperty().get();
        final int IV_SIZE = 25; // the size of the pictures we set as the labels' background


        mediaVideo = new Media(new File(filePath).toURI().toString());
        mpMovie = new MediaPlayer(mediaVideo);
        mvMovie.setMediaPlayer(mpMovie);

        Image imagePlay = new Image(new File("src/gui/images/play.png").toURI().toString());
        ivPlay = new ImageView(imagePlay);
        ivPlay.setFitHeight(IV_SIZE);
        ivPlay.setFitWidth(IV_SIZE);

        Image imagePause = new Image(new File("src/gui/images/pause.png").toURI().toString());
        ivPause = new ImageView(imagePause);
        ivPause.setFitHeight(IV_SIZE);
        ivPause.setFitWidth(IV_SIZE);

        Image imageRestart = new Image(new File("src/gui/images/restart.png").toURI().toString());
        ivRestart = new ImageView(imageRestart);
        ivRestart.setFitHeight(IV_SIZE);
        ivRestart.setFitWidth(IV_SIZE);

        Image imageVolume = new Image(new File("src/gui/images/volume.png").toURI().toString());
        ivVolume = new ImageView(imageVolume);
        ivVolume.setFitHeight(IV_SIZE);
        ivVolume.setFitWidth(IV_SIZE);

        Image imageFullscreen = new Image(new File("src/gui/images/fullscreen.png").toURI().toString());
        ivFullScreen = new ImageView(imageFullscreen);
        ivFullScreen.setFitHeight(IV_SIZE);
        ivFullScreen.setFitWidth(IV_SIZE);

        Image imageMute = new Image(new File("src/gui/images/mute.png").toURI().toString());
        ivMute = new ImageView(imageMute);
        ivMute.setFitHeight(IV_SIZE);
        ivMute.setFitWidth(IV_SIZE);

        Image imageExitFullscreen = new Image(new File("src/gui/images/exitfullscreen.png").toURI().toString());
        ivExitFullscreen = new ImageView(imageExitFullscreen);
        ivExitFullscreen.setFitHeight(IV_SIZE);
        ivExitFullscreen.setFitWidth(IV_SIZE);

        buttonPlayPauseReplay.setGraphic(ivPause);
        sliderVolume.setValue(0.5);
        labelVolume.setGraphic(ivVolume);
        labelSpeed.setText("1X");
        labelFullScreen.setGraphic(ivFullScreen);
        mpMovie.play();

        buttonPlayPauseReplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button buttonPlay = (Button) event.getSource();
                bindCurrentTimeLabel();// rebind the current time label, for when the movie has ended and we press replay
                if(atEndOfVideo){
                    sliderTime.setValue(0);
                    atEndOfVideo = false;
                    isPlaying = false;
                }
                if(isPlaying){
                    buttonPlay.setGraphic(ivPlay);
                    mpMovie.pause();
                    isPlaying = false;
                } else {
                    buttonPlay.setGraphic(ivPause);
                    mpMovie.play();
                    isPlaying = true;
                }
            }
        });

        // remove volume slider initially
        hBoxVolume.getChildren().remove(sliderVolume);

        // bind slider value to volume of the mediaplayer
        mpMovie.volumeProperty().bindBidirectional(sliderVolume.valueProperty());

        bindCurrentTimeLabel();

        sliderVolume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mpMovie.setVolume(sliderVolume.getValue());
                if(mpMovie.getVolume() != 0.0){
                    labelVolume.setGraphic(ivVolume);
                    isMuted = false;
                } else {
                    labelVolume.setGraphic(ivMute);
                    isMuted = true;
                }
            }
        });

        //Sets the speed of the mediaplayer and adjusts the label accordingly
        labelSpeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (labelSpeed.getText()) {
                    case "0.25X" -> {
                        labelSpeed.setText("0.50X");
                        mpMovie.setRate(0.5);
                    }
                    case "0.50X" -> {
                        labelSpeed.setText("0.75X");
                        mpMovie.setRate(0.75);
                    }
                    case "0.75X" -> {
                        labelSpeed.setText("1X");
                        mpMovie.setRate(1.0);
                    }
                    case "1X" -> {
                        labelSpeed.setText("1.25X");
                        mpMovie.setRate(1.25);
                    }
                    case "1.25X" -> {
                        labelSpeed.setText("1.50X");
                        mpMovie.setRate(1.5);
                    }
                    case "1.50X" -> {
                        labelSpeed.setText("1.75X");
                        mpMovie.setRate(1.75);
                    }
                    case "1.75X" -> {
                        labelSpeed.setText("2X");
                        mpMovie.setRate(2.0);
                    }
                    case "2X" -> {
                        labelSpeed.setText("0.25X");
                        mpMovie.setRate(0.25);
                    }
                }
            }
        });

        //checks if the mediaplayer is muted and adjusts the label's graphic accordingly
        labelVolume.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isMuted){
                    labelVolume.setGraphic(ivVolume);
                    sliderVolume.setValue(0.2);
                } else {
                    labelVolume.setGraphic(ivMute);
                    sliderVolume.setValue(0);
                    isMuted = true;
                }
            }
        });

        //Checks if the cursor has entered the hbox that holds the volume slider and shows it if it is not visible
        hBoxVolume.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (hBoxVolume.lookup("#sliderVolume") == null){
                    hBoxVolume.getChildren().add(sliderVolume);
                    sliderVolume.setValue(mpMovie.getVolume());
                }
            }
        });

        //Checks if the cursor has exited the hbox that holds the volume slider, and if it does it removes the slider
        hBoxVolume.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hBoxVolume.getChildren().remove(sliderVolume);
            }
        });

        //Listener for the scene for when we're resizing the scene
        anchorpaneParrent.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
                if (oldScene == null && newScene != null){
                    // bind our mediaview to the height of the parent Scene and subtract the heigh of the container that holds our control buttons plus a little extra
                    mvMovie.fitHeightProperty().bind(newScene.heightProperty().subtract(hBoxControls.getHeight()+sliderTime.getHeight()));
                }
            }
        });


        //if the mouse stops moving after a
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (! isNowMoving) {
                anchorpaneParrent.getChildren().remove(hBoxControls);
            }
        });

        // a transiotion that plays and when it is finished it sets the mouseMoving to false
        PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
        pause.setOnFinished(e -> mouseMoving.set(false));

        //when the mouse is moved within the window the pausetransition above is reset
        anchorpaneParrent.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseMoving.set(true);
                if(anchorpaneParrent.lookup("#hBoxControls") == null) {
                    anchorpaneParrent.getChildren().add(hBoxControls);
                }
                pause.playFromStart();
            }
        });

        // handles the full screen and exit full screen functionality
        labelFullScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //get the source (the label that was clicked)
                Label label = (Label) event.getSource();
                Stage stage = (Stage) label.getScene().getWindow();

                if(stage.isFullScreen()){
                    stage.setFullScreen(false);
                    labelFullScreen.setGraphic(ivFullScreen);
                } else {
                    stage.setFullScreen(true);
                    labelFullScreen.setGraphic(ivExitFullscreen);
                }

                // if escape is pressed while within the stage change the label to fullscreen
                stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ESCAPE){
                            labelFullScreen.setGraphic(ivFullScreen);
                        }
                    }
                });
            }
        });

        //Listerner for the slider and label of total time
        mpMovie.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldDuration, Duration newDuration) {
                bindCurrentTimeLabel();// rebind the current time label, for when the movie has ended and we press replay
                sliderTime.setMax(newDuration.toSeconds()); //set the slider's max value to the time of the movie
                labelTotalTime.setText(getTime(newDuration));
            }
        });

        // Listens whether the value of the timeslider is being changed (a user is dragging it)
        sliderTime.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean wasChanging, Boolean isChanging) {
                bindCurrentTimeLabel();// rebind the current time label, for when the movie has ended and we press replay
                if(!isChanging){
                    mpMovie.seek(Duration.seconds(sliderTime.getValue()));
                }
            }
        });

        //changes the slider value to that of the new time as the movie progresses
        sliderTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                bindCurrentTimeLabel(); // rebind the current time label, for when the movie has ended and we press replay
                double currentTime = mpMovie.getCurrentTime().toSeconds();

                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5){// if the new value is half a second larger than the current time
                    mpMovie.seek(Duration.seconds(newValue.doubleValue())); //go to the new value of the slider
                }

                labelsMatchEndMovie(labelCurrentTime.getText(), labelTotalTime.getText());
            }
        });

        //change the slider's value according to the time of the mediaplayer
        mpMovie.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldTime, Duration newTime) {
                bindCurrentTimeLabel();// rebind the current time label, for when the movie has ended and we press replay
                if (!sliderTime.isValueChanging()){
                    sliderTime.setValue(newTime.toSeconds());
                }
                labelsMatchEndMovie(labelCurrentTime.getText(), labelTotalTime.getText());
            }
        });

        // this method is run when the mediaplayer has finished playing
        mpMovie.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                buttonPlayPauseReplay.setGraphic(ivRestart);
                atEndOfVideo = true;
                if(!labelCurrentTime.textProperty().equals(labelTotalTime.textProperty())){ // fixes an issue with rounding, when comparing total time to current time
                    labelCurrentTime.textProperty().unbind();
                    labelCurrentTime.setText(getTime(mpMovie.getTotalDuration()) + " / ");
                }
            }
        });

        //Listener on the anchorpane that is the root container for this scene
        anchorpaneParrent.sceneProperty().addListener(new ChangeListener<Scene>() {
            // Listener for the scene extracted from the anchorpane
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                newValue.windowProperty().addListener(new ChangeListener<Window>() {
                    // Listener for the window extracted from the scene, listens for the window event WINDOW_CLOSE_REQUEST, meaning the default close button has been pressed.
                    @Override
                    public void changed(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
                        newValue.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                mpMovie.stop(); // stop the mediaplayer if the mediaplayer window is closed
                            }
                        });
                    }
                });
            }
        });
    }


    // binds the label that displays the current time to the time of the mediaplayer
    private void bindCurrentTimeLabel() {
        labelCurrentTime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(mpMovie.getCurrentTime()) + " / ";
            }
        }, mpMovie.currentTimeProperty()));
    }

    //returns the time of the movie in a nicely formated string
    public String getTime(Duration time){
        int hours = (int) time.toHours();
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds();

        //  check if hours, minutes and seconds is greater than 59, if so get remainder 60.
        if(seconds > 59)
            seconds = seconds % 60;
        if(minutes > 59)
            minutes = minutes % 60;
        if (hours > 59)
            hours = hours % 60;

        if(hours > 0)
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        else return String.format("%02d:%02d", minutes, seconds);
    }

    private void labelsMatchEndMovie(String labelTime, String labelTotalTime) {
        for (int i = 0 ; i < labelTotalTime.length(); i++){
            if(labelTime.charAt(i) != labelTotalTime.charAt(i)) { // compares the total time to the current time that the mediaplayer is at
                atEndOfVideo = false;
                if (isPlaying) {
                    buttonPlayPauseReplay.setGraphic(ivPause);
                } else {
                    buttonPlayPauseReplay.setGraphic(ivPlay);
                    break;
                }
            } else { // if the current time and total time are the same
                atEndOfVideo = true;
                buttonPlayPauseReplay.setGraphic(ivRestart); // this might cause the play/ pause button to switch to the restart button unwanted mid-movie for a split second
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dunda;

import java.io.File;
import java.net.URL;
import java.sql.Time;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author LUCY
 */
public class FXMLDocumentController implements Initializable {
    private MediaPlayer mediaplayer;
    @FXML
    private Slider slider;
    @FXML
    private Slider seeslider;
    private Duration duration;
    
    private String filepath; 
    @FXML
    private MediaView mediaview;
    @FXML
    private MenuItem open;
    @FXML
    private Button play;
    @FXML
    private Button pause;
    @FXML
    private Button stop;
    @FXML
    private Button exit;
    private Label Time;
    private void handleButtonAction(ActionEvent event) {
        
    FileChooser filechooser = new FileChooser ();
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select a file", "*.mp4");
    filechooser.getExtensionFilters().add(filter);
    File file =filechooser.showOpenDialog(null);
    filepath = file.toURI().toString();
    if(filepath!=null){
    Media media = new Media(filepath);
    mediaplayer =new MediaPlayer(media);
    mediaview.setMediaPlayer(mediaplayer);
    DoubleProperty width=mediaview.fitWidthProperty();
    DoubleProperty height=mediaview.fitHeightProperty();
    width.bind(Bindings.selectDouble(mediaview.sceneProperty(),"width"));
    height.bind(Bindings.selectDouble(mediaview.sceneProperty(),"height"));
    
    slider.setValue(mediaplayer.getVolume()*100);
    slider.valueProperty().addListener(new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            mediaplayer.setVolume(slider.getValue()/100);
        }
    });
    mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
        @Override
        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
            //seeslider.setValue(newValue.toMinutes());
            updateValues();
            
        }
    });
     seeslider.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (seeslider.isValueChanging()) {
                        // multiply duration by percentage calculated by slider position
                        if(duration!=null) {
                            mediaplayer.seek(duration.multiply(seeslider.getValue() / 100.0));
                        }
                        updateValues();

                    }
                }
            });
    seeslider.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            mediaplayer.seek(Duration.seconds(seeslider.getValue()));
          
        }
    });
    mediaplayer.play();
    }
    
    
    }
    
    protected void updateValues() {
            //if (Time !=null && seeslider != null && slider != null && duration != null) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        Duration currentTime = mediaplayer.getCurrentTime();
                        Time.setText(formatTime(currentTime, duration));
                        seeslider.setDisable(duration.isUnknown());
                        //if (!seeslider.isDisabled() && duration.greaterThan(Duration.ZERO) && !seeslider.isValueChanging()) {
                            seeslider.setValue(currentTime.divide(duration).toMillis() * 100.0);
                        
                        //if (!slider.isValueChanging()) {
                            slider.setValue((int) Math.round(mediaplayer.getVolume() * 100));
                        
                    }
                });
            
        }
     private String formatTime(Duration elapsed, Duration duration) {
            int intElapsed = (int)Math.floor(elapsed.toSeconds());
            int elapsedHours = intElapsed / (60 * 60);
            if (elapsedHours > 0) {
                intElapsed -= elapsedHours * 60 * 60;
            }
            int elapsedMinutes = intElapsed / 60;
            int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

            if (duration.greaterThan(Duration.ZERO)) {
                int intDuration = (int)Math.floor(duration.toSeconds());
                int durationHours = intDuration / (60 * 60);
                if (durationHours > 0) {
                    intDuration -= durationHours * 60 * 60;
                }
                int durationMinutes = intDuration / 60;
                int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

                if (durationHours > 0) {
                    return String.format("%d:%02d:%02d/%d:%02d:%02d",
                                         elapsedHours, elapsedMinutes, elapsedSeconds,
                                         durationHours, durationMinutes, durationSeconds);
                } else {
                    return String.format("%02d:%02d/%02d:%02d",
                                         elapsedMinutes, elapsedSeconds,
                                         durationMinutes, durationSeconds);
                }
            } else {
                if (elapsedHours > 0) {
                    return String.format("%d:%02d:%02d",
                                         elapsedHours, elapsedMinutes, elapsedSeconds);
                } else {
                    return String.format("%02d:%02d",
                                         elapsedMinutes, elapsedSeconds);
                }
            }
        }
    private void pauseVideo(ActionEvent event){
        mediaplayer.pause();
    }
    private void playVideo(ActionEvent event){
        mediaplayer.play();
        mediaplayer.setRate(1);
        
    } 
    private void stopVideo(ActionEvent event){
        mediaplayer.stop();
        
    }    private void exitVideo(ActionEvent event){
        System.exit(0);
        
    }
    

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

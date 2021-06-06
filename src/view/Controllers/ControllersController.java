package view.Controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ControllersController {
    @FXML public TextField playSpeedTF;
    @FXML public Slider progressBar;
    @FXML public Label currentTime;
    public DoubleProperty playSpeed;
    public Runnable onPlay,onPause,onForward,onRewind;

    public ControllersController() {
        playSpeed=new SimpleDoubleProperty(1.0);
    }


    @FXML
    public void pressButtonPlay(){
        if(onPlay!=null){
            onPlay.run();
        }
        progressBar.setDisable(false);
    }
    @FXML
    public void pressButtonPause(){
        if(onPause!=null){
            onPause.run();
        }
        progressBar.setDisable(true);
    }
    @FXML
    public void pressButtonForward(){
        if(onForward!=null){
            onForward.run();
        }
    }
    @FXML
    public void pressButtonRewind(){
        if(onRewind!=null){
            onRewind.run();
        }
    }
    @FXML
    public void pressButtonStop() {
        if (!progressBar.isDisabled()) {
            progressBar.setValue(1);
        }
    }
    @FXML
    public void playSpeedWasChanged(){
        try {
            double newPlaySpeed = Double.parseDouble(playSpeedTF.textProperty().getValue());
            if(playSpeed.getValue().equals(newPlaySpeed)) { return;} //if yes-it doesn't really changed- so do nothing.
            if(newPlaySpeed<=0.0)
                throw new Exception();
            playSpeed.setValue(newPlaySpeed);
        }
        catch (Exception e) {
            showErrorMessage("Problem with the play speed!\nYou must enter a valid play speed number.");
        }
    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}

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
    private void pressButtonPlay(){
        if(onPlay!=null){
            onPlay.run();
        }
    }
    @FXML
    private void pressButtonPause(){
        if(onPause!=null){
            onPause.run();
        }
    }
    @FXML
    private void pressButtonForward(){
        if(onForward!=null){
            onForward.run();
        }
    }
    @FXML
    private void pressButtonRewind(){
        if(onRewind!=null){
            onRewind.run();
        }
    }
    @FXML
    private void pressButtonStop() {
        if (!progressBar.isDisabled()) {
            progressBar.setValue(1);
        }
    }
    @FXML
    private void playSpeedWasChanged(){
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

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}

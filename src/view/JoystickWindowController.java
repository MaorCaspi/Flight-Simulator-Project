package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class JoystickWindowController implements Initializable {
    @FXML
    Slider throttleS, rudderS;
    @FXML
    Circle joystickBorderCircle, joystickControlCircle;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

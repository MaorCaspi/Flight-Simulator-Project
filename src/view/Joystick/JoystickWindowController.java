package view.Joystick;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

public class JoystickWindowController {

    @FXML private Slider throttle, rudder;
    @FXML private Circle joystickControl;

    public Slider getThrottle() { return throttle; }
    public Slider getRudder() {
        return rudder;
    }
    public Circle getJoystickControl() { return joystickControl; }
}
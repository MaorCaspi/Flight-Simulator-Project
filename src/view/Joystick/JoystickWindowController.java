package view.Joystick;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

public class JoystickWindowController{

    @FXML private Slider throttle, rudder;
    public DoubleProperty aliron,elevators;

    // @FXML private Circle joystickBorderCircle, joystickControlCircle;

    public Slider getThrottle() {
        return throttle;
    }

    public Slider getRudder() {
        return rudder;
    }

}
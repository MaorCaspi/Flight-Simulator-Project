package view.Joystick;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ResourceBundle;

public class JoystickWindowController implements Initializable {

    @FXML public Slider throttle, rudder;
    public DoubleProperty aliron,elevators;

    // @FXML private Circle joystickBorderCircle, joystickControlCircle;


    public JoystickWindowController() {
    }

    public Slider getThrottle() {
        return throttle;
    }

    public void setThrottle(Slider throttle) {
        this.throttle = throttle;
    }

    public Slider getRudder() {
        return rudder;
    }

    public void setRudder(Slider rudder) {
        this.rudder = rudder;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
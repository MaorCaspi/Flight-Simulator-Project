package view.Joystick;


import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class JoystickWindowController implements Initializable {

    @FXML private Slider throttle, rudder;
    @FXML private Circle joystickBorder, joystickControl;

    public Slider getThrottle() { return throttle; }
    public Slider getRudder() {
        return rudder;
    }
    public Circle getJoystickBorder() { return joystickBorder; }
    public Circle getJoystickControl() { return joystickControl; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        double x=joystickBorder.getRadius()-joystickControl.getRadius();
        System.out.println("r: "+ x);
        System.out.println("x: "+ joystickControl.getCenterX());
        System.out.println("y: "+ joystickControl.getCenterY());
        //joystickControl.setCenterX(joystickControl.getCenterX()+(x*-1));
        //joystickControl.setCenterY(joystickControl.getCenterY()-x);
         */


    }
}
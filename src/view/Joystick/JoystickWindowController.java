package view.Joystick;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ResourceBundle;

public class JoystickWindowController implements Initializable {
    @FXML private Slider throttleS, rudderS;
    @FXML private Circle joystickBorderCircle, joystickControlCircle;

    public void setthrottle(float val)
    {
        throttleS.setValue(val);
    }
    public void setRudder(float val)
    {
        rudderS.setValue(val);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

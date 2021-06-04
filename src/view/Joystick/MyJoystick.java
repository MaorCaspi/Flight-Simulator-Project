package view.Joystick;


import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MyJoystick extends Pane {

    public DoubleProperty throttle, rudder,aileron,elevators;

    public MyJoystick(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane joy=fxl.load(getClass().getResource("JoystickWindow.fxml").openStream());
            JoystickWindowController joystickWindowController=fxl.getController();

            aileron=joystickWindowController.getJoystickControl().centerXProperty();
            elevators=joystickWindowController.getJoystickControl().centerYProperty();
            rudder=joystickWindowController.getRudder().valueProperty();
            throttle=joystickWindowController.getThrottle().valueProperty();

            this.getChildren().add(joy);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package view.Joystick;


import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MyJoystick extends Pane {

    public DoubleProperty throttle, rudder,aliron,elevators;

    public MyJoystick(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane joy=fxl.load(getClass().getResource("JoystickWindow.fxml").openStream());
            JoystickWindowController joystickWindowController=fxl.getController();
            /*
            aliron=joystickWindowController.aliron;
            elevators=joystickWindowController.elevators;
            rudder=joystickWindowController.rudder.valueProperty();
            throttle=joystickWindowController.throttle.valueProperty();

             */

            this.getChildren().add(joy);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
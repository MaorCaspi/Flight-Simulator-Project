package view.ClocksPanel;

import eu.hansolo.medusa.Gauge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ClocksPanelController{
    @FXML private Gauge heading,speed,altitude,roll,pitch,yaw;

    public Gauge getHeading() {
        return heading;
    }

    public Gauge getSpeed() {
        return speed;
    }

    public Gauge getAltitude() {
        return altitude;
    }

    public Gauge getRoll() {
        return roll;
    }

    public Gauge getPitch() {
        return pitch;
    }

    public Gauge getYaw() {
        return yaw;
    }

}

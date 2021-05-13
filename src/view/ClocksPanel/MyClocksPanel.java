package view.ClocksPanel;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MyClocksPanel extends Pane {

    public MyClocksPanel() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            Pane cp = fxl.load(getClass().getResource("ClocksPanel.fxml").openStream());
            ClocksPanelController clocksPanelController = fxl.getController();

            this.getChildren().add(cp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

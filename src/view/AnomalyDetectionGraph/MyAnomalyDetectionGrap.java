package view.AnomalyDetectionGraph;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MyAnomalyDetectionGrap extends Pane{

    public MyAnomalyDetectionGrap(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane adg=fxl.load(getClass().getResource("AnomalyDetectionGraph.fxml").openStream());
            AnomalyDetectionGraphController anomalyDetectionGraphController=fxl.getController();

            this.getChildren().add(adg);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

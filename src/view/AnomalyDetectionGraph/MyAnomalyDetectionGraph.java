package view.AnomalyDetectionGraph;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import other_classes.Point;
import java.io.IOException;

public class MyAnomalyDetectionGraph extends Pane{

    private AnomalyDetectionGraphController anomalyDetectionGraphController;

    public MyAnomalyDetectionGraph(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane adg=fxl.load(getClass().getResource("AnomalyDetectionGraph.fxml").openStream());
            anomalyDetectionGraphController=fxl.getController();
            this.getChildren().add(adg);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ListProperty<Point> getSelectedAttributePoints() { return anomalyDetectionGraphController.selectedAttributePoints; }
    public ListProperty<Point> getTheMostCorrelativeAttributePoints() { return anomalyDetectionGraphController.theMostCorrelativeAttributePoints; }
    public StringProperty getSelectedFeature(){return anomalyDetectionGraphController.selectedFeature; }
    public StringProperty getTheMostCorrelativeAttribute(){return anomalyDetectionGraphController.theMostCorrelativeAttribute.textProperty(); }
}
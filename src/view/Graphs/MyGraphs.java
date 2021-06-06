package view.Graphs;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import other_classes.Point;
import java.io.IOException;

public class MyGraphs extends Pane{

    private GraphsController anomalyDetectionGraphController;

    public MyGraphs(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane adg=fxl.load(getClass().getResource("Graphs.fxml").openStream());
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
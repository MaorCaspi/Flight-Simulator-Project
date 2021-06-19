package view.Graphs;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import other_classes.Point;
import java.io.IOException;

public class MyGraphs extends Pane{

    private GraphsController controller;

    public MyGraphs(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane adg=fxl.load(getClass().getResource("Graphs.fxml").openStream());
            controller=fxl.getController();
            this.getChildren().add(adg);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ListProperty<Point> getSelectedAttributePoints() { return controller.selectedAttributePoints; }
    public ListProperty<Point> getTheMostCorrelativeAttributePoints() { return controller.theMostCorrelativeAttributePoints; }
    public StringProperty getSelectedFeature(){return controller.selectedFeature; }
    public StringProperty getTheMostCorrelativeAttribute(){return controller.theMostCorrelativeAttribute.textProperty(); }
    public StringProperty getCorrelationPercents(){return controller.correlationPercents.textProperty(); }
}
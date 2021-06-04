package view.AnomalyDetectionGraph;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import other_classes.Point;
import java.net.URL;
import java.util.ResourceBundle;

public class AnomalyDetectionGraphController implements Initializable {
    @FXML private LineChart<Number, Number> selectedAttributeGraph,theMostCorrelativeAttributeGraph,anomalyDetectGraph;
    @FXML public Label theMostCorrelativeAttribute;
    private String localSelectedFeature;
    private int localRowNumber;
    public ListProperty<Point> selectedAttributePoints,theMostCorrelativeAttributePoints;
    public StringProperty selectedFeature;

    public void UpdateLineChart(ObservableList<XYChart.Data<Number, Number>> seriesData, ObservableList<Point> points){
        seriesData.clear();
        for(int i =0;i<points.size();i++) {
            seriesData.add(new XYChart.Data(points.get(i).getX(), points.get(i).getY()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedAttributePoints = new SimpleListProperty<>(FXCollections.observableArrayList());
        theMostCorrelativeAttributePoints = new SimpleListProperty<>(FXCollections.observableArrayList());
        selectedFeature = new SimpleStringProperty();
        localRowNumber=0;

        XYChart.Series<Number, Number> selectedAttributeSeries = new XYChart.Series<>();
        selectedAttributeSeries.setName("Selected attribute");
        selectedAttributeGraph.getData().add(selectedAttributeSeries);

        XYChart.Series<Number, Number> theMostCorrelativeAttributeSeries = new XYChart.Series<>();
        theMostCorrelativeAttributeSeries.setName("The most correlative attribute");
        theMostCorrelativeAttributeGraph.getData().add(theMostCorrelativeAttributeSeries);

        XYChart.Series<Number, Number> anomalyDetectSeries = new XYChart.Series<>();
        anomalyDetectSeries.setName("Anomaly detect");
        anomalyDetectGraph.getData().add(anomalyDetectSeries);


        selectedAttributePoints.addListener((observable, oldValue, newValue) -> {
            if (selectedAttributePoints.size() > 0) {
                if(localSelectedFeature.equals(selectedFeature.getValue()) && localRowNumber+1==newValue.size()) {
                Point newPoint = newValue.get(newValue.size() - 1);
                selectedAttributeSeries.getData().add(new XYChart.Data(newPoint.getX(), newPoint.getY()));
                }
                else {
                    UpdateLineChart(selectedAttributeSeries.getData(), newValue);
                    localSelectedFeature=selectedFeature.getValue();
                }
            }
            localRowNumber=newValue.size();
        });
        theMostCorrelativeAttributePoints.addListener((observable, oldValue, newValue) -> {
            if (theMostCorrelativeAttributePoints.size() > 0) {
                if(localSelectedFeature.equals(selectedFeature.getValue()) && localRowNumber+1==newValue.size()) {
                    Point newPoint = newValue.get(newValue.size() - 1);
                    theMostCorrelativeAttributeSeries.getData().add(new XYChart.Data(newPoint.getX(), newPoint.getY()));
                }
                else {
                    UpdateLineChart(theMostCorrelativeAttributeSeries.getData(), newValue);
                }
            }
            else if (theMostCorrelativeAttributePoints.size() == 0){
                theMostCorrelativeAttributeSeries.getData().clear();
            }
            localRowNumber=newValue.size();
        });
        selectedFeature.addListener((observable, oldValue, newValue) -> {
            if(localSelectedFeature==null) {
                localSelectedFeature = newValue;
            }
        });
    }
}

package view.AnomalyDetectionGraph;

import anomalyDetectors.AnomalyDetectorLinearRegression;
import anomalyDetectors.AnomalyDetectorZScoreAlgorithm;
import anomalyDetectors.AnomalyReport;
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
import javafx.scene.layout.AnchorPane;
import other_classes.Point;
import other_classes.TimeSeries;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AnomalyDetectionGraphController implements Initializable {
    @FXML private LineChart<Number, Number> selectedAttributeGraph,theMostCorrelativeAttributeGraph;
    @FXML public Label theMostCorrelativeAttribute;
    @FXML private AnchorPane anomalyDetectAnchorPane;
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






        TimeSeries ts = null;
        try {
            ts = new TimeSeries("C:\\Users\\Administrator\\Desktop\\reg_flight.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //AnomalyDetectorLinearRegression ad=new AnomalyDetectorLinearRegression();
        AnomalyDetectorZScoreAlgorithm ad=new AnomalyDetectorZScoreAlgorithm();
        //AnomalyDetectorHybridAlgorithm ad=new AnomalyDetectorHybridAlgorithm();

        ad.learnNormal(ts);

        // test the anomaly detector
        TimeSeries ts2= null;
        try {
            ts2 = new TimeSeries("C:\\Users\\Administrator\\Desktop\\anomaly_flight.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<AnomalyReport> reports = ad.detect(ts2);
       ad.paint(anomalyDetectAnchorPane,"throttle",ts2,0);


    }
}

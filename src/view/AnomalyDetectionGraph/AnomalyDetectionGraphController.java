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
import other_classes.Point;
import java.net.URL;
import java.util.ResourceBundle;

public class AnomalyDetectionGraphController implements Initializable {
    @FXML private LineChart<Number, Number> selectedAttributeGraph;
    private ListProperty<Point> selectedAttributePoints;
    private StringProperty selectedFeature;
    private String localSelectedFeature;

    public ListProperty<Point> getSelectedAttributePoints() { return selectedAttributePoints; }
    public StringProperty getSelectedFeature(){return selectedFeature;}

    public void UpdateLineChart(ObservableList<XYChart.Data<Number, Number>> seriesData, ObservableList<Point> points){
        seriesData.clear();
        for(int i =0;i<points.size();i++) {
            seriesData.add(new XYChart.Data(points.get(i).getX(), points.get(i).getY()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Deactivate features that cause slow down
        selectedAttributeGraph.setCreateSymbols(false);
        selectedAttributeGraph.setAnimated(false);
        selectedAttributePoints = new SimpleListProperty<>(FXCollections.observableArrayList());
        selectedFeature = new SimpleStringProperty();

        XYChart.Series<Number, Number> selectedAttributeSeries = new XYChart.Series<>();
        selectedAttributeSeries.setName("Selected attribute");
        selectedAttributeGraph.getData().add(selectedAttributeSeries);

        selectedAttributePoints.addListener((observable, oldValue, newValue) -> {
            if (selectedAttributePoints.size() > 0) {
                if(localSelectedFeature.equals(selectedFeature.getValue())) {
                Point newPoint = newValue.get(newValue.size() - 1);
                selectedAttributeSeries.getData().add(new XYChart.Data(newPoint.getX(), newPoint.getY()));
                }
                else {
                    UpdateLineChart(selectedAttributeSeries.getData(), newValue);
                    localSelectedFeature=selectedFeature.getValue();
                }
            }
        });
        selectedFeature.addListener((observable, oldValue, newValue) -> {
            if(localSelectedFeature==null) {
                localSelectedFeature = newValue;
            }
        });
    }
}
//theMostCorrelativeAttributeSeries,anomalyDetectSeries

package view.AnomalyDetectionGraph;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
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

    public ListProperty<Point> getSelectedAttributePoints() { return selectedAttributePoints; }

    public void UpdateLineChart(XYChart.Series series, ObservableList<Point> points){
        series.getData().clear();
        for(int i =0;i<points.size();i++) {

            series.getData().add(new XYChart.Data(points.get(i).getX(), points.get(i).getY()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Deactivate features that cause slow down
        selectedAttributeGraph.setCreateSymbols(false);
        selectedAttributeGraph.setAnimated(false);

        selectedAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        XYChart.Series<Number, Number> selectedAttributeSeries = new XYChart.Series<>();
        selectedAttributeSeries.setName("Series 1");
        selectedAttributeGraph.getData().add(selectedAttributeSeries);

        selectedAttributePoints.addListener((observable, oldValue, newValue)->{
            if(selectedAttributePoints.size()>0) {
                UpdateLineChart(selectedAttributeSeries, newValue);
                //System.out.println(newValue);
            }
        });
        /*
        selectedAttributeSeries.getData().add(new XYChart.Data<>(1, 20));
        selectedAttributeSeries.getData().add(new XYChart.Data<>(2, 100));
        selectedAttributeSeries.getData().add(new XYChart.Data<>(3, 80));
        selectedAttributeSeries.getData().add(new XYChart.Data<>(4, 180));
        selectedAttributeSeries.getData().add(new XYChart.Data<>(5, 20));
        selectedAttributeSeries.getData().add(new XYChart.Data<>(6, -10));

         */
    }
}

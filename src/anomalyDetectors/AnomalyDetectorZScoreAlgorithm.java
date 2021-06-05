package anomalyDetectors;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import other_classes.Point;
import other_classes.TimeSeries;

import java.util.*;

public class AnomalyDetectorZScoreAlgorithm implements AnomalyDetector{

    private List<Double> thresholds;
    public AnomalyDetectorZScoreAlgorithm(){
        this.thresholds = new LinkedList<>();
    }

    private static double calcAvg(ArrayList<Double> values) {
        double sum = 0.0;
        for (double value : values)
            sum += value;
        return sum / values.size();
    }

    private static float calculateSD(ArrayList<Double> values) {
        double standardDeviation = 0.0;
        int length = values.size();
        double mean = calcAvg(values);

        for (double num : values)
            standardDeviation += Math.pow(num - mean, 2);

        return (float) (Math.sqrt(standardDeviation / length));
    }

    private double zScore(ArrayList<Double> arr, double value) {
        float sDeviation = calculateSD(arr);
        if (sDeviation == 0) return 0.0f;

        double zScore = value - calcAvg(arr);
        return (Math.abs(zScore)) / calculateSD(arr);
    }

    private ArrayList<Double> subColumn(ArrayList<Double> arr, int index) {
        return new ArrayList<>(arr.subList(0, index));
    }


    @Override
    public void learnNormal(TimeSeries ts) {

        for (String feature : ts.getAttributes()) {
            List<Double> checkList = new ArrayList<>();
            int featureIndex = ts.getIndexByFeature(feature);
            ArrayList<Double> column = ts.getAttributeData(featureIndex);
            for (int index = 1; index < column.size(); index++) {
                ArrayList<Double> subColumn =subColumn(column, index);
                double check = zScore(subColumn, column.get(index));
                checkList.add(check);
            }
            thresholds.add(Collections.max(checkList));
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> reports = new ArrayList<>();
        int thresholdIndex = 0;
        for (String feature : ts.getAttributes()) {
            int featureIndex = ts.getIndexByFeature(feature);
            ArrayList<Double> column = ts.getAttributeData(featureIndex);
            for (int index = 1; index < column.size(); index++) {
                ArrayList<Double> subColumn = subColumn(column, index);
                double check = zScore(subColumn, column.get(index));
                if (check > thresholds.get(thresholdIndex))
                    reports.add(new AnomalyReport(feature, index));
            }
            thresholdIndex++;
        }
        return reports;
    }
    private void UpdateLineChart(ObservableList<XYChart.Data<Number, Number>> seriesData, ObservableList<Point> points){
        seriesData.clear();
        for(int i =0;i<points.size();i++) {
            seriesData.add(new XYChart.Data(points.get(i).getX(), points.get(i).getY()));
        }
    }
    /*
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
     */

    @Override
    public void paint(AnchorPane board,String feature,TimeSeries ts,int numOfRow) {
        /*
        private LineChart<Number, Number> selectedAttributeGraph;
         private String localSelectedFeature;
         private int localRowNumber;
          localRowNumber=0;

        XYChart.Series<Number, Number> selectedAttributeSeries = new XYChart.Series<>();
        selectedAttributeSeries.setName("Selected attribute");
        selectedAttributeGraph.getData().add(selectedAttributeSeries);

         lineChart.setPrefHeight(220);
        lineChart.setMinHeight(220);
        lineChart.setMaxHeight(220);
         */


        Label testt=new Label("shalom");
        board.getChildren().add(testt);

    }
}

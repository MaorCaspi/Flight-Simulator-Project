package anomalyDetectors;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import other_classes.TimeSeries;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AnomalyDetectorZScoreAlgorithm implements AnomalyDetector {

    private List<Double> thresholds;
    private TimeSeries anomalyTs;

    public AnomalyDetectorZScoreAlgorithm(){
        thresholds = new LinkedList<>();
    }


    private static double calcAvg(ArrayList<Double> values) {
        double sum = 0;
        for (double value : values)
            sum += value;
        return sum / values.size();
    }

    private static float calculateSD(ArrayList<Double> values) {
        double standardDeviation = 0;
        int length = values.size();
        double mean = calcAvg(values);

        for (double num : values)
            standardDeviation += Math.pow(num - mean, 2);

        return (float) (Math.sqrt(standardDeviation / length));
    }

    private double zScore(ArrayList<Double> arr, double value) {
        float sDeviation = calculateSD(arr);
        if (sDeviation == 0) return 0;

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
            ArrayList<Double> column = ts.getAttributeData(feature);
            for (int index = 0; index < column.size(); index++) {
                ArrayList<Double> subColumn =subColumn(column, index);
                double check = zScore(subColumn, column.get(index));
                checkList.add(check);
            }
            thresholds.add(Collections.max(checkList));
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        anomalyTs=ts;
        List<AnomalyReport> reports = new ArrayList<>();
        int thresholdIndex = 0;
        for (String feature : ts.getAttributes()) {
            ArrayList<Double> column = ts.getAttributeData(feature);
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
    @Override
    public AnchorPane paint() {
        AnchorPane board=new AnchorPane();

        LineChart<Number, Number> zscoreGraph=new LineChart<>(new NumberAxis(), new NumberAxis());
        zscoreGraph.setAnimated(false);
        zscoreGraph.setCreateSymbols(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Zscore Graph");
        zscoreGraph.getData().add(series);

        zscoreGraph.setPrefSize(300, 250);
        zscoreGraph.setMinSize(300, 250);
        zscoreGraph.setMaxSize(300, 250);

        board.getChildren().add(zscoreGraph);

        ArrayList<Double> selectedAttributeData = anomalyTs.getAttributeData(selectedFeature.getValue());

        AtomicInteger localNumOfRow= new AtomicInteger(0);

        selectedFeature.addListener((observable, oldValue, newValue) -> {
            selectedAttributeData.clear();
            selectedAttributeData.addAll(anomalyTs.getAttributeData(selectedFeature.getValue()));
            localNumOfRow.set(-1);
        });
        numOfRow.addListener((observable, oldValue, newValue) -> {
            if(localNumOfRow.get() +1==numOfRow.getValue()){
                double zScoreGrade=zScore(selectedAttributeData,selectedAttributeData.get(numOfRow.getValue()));
                Platform.runLater(()->{
                series.getData().add(new XYChart.Data(numOfRow.getValue(),zScoreGrade));
                });
            }
            else{
                Platform.runLater(()->{
                series.getData().clear();
                for(int i =0;i< numOfRow.getValue();i++) {
                    double zScoreGrade=zScore(selectedAttributeData,selectedAttributeData.get(numOfRow.getValue()));
                    series.getData().add(new XYChart.Data(i, zScoreGrade));
                }
                });
            }
            if(localNumOfRow.get()!=(-1)) {
                localNumOfRow.set(numOfRow.getValue());
            }
        });
        return board;
    }
}

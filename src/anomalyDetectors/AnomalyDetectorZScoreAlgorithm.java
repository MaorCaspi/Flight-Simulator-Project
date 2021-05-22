package anomalyDetectors;

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

    @Override
    public void paint() {

    }
}

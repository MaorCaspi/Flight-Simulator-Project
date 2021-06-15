package anomalyDetectors;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import other_classes.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AnomalyDetectorHybridAlgorithm implements AnomalyDetector {

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class Circle {
        private static final double MULTIPLICATIVE_EPSILON = 1 + 1e-14;
        public final Point center;
        public final double radius;

        public Circle(double x, double y, double radius) {
            center = new Point(x, y);
            this.radius = radius;
        }
        public Circle(Point p, double radius) {
            center = p;
            this.radius = radius;
        }

        public Circle(final Point p1, final Point p2) {
            center = new Point(((p1.getX() + p2.getX()) * 0.5), ((p1.getY() + p2.getY()) * 0.5));
            radius = center.distanceTo(p1);
        }

        public Circle(final Point p1, final Point p2, final Point p3) {
            final double p2MinusP1Y = p2.getY() - p1.getY();
            final double p3MinusP2Y = p3.getY() - p2.getY();

            if (p2MinusP1Y == 0 || p3MinusP2Y == 0) {
                center = new Point(0, 0);
                radius = 0;
            } else {
                final double a = -(p2.getX() - p1.getX()) / p2MinusP1Y;
                final double aPrime = -(p3.getX() - p2.getX()) / p3MinusP2Y;
                final double aPrimeMinusA = aPrime - a;

                if (aPrimeMinusA == 0) {
                    center = new Point(0, 0);
                    radius = 0;
                } else {
                    final double p2SquaredX = p2.getX() * p2.getX();
                    final double p2SquaredY = p2.getY() * p2.getY();


                    final double b = ((p2SquaredX - p1.getX() * p1.getX() + p2SquaredY - p1.getY() * p1.getY()) /
                            (2 * p2MinusP1Y));
                    final double bPrime = ((p3.getX() * p3.getX() - p2SquaredX + p3.getY() * p3.getY() - p2SquaredY) /
                            (2 * p3MinusP2Y));


                    final double xc = (b - bPrime) / aPrimeMinusA;
                    final double yc = a * xc + b;

                    final double dxc = p1.getX() - xc;
                    final double dyc = p1.getY() - yc;

                    center = new Point(xc, yc);
                    radius = Math.sqrt(dxc * dxc + dyc * dyc);
                }
            }
        }

        public boolean containsPoint(final Point p) {
            return center.distanceTo(p) <= radius * MULTIPLICATIVE_EPSILON;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private class WelzlAlgorithm {
        private Random rand = new Random();

        public Circle makeCircle(final List<Point> points) {
            return bMinidisk(points, new ArrayList());
        }

        private Circle bMinidisk(final List<Point> points, final List<Point> boundary) {
            Circle minimumCircle;

            if (boundary.size() == 3) {
                minimumCircle = new Circle(boundary.get(0), boundary.get(1), boundary.get(2));
            } else if (points.isEmpty() && boundary.size() == 2) {
                minimumCircle = new Circle(boundary.get(0), boundary.get(1));
            } else if (points.size() == 1 && boundary.isEmpty()) {
                minimumCircle = new Circle(points.get(0).getX(), points.get(0).getY(), 0);
            } else if (points.size() == 1 && boundary.size() == 1) {
                minimumCircle = new Circle(points.get(0), boundary.get(0));
            } else {
                Point p = points.remove(rand.nextInt(points.size()));
                minimumCircle = bMinidisk(points, boundary);

                if (minimumCircle != null && !minimumCircle.containsPoint(p)) {
                    boundary.add(p);
                    minimumCircle = bMinidisk(points, boundary);
                    boundary.remove(p);
                    points.add(p);
                }
            }
            return minimumCircle;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<String, Circle> welzlCircleModel;//storing circles for each pair of correlated features
    private AnomalyDetectorLinearRegression regressionDetector;
    private AnomalyDetectorZScoreAlgorithm zScoreDetector;
    private TimeSeries regTs, anomalyTs;
    private Map<String, List<Integer>> reportsFromDetect;//this map will help us at the paint method- we'll write here all the anomalies
    private Map<String, CorrelatedFeatures> allTmcf;//the all most correlated feature map
    private Map<String, String> featureNameToAlgorithm;//We will use it in the paint method, so we will know which graph to display to the user
    //if the feature name is not mentioned here then we will assign it to ZScore

    public AnomalyDetectorHybridAlgorithm() {
        welzlCircleModel = new HashMap();
        reportsFromDetect = new HashMap();
    }

    //Gets two lists and returns list of the points
    private List<Point> getListPoint(List<Double> x, List<Double> y) {
        List<Point> result = new ArrayList();
        for (int i = 0; i < x.size(); i++)
            result.add(new Point(x.get(i), y.get(i)));
        return result;
    }

    //We will use it ro know which algorithm
    //if the feature name is not mentioned here then we will assign it to ZScore
    private String getAlgorithmNameByFeatureName(String feature){
        if(featureNameToAlgorithm.containsKey(feature)){
            return featureNameToAlgorithm.get(feature);
        }
        return "ZScore";
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        regTs = ts;
        featureNameToAlgorithm = new HashMap();
        regressionDetector = new AnomalyDetectorLinearRegression();
        regressionDetector.learnNormal(ts);
        WelzlAlgorithm algorithm = new WelzlAlgorithm();
        allTmcf = regressionDetector.getTheMostCorrelatedFeaturesMap(true);
        CorrelatedFeatures c;
        for (String feature : ts.getAttributes()) {
            if(allTmcf.containsKey(feature)) {
                c = allTmcf.get(feature);
                if (Math.abs(c.correlation) >= 0.95) {
                    featureNameToAlgorithm.put(feature, "Regression");
                } else if (Math.abs(c.correlation) >= 0.5) {
                    featureNameToAlgorithm.put(feature, "Welzl");
                    //find who is the MostCorrelativeAttribute from the map
                    String theMostCorrelativeFeature=allTmcf.get(feature).feature1;
                    if(theMostCorrelativeFeature.equals(feature)) {
                        theMostCorrelativeFeature=allTmcf.get(feature).feature2;
                    }
                    welzlCircleModel.put(feature, algorithm.makeCircle(getListPoint(ts.getAttributeData(feature), ts.getAttributeData(theMostCorrelativeFeature))));
                }
                else{
                    featureNameToAlgorithm.put(feature, "ZScore");
                }
            }
            else {
                featureNameToAlgorithm.put(feature, "ZScore");
            }
        }
        zScoreDetector=new AnomalyDetectorZScoreAlgorithm();
        zScoreDetector.learnNormal(ts);
    }

    @Override
    public void detect(TimeSeries ts) {
        anomalyTs = ts;
        regressionDetector.detect(ts);
        Map<String, List<Integer>> reportsFromRegressionDetect = regressionDetector.getReportsFromDetect();
        zScoreDetector.detect(ts);
        Map<String, List<Integer>> reportsFromZscoreDetect = zScoreDetector.getReportsFromDetect();
        CorrelatedFeatures c;
        List<Point> dataFeature;
        for (String feature : ts.getAttributes()) {
            switch (getAlgorithmNameByFeatureName(feature)){
                case "Regression":
                    c = allTmcf.get(feature);
                    if(reportsFromRegressionDetect.containsKey(c.feature1+"-"+c.feature2)) {
                        reportsFromDetect.put(c.feature1+"-"+c.feature2, reportsFromRegressionDetect.get(c.feature1+"-"+c.feature2));
                    }
                    else if(reportsFromRegressionDetect.containsKey(c.feature2+"-"+c.feature1)) {
                        reportsFromDetect.put(c.feature2+"-"+c.feature1, reportsFromRegressionDetect.get(c.feature2+"-"+c.feature1));
                    }
                    break;
                case "Welzl":
                    c = allTmcf.get(feature);

                   //find who is the MostCorrelativeAttribute from the map
                    String theMostCorrelativeFeature=allTmcf.get(feature).feature1;
                    if(theMostCorrelativeFeature.equals(feature)) {
                        theMostCorrelativeFeature=allTmcf.get(feature).feature2;
                    }

                    dataFeature = getListPoint(ts.getAttributeData(feature), ts.getAttributeData(theMostCorrelativeFeature));
                    List<Integer> rowNumbersWithAnomalies = new ArrayList();
                    for (int i = 0; i < dataFeature.size(); i++) {
                        if (!welzlCircleModel.get(feature).containsPoint(dataFeature.get(i))) {
                            rowNumbersWithAnomalies.add(i);
                        }
                    }
                    if(rowNumbersWithAnomalies.size()>0) {
                        reportsFromDetect.put(feature, rowNumbersWithAnomalies);
                    }
                    break;
                case "ZScore":
                    if(reportsFromZscoreDetect.containsKey(feature)) {
                        reportsFromDetect.put(feature, reportsFromZscoreDetect.get(feature));
                    }
                    break;
            }
        }
    }

    //We will use it in the paint method
    private void selectedFeatureWasChange(AtomicReference<String> algorithmName,AtomicReference<String> description,LineChart zscoreLineChart,LineChart regressionLineChart,BubbleChart bubbleChart,ArrayList<Double> selectedAttributeData,AtomicInteger localNumOfRow,AtomicReference<String> theMostCorrelativeFeature,XYChart.Series<Number, Number> regressionPointsSeries, XYChart.Series<Number, Number> regressionLineSeries,AnchorPane board) {
        algorithmName.set(getAlgorithmNameByFeatureName(selectedFeature.getValue()));
        String temp;
        switch(algorithmName.get()) {
            case "Regression":
                temp=allTmcf.get(selectedFeature.getValue()).feature1;//find who is the MostCorrelativeAttribute from the map
                if(!temp.equals(selectedFeature.getValue())) {
                    theMostCorrelativeFeature.set(temp);
                }
                else{
                    theMostCorrelativeFeature.set(allTmcf.get(selectedFeature.getValue()).feature2);
                }
                description.set(selectedFeature.getValue() + "-" + theMostCorrelativeFeature.get());
                board.getStylesheets().add("anomalyDetectors/styleAnomalyGraphs.css");
                regressionDetector.paintLine(regressionPointsSeries,regressionLineSeries,allTmcf,regressionLineChart);
                regressionLineChart.setVisible(true);
                bubbleChart.setVisible(false);
                zscoreLineChart.setVisible(false);
                break;
            case "Welzl":
                description.set(selectedFeature.getValue());
                board.getStylesheets().clear();
                regressionLineChart.setVisible(false);
                bubbleChart.setVisible(true);
                zscoreLineChart.setVisible(false);
                break;
            case "ZScore":
                description.set(selectedFeature.getValue());
                board.getStylesheets().clear();
                selectedAttributeData.clear();
                selectedAttributeData.addAll(anomalyTs.getAttributeData(selectedFeature.getValue()));
                localNumOfRow.set(-1);
                regressionLineChart.setVisible(false);
                bubbleChart.setVisible(false);
                zscoreLineChart.setVisible(true);
                break;
        }
    }
    @Override
    public AnchorPane paint() {
        if(anomalyTs==null || regTs==null){return null;}
        AnchorPane board= new AnchorPane(new AnchorPane());
        AtomicInteger localNumOfRow= new AtomicInteger(0);
        AtomicReference<String> theMostCorrelativeFeature=new AtomicReference ();
        Background redColorBackground=new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
        allTmcf=regressionDetector.getTheMostCorrelatedFeaturesMap(true);
        AtomicReference<String> algorithmName=new AtomicReference();
        AtomicReference<String> description=new AtomicReference ();
        ///////////////////////////////////Regression:
        XYChart.Series<Number, Number> regressionPointsSeries = new XYChart.Series();//points
        XYChart.Series<Number, Number> regressionLineSeries = new XYChart.Series();//line
        LineChart<Number, Number> regressionLineChart=new LineChart(new NumberAxis(), new NumberAxis());
        regressionLineChart.setPrefSize(300, 250);
        regressionLineChart.getData().addAll(regressionPointsSeries,regressionLineSeries);
        regressionLineChart.setAnimated(false);
        board.getChildren().add(regressionLineChart);
        ///////////////////////////////////zscore:
        XYChart.Series<Number, Number> zscoreSeries = new XYChart.Series();
        LineChart<Number, Number> zscoreLineChart=new LineChart(new NumberAxis(), new NumberAxis());
        zscoreLineChart.setAnimated(false);
        zscoreLineChart.setCreateSymbols(false);
        zscoreLineChart.getData().add(zscoreSeries);
        zscoreLineChart.setPrefSize(300, 250);
        board.getChildren().add(zscoreLineChart);
        ArrayList<Double> selectedAttributeData = new ArrayList();//zscore
        /////////////////////////////////////Welzl:
        BubbleChart<Number, Number> bubbleChart=new BubbleChart(new NumberAxis(), new NumberAxis());
        bubbleChart.setAnimated(false);
        bubbleChart.setPrefSize(300, 250);
        board.getChildren().add(bubbleChart);


        selectedFeatureWasChange(algorithmName,description,zscoreLineChart,regressionLineChart, bubbleChart,selectedAttributeData,localNumOfRow,theMostCorrelativeFeature,regressionPointsSeries,regressionLineSeries,board);

        selectedFeature.addListener((observable, oldValue, newValue) -> {
            selectedFeatureWasChange(algorithmName,description,zscoreLineChart,regressionLineChart, bubbleChart,selectedAttributeData,localNumOfRow,theMostCorrelativeFeature,regressionPointsSeries,regressionLineSeries,board);
        });

        numOfRow.addListener((observable, oldValue, newValue) -> {

            if((reportsFromDetect.containsKey(description.get())) && (reportsFromDetect.get(description.get()).contains(numOfRow.getValue()))){
                board.setBackground(redColorBackground);
            }
            else{
                board.setBackground(null);
            }
            switch (algorithmName.get()) {
                case "Regression":
                    if (localNumOfRow.get() + 1 == numOfRow.getValue()) { //If the row number increases by 1 and the feature has not changed
                        Platform.runLater(() -> {
                            regressionPointsSeries.getData().add(new XYChart.Data(anomalyTs.getDataFromSpecificRowAndColumn(selectedFeature.getValue(), numOfRow.getValue()), anomalyTs.getDataFromSpecificRowAndColumn(theMostCorrelativeFeature.get(), numOfRow.getValue())));
                        });
                    }
                    else if (localNumOfRow.get() - 1 == numOfRow.getValue()) { //this is for the rewind option
                        Platform.runLater(() -> {
                            int length = regressionPointsSeries.getData().size();
                            if (length > 0) {
                                regressionPointsSeries.getData().remove(length - 1);
                            }
                        });
                    }
                    else {//Create the graph again from scratch
                        Platform.runLater(() -> {
                            regressionPointsSeries.getData().clear();
                            for (int i = 0; i < numOfRow.getValue(); i++) {
                                regressionPointsSeries.getData().add(new XYChart.Data(anomalyTs.getDataFromSpecificRowAndColumn(selectedFeature.getValue(), i), anomalyTs.getDataFromSpecificRowAndColumn(theMostCorrelativeFeature.get(), i)));
                            }
                        });
                    }
                    break;
               case "Welzl":

                   break;
               case "ZScore":
                   if (localNumOfRow.get() + 1 == numOfRow.getValue()) { //If the row number increases by 1 and the property has not changed
                       double zScoreGrade = zScoreDetector.zScore(selectedAttributeData, selectedAttributeData.get(numOfRow.getValue()));
                       Platform.runLater(() -> {
                           zscoreSeries.getData().add(new XYChart.Data(numOfRow.getValue(), zScoreGrade));
                       });
                   }
                   else if (localNumOfRow.get() - 1 == numOfRow.getValue()) { //this is for the rewind option
                       Platform.runLater(() -> {
                           int length = zscoreSeries.getData().size();
                           if (length > 0) {
                               zscoreSeries.getData().remove(length - 1);
                           }
                       });
                   }
                   else {//Create the graph again from scratch
                       Platform.runLater(() -> {
                           zscoreSeries.getData().clear();
                           for (int i = 0; i < numOfRow.getValue(); i++) {
                               double zScoreGrade = zScoreDetector.zScore(selectedAttributeData, selectedAttributeData.get(numOfRow.getValue()));
                               zscoreSeries.getData().add(new XYChart.Data(i, zScoreGrade));
                           }
                       });
                   }
                   if (localNumOfRow.get() != (-1)) {//If the property has not changed
                       localNumOfRow.set(numOfRow.getValue());
                   }
                   break;
            }
        });







/*

        BubbleChart<Number, Number> zscoreGraph=new BubbleChart(new NumberAxis(), new NumberAxis());
        zscoreGraph.setAnimated(false);

        XYChart.Series<Number, Number> seriesPoints = new XYChart.Series();
        XYChart.Series<Number, Number> seriesCircle = new XYChart.Series();

        zscoreGraph.getData().addAll(seriesPoints,seriesCircle);

        zscoreGraph.setPrefSize(300, 250);

        seriesCircle.getData().add(new XYChart.Data(4,4,3));
        seriesPoints.getData().add(new XYChart.Data(3,3,0.2));
        seriesPoints.getData().add(new XYChart.Data(6,6,0.2));
        seriesPoints.getData().add(new XYChart.Data(3,6,0.2));
        seriesPoints.getData().add(new XYChart.Data(4,4,0.2));

        board.getChildren().add(zscoreGraph);

 */


        return board;
    }
}

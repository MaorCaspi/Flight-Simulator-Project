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
import java.util.HashSet;
import java.util.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AnomalyDetectorHybridAlgorithm implements AnomalyDetector {

    private Map<String,HashSet<CorrelatedFeatures>> featuresToAlgorithm;
    private Map<CorrelatedFeatures,Circle> welzlCircleModel;//storing circles for each pair of correlated features
    private AnomalyDetectorLinearRegression regressionDetector;
    private AnomalyDetectorZScoreAlgorithm zScoreDetector;
    private TimeSeries regTs,anomalyTs;
    private Map<String, List<Integer>> reportsFromDetect;
    private Map<String, String[]> tmcf;//the most correlated feature map
    private Map<String,String> featureNameToAlgorithm;//We will use it in the paint method, so we will know which graph to display to the user
    //if the feature name is not mentioned here then we will assign it to ZScore


    public AnomalyDetectorHybridAlgorithm() {
        welzlCircleModel=new HashMap();
        reportsFromDetect=new HashMap();
    }

    private class WelzlAlgorithm{
        private Random rand = new Random();

        public Circle miniDisk(final List<Point> points) {
            return bMinidisk(points, new ArrayList());
        }

        private Circle bMinidisk(final List<Point> points, final List<Point> boundary) {
            Circle minimumCircle;

            if (boundary.size() == 3) {
                minimumCircle = new Circle(boundary.get(0), boundary.get(1), boundary.get(2));
            }
            else if (points.isEmpty() && boundary.size() == 2) {
                minimumCircle = new Circle(boundary.get(0), boundary.get(1));
            }
            else if (points.size() == 1 && boundary.isEmpty()) {
                minimumCircle = new Circle(points.get(0).getX(), points.get(0).getY(), 0);
            }
            else if (points.size() == 1 && boundary.size() == 1) {
                minimumCircle = new Circle(points.get(0), boundary.get(0));
            }
            else {
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
    private List<Point> getListPoint(List<Double> x,List<Double> y){
        List<Point> result=new ArrayList();
        for(int i=0;i<x.size();i++)
            result.add(new Point(x.get(i),y.get(i)));
        return result;
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        regTs=ts;
        featureNameToAlgorithm=new HashMap();
        regressionDetector=new AnomalyDetectorLinearRegression();
        regressionDetector.learnNormal(ts);
        List<CorrelatedFeatures> mostCorrelated=regressionDetector.getNormalModel();
        WelzlAlgorithm algorithm=new WelzlAlgorithm();
       //sorting the features by the correlation value
        featuresToAlgorithm=new HashMap();
        featuresToAlgorithm.put("ZScore", new HashSet());
        featuresToAlgorithm.put("Regression", new HashSet());
        featuresToAlgorithm.put("Welzl", new HashSet());
        for(CorrelatedFeatures c:mostCorrelated) {
            if(Math.abs(c.correlation)>=0.95) {
                featuresToAlgorithm.get("Regression").add(c);
                //Preparation for the paint method
                featureNameToAlgorithm.put(c.feature1,"Regression");
                featureNameToAlgorithm.put(c.feature2,"Regression");
            }
            else {
                if(Math.abs(c.correlation)<0.5) {
                    featuresToAlgorithm.get("ZScore").add(c);
                }
                else {
                    featuresToAlgorithm.get("Welzl").add(c);
                    //Preparation for the paint method
                    featureNameToAlgorithm.put(c.feature1, "Welzl");
                    featureNameToAlgorithm.put(c.feature2, "Welzl");
                }
            }
        }
        for(CorrelatedFeatures c:featuresToAlgorithm.get("Welzl")) {
            welzlCircleModel.put(c,algorithm.miniDisk(
                    getListPoint(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2))));
        }
        TimeSeries trainZScoreAlgorithm=new TimeSeries();
        for(CorrelatedFeatures c:featuresToAlgorithm.get("ZScore")) {
            trainZScoreAlgorithm.addCol(c.feature1,ts.getAttributeData(c.feature1));
            trainZScoreAlgorithm.addCol(c.feature2,ts.getAttributeData(c.feature2));
        }
        zScoreDetector=new AnomalyDetectorZScoreAlgorithm();
        zScoreDetector.learnNormal(trainZScoreAlgorithm);
        regressionDetector.learnNormal(ts);
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        anomalyTs=ts;
        List<AnomalyReport> detected=new ArrayList();
        detected.addAll(regressionDetector.detect(ts));
        reportsFromDetect.putAll(regressionDetector.getReportsFromDetect());

        TimeSeries testZScoreAlgorithm=new TimeSeries();
        //build new TimeSeries
        for(CorrelatedFeatures c:featuresToAlgorithm.get("ZScore")) {
            testZScoreAlgorithm.addCol(c.feature1,ts.getAttributeData(c.feature1));
            testZScoreAlgorithm.addCol(c.feature2,ts.getAttributeData(c.feature2));
        }
        detected.addAll(zScoreDetector.detect(testZScoreAlgorithm));
        reportsFromDetect.putAll(zScoreDetector.getReportsFromDetect());

        for(CorrelatedFeatures c:featuresToAlgorithm.get("Welzl")) {
            int count=1;
            List<Point> dataFeature=getListPoint(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2));
            for(Point p:dataFeature) {
                if(!welzlCircleModel.get(c).containsPoint(p)) {
                    String d=c.feature1 + "-" + c.feature2;
                    detected.add(new AnomalyReport(d, count));
                    if(!reportsFromDetect.containsKey(d)){
                        reportsFromDetect.put(d,new ArrayList());
                    }
                    reportsFromDetect.get(d).add(count-1);
                }
                count++;
            }
        }
        return detected;
    }

    //We will use it in the paint method, so we will know which graph to display to the user
    //if the feature name is not mentioned here then we will assign it to ZScore
    private String getAlgorithmNameByFeatureName(String feature){
        if(featureNameToAlgorithm.containsKey(feature)){
            return featureNameToAlgorithm.get(feature);
        }
        return "ZScore";
    }
    //We will use it in the paint method
    private void selectedFeatureWasChange(AtomicReference<String> algorithmName,AtomicReference<String> description,LineChart lineChart,BubbleChart bubbleChart,ArrayList<Double> selectedAttributeData,AtomicInteger localNumOfRow) {
        String theMostCorrelativeFeature;
        algorithmName.set(getAlgorithmNameByFeatureName(selectedFeature.getValue()));
        switch(algorithmName.get()) {
            case "Regression":
                theMostCorrelativeFeature = tmcf.get(selectedFeature.getValue())[0];
                description.set(selectedFeature.getValue() + "-" + theMostCorrelativeFeature);
                lineChart.setVisible(true);
                bubbleChart.setVisible(false);
                break;
            case "ZScore":
                description.set(selectedFeature.getValue());
                selectedAttributeData.clear();
                selectedAttributeData.addAll(anomalyTs.getAttributeData(selectedFeature.getValue()));
                localNumOfRow.set(-1);
                lineChart.setVisible(true);
                bubbleChart.setVisible(false);

                break;
            case "Welzl":
                theMostCorrelativeFeature = tmcf.get(selectedFeature.getValue())[0];
                description.set(selectedFeature.getValue() + "-" + theMostCorrelativeFeature);
                lineChart.setVisible(false);
                bubbleChart.setVisible(true);
                break;
        }
    }
    @Override
    public AnchorPane paint() {
        if(anomalyTs==null || regTs==null){return null;}
        AnchorPane board= new AnchorPane(new AnchorPane());

        Background redColorBackground=new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
        tmcf=regressionDetector.getTheMostCorrelatedFeaturesMap();
        AtomicReference<String> algorithmName=new AtomicReference();
        AtomicReference<String> description=new AtomicReference ();
        AtomicInteger localNumOfRow= new AtomicInteger(0);

        XYChart.Series<Number, Number> series1 = new XYChart.Series();
        XYChart.Series<Number, Number> series2 = new XYChart.Series();

        LineChart<Number, Number> lineChart=new LineChart(new NumberAxis(), new NumberAxis());
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.getData().add(series1);
        lineChart.setPrefSize(300, 250);
        board.getChildren().add(lineChart);

        BubbleChart<Number, Number> bubbleChart=new BubbleChart(new NumberAxis(), new NumberAxis());
        bubbleChart.setAnimated(false);
        /////////lineChart.getData().addAll(series1,series2);
        bubbleChart.setPrefSize(300, 250);
        board.getChildren().add(bubbleChart);



        ArrayList<Double> selectedAttributeData = new ArrayList();//zscore
        selectedFeatureWasChange(algorithmName,description,lineChart, bubbleChart,selectedAttributeData,localNumOfRow);//this is for the initialization

        selectedFeature.addListener((observable, oldValue, newValue) -> {
            selectedFeatureWasChange(algorithmName,description,lineChart, bubbleChart,selectedAttributeData,localNumOfRow);
        });

        numOfRow.addListener((observable, oldValue, newValue) -> {

            if((reportsFromDetect.containsKey(description.get())) && (reportsFromDetect.get(description.get()).contains(numOfRow.getValue()))){
                board.setBackground(redColorBackground);
            }
            else{
                board.setBackground(null);
            }
            switch (algorithmName.get()){
                case "ZScore":
                    if(localNumOfRow.get() +1==numOfRow.getValue()){ //If the row number increases by 1 and the property has not changed
                        double zScoreGrade=zScoreDetector.zScore(selectedAttributeData,selectedAttributeData.get(numOfRow.getValue()));
                        Platform.runLater(()->{
                            series1.getData().add(new XYChart.Data(numOfRow.getValue(),zScoreGrade));
                        });
                    }
                    else{//Create the graph again from scratch
                        Platform.runLater(()->{
                            series1.getData().clear();
                            for(int i =0;i< numOfRow.getValue();i++) {
                                double zScoreGrade=zScoreDetector.zScore(selectedAttributeData,selectedAttributeData.get(numOfRow.getValue()));
                                series1.getData().add(new XYChart.Data(i, zScoreGrade));
                            }
                        });
                    }
                    if(localNumOfRow.get()!=(-1)) {//If the property has not changed
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

//////////////////////////////////////////////////////////////////////////////////////////////////////
class Circle {
    public Point center;
    public double radius;

    public Circle(double x, double y, double radius) {
        center = new Point(x, y);
        this.radius = radius;
    }

    public Circle(final Point p1, final Point p2) {
        center = new Point(((p1.getX() + p2.getX()) * 0.5), ((p1.getY() + p2.getY()) * 0.5));
        radius = center.distanceTo(p1);
    }

    public Circle(final Point p1, final Point p2, final Point p3) {
        final double p2MinusP1Y = p2.getY() - p1.getY();
        final double p3MinusP2Y =  p3.getY() - p2.getY();

        if (p2MinusP1Y == 0 || p3MinusP2Y == 0) {
            center = new Point(0,0);
            radius = 0;
        }
        else {
            final double a = -(p2.getX() - p1.getX()) / p2MinusP1Y;
            final double aPrime = -(p3.getX() - p2.getX()) / p3MinusP2Y;
            final double aPrimeMinusA = aPrime - a;

            if (aPrimeMinusA == 0) {
                center = new Point(0, 0);
                radius = 0;
            }
            else {
                final double p2SquaredX = p2.getX() * p2.getX();
                final double p2SquaredY = p2.getY() * p2.getY();


                final double b = ((p2SquaredX - p1.getX() * p1.getX() + p2SquaredY - p1.getY() * p1.getY()) /
                        (2 * p2MinusP1Y));
                final double bPrime =((p3.getX() * p3.getX() - p2SquaredX + p3.getY() * p3.getY() - p2SquaredY) /
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
        return p.distanceSquaredTo(center) <= radius * radius;
    }
}

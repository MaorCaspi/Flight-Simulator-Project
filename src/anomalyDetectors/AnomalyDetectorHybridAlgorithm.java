package anomalyDetectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import other_classes.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;
import java.util.Random;

public class AnomalyDetectorHybridAlgorithm implements AnomalyDetector {

    HashMap<String,HashSet<CorrelatedFeatures>> featuresToAlgorithm;
    HashMap<CorrelatedFeatures,Circle> welzlCircleModel;//storing circles for each
    //pair of correlated features
    AnomalyDetectorLinearRegression regressionDetector;
    AnomalyDetectorZScoreAlgorithm zScoreDetector;

    public AnomalyDetectorHybridAlgorithm() {
        welzlCircleModel=new HashMap<>();
    }


    private class WelzlAlgorithm{
        private Random rand = new Random();

        public Circle miniDisk(final List<Point> points) {
            return bMinidisk(points, new ArrayList<>());
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
        List<Point> result=new ArrayList<>();
        for(int i=0;i<x.size();i++)
            result.add(new Point(x.get(i),y.get(i)));
        return result;
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        regressionDetector=new AnomalyDetectorLinearRegression();
        regressionDetector.learnNormal(ts);
        List<CorrelatedFeatures> mostCorrelated=regressionDetector.getNormalModel();
        WelzlAlgorithm algorithm=new WelzlAlgorithm();
        /* ----------sorting the features by the correlation value */
        featuresToAlgorithm=new HashMap<>();
        featuresToAlgorithm.put("ZScore", new HashSet<>());
        featuresToAlgorithm.put("Regression", new HashSet<>());
        featuresToAlgorithm.put("Welzl", new HashSet<>());
        for(CorrelatedFeatures c:mostCorrelated) {
            if(Math.abs(c.correlation)>=0.95)
                featuresToAlgorithm.get("Regression").add(c);
            else {
                if(Math.abs(c.correlation)<0.5)
                    featuresToAlgorithm.get("ZScore").add(c);
                else
                    featuresToAlgorithm.get("Welzl").add(c);
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
        List<AnomalyReport> detected=new ArrayList<>();
        detected.addAll(regressionDetector.detect(ts));
        TimeSeries testZScoreAlgorithm=new TimeSeries();
        // ------build new TimeSeries-----
        for(CorrelatedFeatures c:featuresToAlgorithm.get("ZScore")) {
            testZScoreAlgorithm.addCol(c.feature1,ts.getAttributeData(c.feature1));
            testZScoreAlgorithm.addCol(c.feature2,ts.getAttributeData(c.feature2));
        }
        detected.addAll(zScoreDetector.detect(testZScoreAlgorithm));
        for(CorrelatedFeatures c:featuresToAlgorithm.get("Welzl")) {
            int count=1;
            List<Point> dataFeature=getListPoint(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2));
            for(Point p:dataFeature) {
                if(!welzlCircleModel.get(c).containsPoint(p))
                    detected.add(new AnomalyReport(c.feature1+"-"+c.feature2, count));
                ++count;
            }
        }
        return detected;
    }

    @Override
    public AnchorPane paint() {
        AnchorPane board=new AnchorPane();
        return board;
    }

}

//////////////////////////////////////////////////////////////////////////////////////////////////////
class Circle {
    public Point center;
    public double radius;

    public Circle(final Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle(double x, double y, double radius) {
        center = new Point(x, y);
        this.radius = radius;
    }

    public Circle(final Point p1, final Point p2) {
        center = new Point(((p1.getX() + p2.getX()) * 0.5), ((p1.getY() + p2.getY()) * 0.5));
        radius = center.distanceTo(p1);
    }

    public Circle(final Point p1, final Point p2, final Point p3) {
        final double P2_MINUS_P1_Y = p2.getY() - p1.getY();
        final double P3_MINUS_P2_Y =  p3.getY() - p2.getY();

        if (P2_MINUS_P1_Y == 0.0 || P3_MINUS_P2_Y == 0.0) {
            center = new Point(0,0);
            radius = 0;
        }
        else {
            final double A = -(p2.getX() - p1.getX()) / P2_MINUS_P1_Y;
            final double A_PRIME = -(p3.getX() - p2.getX()) / P3_MINUS_P2_Y;
            final double A_PRIME_MINUS_A = A_PRIME - A;

            if (A_PRIME_MINUS_A == 0.0) {
                center = new Point(0, 0);
                radius = 0;
            }
            else {
                final double P2_SQUARED_X = p2.getX() * p2.getX();
                final double P2_SQUARED_Y = p2.getY() * p2.getY();


                final double B = ((P2_SQUARED_X - p1.getX() * p1.getX() + P2_SQUARED_Y - p1.getY() * p1.getY()) /
                        (2.0 * P2_MINUS_P1_Y));
                final double B_PRIME =((p3.getX() * p3.getX() - P2_SQUARED_X + p3.getY() * p3.getY() - P2_SQUARED_Y) /
                        (2.0 * P3_MINUS_P2_Y));


                final double XC = (B - B_PRIME) / A_PRIME_MINUS_A;
                final double YC = A * XC + B;

                final double DXC = p1.getX() - XC;
                final double DYC = p1.getY() - YC;

                center = new Point(XC, YC);
                radius = Math.sqrt(DXC * DXC + DYC * DYC);
            }
        }
    }

    public boolean containsAllPoints(final List<Point> points) {
        for (final Point p : points) {
            if (p != center && !containsPoint(p)) {
                return false;
            }
        }

        return true;
    }

    public boolean containsPoint(final Point p) {
        return p.distanceSquaredTo(center) <= radius * radius;
    }

    @Override
    public String toString() {
        return center.toString()  +  ", Radius: " + radius;
    }

}

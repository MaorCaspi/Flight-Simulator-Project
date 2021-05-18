package view.AnomalyDetectionGraph;

import javafx.fxml.FXML;
import other_classes.Point;

import java.util.ArrayList;
import java.util.List;

public class AnomalyDetectionGraphController{

    @FXML private List<Point> points1= new ArrayList<Point>() ;
    @FXML private List<Point> points2= new ArrayList<Point>() ;
    @FXML private List<Point> points3= new ArrayList<Point>() ;

    public List<Point> getPoints1() {
        return points1;
    }

    public List<Point> getPoints2() {
        return points2;
    }

    public List<Point> getPoints3() {
        return points3;
    }



}

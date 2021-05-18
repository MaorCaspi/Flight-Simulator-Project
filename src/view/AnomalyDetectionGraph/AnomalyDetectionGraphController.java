package view.AnomalyDetectionGraph;

import javafx.fxml.FXML;
import other_classes.PointGraph;

import java.util.ArrayList;
import java.util.List;

public class AnomalyDetectionGraphController{

    @FXML private List<PointGraph> points1= new ArrayList<>() ;
    @FXML private List<PointGraph> points2= new ArrayList<>() ;
    @FXML private List<PointGraph> points3= new ArrayList<>() ;

    public List<PointGraph> getPoints1() {
        return points1;
    }

    public List<PointGraph> getPoints2() {
        return points2;
    }

    public List<PointGraph> getPoints3() {
        return points3;
    }



}

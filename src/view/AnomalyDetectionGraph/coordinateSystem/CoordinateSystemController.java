package view.AnomalyDetectionGraph.coordinateSystem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import other_classes.Point;

public class CoordinateSystemController implements Initializable {

	private @FXML AnchorPane board;
	private double height,width,maxValue,minValue;
	private List<Circle> points,circles;
	private List<Line> lines;

	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public CoordinateSystemController() {
		maxValue=1000;////
		minValue=0;
	}
	public void changeSetting(double newMaxVal,double newMinVal) {
		maxValue=newMaxVal;
		minValue=newMinVal;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		points=new ArrayList<>();
		lines=new ArrayList<>();
		circles=new ArrayList<>();
		height=board.getPrefHeight();
		width=board.getPrefWidth();
	}
	public void addPoint(Point p, Paint color) {
		double displayX=(p.getX()/(maxValue-minValue))*width+width/2;
		double displayY=height-(p.getY()/(maxValue-minValue))*height-height/2;
		Circle toDisplay=new Circle();
		toDisplay.setRadius(5);
		toDisplay.setCenterX(displayX);
		toDisplay.setCenterY(displayY);
		toDisplay.setFill(color);
		points.add(toDisplay);
		board.getChildren().removeAll(points);
		board.getChildren().addAll(points);
	}
	public void addLine(other_classes.Line l,Paint color) {
		double valueForMinX=l.a*minValue+l.b;
		double valueForMaxX=l.a*maxValue+l.b;
		Line toDisplay=new Line();
		toDisplay.setStroke(color);
		toDisplay.setStrokeWidth(1.0);
		toDisplay.setStartX((minValue/(maxValue-minValue))*width+width/2);
		toDisplay.setEndX((maxValue/(maxValue-minValue))*width+width/2);
		toDisplay.setStartY((height-(valueForMinX/(maxValue-minValue))*height-height/2));
		toDisplay.setEndY((height-(valueForMaxX/(maxValue-minValue))*height-height/2));
		lines.add(toDisplay);
		board.getChildren().removeAll(lines);
		board.getChildren().addAll(lines);
	}
	public void addCircle(Point center,double radius,Paint color) {
		Circle toDisplay=new Circle();
		toDisplay.setCenterX((center.getX()/(maxValue-minValue))*width+width/2);
		toDisplay.setCenterY(height-(center.getY()/(maxValue-minValue))*height-height/2);
		toDisplay.setStroke(Color.GREENYELLOW);
		double radiusDisplay=((center.getX()+radius)/(maxValue-minValue))*width+width/2-(((center.getX())/(maxValue-minValue))*width+width/2);
		toDisplay.setRadius(radiusDisplay);
		toDisplay.setFill(Color.rgb(255, 255, 255, 0));
		toDisplay.setStroke(color);
		toDisplay.setStrokeWidth(2.0);
		circles.add(toDisplay);
		board.getChildren().removeAll(circles);
		board.getChildren().addAll(circles);
	}
	public void addSetPoints(List<Point> listPoints,Paint color){
		listPoints.forEach((p)->addPoint(p,color));
	}
	public void clear() {
		board.getChildren().removeAll(points);
		board.getChildren().removeAll(lines);
		board.getChildren().removeAll(circles);
		points.removeAll(points);
		lines.removeAll(lines);
		circles.removeAll(circles);
	}
}

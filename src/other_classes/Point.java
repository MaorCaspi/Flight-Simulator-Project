package other_classes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {

    private final DoubleProperty x,y;

    public Point(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }
    public double getX() {
        return x.getValue();
    }

    public double getY() {
        return y.getValue();
    }

    public double distanceTo(Point p) {
         return Math.hypot(x.getValue() - p.x.getValue(), y.getValue() - p.y.getValue());
    }
}
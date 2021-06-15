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

    public double distanceSquaredTo(final Point p) {
        final double dx = getX() - p.getX();
        final double dy = getY() - p.getY();
        return dx * dx + dy * dy;
    }
/*
    public double distanceTo(final Point p) {
        return Math.sqrt(distanceSquaredTo(p));
    }
 */
    public double distanceTo(Point p) {
         return Math.hypot(x.getValue() - p.x.getValue(), y.getValue() - p.y.getValue());
    }
    // Signed area / determinant thing
    public double cross(Point p) {
        return x.getValue() * p.y.getValue() - y.getValue() * p.x.getValue();
    }
    public Point subtract(Point p) {
        return new Point(x.getValue() - p.x.getValue(), y.getValue() - p.y.getValue());
    }

}
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

    public float distanceTo(final Point p) {
        return (float)Math.sqrt(distanceSquaredTo(p));
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY();
    }

}
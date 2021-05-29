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
        final double DX = getX() - p.getX();
        final double DY = getY() - p.getY();

        return DX * DX + DY * DY;
    }

    public float distanceTo(final Point p) {
        return (float)Math.sqrt(distanceSquaredTo(p));
    }

    public static boolean areColinear(final Point p1, final Point p2, final Point p3) {
        return Math.abs(p1.getX() * (p2.getY() - p3.getY()) + p2.getX() * (p3.getY() - p1.getY()) + p3.getX() * (p1.getY() - p2.getY())) == 0.0;
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY();
    }

}
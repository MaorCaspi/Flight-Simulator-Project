package other_classes;

public class Line {
    public final double a,b;
    public Line(double a, double b){
        this.a=a;
        this.b=b;
    }
    public double f(double x){
        return a*x+b;
    }
}

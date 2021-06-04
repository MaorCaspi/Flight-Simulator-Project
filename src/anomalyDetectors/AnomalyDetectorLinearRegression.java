package anomalyDetectors;

import other_classes.CorrelatedFeatures;
import other_classes.Line;
import other_classes.Point;
import other_classes.TimeSeries;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnomalyDetectorLinearRegression implements AnomalyDetector {

	private ArrayList<CorrelatedFeatures> cf;
	private double correlationThreshold;
	
	public AnomalyDetectorLinearRegression()
	{
		cf=new ArrayList<>();
		correlationThreshold=0.9;
	}

	public Map<String, String[]> getTheMostCorrelatedFeaturesMap() {
		Map<String, String[]> tmcf=new HashMap<>();
		for(int i=0;i<cf.size();i++){
			CorrelatedFeatures correlatedFeature= cf.get(i);
			String feature1 =correlatedFeature.getFeature1();
			String feature2 =correlatedFeature.getFeature2();
			double correlation=correlatedFeature.getCorrelation();

			if(!tmcf.containsKey(feature1)){
				tmcf.put(feature1, new String[]{feature2, String.valueOf(correlation)});
				tmcf.put(feature2, new String[]{feature1, String.valueOf(correlation)});
			}
			else if(Double.parseDouble(tmcf.get(feature1)[1])>correlation) {
					tmcf.get(feature1)[0] = feature2;
					tmcf.get(feature1)[1] = String.valueOf(correlation);
					if (tmcf.containsKey(feature2)) {
						tmcf.get(feature2)[0] = feature1;
						tmcf.get(feature2)[1] = String.valueOf(correlation);
					}
					else{
						tmcf.put(feature2, new String[]{feature1, String.valueOf(correlation)});
					}
				}
			}
		return tmcf;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		double[][] vals =new double[ts.getNumOfColumns()][ts.getRowSize()];
		for(int i=1;i<=ts.getNumOfColumns();i++){
			for(int j=0;j<ts.getRowSize();j++){
				vals[i-1][j]=ts.getAttributeData(i).get(j);
			}
		}

		for(int i=1;i<=ts.getNumOfColumns();i++){
			for(int j=i+1;j<=ts.getNumOfColumns();j++){
				double p=StatLib.pearson(vals[i-1],vals[j-1]);
				if(Math.abs(p)>correlationThreshold){

					Point ps[]=toPoints(ts.getAttributeData(i),ts.getAttributeData(j));
					Line lin_reg=StatLib.linear_reg(ps);
					double threshold=findThreshold(ps,lin_reg)*1.1f; // 10% increase

					CorrelatedFeatures c=new CorrelatedFeatures(ts.getFeatureByIndex(i), ts.getFeatureByIndex(j), p, lin_reg, threshold);

					cf.add(c);
				}
			}
		}
	}

	private Point[] toPoints(ArrayList<Double> x, ArrayList<Double> y) {
		Point[] ps=new Point[x.size()];
		for(int i=0;i<ps.length;i++)
			ps[i]=new Point(x.get(i),y.get(i));
		return ps;
	}
	
	private double findThreshold(Point ps[],Line rl){
		double max=0;
		for(int i=0;i<ps.length;i++){
			double d=Math.abs(ps[i].getY() - rl.f(ps[i].getX()));
			if(d>max)
				max=d;
		}
		return max;
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		ArrayList<AnomalyReport> v=new ArrayList<>();
		
		for(CorrelatedFeatures c : cf) {
			ArrayList<Double> x=ts.getAttributeData(ts.getIndexByFeature(c.getFeature1()));
			ArrayList<Double> y=ts.getAttributeData(ts.getIndexByFeature(c.getFeature2()));
			for(int i=0;i<x.size();i++){
				if(Math.abs(y.get(i) - c.getLin_reg().f(x.get(i)))>c.getThreshold()){
					String d=c.getFeature1() + "-" + c.getFeature2();
					v.add(new AnomalyReport(d,(i+1)));
				}
			}			
		}
		return v;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return cf;
	}

	@Override
	public void paint() {

	}
}
//////////////////////////////////////////////////////////////////////////////

class StatLib {

	// simple average
	public static double avg(double[] x){
		double sum=0;
		for(int i=0;i<x.length;sum+=x[i],i++);
		return sum/x.length;
	}

	// returns the variance of X and Y
	public static double var(double[] x){
		double av=avg(x);
		double sum=0;
		for(int i=0;i<x.length;i++){
			sum+=x[i]*x[i];
		}
		return sum/x.length - av*av;
	}

	// returns the covariance of X and Y
	public static double cov(double[] x, double[] y){
		double sum=0;
		for(int i=0;i<x.length;i++){
			sum+=x[i]*y[i];
		}
		sum/=x.length;

		return sum - avg(x)*avg(y);
	}


	// returns the Pearson correlation coefficient of X and Y
	public static double pearson(double[] x, double[] y){
		return (double) (cov(x,y)/(Math.sqrt(var(x))*Math.sqrt(var(y))));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		double x[]=new double[points.length];
		double y[]=new double[points.length];
		for(int i=0;i<points.length;i++){
			x[i]=points[i].getX();
			y[i]=points[i].getY();
		}
		double a=cov(x,y)/var(x);
		double b=avg(y) - a*(avg(x));

		return new Line(a,b);
	}

	// returns the deviation between point p and the line equation of the points
	public static double dev(Point p,Point[] points){
		Line l=linear_reg(points);
		return dev(p,l);
	}

	// returns the deviation between point p and the line
	public static double dev(Point p,Line l){
		return Math.abs(p.getY()-l.f(p.getX()));
	}
}
package other_classes;

import anomalyDetectors.CorrelatedFeatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PearsonCorrelation {

	public Map<String, CorrelatedFeatures> getTheMostCorrelatedFeaturesMap(TimeSeries ts) {//return The Most Correlated Features Map
		ArrayList<CorrelatedFeatures> allCf = new ArrayList();
		double[][] vals = new double[ts.getNumOfColumns()][ts.getRowSize()];
		for (int i = 0; i < ts.getNumOfColumns(); i++) {
			for (int j = 0; j < ts.getRowSize(); j++) {
				vals[i][j] = ts.getAttributeData(ts.getAttributes().get(i)).get(j);
			}
		}
		for (int i = 0; i < ts.getNumOfColumns(); i++) {
			for (int j = i + 1; j < ts.getNumOfColumns(); j++) {
				double p = StatLib.pearson(vals[i], vals[j]);

				Point ps[] = toPoints(ts.getAttributeData(ts.getAttributes().get(i)), ts.getAttributeData(ts.getAttributes().get(j)));
				Line lin_reg = StatLib.linear_reg(ps);
				double threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase

				CorrelatedFeatures c = new CorrelatedFeatures(ts.getAttributes().get(i), ts.getAttributes().get(j), p, lin_reg, threshold);
				if (Math.abs(p) > 0) {
					allCf.add(c);//we do this for the most correlated attribute- we want all of the correlation feature no meter what is the correlationThreshold
				}
			}
		}


		Map<String, CorrelatedFeatures> tmcf = new HashMap();
		String feature1, feature2;
		double correlation;

		for (int i = 0; i < allCf.size(); i++) {
			CorrelatedFeatures correlatedFeature = allCf.get(i);
			correlation = Math.abs(correlatedFeature.getCorrelation());
			feature1 = correlatedFeature.getFeature1();
			feature2 = correlatedFeature.getFeature2();

			if (!tmcf.containsKey(feature1) || tmcf.get(feature1).correlation < correlation) {
				tmcf.put(feature1, correlatedFeature);
			}
			if (!tmcf.containsKey(feature2) || tmcf.get(feature2).correlation < correlation) {
				tmcf.put(feature2, correlatedFeature);
			}
		}
		return tmcf;
	}
	private double findThreshold (Point ps[],Line rl){
		double max = 0;
		for (int i = 0; i < ps.length; i++) {
			double d = Math.abs(ps[i].getY() - rl.f(ps[i].getX()));
			if (d > max)
				max = d;
		}
		return max;
	}
	private Point[] toPoints (ArrayList < Double > x, ArrayList < Double > y){
		Point[] ps = new Point[x.size()];
		for (int i = 0; i < ps.length; i++)
			ps[i] = new Point(x.get(i), y.get(i));
		return ps;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static class StatLib {
		// simple average
		public static double avg(double[] x) {
			double sum = 0;
			for (int i = 0; i < x.length; sum += x[i], i++) ;
			return sum / x.length;
		}

		// returns the variance of X and Y
		public static double var(double[] x) {
			double av = avg(x);
			double sum = 0;
			for (int i = 0; i < x.length; i++) {
				sum += x[i] * x[i];
			}
			return sum / x.length - av * av;
		}

		// returns the covariance of X and Y
		public static double cov(double[] x, double[] y) {
			double sum = 0;
			for (int i = 0; i < x.length; i++) {
				sum += x[i] * y[i];
			}
			sum /= x.length;
			return sum - avg(x) * avg(y);
		}

		// returns the Pearson correlation coefficient of X and Y
		public static double pearson(double[] x, double[] y) {
			return (double) (cov(x, y) / (Math.sqrt(var(x)) * Math.sqrt(var(y))));
		}

		// performs a linear regression and returns the line equation
		public static Line linear_reg(Point[] points) {
			double x[] = new double[points.length];
			double y[] = new double[points.length];
			for (int i = 0; i < points.length; i++) {
				x[i] = points[i].getX();
				y[i] = points[i].getY();
			}
			double a = cov(x, y) / var(x);
			double b = avg(y) - a * (avg(x));

			return new Line(a, b);
		}

		// returns the deviation between point p and the line
		public static double dev(Point p, Line l) {
			return Math.abs(p.getY() - l.f(p.getX()));
			}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}


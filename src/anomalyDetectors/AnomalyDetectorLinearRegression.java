package anomalyDetectors;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import other_classes.Point;
import other_classes.TimeSeries;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AnomalyDetectorLinearRegression implements AnomalyDetector {
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static class StatLib {
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
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ArrayList<CorrelatedFeatures> cf,allCf;
	private double correlationThreshold;
	private Map<String, List<Integer>> reportsFromDetect;//this map will help us at the paint method- we'll write here all the anomalies
	private TimeSeries regTs,anomalyTs;
	
	public AnomalyDetectorLinearRegression()
	{
		cf=new ArrayList();
		correlationThreshold=0.9;
		reportsFromDetect=new HashMap();
	}

	public Map<String, List<Integer>> getReportsFromDetect() {
		return reportsFromDetect;
	}

	public Map<String, CorrelatedFeatures> getTheMostCorrelatedFeaturesMap(boolean findAllCf) {//return The Most Correlated Features Map, this will run after learnNormal methode
		Map<String, CorrelatedFeatures> tmcf=new HashMap();
		String feature1,feature2;
		double correlation;
		ArrayList<CorrelatedFeatures> arrayListOfCf;
		if(findAllCf){
			arrayListOfCf=allCf;
		}
		else{
			arrayListOfCf=cf;
		}
		for(int i=0;i<arrayListOfCf.size();i++){
			CorrelatedFeatures correlatedFeature= arrayListOfCf.get(i);
			correlation=Math.abs(correlatedFeature.getCorrelation());
			feature1 =correlatedFeature.getFeature1();
			feature2 =correlatedFeature.getFeature2();

			if(!tmcf.containsKey(feature1) || tmcf.get(feature1).correlation<correlation){
				tmcf.put(feature1, correlatedFeature);
			}
			if(!tmcf.containsKey(feature2) || tmcf.get(feature2).correlation<correlation){
				tmcf.put(feature2, correlatedFeature);
			}
		}
		return tmcf;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		regTs=ts;
		allCf=new ArrayList();
		double[][] vals =new double[ts.getNumOfColumns()][ts.getRowSize()];
		for(int i=0;i<ts.getNumOfColumns();i++){
			for(int j=0;j<ts.getRowSize();j++){
				vals[i][j]=ts.getAttributeData(ts.getAttributes().get(i)).get(j);
			}
		}

		for(int i=0;i<ts.getNumOfColumns();i++){
			for(int j=i+1;j<ts.getNumOfColumns();j++){
				double p=StatLib.pearson(vals[i],vals[j]);

				Point ps[]=toPoints(ts.getAttributeData(ts.getAttributes().get(i)),ts.getAttributeData(ts.getAttributes().get(j)));
				Line lin_reg=StatLib.linear_reg(ps);
				double threshold=findThreshold(ps,lin_reg)*1.1f; // 10% increase

				CorrelatedFeatures c=new CorrelatedFeatures(ts.getAttributes().get(i), ts.getAttributes().get(j), p, lin_reg, threshold);
				if(Math.abs(p)>0) {
					allCf.add(c);//we do this for the most correlated attribute- we want all of the correlation feature no meter what is the correlationThreshold
				}
				if(Math.abs(p)>correlationThreshold){
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
	public void detect(TimeSeries ts) {
		anomalyTs=ts;

		for(CorrelatedFeatures c : cf) {
			ArrayList<Double> x=ts.getAttributeData(c.getFeature1());
			ArrayList<Double> y=ts.getAttributeData(c.getFeature2());
			for(int i=0;i<x.size();i++){
				if(Math.abs(y.get(i) - c.getLin_reg().f(x.get(i)))>c.getThreshold()){
					String d=c.getFeature1() + "-" + c.getFeature2();
					if(!reportsFromDetect.containsKey(d)){
						reportsFromDetect.put(d,new ArrayList());
					}
					reportsFromDetect.get(d).add(i);
				}
			}			
		}
	}

	public List<CorrelatedFeatures> getAllTheCorrelatedFeatures(){
		return allCf;
	}

	void paintLine(XYChart.Series<Number, Number> pointsSeries, XYChart.Series<Number, Number> lineSeries, Map<String, CorrelatedFeatures> tmcf, LineChart<Number, Number> algGraph){//draw the linear straight
		if(!tmcf.containsKey(selectedFeature.getValue())){//if the selected feature has less then the threshold, so do nothing
			Platform.runLater(()->{
				pointsSeries.getData().clear();
				lineSeries.getData().clear();
				algGraph.setVisible(false);
			});
			return;
		}
		algGraph.setVisible(true);
		double maxX=Double.MIN_VALUE,minX=Double.MIN_VALUE;//The two points from which we construct the linear straight are x1 = the maximum value and x2 = the minimum value
		double temp;
		for(int i=0;i<regTs.getRowSize();i++) {
			temp=regTs.getAttributeData(selectedFeature.getValue()).get(i);
			maxX=(temp>maxX) ? temp:maxX;
			minX=(temp<minX) ? temp:minX;
		}
		Line line=tmcf.get(selectedFeature.getValue()).getLin_reg();

		final double finalMaxX = maxX;
		final double finalMinX = minX;
		Platform.runLater(()->{
			pointsSeries.getData().clear();
			lineSeries.getData().clear();
			lineSeries.getData().add(new XYChart.Data(finalMaxX,line.f(finalMaxX)));
			lineSeries.getData().add(new XYChart.Data(finalMinX,line.f(finalMinX)));
		});
	}

	@Override
	public AnchorPane paint() {
		if(anomalyTs==null || regTs==null){return null;}
		AnchorPane board=new AnchorPane();
		LineChart<Number, Number> algGraph=new LineChart(new NumberAxis(), new NumberAxis());

		algGraph.setPrefSize(300, 250);

		XYChart.Series<Number, Number> pointsSeries = new XYChart.Series();//points
		XYChart.Series<Number, Number> lineSeries = new XYChart.Series();//line

		algGraph.getData().addAll(pointsSeries,lineSeries);
		algGraph.setAnimated(false);
		board.getChildren().add(algGraph);
		board.getStylesheets().add("anomalyDetectors/styleAnomalyGraphs.css");

		Map<String, CorrelatedFeatures> tmcf=getTheMostCorrelatedFeaturesMap(false);

		paintLine(pointsSeries,lineSeries,tmcf,algGraph);//this is for the initialization

		selectedFeature.addListener((observable, oldValue, newValue) -> {
			paintLine(pointsSeries,lineSeries,tmcf,algGraph);
		});

		Background redColorBackground=new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
		AtomicInteger localNumOfRow= new AtomicInteger(0);

		numOfRow.addListener((observable, oldValue, newValue) -> {
			if(!tmcf.containsKey(selectedFeature.getValue())) {
				Platform.runLater(() -> {
					pointsSeries.getData().clear();
					lineSeries.getData().clear();
				});
				return;
			}
			String theMostCorrelativeFeature;
			theMostCorrelativeFeature=tmcf.get(selectedFeature.getValue()).feature1;//find who is the MostCorrelativeAttribute from the map
			if(theMostCorrelativeFeature.equals(selectedFeature.getValue())) {
				theMostCorrelativeFeature=tmcf.get(selectedFeature.getValue()).feature2;
			}

			String d=selectedFeature.getValue()+"-"+ theMostCorrelativeFeature;
			String d2=theMostCorrelativeFeature+"-"+selectedFeature.getValue();
			if((reportsFromDetect.containsKey(d)) && (reportsFromDetect.get(d).contains(numOfRow.getValue()))){
				board.setBackground(redColorBackground);
			}
			else if((reportsFromDetect.containsKey(d2)) && (reportsFromDetect.get(d2).contains(numOfRow.getValue()))){
				board.setBackground(redColorBackground);
			}
			else{
				board.setBackground(null);
			}
			if(localNumOfRow.get() +1==numOfRow.getValue()){ //If the row number increases by 1 and the feature has not changed
				final String finalTheMostCorrelativeFeature1 = theMostCorrelativeFeature;
				Platform.runLater(()->{
					pointsSeries.getData().add(new XYChart.Data(anomalyTs.getDataFromSpecificRowAndColumn(selectedFeature.getValue(),numOfRow.getValue()),anomalyTs.getDataFromSpecificRowAndColumn(finalTheMostCorrelativeFeature1,numOfRow.getValue())));
				});
			}
			else if(localNumOfRow.get() -1==numOfRow.getValue()){ //this is for the rewind option
				Platform.runLater(()->{
					int length=pointsSeries.getData().size();
					if(length>0) {
						pointsSeries.getData().remove(length - 1);
					}
				});
			}
			else{//Create the graph again from scratch
				final String finalTheMostCorrelativeFeature = theMostCorrelativeFeature;
				Platform.runLater(()->{
					pointsSeries.getData().clear();
					for(int i =0;i< numOfRow.getValue();i++) {
						pointsSeries.getData().add(new XYChart.Data(anomalyTs.getDataFromSpecificRowAndColumn(selectedFeature.getValue(),i),anomalyTs.getDataFromSpecificRowAndColumn(finalTheMostCorrelativeFeature,i)));
					}
				});
			}
			localNumOfRow.set(numOfRow.getValue());
		});
		return board;
	}
}


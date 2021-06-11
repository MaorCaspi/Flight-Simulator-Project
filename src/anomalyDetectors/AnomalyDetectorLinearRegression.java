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
import other_classes.CorrelatedFeatures;
import other_classes.Line;
import other_classes.Point;
import other_classes.TimeSeries;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AnomalyDetectorLinearRegression implements AnomalyDetector {

	private ArrayList<CorrelatedFeatures> cf;
	private double correlationThreshold;
	private Map<String, List<Integer>> reportsFromDetect;
	private TimeSeries regTs,anomalyTs;
	
	public AnomalyDetectorLinearRegression()
	{
		cf=new ArrayList<>();
		correlationThreshold=0.9;
		reportsFromDetect=new HashMap<>();
	}

	public Map<String, String[]> getTheMostCorrelatedFeaturesMap() {//return The Most Correlated Features Map, this will run after learnNormal methode
		Map<String, String[]> tmcf=new HashMap<>();
		for(int i=0;i<cf.size();i++){
			CorrelatedFeatures correlatedFeature= cf.get(i);
			String feature1 =correlatedFeature.getFeature1();
			String feature2 =correlatedFeature.getFeature2();
			double correlation=correlatedFeature.getCorrelation();

			if(!tmcf.containsKey(feature1)){
				tmcf.put(feature1, new String[]{feature2, String.valueOf(correlation),String.valueOf(i)});
				tmcf.put(feature2, new String[]{feature1, String.valueOf(correlation),String.valueOf(i)});
			}
			else if(Double.parseDouble(tmcf.get(feature1)[1])>correlation) {
					tmcf.get(feature1)[0] = feature2;
					tmcf.get(feature1)[1] = String.valueOf(correlation);
					tmcf.get(feature1)[2] =String.valueOf(i);
					if (tmcf.containsKey(feature2)) {
						tmcf.get(feature2)[0] = feature1;
						tmcf.get(feature2)[1] = String.valueOf(correlation);
						tmcf.get(feature2)[2] =String.valueOf(i);
					}
					else{
						tmcf.put(feature2, new String[]{feature1, String.valueOf(correlation),String.valueOf(i)});
					}
				}
			}
		return tmcf;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		regTs=ts;
		double[][] vals =new double[ts.getNumOfColumns()][ts.getRowSize()];
		for(int i=0;i<ts.getNumOfColumns();i++){
			for(int j=0;j<ts.getRowSize();j++){
				vals[i][j]=ts.getAttributeData(ts.getAttributes().get(i)).get(j);
			}
		}

		for(int i=0;i<ts.getNumOfColumns();i++){
			for(int j=i+1;j<ts.getNumOfColumns();j++){
				double p=StatLib.pearson(vals[i],vals[j]);
				if(Math.abs(p)>correlationThreshold){

					Point ps[]=toPoints(ts.getAttributeData(ts.getAttributes().get(i)),ts.getAttributeData(ts.getAttributes().get(j)));
					Line lin_reg=StatLib.linear_reg(ps);
					double threshold=findThreshold(ps,lin_reg)*1.1f; // 10% increase

					CorrelatedFeatures c=new CorrelatedFeatures(ts.getAttributes().get(i), ts.getAttributes().get(j), p, lin_reg, threshold);

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
		anomalyTs=ts;
		ArrayList<AnomalyReport> v=new ArrayList<>();
		
		for(CorrelatedFeatures c : cf) {
			ArrayList<Double> x=ts.getAttributeData(c.getFeature1());
			ArrayList<Double> y=ts.getAttributeData(c.getFeature2());
			for(int i=0;i<x.size();i++){
				if(Math.abs(y.get(i) - c.getLin_reg().f(x.get(i)))>c.getThreshold()){
					String d=c.getFeature1() + "-" + c.getFeature2();
					v.add(new AnomalyReport(d,(i+1)));
					if(!reportsFromDetect.containsKey(d)){
						reportsFromDetect.put(d,new ArrayList<>());
					}
					reportsFromDetect.get(d).add(i+1);
				}
			}			
		}
		return v;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return cf;
	}

	private void paintLine(XYChart.Series<Number, Number> pointsSeries,XYChart.Series<Number, Number> lineSeries,Map<String, String[]> tmcf){//draw the linear straight
		if(!tmcf.containsKey(selectedFeature.getValue())){
			Platform.runLater(()->{
				pointsSeries.getData().clear();
				lineSeries.getData().clear();
			});
			return;
		}
		double maxX=Double.MIN_VALUE,minX=Double.MIN_VALUE;//The two points from which we construct the linear straight are x1 = the maximum value and x2 = the minimum value
		double temp;
		for(int i=0;i<regTs.getRowSize();i++) {
			temp=regTs.getAttributeData(selectedFeature.getValue()).get(i);
			maxX=(temp>maxX) ? temp:maxX;
			minX=(temp<minX) ? temp:minX;
		}
		int indexAtCf=Integer.valueOf(tmcf.get(selectedFeature.getValue())[2]);
		CorrelatedFeatures currentCorrelatedFeatures=cf.get(indexAtCf);
		Line line=currentCorrelatedFeatures.getLin_reg();

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
		AnchorPane board=new AnchorPane();
		LineChart<Number, Number> algGraph=new LineChart<>(new NumberAxis(), new NumberAxis());

		algGraph.setPrefSize(300, 250);

		XYChart.Series<Number, Number> pointsSeries = new XYChart.Series();//points
		XYChart.Series<Number, Number> lineSeries = new XYChart.Series();//line

		algGraph.getData().addAll(pointsSeries,lineSeries);
		algGraph.setAnimated(false);
		board.getChildren().add(algGraph);
		board.getStylesheets().add("anomalyDetectors/styleAnomalyGraphs.css");

		Map<String, String[]> tmcf=getTheMostCorrelatedFeaturesMap();

		paintLine(pointsSeries,lineSeries,tmcf);//this is for the initialization

		selectedFeature.addListener((observable, oldValue, newValue) -> {
			paintLine(pointsSeries,lineSeries,tmcf);
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
			String theMostCorrelativeFeature=tmcf.get(selectedFeature.getValue())[0];
			String d=selectedFeature.getValue()+"-"+ theMostCorrelativeFeature;
			if((reportsFromDetect.containsKey(d)) && (reportsFromDetect.get(d).contains(numOfRow.getValue()))){
				board.setBackground(redColorBackground);
			}
			else{
				board.setBackground(null);
			}
			if(localNumOfRow.get() +1==numOfRow.getValue()){ //If the row number increases by 1 and the feature has not changed
				Platform.runLater(()->{
					pointsSeries.getData().add(new XYChart.Data(anomalyTs.getDataFromSpecificRowAndColumn(selectedFeature.getValue(),numOfRow.getValue()),anomalyTs.getDataFromSpecificRowAndColumn(theMostCorrelativeFeature,numOfRow.getValue())));
				});
			}
			else{//Create the graph again from scratch
				Platform.runLater(()->{
					pointsSeries.getData().clear();
					for(int i =0;i< numOfRow.getValue();i++) {
						pointsSeries.getData().add(new XYChart.Data(anomalyTs.getDataFromSpecificRowAndColumn(selectedFeature.getValue(),i),anomalyTs.getDataFromSpecificRowAndColumn(theMostCorrelativeFeature,i)));
					}
				});
			}
			localNumOfRow.set(numOfRow.getValue());
		});
		return board;
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
package anomalyDetectors;


import other_classes.TimeSeries;

import java.io.IOException;
import java.util.List;

public class MainTrainAnomalyDetector {

	public static void main(String[] args) {

		TimeSeries ts = null;
		try {
			ts = new TimeSeries("C:\\Users\\Administrator\\Desktop\\reg_flight.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//AnomalyDetectorLinearRegression ad=new AnomalyDetectorLinearRegression();
		//AnomalyDetectorZScoreAlgorithm ad=new AnomalyDetectorZScoreAlgorithm();
		AnomalyDetectorHybridAlgorithm ad=new AnomalyDetectorHybridAlgorithm();

		ad.learnNormal(ts);
	
		// test the anomaly detector
		TimeSeries ts2= null;
		try {
			ts2 = new TimeSeries("C:\\Users\\Administrator\\Desktop\\anomaly_flight.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<AnomalyReport> reports = ad.detect(ts2);
		for(AnomalyReport ar : reports) {
			System.out.println(ar.timeStep);
		}

		System.out.println("done");
	}

}

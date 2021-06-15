package anomalyDetectors;

public class CorrelatedFeatures {

	public final String feature1,feature2;
	public final double correlation;
	public final Line lin_reg;
	public final double threshold;
	
	public CorrelatedFeatures(String feature1, String feature2, double correlation, Line lin_reg, double threshold) {
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.correlation = correlation;
		this.lin_reg = lin_reg;
		this.threshold = threshold;
	}

	public String getFeature1() {
		return feature1;
	}

	public String getFeature2() {
		return feature2;
	}

	public double getCorrelation() {
		return correlation;
	}

	public Line getLin_reg() {
		return lin_reg;
	}

	public double getThreshold() {
		return threshold;
	}
	
}

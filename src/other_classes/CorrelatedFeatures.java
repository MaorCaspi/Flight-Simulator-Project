package other_classes;

public class CorrelatedFeatures {
	public final String feature1,feature2;
	public final double corrlation;
	public final Line lin_reg;
	public final double threshold;
	
	public CorrelatedFeatures(String feature1, String feature2, double corrlation, Line lin_reg, double threshold) {
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.corrlation = corrlation;
		this.lin_reg = lin_reg;
		this.threshold = threshold;
	}
	
}

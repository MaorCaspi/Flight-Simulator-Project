package other_classes;

import java.util.List;

public interface AnomalyDetector {
    public void learnNormal(TimeSeries ts);
    public List<AnomalyReport> detect(TimeSeries ts);
    public void paint();
}

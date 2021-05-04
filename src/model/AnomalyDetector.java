package model;

import other_classes.TimeSeries;

public interface AnomalyDetector {
    public void learn(TimeSeries ts);
    public void detect(TimeSeries ts);
    public void paint();
}

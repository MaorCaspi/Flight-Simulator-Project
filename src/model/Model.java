package model;

import other_classes.TimeSeries;

public interface Model {
    public boolean getPainter(Runnable r);
    public void setTimeSeries(TimeSeries ts);
    public void play(double start, double speed);
    public void pause();
}

package model;

import other_classes.TimeSeries;

public interface Model {
    public boolean getPainter(Runnable r);
    public void setTimeSeries(TimeSeries ts);
    public void setProgression(int rowNumber);
    public void play(int start);
    public void pause();
    public void forward();
    public void rewind();

}

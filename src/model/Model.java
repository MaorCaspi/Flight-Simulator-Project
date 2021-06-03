package model;

import other_classes.Properties;
import other_classes.TimeSeries;

public interface Model {
    public boolean getPainter(Runnable r);
    public void setPlaySpeed(double val);
    public void setProperties(Properties properties);
    public void setTimeSeries(TimeSeries ts);
    public void setProgression(int rowNumber);
    public void setNumOfRow(int numOfRow);
    public int getNumOfRow();
    public Properties getProperties();
    public void play(int start);
    public void pause();
    public void forward();
    public void rewind();
}

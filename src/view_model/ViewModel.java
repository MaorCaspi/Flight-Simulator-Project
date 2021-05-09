package view_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.FlightSimulatorModel;
import other_classes.TimeSeries;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer  {
    FlightSimulatorModel m;
    boolean firstTimePlay;
    TimeSeries ts;
    public DoubleProperty playSpeed,progression;
    public StringProperty anomalyFlightPath,time;

    public ViewModel(FlightSimulatorModel m){
        this.m=m;
        firstTimePlay=true;
        playSpeed = new SimpleDoubleProperty();
        progression = new SimpleDoubleProperty();
        anomalyFlightPath = new SimpleStringProperty();
        time = new SimpleStringProperty();
        playSpeed.addListener((observable, oldValue, newValue)->{m.setPlaySpeed((double)newValue);});
        anomalyFlightPath.addListener((observable, oldValue, newValue) -> {ts=new TimeSeries(newValue); m.setTimeSeries(ts);});
        progression.addListener((observable, oldValue, newValue)->{
            if(ts!=null)
                m.setProgression((int)(ts.getRowSize() * newValue.doubleValue()));});
    }
    private void setTime(int rowNumber)
    {
        int totalMilliseconds=(int)(rowNumber*100);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int seconds = (int) (totalMilliseconds / 1000) % 60 ;
        int minutes = (int) ((totalMilliseconds / (1000*60)) % 60);
        time.setValue(minutes+":"+seconds);
    }
    public void play(){
        m.play((int)(progression.getValue()* ts.getRowSize()));
    }
    public void pause() {
        m.pause();
    }
    public void forward(){ m.forward();}
    public void rewind(){ m.rewind();}

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){
            progression.setValue((double)m.getNumOfRow()/ts.getRowSize());
            setTime(m.getNumOfRow());
        }
    }

}

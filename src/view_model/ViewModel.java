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
    public StringProperty anomalyFlightPath;

    public ViewModel(FlightSimulatorModel m){
        this.m=m;
        firstTimePlay=true;
        playSpeed = new SimpleDoubleProperty();
        progression = new SimpleDoubleProperty();
        anomalyFlightPath = new SimpleStringProperty();
        playSpeed.addListener((observable, oldValue, newValue)->{m.setPlaySpeed((double)newValue);});
        anomalyFlightPath.addListener((observable, oldValue, newValue) -> {ts=new TimeSeries(newValue); m.setTimeSeries(ts);});
        progression.addListener((observable, oldValue, newValue)->{m.setProgression((int)(ts.getRowSize() * newValue.doubleValue()));});
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
        }
    }

}

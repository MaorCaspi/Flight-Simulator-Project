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
    public DoubleProperty playSpeed;
    public StringProperty anomalyFlightPath;

    public ViewModel(FlightSimulatorModel m){
        this.m=m;
        m.addObserver(this);
        firstTimePlay=true;
        playSpeed = new SimpleDoubleProperty();
        anomalyFlightPath = new SimpleStringProperty();
        //playSpeed.addListener(observable, oldValue, newValue)->m.setPlaySpeed((double)newValue);
        anomalyFlightPath.addListener((observable, oldValue, newValue) -> {ts=new TimeSeries(newValue);});

    }
    public void play(){
        if(firstTimePlay) {
            firstTimePlay=false;
            Thread t1=new Thread()
            {
                public void run() {
                    m.playCsv(ts);
                }
            };
            t1.setDaemon(true);//when the main program ends-this thread will terminate too
            t1.start();
        }
        else {
            m.wakeUP();
        }
    }
    public void pause() {
        if(!m.pause)
        {
            m.pause=true;
        }
    }
    public void forward(){}
    public void rewind(){}

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){}
    }

}

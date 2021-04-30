package view_model;

import model.FlightGearModel;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer  {
    FlightGearModel m;
    public ViewModel(FlightGearModel m){
        this.m=m;
    }
    public void setTimeSeries() { m.setTimeSeries(); }
    public void play(){ m.play(); }
    public void pause() {m.pause(); }
    public void forward() {m.forward();  }
    public void rewind(){m.rewind(); }

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){}
    }

}

package view_model;

import model.Model;
import other_classes.TimeSeries;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer  {
    Model m;
    boolean firstTimePlay;
    TimeSeries ts;

    public ViewModel(Model m){
        this.m=m;
        firstTimePlay=true;
    }
    public void setTimeSeries() { System.out.println("setTimeSeries"); }
    public void play(){
        if(firstTimePlay) {
            firstTimePlay=false;
            ts = new TimeSeries("reg_flight.csv");
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
    public void forward() { }
    public void rewind(){ }

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){}
    }

}

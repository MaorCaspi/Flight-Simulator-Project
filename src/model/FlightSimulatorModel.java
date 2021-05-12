package model;

import other_classes.FGPlayer;
import other_classes.Properties;
import other_classes.TimeSeries;
import java.util.Observable;


public class FlightSimulatorModel extends Observable implements Model{
    private double playSpeed;
    private FGPlayer fgPlayer;
    private boolean firstTimePlay;
    private TimeSeries ts;
    private Thread thread;
    private int numOfRow;
    private Properties properties;


    public FlightSimulatorModel() {

        firstTimePlay=true;
        numOfRow=0;
        properties=new Properties();
    }
    public void setPlaySpeed(double val) {
        playSpeed=val;
        if(!firstTimePlay) {
            fgPlayer.setPlaySpeed(val);
        }
    }
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public Properties getProperties() {
        return properties;
    }

    @Override
    public boolean getPainter(Runnable r) {
        ///!
        return false;
    }

    @Override
    public void setTimeSeries(TimeSeries ts) {
        this.ts=ts;
    }

    @Override
    public void setProgression(int rowNumber) {
        if (!firstTimePlay && !(rowNumber-5<this.numOfRow && this.numOfRow<rowNumber+5)) {
            thread.stop();
            thread=new Thread()
            {
                public void run() {
                    fgPlayer.play(rowNumber);
                }
            };
            thread.setDaemon(true);//when the main program ends-this thread will terminate too
            thread.start();
        }
    }

    @Override
    public void play(int start) {
        if(firstTimePlay) {
            firstTimePlay=false;
            fgPlayer=new FGPlayer(ts,playSpeed,this);
            thread=new Thread()
            {
                public void run() {
                    fgPlayer.play(start);
                }
            };
            thread.setDaemon(true);//when the main program ends-this thread will terminate too
            thread.start();
        }
        else if(fgPlayer.isMoveForwardIsInProgress()) {
            fgPlayer.unForward();
        }
        else if(fgPlayer.isMoveRewindIsInProgress()) {
            fgPlayer.setMoveRewindIsInProgress(false);
            thread.stop();
            thread=new Thread()
            {
                public void run() {
                    fgPlayer.play(start);
                }
            };
            thread.setDaemon(true);//when the main program ends-this thread will terminate too
            thread.start();
        }
        else {
            fgPlayer.wakeUP();
        }

    }

    @Override
    public void pause() {
        if(fgPlayer!=null && !fgPlayer.isPause()) {
            fgPlayer.setPause(true);
        }
    }

    @Override
    public void forward() {
        if(fgPlayer!=null) {
            fgPlayer.forward();
        }
    }

    @Override
    public void rewind() {
        if(fgPlayer!=null && !fgPlayer.isMoveRewindIsInProgress()) {
            fgPlayer.setMoveRewindIsInProgress(true);
            thread.stop();
            thread=new Thread()
            {
                public void run() {
                    fgPlayer.rewind(numOfRow);
                }
            };
            thread.setDaemon(true);//when the main program ends-this thread will terminate too
            thread.start();
        }
    }

    public void setNumOfRow(int numOfRow){
        this.numOfRow=numOfRow;
        setChanged();
        notifyObservers();
    }
    public int getNumOfRow(){
        return numOfRow;
    }
}


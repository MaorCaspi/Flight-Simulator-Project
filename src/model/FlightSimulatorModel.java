package model;

import anomalyDetectors.AnomalyDetector;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import other_classes.FGPlayer;
import other_classes.Properties;
import other_classes.TimeSeries;
import java.util.Observable;
import java.util.concurrent.Callable;

public class FlightSimulatorModel extends Observable implements Model{
    public IntegerProperty numOfRow;
    private double playSpeed;
    private FGPlayer fgPlayer;
    private boolean firstTimePlay;
    private TimeSeries ts;
    private Thread thread;
    private Properties properties;
    private AnomalyDetector ad;

    public FlightSimulatorModel() {
        firstTimePlay=true;
        numOfRow= new SimpleIntegerProperty(0);
        properties=new Properties();
    }

    @Override
    public boolean setAnomalyDetector(AnomalyDetector ad, StringProperty selectedFeature) {
        if(selectedFeature.getValue()==null)
            return false;
        this.ad=ad;
        this.ad.selectedFeature.bind(selectedFeature);
        this.ad.numOfRow.bindBidirectional(numOfRow);
        return true;/////
    }

    @Override
    public Callable<AnchorPane> getPainter() {
        //////////
        if(ad!=null){
            return ()->ad.paint();
        }
        return null;
    }

    @Override
    public void setPlaySpeed(double val) {
        playSpeed=val;
        if(!firstTimePlay) {
            fgPlayer.setPlaySpeed(val);
        }
    }
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setTimeSeries(TimeSeries ts) {
        this.ts=ts;
    }

    @Override
    public void setProgression(int rowNumber) {
        if (!firstTimePlay && !(rowNumber-5<this.numOfRow.getValue() && this.numOfRow.getValue()<rowNumber+5)) {
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
                    fgPlayer.rewind(numOfRow.getValue());
                }
            };
            thread.setDaemon(true);//when the main program ends-this thread will terminate too
            thread.start();
        }
    }

    @Override
    public void setNumOfRow(int numOfRow){
        this.numOfRow.setValue(numOfRow);
        setChanged();
        notifyObservers(numOfRow);
    }
}


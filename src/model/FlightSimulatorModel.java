package model;

import other_classes.FGPlayer;
import other_classes.TimeSeries;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;


public class FlightSimulatorModel extends Observable implements Model{
    double playSpeed;
    FGPlayer fgPlayer;
    boolean firstTimePlay;
    TimeSeries ts;
    Thread thread;
    int numOfRow;


    public FlightSimulatorModel() {

        firstTimePlay=true;
        numOfRow=0;
    }
    public void setPlaySpeed(double val) {
        playSpeed=val;
        if(!firstTimePlay) {
            fgPlayer.setPlaySpeed(val);
        }
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
        if (!firstTimePlay) {
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
        else {
            fgPlayer.wakeUP();
        }

    }

    @Override
    public void pause() {
        if(!fgPlayer.pause)
        {
            fgPlayer.pause=true;
        }
    }

    @Override
    public void forward() {
    }

    @Override
    public void rewind() {

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


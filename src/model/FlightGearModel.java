package model;

import other_classes.TimeSeries;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;


class PlayCSV extends Thread {
    Socket fg;
    PrintWriter out;
    boolean pause;
    HashMap<String, String> properties;
    String propertiesFileName;

    public PlayCSV(String propertiesFileName)
    {
        this.propertiesFileName=propertiesFileName;
        properties=new HashMap<>();
        try {
            BufferedReader in=new BufferedReader((new FileReader(propertiesFileName)));
            String line;
            while((line=in.readLine())!=null){
                String sp[]=line.split(",");
                properties.put(sp[0],sp[1]);
            }
            in.close();
        }
        catch (Exception e) {
            System.out.println("Error with properties text file");
        }
    }

    @Override
    public synchronized void run() {
        FlightGearModel f=new FlightGearModel("properties.txt");
        pause=false;
        try {
            fg = new Socket(properties.get("ip"), Integer.parseInt(properties.get("port")));
            out = new PrintWriter(fg.getOutputStream());
        }
        catch (Exception e) {
            System.out.println("FlightGear Connection Error");
        }
        try {
            for (int i=0;i<f.ts.getRowSize();i++) {

                while (pause) {
                    System.out.println("im in pause");
                    wait();
                }
                try {
                    out.println(f.ts.getRowByRowNumber(i));
                    out.flush();
                }
                catch (Exception e) { }
                System.out.println(f.ts.getRowByRowNumber(i));//!!!!
                Thread.sleep(100);
            }
            out.close();
            fg.close();
        }
        catch (Exception e) {
            System.out.println("Time Series Error");
        }
    }
    public synchronized void wakeUP()
    {
        pause=false;
        notify();
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FlightGearModel extends Observable {
    boolean firstTimePlay=true;
    TimeSeries ts;
    PlayCSV playCSV;


    public FlightGearModel(String propertiesFileName)
    {
        playCSV=new PlayCSV(propertiesFileName);
    }
    public void play()
    {
        if(firstTimePlay) {
            ts = new TimeSeries("reg_flight.csv");
            firstTimePlay=false;
            Thread t1=new Thread(playCSV);
            t1.setDaemon(true);//Threads as deamons-when the main program ends-this thread will terminate too
            t1.start();
            System.out.println("im the dad");
        }
        else {
            playCSV.wakeUP();
        }
    }
    public void pause()
    {
        if(!playCSV.pause)
        {
            playCSV.pause=true;
        }
    }
    public void forward()
    {
        System.out.println("Maor 123");
    }
    public void rewind()
    {
        System.out.println("Maor 123");
    }
}


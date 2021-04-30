package model;

import other_classes.TimeSeries;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;


public class FlightGearModel extends Observable implements Runnable{
    static boolean firstTimePlay;
    static boolean pause;
    FlightGearModel flightGearModel;
    static Socket fg;
    static PrintWriter out;
    static TimeSeries ts;
    static int i=0;
    HashMap<String, String> properties;
    String propertiesFileName;


    public FlightGearModel(String propertiesFileName)
    {
        firstTimePlay=true;
        pause=false;
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
            e.printStackTrace();
        }

    }
    @Override
    public synchronized void run() {//playCSV
        while (true) {
            try {
                for (;i<ts.getRowSize();i++) {

                    while (FlightGearModel.pause) {
                        System.out.println("im in pause");
                        wait();
                    }

                    out.println(ts.getRowByRowNumber(i));
                    out.flush();
                    System.out.println(ts.getRowByRowNumber(i));//!!!!
                    Thread.sleep(100);
                }
                out.close();
                fg.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }

    public void setTimeSeries()
    {
        System.out.println("setTimeSeries");
    }
    public synchronized void play()
    {
        if(firstTimePlay) {
            try {
                fg = new Socket(properties.get("ip"), Integer.parseInt(properties.get("port")));
                out = new PrintWriter(fg.getOutputStream());
                ts = new TimeSeries("reg_flight.csv");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            firstTimePlay=false;
            flightGearModel=new FlightGearModel(propertiesFileName);
            Thread t1=new Thread(flightGearModel);
            t1.setDaemon(true);//Threads as deamons means that when the main program ends all deamon threads will terminate too
            t1.start();
        }
        else {
            pause=false;
            notify();
        }
    }
    public static void pause()
    {
        if(!pause)
        {
            pause=true;
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


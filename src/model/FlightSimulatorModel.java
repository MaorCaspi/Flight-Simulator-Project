package model;

import other_classes.TimeSeries;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;


public class FlightSimulatorModel extends Observable implements Model{
    Socket fg;
    PrintWriter out;
    public boolean pause;
    HashMap<String, String> properties;
    double playSpeed;


    public FlightSimulatorModel(String propertiesFileName)
    {
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

    public synchronized void playCsv(TimeSeries ts) {
        pause=false;
        try {
            fg = new Socket(properties.get("ip"), Integer.parseInt(properties.get("port")));
            out = new PrintWriter(fg.getOutputStream());
        }
        catch (Exception e) {
            System.out.println("FlightGear Connection Error");
        }
        try {
            for (int i = 0; i< ts.getRowSize(); i++) {

                while (pause) {
                    System.out.println("im in pause");
                    wait();
                }
                try {
                    out.println(ts.getRowByRowNumber(i));
                    out.flush();
                }
                catch (Exception e) { }
                System.out.println(ts.getRowByRowNumber(i));//!!!!
                Thread.sleep((long) (100/playSpeed));
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
    public void setPlaySpeed(double val)
    {
        playSpeed=val;
    }

    @Override
    public boolean getPainter(Runnable r) {
        return false;////////////////////!!!!
    }
}


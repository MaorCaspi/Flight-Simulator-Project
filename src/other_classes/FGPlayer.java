package other_classes;

import model.FlightSimulatorModel;

import java.io.PrintWriter;
import java.net.Socket;

public class FGPlayer {
    Socket fg;
    PrintWriter out;
    public boolean pause;
    double playSpeed;
    TimeSeries ts;
    FlightSimulatorModel flightSimulatorModel;

    public FGPlayer(TimeSeries ts,double speed,FlightSimulatorModel flightSimulatorModel)
    {
        try {
            fg = new Socket("localhost", 5400);
            out = new PrintWriter(fg.getOutputStream());
        }
        catch (Exception e) {
            System.out.println("FlightGear Connection Error");
        }
        this.ts=ts;
        playSpeed=speed;
        this.flightSimulatorModel=flightSimulatorModel;
    }

    public synchronized void play(int start) {
        try {
            for (int i = start; i< ts.getRowSize(); i++) {
                while (pause) {
                    System.out.println("im in pause");
                    wait();
                }
                try {
                    out.println(ts.getRowByRowNumber(i));
                    out.flush();
                }
                catch (Exception e) { }
                flightSimulatorModel.setNumOfRow(i);
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
    public synchronized void wakeUP() {
        pause=false;
        notify();
    }
    public void setPlaySpeed(double speed) {
       playSpeed=speed;
    }

}

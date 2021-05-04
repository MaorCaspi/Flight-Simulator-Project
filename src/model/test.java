package model;
import other_classes.TimeSeries;

import java.io.*;
import java.net.Socket;
/*
public class test {
    public void connect() {
        try {
            Socket fg = new Socket("localhost", 5400);
            PrintWriter out = new PrintWriter(fg.getOutputStream());
            TimeSeries ts = new TimeSeries("reg_flight.csv");
            System.out.println("mango");
            for (int i = 0; i < ts.getRowSize(); i++) {
                out.println(ts.getRowByRowNumber(i));
                out.flush();
                Thread.sleep(100);
            }

            out.close();
            fg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        test j=new test();
        j.connect();
    }
}

 */
public class test {
    public void connect() {
        try {
            Socket fg = new Socket("localhost", 5400);
            PrintWriter out = new PrintWriter(fg.getOutputStream());
            TimeSeries ts = new TimeSeries("reg_flight.csv");

            for (int i = 0; i < 500; i++) {
                out.println(ts.getRowByRowNumber(i));
                out.flush();
                Thread.sleep(100);
            }
            System.out.println("going to sleep");
            Thread.sleep(20000);
            System.out.println("im  waked up");
            for (int i = 500; i < ts.getRowSize(); i++) {
                out.println(ts.getRowByRowNumber(i));
                out.flush();
                Thread.sleep(100);
            }

            out.close();
            fg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        test j=new test();
        j.connect();
    }
}


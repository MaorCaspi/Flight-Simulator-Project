package view_model;

import javafx.application.Platform;
import javafx.beans.property.*;
import model.FlightSimulatorModel;
import other_classes.Properties;
import other_classes.TimeSeries;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer  {
    private FlightSimulatorModel m;
    private TimeSeries ts;
    private Properties properties;
    private int csvLength;
    private DoubleProperty playSpeed,progression;
    private StringProperty anomalyFlightPath,propertiesPath,currentTime;

    public ViewModel(FlightSimulatorModel m){
        this.m=m;
        playSpeed = new SimpleDoubleProperty();
        progression = new SimpleDoubleProperty();
        anomalyFlightPath = new SimpleStringProperty();
        propertiesPath=new SimpleStringProperty();
        currentTime = new SimpleStringProperty("0:0");
        properties=new Properties();
        properties.deserializeFromXML("settings.xml");
        m.setProperties(properties);
        playSpeed.addListener((observable, oldValue, newValue)->{m.setPlaySpeed((double)newValue);});
        anomalyFlightPath.addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                try {
                    ts = new TimeSeries(newValue);
                    if (ts.getNumOfColumns() != 39) {
                        throw new Exception();
                    }
                    m.setTimeSeries(ts);
                    csvLength = ts.getRowSize();
                } catch (Exception e) {
                    setChanged();
                    notifyObservers("CSV file error");
                }
            }
        });

        propertiesPath.addListener((observable, oldValue, newValue) -> {
            if(!properties.deserializeFromXML(newValue)){
                propertiesPath.setValue(null);
                setChanged();
                notifyObservers("properties file error");
            }
            else{
                m.setProperties(properties);
            }
        });
        progression.addListener((observable, oldValue, newValue)->{
            if(ts!=null)
                m.setProgression((int)(ts.getRowSize() * newValue.doubleValue()));});
    }
    private void setTime(int rowNumber)
    {
        int totalMilliseconds=(int)(rowNumber*properties.getRate());
        int seconds=(int)(totalMilliseconds / 1000) % 60;
        int minutes=(int) ((totalMilliseconds / (60000)) % 60);
        Platform.runLater(() -> currentTime.set((minutes+":"+seconds)));
    }
    public DoubleProperty getProgression() {
        return progression;
    }
    public DoubleProperty getPlaySpeed() {
        return playSpeed;
    }
    public StringProperty getAnomalyFlightPath() {
        return anomalyFlightPath;
    }
    public StringProperty getPropertiesPath() {
        return propertiesPath;
    }
    public StringProperty getCurrentTime() {
        return currentTime;
    }

    public void play(){
        m.play((int)(progression.getValue()* ts.getRowSize()));
    }
    public void pause() {
        m.pause();
    }
    public void forward(){ m.forward(); }
    public void rewind(){ m.rewind();}

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){
            int numOfRow=m.getNumOfRow();
            progression.setValue((double)numOfRow/csvLength);
            setTime(numOfRow);
        }
    }

}

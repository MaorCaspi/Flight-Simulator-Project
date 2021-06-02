package view_model;

import anomalyDetectors.AnomalyDetectorLinearRegression;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import model.FlightSimulatorModel;
import other_classes.Point;
import other_classes.Properties;
import other_classes.TimeSeries;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewModel extends Observable implements Observer{
    private FlightSimulatorModel m;
    private TimeSeries ts;
    private Properties properties;
    private int csvLength,selectedFeatureId,localSelectedFeatureId,theMostCorrelativeAttributeId,localNumOfRow;
    private ExecutorService executor;
    private DoubleProperty playSpeed,progression,throttle,rudder,aileron,elevators,heading,speed,altitude,roll,pitch,yaw;
    private StringProperty anomalyFlightPath,propertiesPath,currentTime,selectedFeature;
    private ListProperty<Point> selectedAttributePoints,theMostCorrelativeAttributePoints;
    private Map<String, String[]> correlatedFeaturesMap;

    public ViewModel(FlightSimulatorModel m){
        this.m=m;
        executor = Executors.newSingleThreadExecutor();
        playSpeed = new SimpleDoubleProperty();
        progression = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
        heading = new SimpleDoubleProperty();
        speed = new SimpleDoubleProperty();
        altitude = new SimpleDoubleProperty();
        roll = new SimpleDoubleProperty();
        pitch = new SimpleDoubleProperty();
        yaw = new SimpleDoubleProperty();
        anomalyFlightPath = new SimpleStringProperty();
        propertiesPath=new SimpleStringProperty();
        currentTime = new SimpleStringProperty("0:0");
        selectedFeature=new SimpleStringProperty();
        selectedAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        theMostCorrelativeAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        properties=new Properties();
        properties.deserializeFromXML("settings.xml");//the default path for the properties file
        m.setProperties(properties);

        playSpeed.addListener((observable, oldValue, newValue)-> m.setPlaySpeed((double)newValue));
        anomalyFlightPath.addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                AnomalyDetectorLinearRegression anomalyDetectorLinearRegression=new AnomalyDetectorLinearRegression();
                try {
                    ts = new TimeSeries(newValue);
                    if (ts.getNumOfColumns() != 42) {
                        throw new Exception();
                    }
                    anomalyDetectorLinearRegression.learnNormal(new TimeSeries(properties.getNormalFlightCsvPath()));
                }
                catch (Exception e) {
                    setChanged();
                    notifyObservers("CSV file error");
                    return;
                }
                m.setTimeSeries(ts);
                csvLength = ts.getRowSize();
                correlatedFeaturesMap=anomalyDetectorLinearRegression.getTheMostCorrelatedFeaturesMap();
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
        selectedFeature.addListener((observable, oldValue, newValue) -> {
            if(ts!=null){
                selectedFeatureId=ts.getIndexByFeature(newValue);
                if(correlatedFeaturesMap.containsKey(newValue)){
                    theMostCorrelativeAttributeId=ts.getIndexByFeature(newValue);
                }
                else{
                    theMostCorrelativeAttributeId=-1;// -1  =  there is no correlative feature
                }
            }
        });
    }
    private void setTime(int rowNumber)
    {
        int totalMilliseconds=(int)(rowNumber*properties.getRate());
        int seconds=(totalMilliseconds / 1000) % 60;
        int minutes=((totalMilliseconds / (60000)) % 60);
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
    public StringProperty getSelectedFeature() {
        return selectedFeature;
    }
    public DoubleProperty getThrottle() { return throttle; }
    public DoubleProperty getRudder() { return rudder; }
    public DoubleProperty getAileron() { return aileron; }
    public DoubleProperty getElevators() { return elevators; }
    public DoubleProperty getHeading() { return heading; }
    public DoubleProperty getSpeed() { return speed; }
    public DoubleProperty getAltitude() { return altitude; }
    public DoubleProperty getRoll() { return roll; }
    public DoubleProperty getPitch() { return pitch; }
    public DoubleProperty getYaw() { return yaw; }
    public ListProperty<Point> getSelectedAttributePoints() { return selectedAttributePoints; }
    public ListProperty<Point> getTheMostCorrelativeAttributePoints() { return theMostCorrelativeAttributePoints; }
    public void shutdownExecutor() { executor.shutdown(); }

    public void play(){
        m.play((int)(progression.getValue()* ts.getRowSize()));
    }
    public void pause() {
        m.pause();
    }
    public void forward(){ m.forward(); }
    public void rewind(){ m.rewind(); }

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){
            int numOfRow=m.getNumOfRow();
            executor.execute(() -> progression.setValue((double)numOfRow/csvLength));
            executor.execute(() -> setTime(numOfRow));
            executor.execute(() -> throttle.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("throttle"),numOfRow)));
            executor.execute(() -> rudder.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("rudder"),numOfRow)));
            executor.execute(() -> aileron.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("aileron"),numOfRow)*40));
            executor.execute(() -> elevators.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("elevators"),numOfRow)*40));
            executor.execute(() -> heading.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("heading"),numOfRow)));
            executor.execute(() -> speed.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("speed"),numOfRow)));
            executor.execute(() -> altitude.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("altitude"),numOfRow)));
            executor.execute(() -> roll.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("roll"),numOfRow)));
            executor.execute(() -> pitch.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("pitch"),numOfRow)));
            executor.execute(() -> yaw.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("yaw"),numOfRow)));
            Platform.runLater(()->{
            if(localSelectedFeatureId==selectedFeatureId && localNumOfRow+1==numOfRow) {
                selectedAttributePoints.add(new Point(numOfRow,ts.getAttributeData(localSelectedFeatureId).get(numOfRow)));
                if(theMostCorrelativeAttributeId!=-1){
                    theMostCorrelativeAttributePoints.add(new Point(numOfRow,ts.getAttributeData(theMostCorrelativeAttributeId).get(numOfRow)));
                }
            }
            else {
                selectedAttributePoints.setValue(ts.getListOfPointsUntilSpecificRow(selectedFeatureId,numOfRow));
                localSelectedFeatureId=selectedFeatureId;
                if(theMostCorrelativeAttributeId!=-1){
                    theMostCorrelativeAttributePoints.setValue(ts.getListOfPointsUntilSpecificRow(theMostCorrelativeAttributeId,numOfRow));
                }
            }
                localNumOfRow=numOfRow;
            if(theMostCorrelativeAttributeId==-1){//if there is no correlative feature
                theMostCorrelativeAttributePoints.setValue(new SimpleListProperty<>(FXCollections.observableArrayList()));
                }
            });
        }
    }
}

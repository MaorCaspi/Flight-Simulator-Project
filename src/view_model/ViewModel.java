package view_model;

import anomalyDetectors.AnomalyDetectorLinearRegression;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.AnchorPane;
import model.FlightSimulatorModel;
import other_classes.Point;
import other_classes.Properties;
import other_classes.TimeSeries;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewModel extends Observable implements Observer{
    private FlightSimulatorModel m;
    private TimeSeries ts,regTs;
    private Properties properties;
    private int csvLength,selectedFeatureId,localSelectedFeatureId,theMostCorrelativeAttributeId,localNumOfRow;
    private ExecutorService executor;
    private Map<String, String[]> correlatedFeaturesMap;
    public DoubleProperty playSpeed,progression,throttle,rudder,aileron,elevators,heading,speed,altitude,roll,pitch,yaw;
    public StringProperty anomalyFlightPath,propertiesPath,currentTime,selectedFeature,theMostCorrelativeAttribute;
    public ListProperty<Point> selectedAttributePoints,theMostCorrelativeAttributePoints;
    public ListProperty<String> features;

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
        theMostCorrelativeAttribute=new SimpleStringProperty();
        selectedAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        theMostCorrelativeAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        features=new SimpleListProperty<>(FXCollections.observableArrayList());
        properties=new Properties();
        properties.deserializeFromXML("settings.xml");//the default path for the properties file
        m.setProperties(properties);

        playSpeed.addListener((observable, oldValue, newValue)-> m.setPlaySpeed((double)newValue));
        anomalyFlightPath.addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                AnomalyDetectorLinearRegression anomalyDetectorLinearRegression=new AnomalyDetectorLinearRegression();
                try {
                    ts = new TimeSeries(newValue);
                    regTs =new TimeSeries(properties.getNormalFlightCsvPath());
                    if ((ts.getNumOfColumns() != 42) || (regTs.getNumOfColumns() != 42)) {
                        throw new Exception();
                    }
                    anomalyDetectorLinearRegression.learnNormal(regTs);
                }
                catch (Exception e) {
                    setChanged();
                    notifyObservers("CSV file error");
                    return;
                }
                m.setTimeSeries(ts);
                csvLength = ts.getRowSize();
                correlatedFeaturesMap=anomalyDetectorLinearRegression.getTheMostCorrelatedFeaturesMap();
                features.set(ts.getAttributes());
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
                    theMostCorrelativeAttribute.setValue(correlatedFeaturesMap.get(newValue)[0]);
                    theMostCorrelativeAttributeId=ts.getIndexByFeature(theMostCorrelativeAttribute.getValue());
                }
                else{
                    theMostCorrelativeAttributeId=-1;// -1  =  there is no correlative feature
                    theMostCorrelativeAttribute.setValue("");
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
    public void shutdownExecutor() { executor.shutdown(); }

    public void play(){
        m.play((int)(progression.getValue()* ts.getRowSize()));
    }
    public void pause() {
        m.pause();
    }
    public void forward(){ m.forward(); }
    public void rewind(){ m.rewind(); }

    public Callable<AnchorPane> getPainter(){

        return m.getPainter();
    }

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

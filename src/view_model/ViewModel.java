package view_model;

import anomalyDetectors.*;
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
    private int csvLength,localNumOfRow;
    private String localSelectedFeature;
    private ExecutorService executor;
    private Map<String, CorrelatedFeatures> correlatedFeaturesMap;
    public DoubleProperty playSpeed,progression,throttle,rudder,aileron,elevators,heading,speed,altitude,roll,pitch,yaw;
    public StringProperty anomalyFlightPath,propertiesPath,currentTime,selectedFeature,theMostCorrelativeAttribute;
    public ListProperty<Point> selectedAttributePoints,theMostCorrelativeAttributePoints;
    public ListProperty<String> features;
    public IntegerProperty numOfRow;

    public ViewModel(FlightSimulatorModel m){
        this.m=m;
        executor = Executors.newSingleThreadExecutor();
        numOfRow= new SimpleIntegerProperty();
        numOfRow.bind(m.numOfRow);
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
        theMostCorrelativeAttribute=new SimpleStringProperty("");
        selectedAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        theMostCorrelativeAttributePoints=new SimpleListProperty<>(FXCollections.observableArrayList());
        features=new SimpleListProperty<>(FXCollections.observableArrayList());
        properties=new Properties();
        properties.deserializeFromXML("settings.xml");//the default path for the properties file
        m.setProperties(properties);
        localSelectedFeature="";

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
                correlatedFeaturesMap=anomalyDetectorLinearRegression.getTheMostCorrelatedFeaturesMap(true);
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
                if(correlatedFeaturesMap.containsKey(newValue)){//find who is the MostCorrelativeAttribute from the map
                    String temp=correlatedFeaturesMap.get(newValue).feature1;
                    if(!temp.equals(selectedFeature.getValue())) {
                        theMostCorrelativeAttribute.setValue(temp);
                    }
                    else{
                        theMostCorrelativeAttribute.setValue(correlatedFeaturesMap.get(newValue).feature2);
                    }
                }
                else{
                    theMostCorrelativeAttribute.setValue(""); //there is no correlative feature
                }
            }
        });
        numOfRow.addListener((observable, oldValue, newValue) -> {
            executor.execute(() -> progression.setValue((double)numOfRow.getValue()/csvLength));
            executor.execute(() -> setTime(numOfRow.getValue()));
            executor.execute(() -> throttle.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("throttle"),numOfRow.getValue())));
            executor.execute(() -> rudder.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("rudder"),numOfRow.getValue())));
            executor.execute(() -> aileron.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("aileron"),numOfRow.getValue())*40));
            executor.execute(() -> elevators.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("elevators"),numOfRow.getValue())*40));
            executor.execute(() -> heading.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("heading"),numOfRow.getValue())));
            executor.execute(() -> speed.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("speed"),numOfRow.getValue())));
            executor.execute(() -> altitude.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("altitude"),numOfRow.getValue())));
            executor.execute(() -> roll.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("roll"),numOfRow.getValue())));
            executor.execute(() -> pitch.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("pitch"),numOfRow.getValue())));
            executor.execute(() -> yaw.setValue(ts.getDataFromSpecificRowAndColumn(properties.propertyName("yaw"),numOfRow.getValue())));

            boolean theFeatureNameNotChanged=localSelectedFeature.intern()==selectedFeature.getValue().intern();

            if(theFeatureNameNotChanged && localNumOfRow+1==numOfRow.getValue()) {//If the row number increases by only one
                Platform.runLater(()->selectedAttributePoints.add(new Point(numOfRow.getValue(),ts.getAttributeData(localSelectedFeature).get(numOfRow.getValue()))));
                if(theMostCorrelativeAttribute.getValue().intern()!=("").intern()){//if there is correlative feature
                    Platform.runLater(()->theMostCorrelativeAttributePoints.add(new Point(numOfRow.getValue(),ts.getAttributeData(theMostCorrelativeAttribute.getValue()).get(numOfRow.getValue()))));
                }
            }
            else if(theFeatureNameNotChanged && localNumOfRow-1==numOfRow.getValue()) {//this is for the rewind option
                Platform.runLater(()->{
                int length=selectedAttributePoints.size();
                if(length>0) {
                    selectedAttributePoints.remove(length - 1);
                    if(theMostCorrelativeAttribute.getValue().intern()!=("").intern()) {//if there is correlative feature
                        theMostCorrelativeAttributePoints.remove(length - 1);
                      }
                    }
                });
            }
            else {
                Platform.runLater(()-> selectedAttributePoints.setValue(ts.getListOfPointsUntilSpecificRow(selectedFeature.getValue(),numOfRow.getValue())));
                localSelectedFeature=selectedFeature.getValue();
                if(theMostCorrelativeAttribute.getValue().intern()!=("").intern()){
                    Platform.runLater(()->theMostCorrelativeAttributePoints.setValue(ts.getListOfPointsUntilSpecificRow(theMostCorrelativeAttribute.getValue(),numOfRow.getValue())));
                    }
                }
                localNumOfRow=numOfRow.getValue();
                if(theMostCorrelativeAttribute.getValue().intern()==("").intern()){//if there is no correlative feature
                    Platform.runLater(()->theMostCorrelativeAttributePoints.setValue(new SimpleListProperty(FXCollections.observableArrayList())));
                }
            });
    }
    private void setTime(int rowNumber)
    {
        int totalMilliseconds=(int)(rowNumber*properties.getRate());
        int seconds=(totalMilliseconds / 1000) % 60;
        int minutes=(totalMilliseconds / 60000) % 60;
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
        setAnomalyDetector("maorrrrrr");////////////////
        return m.getPainter();
    }

    public void setAnomalyDetector(String adPath) {
        //AnomalyDetector ad=new AnomalyDetectorZScoreAlgorithm();
        //AnomalyDetector ad=new AnomalyDetectorLinearRegression();
        AnomalyDetector ad=new AnomalyDetectorHybridAlgorithm();
        if(!m.setAnomalyDetector(ad,selectedFeature,regTs)){

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){
        }
    }
}

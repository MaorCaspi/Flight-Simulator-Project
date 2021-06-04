package view;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.AnomalyDetectionGraph.MyAnomalyDetectionGraph;
import view.AttributesView.MyAttributes;
import view.ClocksPanel.MyClocksPanel;
import view.Joystick.MyJoystick;
import view_model.ViewModel;

public class MainWindowController implements Observer{
    private ViewModel vm;
    public DoubleProperty playSpeed;
    public StringProperty anomalyFlightPath,propertiesPath;

    @FXML private TextField playSpeedTF;
    @FXML private Slider progressBar;
    @FXML private Label currentTime;
    @FXML private MyJoystick joystick;
    @FXML private MyClocksPanel clocksPanel;
    @FXML private MyAttributes attributes;
    @FXML private MyAnomalyDetectionGraph graphs;

    public void setViewModel(ViewModel vm){
        this.vm=vm;
        playSpeed=new SimpleDoubleProperty(1.0);
        vm.playSpeed.bind(playSpeed);
        anomalyFlightPath=new SimpleStringProperty();
        vm.anomalyFlightPath.bind(anomalyFlightPath);
        propertiesPath=new SimpleStringProperty();
        vm.propertiesPath.bind(propertiesPath);
        progressBar.valueProperty().bindBidirectional(vm.progression);
        currentTime.textProperty().bind(vm.currentTime);
        joystick.rudder.bind(vm.rudder);
        joystick.throttle.bind(vm.throttle);
        joystick.aileron.bind(vm.aileron);
        joystick.elevators.bind(vm.elevators);
        clocksPanel.getHeading().bind(vm.heading);
        clocksPanel.getSpeed().bind(vm.speed);
        clocksPanel.getAltitude().bind(vm.altitude);
        clocksPanel.getRoll().bind(vm.roll);
        clocksPanel.getPitch().bind(vm.pitch);
        clocksPanel.getYaw().bind(vm.yaw);
        graphs.getSelectedAttributePoints().bind(vm.selectedAttributePoints);
        graphs.getTheMostCorrelativeAttributePoints().bind(vm.theMostCorrelativeAttributePoints);
        graphs.getTheMostCorrelativeAttribute().bind(vm.theMostCorrelativeAttribute);
        attributes.setXml("settings.xml"); //the default path for the properties file
    }
    public void showErrorMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    public String uploadFile(String title,String description,String extensions){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(description, extensions));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    @FXML
    public void pressButtonPlay(){
        if(anomalyFlightPath.getValue()==null) {
            showErrorMessage("Problem with flight recording file!\nYou must upload a CSV file.");
            return;
        }
        vm.play();
        progressBar.setDisable(false);
    }
    @FXML
    public void pressButtonPause(){
        vm.pause();
        progressBar.setDisable(true);
    }
    @FXML
    public void pressButtonForward(){
        vm.forward();
    }
    @FXML
    public void pressButtonRewind(){ vm.rewind(); }
    @FXML
    public void pressButtonStop() {
        if (!progressBar.isDisabled()) {
            progressBar.setValue(1);
        }
    }
    @FXML
    public void pressButtonLoadCSV(){
        String filePath=uploadFile("Upload flight recording file - CSV","CSV file","*.csv*");
        if (filePath != null) {
            anomalyFlightPath.setValue(filePath);
            attributes.setCsv(filePath);
            attributes.LoadList();
            vm.selectedFeature.bind(attributes.selectedFeature);
            graphs.getSelectedFeature().bind(attributes.selectedFeature);
        }
    }
    @FXML
    public void pressButtonLoadProperties(){
        String filePath=uploadFile("Upload properties file - XML","XML file","*.xml*");
        if (filePath != null) {
            propertiesPath.setValue(filePath);
            attributes.setXml(filePath);
        }
    }
    @FXML
    public void playSpeedWasChanged(){
        try {
            double newPlaySpeed = Double.parseDouble(playSpeedTF.textProperty().getValue());
            if(playSpeed.getValue().equals(newPlaySpeed)) { return;} //if yes-it doesn't really changed- so do nothing.
            if(newPlaySpeed<=0.0)
                throw new Exception();
            playSpeed.setValue(newPlaySpeed);
        }
        catch (Exception e)
        {
            showErrorMessage("Problem with the play speed!\nYou must enter a valid play speed number.");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (arg.toString()) {
            case "CSV file error":
                showErrorMessage("Problem with flight recording file!\nYou must upload a CSV file.");
                anomalyFlightPath.setValue(null);
                break;
            case "properties file error":
                showErrorMessage("Problem with properties file!\nPlease try again.");
                break;
        }
    }
}
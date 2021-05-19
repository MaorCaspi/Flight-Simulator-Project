package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.AttributesView.MyAttributes;
import view.ClocksPanel.MyClocksPanel;
import view.Joystick.MyJoystick;
import view_model.ViewModel;

public class MainWindowController implements Observer{
    private ViewModel vm;
    private DoubleProperty playSpeed;
    private StringProperty anomalyFlightPath,propertiesPath;

    @FXML private TextField playSpeedTF;
    @FXML private Slider progressBar;
    @FXML private Label currentTime;
    @FXML private MyJoystick joystick;
    @FXML private MyClocksPanel clocksPanel;
    @FXML private MyAttributes attributes;

    public void setViewModel(ViewModel vm){
        this.vm=vm;
        playSpeed=new SimpleDoubleProperty(1.0);
        vm.getPlaySpeed().bind(playSpeed);
        anomalyFlightPath=new SimpleStringProperty();
        vm.getAnomalyFlightPath().bind(anomalyFlightPath);
        propertiesPath=new SimpleStringProperty();
        vm.getPropertiesPath().bind(propertiesPath);
        progressBar.valueProperty().bindBidirectional(vm.getProgression());
        currentTime.textProperty().bind(vm.getCurrentTime());
        joystick.getRudder().bind(vm.getRudder());
        joystick.getThrottle().bind(vm.getThrottle());
        joystick.getAileron().bind(vm.getAileron());
        joystick.getElevators().bind(vm.getElevators());
        clocksPanel.getHeading().bind(vm.getHeading());
        clocksPanel.getSpeed().bind(vm.getSpeed());
        clocksPanel.getAltitude().bind(vm.getAltitude());
        clocksPanel.getRoll().bind(vm.getRoll());
        clocksPanel.getPitch().bind(vm.getPitch());
        clocksPanel.getYaw().bind(vm.getYaw());
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
        }
    }
    @FXML
    public void pressButtonLoadProperties(){
        String filePath=uploadFile("Upload properties file - XML","XML file","*.xml*");
        if (filePath != null) {
            propertiesPath.setValue(filePath);
            attributes.setXml(filePath);
            attributes.LoadList();
        }
    }
    @FXML
    public void playSpeedWasChanged(){
        try {
            double newPlaySpeed = Double.parseDouble(playSpeedTF.textProperty().getValue());
            if(playSpeed.equals(newPlaySpeed)) { return;} //if yes-it doesn't really changed- so do nothing.
            if(newPlaySpeed<=0.0)
                throw new Exception();
            playSpeed.setValue(newPlaySpeed);
        }
        catch (Exception e)
        {
            showErrorMessage("Problem with the play speed!\nYou must enter a valid play speed number.");
            return;
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

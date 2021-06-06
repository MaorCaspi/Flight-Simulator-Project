package view;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.Controllers.MyControllers;
import view.Graphs.MyGraphs;
import view.AttributesView.MyAttributes;
import view.ClocksPanel.MyClocksPanel;
import view.Joystick.MyJoystick;
import view_model.ViewModel;

public class MainWindowController implements Observer{
    private ViewModel vm;
    public StringProperty anomalyFlightPath,propertiesPath;

    @FXML private MyJoystick joystick;
    @FXML private MyClocksPanel clocksPanel;
    @FXML private MyAttributes attributes;
    @FXML private MyGraphs graphs;
    @FXML private MyControllers controllers;

    public void setViewModel(ViewModel vm){
        this.vm=vm;
        anomalyFlightPath=new SimpleStringProperty();
        vm.anomalyFlightPath.bind(anomalyFlightPath);
        propertiesPath=new SimpleStringProperty();
        vm.propertiesPath.bind(propertiesPath);
        controllers.controller.progressBar.valueProperty().bindBidirectional(vm.progression);
        controllers.controller.currentTime.textProperty().bind(vm.currentTime);
        vm.playSpeed.bind(controllers.controller.playSpeed);
        controllers.controller.onPlay=()->{
            if(anomalyFlightPath.getValue()==null) {
                showErrorMessage("Problem with flight recording file!\nYou must upload a CSV file.");
                return;
            }
            vm.play();
        };
        controllers.controller.onPause=()->vm.pause();
        controllers.controller.onForward=()->vm.forward();
        controllers.controller.onRewind=()->vm.rewind();
        joystick.rudder.bind(vm.rudder);
        joystick.throttle.bind(vm.throttle);
        joystick.aileron.bind(vm.aileron);
        joystick.elevators.bind(vm.elevators);
        clocksPanel.heading.bind(vm.heading);
        clocksPanel.speed.bind(vm.speed);
        clocksPanel.altitude.bind(vm.altitude);
        clocksPanel.roll.bind(vm.roll);
        clocksPanel.pitch.bind(vm.pitch);
        clocksPanel.yaw.bind(vm.yaw);
        graphs.getSelectedAttributePoints().bind(vm.selectedAttributePoints);
        graphs.getTheMostCorrelativeAttributePoints().bind(vm.theMostCorrelativeAttributePoints);
        graphs.getTheMostCorrelativeAttribute().bind(vm.theMostCorrelativeAttribute);
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
    public void pressButtonLoadCSV(){
        String filePath=uploadFile("Upload flight recording file - CSV","CSV file","*.csv*");
        if (filePath != null) {
            anomalyFlightPath.setValue(filePath);
            attributes.LoadList();
            attributes.features.bind(vm.features);
            vm.selectedFeature.bind(attributes.selectedFeature);
            graphs.getSelectedFeature().bind(attributes.selectedFeature);
        }
    }
    @FXML
    public void pressButtonLoadProperties(){
        String filePath=uploadFile("Upload properties file - XML","XML file","*.xml*");
        if (filePath != null) {
            propertiesPath.setValue(filePath);
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
package view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import other_classes.FxmlLoader;
import view.AnomalyDetectionGraph.AnomalyDetectionGraphController;
import view.AttributesView.AttributesViewController;
import view.ClocksPanel.ClocksPanelController;
import view.Joystick.JoystickWindowController;
import view_model.ViewModel;


public class MainWindowController implements Initializable, Observer{
    ViewModel vm;
    DoubleProperty playSpeed;
    StringProperty anomalyFlightPath;
    StringProperty propertiesPath;

    JoystickWindowController joystick;
    ClocksPanelController clocksPanel;
    AttributesViewController attributesView;
    AnomalyDetectionGraphController anomalyDetectionGraph;


    @FXML
    private BorderPane joystickPane,clocksPanelPane,attributesViewPane,anomalyDetectionGraphPane;
    @FXML
    private Button play,pause,forward,rewind,stop;
    @FXML
    TextField playSpeedTF;
    @FXML
    Slider progressBar;
    @FXML
    Label currentTime;

    public MainWindowController()
    {
        joystick=new JoystickWindowController();
        clocksPanel=new ClocksPanelController();
        attributesView=new AttributesViewController();
        anomalyDetectionGraph=new AnomalyDetectionGraphController();
    }
    public void setViewModel(ViewModel vm){
        this.vm=vm;
        playSpeed=new SimpleDoubleProperty(1.0);
        vm.playSpeed.bind(playSpeed);
        anomalyFlightPath=new SimpleStringProperty();
        vm.anomalyFlightPath.bind(anomalyFlightPath);
        propertiesPath=new SimpleStringProperty();
        vm.propertiesPath.bind(propertiesPath);///////////////////
        progressBar.valueProperty().bindBidirectional(vm.progression);
        currentTime.textProperty().bind(vm.currentTime);
    }
    @FXML
    public void pressButtonPlay(ActionEvent event){
        if(anomalyFlightPath.getValue()==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Problem with flight recording file!\nYou must upload a CSV file.");
            alert.showAndWait();
            return;
        }
        vm.play();
        progressBar.setDisable(false);
    }
    @FXML
    public void pressButtonPause(ActionEvent event){
        vm.pause();
        progressBar.setDisable(true);
    }
    @FXML
    public void pressButtonForward(ActionEvent event){
        vm.forward();
    }
    @FXML
    public void pressButtonRewind(ActionEvent event){ vm.rewind(); }
    @FXML
    public void pressButtonStop(ActionEvent event) {
        if (!progressBar.isDisabled()) {
            progressBar.setValue(1);
        }
    }
    @FXML
    public void pressButtonLoadCSV(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload flight recording file - CSV");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV file", "*.csv*"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            anomalyFlightPath.setValue(file.getPath());
        }
    }
    @FXML
    public void pressButtonLoadProperties(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload properties file - XML");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML file", "*.xml*"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            propertiesPath.setValue(file.getPath());
        }
    }
    @FXML
    public void playSpeedWasChanged(ActionEvent event){
        try {
            double newPlaySpeed = Double.parseDouble(playSpeedTF.textProperty().getValue());
            if(playSpeed.equals(newPlaySpeed)) { return;} //if yes-it doesn't really changed- so do nothing.
            if(newPlaySpeed<=0.0)
                throw new Exception();
            playSpeed.setValue(newPlaySpeed);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Problem with the play speed!\nYou must enter a valid play speed number.");
            alert.showAndWait();
            return;
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pane joystickView = new FxmlLoader().getPage("Joystick/JoystickWindow.fxml");
        joystickPane.setCenter(joystickView);

        Pane clocksPanelView = new FxmlLoader().getPage("ClocksPanel/ClocksPanel.fxml");
        clocksPanelPane.setCenter(clocksPanelView);

        Pane attributesViewView = new FxmlLoader().getPage("AttributesView/AttributesView.fxml");
        attributesViewPane.setCenter(attributesViewView);

        Pane anomalyDetectionGraphView = new FxmlLoader().getPage("AnomalyDetectionGraph/AnomalyDetectionGraph.fxml");
        anomalyDetectionGraphPane.setCenter(anomalyDetectionGraphView);

        play.setBackground(Background.EMPTY);
        pause.setBackground(Background.EMPTY);
        forward.setBackground(Background.EMPTY);
        rewind.setBackground(Background.EMPTY);
        stop.setBackground(Background.EMPTY);

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

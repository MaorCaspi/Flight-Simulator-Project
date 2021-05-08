package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import view_model.ViewModel;


public class MainWindowController implements Initializable, Observer{
    ViewModel vm;
    DoubleProperty playSpeed;
    StringProperty anomalyFlightPath;


    @FXML
    private BorderPane joystickPane,clocksPanelPane,attributesViewPane,anomalyDetectionGraphPane;
    @FXML
    private Button play,pause,forward,rewind;
    @FXML
    TextField playSpeedTF;
    @FXML
    Slider progressBar;

    public MainWindowController()
    {

    }

    public void setViewModel(ViewModel vm){
        this.vm=vm;
        playSpeed=new SimpleDoubleProperty(1.0);
        vm.playSpeed.bind(playSpeed);
        anomalyFlightPath=new SimpleStringProperty();
        vm.anomalyFlightPath.bind(anomalyFlightPath);
        progressBar.valueProperty().bindBidirectional(vm.progression);
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
    }
    @FXML
    public void pressButtonPause(ActionEvent event){
        vm.pause();
    }
    @FXML
    public void pressButtonForward(ActionEvent event){
        vm.forward();
    }
    @FXML
    public void pressButtonRewind(ActionEvent event){
        vm.rewind();
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
        Pane joystickView = new FxmlLoader().getPage("JoystickWindow.fxml");
        joystickPane.setCenter(joystickView);

        Pane clocksPanelView = new FxmlLoader().getPage("ClocksPanel.fxml");
        clocksPanelPane.setCenter(clocksPanelView);

        Pane attributesViewView = new FxmlLoader().getPage("AttributesView.fxml");
        attributesViewPane.setCenter(attributesViewView);

        Pane anomalyDetectionGraphView = new FxmlLoader().getPage("AnomalyDetectionGraph.fxml");
        anomalyDetectionGraphPane.setCenter(anomalyDetectionGraphView);

        ImageView PlayView = new ImageView(new Image("media/btn_play.png"));
        PlayView.setFitHeight(25);
        PlayView.setPreserveRatio(true);
        play.setGraphic(PlayView);
        play.setBackground(Background.EMPTY);

        ImageView PauseView = new ImageView(new Image("media/btn_pause.png"));
        PauseView.setFitHeight(25);
        PauseView.setPreserveRatio(true);
        pause.setGraphic(PauseView);
        pause.setBackground(Background.EMPTY);

        ImageView ForwardView = new ImageView(new Image("media/btn_forward.png"));
        ForwardView.setFitHeight(25);
        ForwardView.setPreserveRatio(true);
        forward.setGraphic(ForwardView);
        forward.setBackground(Background.EMPTY);

        ImageView RewindView = new ImageView(new Image("media/btn_rewind.png"));
        RewindView.setFitHeight(25);
        RewindView.setPreserveRatio(true);
        rewind.setGraphic(RewindView);
        rewind.setBackground(Background.EMPTY);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
    public void progressBarWasDragged(){
        vm.progressBarWasDragged();
    }
}

package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;
import other_classes.FxmlLoader;
import view_model.ViewModel;


public class MainWindowController implements Initializable, Observer {
    ViewModel vm;
    @FXML
    private BorderPane joystickPane;
    @FXML
    private BorderPane clocksPanelPane;
    @FXML
    private BorderPane attributesViewPane;
    @FXML
    private BorderPane anomalyDetectionGraphPane;
    @FXML
    private Button play;
    @FXML
    private Button pause;
    @FXML
    private Button forward;
    @FXML
    private Button rewind;
    @FXML
    private Button openCSV;

    public void setViewModel(ViewModel vm){
        this.vm=vm;
    }

    @FXML
    public void pressButtonPlay(ActionEvent event){ vm.play(); }
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
        vm.setTimeSeries();
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

        ImageView PauseView = new ImageView(new Image("media/btn_pause.png"));
        PauseView.setFitHeight(25);
        PauseView.setPreserveRatio(true);
        pause.setGraphic(PauseView);

        ImageView ForwardView = new ImageView(new Image("media/btn_forward.png"));
        ForwardView.setFitHeight(25);
        ForwardView.setPreserveRatio(true);
        forward.setGraphic(ForwardView);

        ImageView RewindView = new ImageView(new Image("media/btn_rewind.png"));
        RewindView.setFitHeight(25);
        RewindView.setPreserveRatio(true);
        rewind.setGraphic(RewindView);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

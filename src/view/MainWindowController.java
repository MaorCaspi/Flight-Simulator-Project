package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;


public class MainWindowController implements Initializable {
    @FXML
    private BorderPane joystickPane;
    @FXML
    private BorderPane clocksPanelPane;
    @FXML
    private BorderPane attributesViewPane;
    @FXML
    private BorderPane playbackControlsPane;
    @FXML
    private Button button;

    @FXML
    public void pressButton(ActionEvent event){
        System.out.println("hello");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pane joystickView = new FxmlLoader().getPage("joystickWindow.fxml");
        joystickPane.setCenter(joystickView);

        Pane clocksPanelView = new FxmlLoader().getPage("clocksPanel.fxml");
        clocksPanelPane.setCenter(clocksPanelView);

        Pane attributesViewView = new FxmlLoader().getPage("attributesView.fxml");
        attributesViewPane.setCenter(attributesViewView);

        Pane playbackControlsView = new FxmlLoader().getPage("playbackControls.fxml");
        playbackControlsPane.setCenter(playbackControlsView);
    }
}

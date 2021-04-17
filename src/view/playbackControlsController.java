package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class playbackControlsController implements Initializable {

    @FXML
    private Button play;
    @FXML
    private Button pause;
    @FXML
    private Button forward;
    @FXML
    private Button rewind;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
}

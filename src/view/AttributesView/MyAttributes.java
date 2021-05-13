package view.AttributesView;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MyAttributes extends Pane {

    public MyAttributes(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane attr=fxl.load(getClass().getResource("AttributesView.fxml").openStream());
            AttributesViewController attributesViewController=fxl.getController();


            this.getChildren().add(attr);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

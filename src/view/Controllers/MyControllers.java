package view.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MyControllers extends Pane {

    public final ControllersController controller;

    public MyControllers() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        Pane controllers=null;
        try {
            controllers = fxl.load(getClass().getResource("Controllers.fxml").openStream());
            this.getChildren().add(controllers);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if(controllers!=null){
            controller = fxl.getController();
        }
        else{
            controller=null;
        }
    }
}

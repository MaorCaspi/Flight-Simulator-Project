package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.FlightGearModel;
import view_model.ViewModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxl=new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root=(Parent) fxl.load();
        Image icon = new Image("media/Icon.png");
        primaryStage.setTitle("Flight Simulator");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        FlightGearModel flightGearModel=new FlightGearModel("properties.txt");
        ViewModel vm=new ViewModel(flightGearModel);
       flightGearModel.addObserver(vm);
        MainWindowController mwc=fxl.getController();
        mwc.setViewModel(vm);
        vm.addObserver(mwc);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

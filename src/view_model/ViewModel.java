package view_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import other_classes.TimeSeries;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer  {
    Model m;
    boolean firstTimePlay;
    TimeSeries ts;
    Stage stage;
    public DoubleProperty playSpeed;

    public ViewModel(Model m, Stage stage){
        this.m=m;
        m.addObserver(this);
        this.stage=stage;
        firstTimePlay=true;
        playSpeed = new SimpleDoubleProperty();
        //playSpeed.addListener(observable, oldValue, newValue)->m.setPlaySpeed((double)newValue);
    }
    public void setTimeSeries() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload flight recording file - CSV");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV file", "*.csv*"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            ts = new TimeSeries(file.getPath());
        }
    }
    public void play(){
        if(firstTimePlay) {
            if(ts==null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Problem with flight recording file!\nYou must upload a valid CSV file.");
                alert.showAndWait();
                return;
            }
            firstTimePlay=false;
            Thread t1=new Thread()
            {
                public void run() {
                    m.playCsv(ts);
                }
            };
            t1.setDaemon(true);//when the main program ends-this thread will terminate too
            t1.start();
        }
        else {
            m.wakeUP();
        }
    }
    public void pause() {
        if(!m.pause)
        {
            m.pause=true;
        }
    }
    public void forward() { }
    public void rewind(){ }

    @Override
    public void update(Observable o, Object arg) {
        if(o==m){}
    }

}

package view.AttributesView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import other_classes.Properties;
import other_classes.TimeSeries;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MyAttributes extends Pane {

    private  String csv,xml;
    public StringProperty selectedFeature;

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void LoadList() {
        if(!new File(xml).exists()||!new File(csv).exists())
            return;
        FXMLLoader fxl = new FXMLLoader();
        try {
            Pane attr = fxl.load(getClass().getResource("AttributesView.fxml").openStream());
            AttributesViewController attributesViewController = fxl.getController();
            ListView<String> attributesListView = attributesViewController.attributeslistView;
            TimeSeries ts = new TimeSeries(csv);
            ArrayList<String> as = ts.getAttributes();

            Properties p = new Properties();
            p.deserializeFromXML(xml);
            Map<String, Integer> rIndex = p.getRowsIndex();
            float f=p.getRate();
            for (Map.Entry<String, Integer> e : rIndex.entrySet()) {
                if (e.getValue() > 0 && e.getValue() < as.size() + 1){
                    attributesListView.getItems().add(as.get(e.getValue() - 1));
                }
            }
            attributesListView.getSelectionModel().select(0);//show by default the first feature
            selectedFeature=new SimpleStringProperty(attributesListView.getSelectionModel().getSelectedItems().get(0));
            attributesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedFeature.setValue(newValue);
            });

            this.getChildren().add(attr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

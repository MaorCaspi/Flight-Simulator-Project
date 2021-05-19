package view.AttributesView;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import other_classes.Properties;
import other_classes.TimeSeries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MyAttributes extends Pane {

    private  String csv;
    private String xml;

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

            TimeSeries ts = new TimeSeries(csv);
            ArrayList<String> as = ts.getAttributes();


            Properties p = new Properties();
            p.deserializeFromXML(xml);
            Map<String, Integer> rIndex = p.getRowsIndex();
            float f=p.getRate();
            for (Map.Entry<String, Integer> e : rIndex.entrySet()) {
                //  System.out.println("k: "+e.getKey()+", v: "+e.getValue());
                if (e.getValue() > 0 && e.getValue() < as.size() + 1){
                    attributesViewController.getMyStrings().add(as.get(e.getValue() - 1));
                }

            }


            this.getChildren().add(attr);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public MyAttributes() {


    }
}

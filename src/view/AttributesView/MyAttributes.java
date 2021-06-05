package view.AttributesView;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MyAttributes extends Pane {

    public StringProperty selectedFeature;
    public ListProperty<String> features;
    public ListView<String> attributesListView;

    public void LoadList() {
        FXMLLoader fxl = new FXMLLoader();
        try {
            Pane attr = fxl.load(getClass().getResource("AttributesView.fxml").openStream());
            AttributesViewController attributesViewController = fxl.getController();
            features=new SimpleListProperty<>(FXCollections.observableArrayList());
            attributesListView = attributesViewController.attributeslistView;
            features.addListener((observable, oldValue, newValue) -> {
                attributesListView.setItems(newValue);
                attributesListView.getSelectionModel().select(0);//show by default the first feature
                selectedFeature=new SimpleStringProperty(attributesListView.getSelectionModel().getSelectedItems().get(0));
            });

            attributesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(selectedFeature!=null) {
                    selectedFeature.setValue(newValue);
                }
            });

            this.getChildren().add(attr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

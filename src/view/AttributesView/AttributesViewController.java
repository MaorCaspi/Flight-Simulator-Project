package view.AttributesView;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class AttributesViewController{

    @FXML private ListView<String> attributeslistView;

    public ListView<String> getAttributeslistView() {
        return attributeslistView;
    }
}
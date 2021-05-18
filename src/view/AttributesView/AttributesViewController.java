package view.AttributesView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AttributesViewController{
    private final ObservableList<String> myStrings = FXCollections.observableArrayList();

    public AttributesViewController() {
        myStrings.addAll("One", "Two", "Three");
    }

    public ObservableList<String> getMyStrings() {
        return myStrings ;
    }

}

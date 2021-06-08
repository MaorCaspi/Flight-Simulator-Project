package anomalyDetectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import other_classes.TimeSeries;
import java.util.List;

public interface AnomalyDetector{
    public StringProperty selectedFeature=new SimpleStringProperty();
    public IntegerProperty numOfRow= new SimpleIntegerProperty();
    public void learnNormal(TimeSeries ts);
    public List<AnomalyReport> detect(TimeSeries ts);
    public AnchorPane paint();
}

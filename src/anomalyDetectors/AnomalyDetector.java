package anomalyDetectors;

import javafx.scene.layout.AnchorPane;
import other_classes.TimeSeries;
import java.util.List;
import java.util.Observer;

public interface AnomalyDetector extends Observer {
    public void learnNormal(TimeSeries ts);
    public List<AnomalyReport> detect(TimeSeries ts);
    public AnchorPane paint();
}

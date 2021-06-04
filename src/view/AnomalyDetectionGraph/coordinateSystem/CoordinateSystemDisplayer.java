package view.AnomalyDetectionGraph.coordinateSystem;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class CoordinateSystemDisplayer extends Pane {
	public CoordinateSystemController controller;
	public CoordinateSystemDisplayer() {
		super();
		FXMLLoader loader=new FXMLLoader();
		try {
			Pane toDisplay=loader.load(getClass().getResource("CoordinateSystem.fxml").openStream());
			controller=loader.getController();
			this.getChildren().add(toDisplay);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

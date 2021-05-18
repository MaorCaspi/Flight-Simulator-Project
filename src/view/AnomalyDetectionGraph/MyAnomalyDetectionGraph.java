package view.AnomalyDetectionGraph;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import other_classes.PointGraph;

import java.io.IOException;
import java.util.List;

public class MyAnomalyDetectionGraph extends Pane{

    public MyAnomalyDetectionGraph(){
        super();
        FXMLLoader fxl=new FXMLLoader();
        try {
            Pane adg=fxl.load(getClass().getResource("AnomalyDetectionGraph.fxml").openStream());
            AnomalyDetectionGraphController anomalyDetectionGraphController=fxl.getController();


            //adg.setOrientation(Orientation.VERTICAL);
           // Stage stage = (Stage) lbl.getScene().getWindow();

           // stage.setTitle("Line Chart Sample");
           List<PointGraph> points1= anomalyDetectionGraphController.getPoints1();
           List<PointGraph> points2= anomalyDetectionGraphController.getPoints1();
           List<PointGraph> points3= anomalyDetectionGraphController.getPoints1();
//            points.add(new Point(3,4));
//            points.add(new Point(12,4));
//            points.add(new Point(9,10));
//            points.add(new Point(7,4));
//            points.add(new Point(3,5));
//            points.add(new Point(6,4));
//            points.add(new Point(2,4));

            LineChart<Number, Number> lineChart1 =CreateLineChart(points1,false);
            LineChart<Number, Number> lineChart2 =CreateLineChart(points2,false);
            LineChart<Number, Number> lineChart3 =CreateLineChart(points3,true);



            SplitPane wrapPane = new SplitPane();
            wrapPane.setOrientation(Orientation.VERTICAL);

            SplitPane topPane = new SplitPane();
            topPane.getItems().add(lineChart1);
            topPane.getItems().add(lineChart2);

            wrapPane.getItems().add(topPane);
            wrapPane.getItems().add(lineChart3);
            adg.getChildren().add(wrapPane);





            this.getChildren().add(adg);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LineChart<Number, Number> CreateLineChart(List<PointGraph> points, boolean isWider)
    {

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(new NumberAxis(), new NumberAxis());

        lineChart.setTitle("Stock Monitoring, 2010");
        lineChart.setPrefHeight(220);
        lineChart.setMinHeight(220);
        lineChart.setMaxHeight(220);

        if(isWider)
        {
            lineChart.setPrefWidth(600);
            lineChart.setMinWidth(600);
            lineChart.setMaxWidth(600);

            lineChart.setPrefSize(600, 220);
            lineChart.setMinSize(600, 220);
            lineChart.setMaxSize(600, 220);
        }
        else
        {
            lineChart.setPrefWidth(300);
            lineChart.setMinWidth(300);
            lineChart.setMaxWidth(300);

            lineChart.setPrefSize(300, 220);
            lineChart.setMinSize(300, 220);
            lineChart.setMaxSize(300, 220);
        }









        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        for(int i =0;i<points.size();i++)
        {
            series.getData().add(new XYChart.Data(points.get(i).getX(), points.get(i).getY()));
        }


//        Scene scene2 = new Scene(lineChart2, 800, 600);
        lineChart.getData().add(series);
        return lineChart;
    }
}

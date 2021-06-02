package other_classes;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeSeries {

    private Map<Integer, ArrayList<Double>> ts;
    private ArrayList<String> atts;

    public TimeSeries() {
        ts=new HashMap<>();
        atts=new ArrayList<>();
    }
    public TimeSeries(String csvFileName) throws IOException {
        ts=new HashMap<>();
        atts=new ArrayList<>();
        BufferedReader in=new BufferedReader(new FileReader(csvFileName));
        String line=in.readLine();
        int i=1;
        for(String att : line.split(",")) {
            atts.add(att);
            ts.put(i, new ArrayList<>());
            i++;
        }
        while((line=in.readLine())!=null) {
            i=1;
            for(String val : line.split(",")) {
                ts.get(i).add(Double.parseDouble(val));
                i++;
            }
        }
        in.close();
    }

    public ArrayList<Double> getAttributeData(int id){
        return ts.get(id);
    }

    public ListProperty<Point> getListOfPointsUntilSpecificRow(int id, int rowNumber){
        ListProperty<Point> tempList=new SimpleListProperty<>(FXCollections.observableArrayList());
        for(int i=0; i<rowNumber;i++){
            tempList.add(new Point(i,ts.get(id).get(i)));
        }
        return tempList;
    }

    public ArrayList<String> getAttributes(){
        return atts;
    }

    public int getRowSize() {
        return ts.get(1).size();
    }
    public int getNumOfColumns(){
        return ts.size();
    }

    public String getRowByRowNumber(int rowNumber)
    {
        StringBuilder result= new StringBuilder();
        for(int i=1;i<atts.size();i++)
        {
            result.append(ts.get(i).get(rowNumber)+",");
        }
        result.append(ts.get(atts.size()).get(rowNumber));
        return result.toString();
    }
    public double getDataFromSpecificRowAndColumn(int columnNumber,int rowNumber){
        return ts.get(columnNumber).get(rowNumber);
    }
    public int getIndexByFeature(String FeatureName){
        for(int i=0;i<atts.size();i++) {
            if(atts.get(i).equals(FeatureName)) {
                return i+1;
            }
        }
        return 0;
    }
    public String getFeatureByIndex(int index){
        return atts.get(index-1);
    }
    public void addCol(String feature,ArrayList<Double> data) {
        atts.add(feature);
        ts.put(getNumOfColumns(), data);
    }
}

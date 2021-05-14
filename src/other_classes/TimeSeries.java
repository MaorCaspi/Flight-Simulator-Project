package other_classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeSeries {

    private Map<Integer, ArrayList<Double>> ts;
    private ArrayList<String> atts;
    private int dataRowSize;

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
        dataRowSize=ts.get(1).size();
    }

    public ArrayList<Double> getAttributeData(int id){
        return ts.get(id);
    }

    public ArrayList<String> getAttributes(){
        return atts;
    }

    public int getRowSize() {
        return dataRowSize;
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
}

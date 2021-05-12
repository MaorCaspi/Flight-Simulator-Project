package other_classes;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Properties {

    private Map<String, Integer> rowsIndex;
    private Float rate;
    private String flightGearIP;
    private int flightGearPort;
    private String normalFlightCsvPath;
    private String anomalyDetectionFilePath;

    public Properties() {
        rowsIndex=new HashMap<>();
    }

    public void setRowsIndex(Map<String, Integer> rowsIndex) {
        this.rowsIndex = rowsIndex;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public void setFlightGearIP(String flightGearIP) {
        this.flightGearIP = flightGearIP;
    }

    public void setFlightGearPort(int flightGearPort) {
        this.flightGearPort = flightGearPort;
    }

    public void setNormalFlightCsvPath(String normalFlightCsvPath) {
        this.normalFlightCsvPath = normalFlightCsvPath;
    }

    public void setAnomalyDetectionFilePath(String anomalyDetectionFilePath) {
        this.anomalyDetectionFilePath = anomalyDetectionFilePath;
    }

    public Map<String, Integer> getRowsIndex() {
        return rowsIndex;
    }

    public Float getRate() {
        return rate;
    }

    public String getFlightGearIP() {
        return flightGearIP;
    }

    public int getFlightGearPort() {
        return flightGearPort;
    }

    public String getNormalFlightCsvPath() {
        return normalFlightCsvPath;
    }

    public String getAnomalyDetectionFilePath() {
        return anomalyDetectionFilePath;
    }
    public void setFromAnotherProperties(Properties properties){
        setRowsIndex(properties.getRowsIndex());
        setRate(properties.getRate());
        setFlightGearIP(properties.getFlightGearIP());
        setFlightGearPort(properties.getFlightGearPort());
        setNormalFlightCsvPath(properties.getNormalFlightCsvPath());
        setAnomalyDetectionFilePath(properties.getAnomalyDetectionFilePath());
    }

    public void serializeToXML (String filePath)
    {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.writeObject(this);
            encoder.close();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Boolean deserializeFromXML(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XMLDecoder decoder = new XMLDecoder(fis);
            Properties decodedSettings =(Properties)decoder.readObject();
            decoder.close();
            fis.close();
            this.setFromAnotherProperties(decodedSettings);
            serializeToXML("settings.xml");//Save all information in a separate XML file for future execution.
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

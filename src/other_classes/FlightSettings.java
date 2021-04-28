package other_classes;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FlightSettings {
    private String nameOfLocationOnEarth;
    private String nameOfHeight;
    private String nameOfDirection;
    private String nameOYaw;
    private String nameOfPitch;
    private String nameOfRoll;
    private String nameOfThrottle;
    private String nameOfValueOnJoystickX;
    private String nameOfValueOnJoystickY;
    private String nameOfValueOnSteeringWheelX;
    private String nameOfValueOnSteeringWheelY;

    private Float maxLocationOnEarth;
    private Float minLocationOnEarth;
    private Float maxHeight;
    private Float minHeight;
    private Float maxDirection;
    private Float minDirection;
    private Float maxYaw;
    private Float minYaw;
    private Float maxPitch;
    private Float minPitch;
    private Float maxRoll;
    private Float minRoll;
    private Float maxThrottle;
    private Float minThrottle;
    private Float maxValueOnJoystickX;
    private Float minValueOnJoystickX;
    private Float maxValueOnJoystickY;
    private Float minValueOnJoystickY;
    private Float maxValueOnSteeringWheelX;
    private Float minValueOnSteeringWheelX;
    private Float maxValueOnSteeringWheelY;
    private Float minValueOnSteeringWheelY;

    private Float fileSampleRateInSecond;
    private String nameOfNormalFlightFile;
    private String nameOfAnomalyDetectionFile;

    void serializeToXML (String filePath)
    {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.writeObject(this);
            encoder.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Boolean deserializeFromXML(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XMLDecoder decoder = new XMLDecoder(fis);
            FlightSettings decodedSettings = (FlightSettings) decoder.readObject();
            decoder.close();
            fis.close();

            if(decodedSettings.getNameOfLocationOnEarth() != null &&
                    decodedSettings.getNameOfHeight() != null &&
                    decodedSettings.getNameOfDirection() != null &&
                    decodedSettings.getNameOYaw() != null &&
                    decodedSettings.getNameOfPitch() != null &&
                    decodedSettings.getNameOfRoll() != null &&
                    decodedSettings.getNameOfThrottle() != null &&
                    decodedSettings.getNameOfValueOnJoystickX() != null &&
                    decodedSettings.getNameOfValueOnJoystickY() != null &&
                    decodedSettings.getNameOfValueOnSteeringWheelX() != null &&
                    decodedSettings.getNameOfValueOnSteeringWheelY() != null &&
                    decodedSettings.getMaxLocationOnEarth() != null &&
                    decodedSettings.getMinLocationOnEarth() != null &&
                    decodedSettings.getMaxHeight() != null &&
                    decodedSettings.getMinHeight() != null &&
                    decodedSettings.getMaxDirection() != null &&
                    decodedSettings.getMinDirection() != null &&
                    decodedSettings.getMaxYaw() != null &&
                    decodedSettings.getMinYaw() != null &&
                    decodedSettings.getMaxPitch() != null &&
                    decodedSettings.getMinPitch() != null &&
                    decodedSettings.getMaxRoll() != null &&
                    decodedSettings.getMinRoll() != null &&
                    decodedSettings.getMaxThrottle() != null &&
                    decodedSettings.getMinThrottle() != null &&
                    decodedSettings.getMaxValueOnJoystickX() != null &&
                    decodedSettings.getMinValueOnJoystickX() != null &&
                    decodedSettings.getMaxValueOnJoystickY() != null &&
                    decodedSettings.getMinValueOnJoystickY() != null &&
                    decodedSettings.getMaxValueOnSteeringWheelX() != null &&
                    decodedSettings.getMinValueOnSteeringWheelX() != null &&
                    decodedSettings.getMaxValueOnSteeringWheelY() != null &&
                    decodedSettings.getMinValueOnSteeringWheelY() != null &&
                    decodedSettings.getFileSampleRateInSecond() != null &&
                    decodedSettings.getNameOfNormalFlightFile() != null &&
                    decodedSettings.getNameOfAnomalyDetectionFile() != null)
            {
                setNameOfLocationOnEarth(decodedSettings.getNameOfLocationOnEarth());
                setNameOfHeight(decodedSettings.getNameOfHeight());
                setNameOfDirection(decodedSettings.getNameOfDirection());
                setNameOYaw(decodedSettings.getNameOYaw());
                setNameOfPitch(decodedSettings.getNameOfPitch());
                setNameOfRoll(decodedSettings.getNameOfRoll());
                setNameOfThrottle(decodedSettings.getNameOfThrottle());
                setNameOfValueOnJoystickX(decodedSettings.getNameOfValueOnJoystickX());
                setNameOfValueOnJoystickY(decodedSettings.getNameOfValueOnJoystickY());
                setNameOfValueOnSteeringWheelX(decodedSettings.getNameOfValueOnSteeringWheelX());
                setNameOfValueOnSteeringWheelY(decodedSettings.getNameOfValueOnSteeringWheelY());
                setMaxLocationOnEarth(decodedSettings.getMaxLocationOnEarth());
                setMinLocationOnEarth(decodedSettings.getMinLocationOnEarth());
                setMaxHeight(decodedSettings.getMaxHeight());
                setMinHeight(decodedSettings.getMinHeight());
                setMaxDirection(decodedSettings.getMaxDirection());
                setMinDirection(decodedSettings.getMinDirection());
                setMaxYaw(decodedSettings.getMaxYaw());
                setMinYaw(decodedSettings.getMinYaw());
                setMaxPitch(decodedSettings.getMaxPitch());
                setMinPitch(decodedSettings.getMinPitch());
                setMaxRoll(decodedSettings.getMaxRoll());
                setMinRoll(decodedSettings.getMinRoll());
                setMaxThrottle(decodedSettings.getMaxThrottle());
                setMinThrottle(decodedSettings.getMinThrottle());
                setMaxValueOnJoystickX(decodedSettings.getMaxValueOnJoystickX());
                setMinValueOnJoystickX(decodedSettings.getMinValueOnJoystickX());
                setMaxValueOnJoystickY(decodedSettings.getMaxValueOnJoystickY());
                setMinValueOnJoystickY(decodedSettings.getMinValueOnJoystickY());
                setMaxValueOnSteeringWheelX(decodedSettings.getMaxValueOnSteeringWheelX());
                setMinValueOnSteeringWheelX(decodedSettings.getMinValueOnSteeringWheelX());
                setMaxValueOnSteeringWheelY(decodedSettings.getMaxValueOnSteeringWheelY());
                setMinValueOnSteeringWheelY(decodedSettings.getMinValueOnSteeringWheelY());
                setFileSampleRateInSecond(decodedSettings.getFileSampleRateInSecond());
                setNameOfNormalFlightFile(decodedSettings.getNameOfNormalFlightFile());
                setNameOfAnomalyDetectionFile(decodedSettings.getNameOfAnomalyDetectionFile());

                serializeToXML("settings.xml");//Save all information in a separate XML file for future execution.
                return true;
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return false;
    }

    public String getNameOfLocationOnEarth() {
        return nameOfLocationOnEarth;
    }
    public void setNameOfLocationOnEarth(String nameOfLocationOnEarth) {
        this.nameOfLocationOnEarth = nameOfLocationOnEarth;
    }
    public String getNameOfHeight() {
        return nameOfHeight;
    }
    public void setNameOfHeight(String nameOfHeight) {
        this.nameOfHeight = nameOfHeight;
    }
    public String getNameOfDirection() {
        return nameOfDirection;
    }
    public void setNameOfDirection(String nameOfDirection) {
        this.nameOfDirection = nameOfDirection;
    }
    public String getNameOYaw() {
        return nameOYaw;
    }
    public void setNameOYaw(String nameOYaw) {
        this.nameOYaw = nameOYaw;
    }
    public String getNameOfPitch() {
        return nameOfPitch;
    }
    public void setNameOfPitch(String nameOfPitch) {
        this.nameOfPitch = nameOfPitch;
    }
    public String getNameOfRoll() {
        return nameOfRoll;
    }
    public void setNameOfRoll(String nameOfRoll) {
        this.nameOfRoll = nameOfRoll;
    }
    public String getNameOfThrottle() {
        return nameOfThrottle;
    }
    public void setNameOfThrottle(String nameOfThrottle) {
        this.nameOfThrottle = nameOfThrottle;
    }
    public String getNameOfValueOnJoystickX() {
        return nameOfValueOnJoystickX;
    }
    public void setNameOfValueOnJoystickX(String nameOfValueOnJoystickX) {
        this.nameOfValueOnJoystickX = nameOfValueOnJoystickX;
    }
    public String getNameOfValueOnJoystickY() {
        return nameOfValueOnJoystickY;
    }
    public void setNameOfValueOnJoystickY(String nameOfValueOnJoystickY) {
        this.nameOfValueOnJoystickY = nameOfValueOnJoystickY;
    }
    public String getNameOfValueOnSteeringWheelX() {
        return this.nameOfValueOnSteeringWheelX;
    }
    public void setNameOfValueOnSteeringWheelX(String nameOfValueOnSteeringWheelX) {
        this.nameOfValueOnSteeringWheelX = nameOfValueOnSteeringWheelX;
    }
    public String getNameOfValueOnSteeringWheelY() {
        return nameOfValueOnSteeringWheelY;
    }
    public void setNameOfValueOnSteeringWheelY(String nameOfValueOnSteeringWheelY) {
        this.nameOfValueOnSteeringWheelY = nameOfValueOnSteeringWheelY;
    }
    public Float getMaxLocationOnEarth() {
        return maxLocationOnEarth;
    }
    public void setMaxLocationOnEarth(Float maxLocationOnEarth) {
        this.maxLocationOnEarth = maxLocationOnEarth;
    }
    public Float getMinLocationOnEarth() {
        return minLocationOnEarth;
    }
    public void setMinLocationOnEarth(Float minLocationOnEarth) {
        this.minLocationOnEarth = minLocationOnEarth;
    }
    public Float getMaxHeight() {
        return maxHeight;
    }
    public void setMaxHeight(Float maxHeight) {
        this.maxHeight = maxHeight;
    }
    public Float getMinHeight() {
        return minHeight;
    }
    public void setMinHeight(Float minHeight) {
        this.minHeight = minHeight;
    }
    public Float getMaxDirection() {
        return maxDirection;
    }
    public void setMaxDirection(Float maxDirection) {
        this.maxDirection = maxDirection;
    }
    public Float getMinDirection() {
        return minDirection;
    }
    public void setMinDirection(Float minDirection) {
        this.minDirection = minDirection;
    }
    public Float getMaxYaw() {
        return maxYaw;
    }
    public void setMaxYaw(Float maxYaw) {
        this.maxYaw = maxYaw;
    }
    public Float getMinYaw() {
        return minYaw;
    }
    public void setMinYaw(Float minYaw) {
        this.minYaw = minYaw;
    }
    public Float getMaxPitch() {
        return maxPitch;
    }
    public void setMaxPitch(Float maxPitch) {
        this.maxPitch = maxPitch;
    }
    public Float getMinPitch() {
        return minPitch;
    }
    public void setMinPitch(Float minPitch) {
        this.minPitch = minPitch;
    }
    public Float getMaxRoll() {
        return maxRoll;
    }
    public void setMaxRoll(Float maxRoll) {
        this.maxRoll = maxRoll;
    }
    public Float getMinRoll() {
        return minRoll;
    }
    public void setMinRoll(Float minRoll) {
        this.minRoll = minRoll;
    }
    public Float getMaxThrottle() {
        return maxThrottle;
    }
    public void setMaxThrottle(Float maxThrottle) {
        this.maxThrottle = maxThrottle;
    }
    public Float getMinThrottle() {
        return minThrottle;
    }
    public void setMinThrottle(Float minThrottle) {
        this.minThrottle = minThrottle;
    }
    public Float getMaxValueOnJoystickX() {
        return maxValueOnJoystickX;
    }
    public void setMaxValueOnJoystickX(Float maxValueOnJoystickX) {
        this.maxValueOnJoystickX = maxValueOnJoystickX;
    }
    public Float getMinValueOnJoystickX() {
        return minValueOnJoystickX;
    }
    public void setMinValueOnJoystickX(Float minValueOnJoystickX) {
        this.minValueOnJoystickX = minValueOnJoystickX;
    }
    public Float getMaxValueOnJoystickY() {
        return maxValueOnJoystickY;
    }
    public void setMaxValueOnJoystickY(Float maxValueOnJoystickY) {
        this.maxValueOnJoystickY = maxValueOnJoystickY;
    }
    public Float getMinValueOnJoystickY() {
        return minValueOnJoystickY;
    }
    public void setMinValueOnJoystickY(Float minValueOnJoystickY) {
        this.minValueOnJoystickY = minValueOnJoystickY;
    }
    public Float getMaxValueOnSteeringWheelX() {
        return maxValueOnSteeringWheelX;
    }
    public void setMaxValueOnSteeringWheelX(Float maxValueOnSteeringWheelX) {
        this.maxValueOnSteeringWheelX = maxValueOnSteeringWheelX;
    }
    public Float getMinValueOnSteeringWheelX() {
        return minValueOnSteeringWheelX;
    }
    public void setMinValueOnSteeringWheelX(Float minValueOnSteeringWheelX) {
        this.minValueOnSteeringWheelX = minValueOnSteeringWheelX;
    }
    public Float getMaxValueOnSteeringWheelY() {
        return maxValueOnSteeringWheelY;
    }
    public void setMaxValueOnSteeringWheelY(Float maxValueOnSteeringWheelY) {
        this.maxValueOnSteeringWheelY = maxValueOnSteeringWheelY;
    }
    public Float getMinValueOnSteeringWheelY() {
        return minValueOnSteeringWheelY;
    }
    public void setMinValueOnSteeringWheelY(Float minValueOnSteeringWheelY) {
        this.minValueOnSteeringWheelY = minValueOnSteeringWheelY;
    }
    public Float getFileSampleRateInSecond() {
        return fileSampleRateInSecond;
    }
    public void setFileSampleRateInSecond(Float fileSampleRateInSecond) {
        this.fileSampleRateInSecond = fileSampleRateInSecond;
    }
    public String getNameOfNormalFlightFile() {
        return nameOfNormalFlightFile;
    }
    public void setNameOfNormalFlightFile(String nameOfNormalFlightFile) {
        this.nameOfNormalFlightFile = nameOfNormalFlightFile;
    }
    public String getNameOfAnomalyDetectionFile() {
        return nameOfAnomalyDetectionFile;
    }
    public void setNameOfAnomalyDetectionFile(String nameOfAnomalyDetectionFile) {
        this.nameOfAnomalyDetectionFile = nameOfAnomalyDetectionFile;
    }
}

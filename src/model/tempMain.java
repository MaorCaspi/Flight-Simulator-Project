package model;

public class tempMain {

	public static void main(String[] args) {
        FlightSettings settings=new FlightSettings();
        /*
		settings.setNameOfLocationOnEarth("maorr");
		settings.setNameOfHeight("maorr");
		settings.setNameOfDirection("maorr");
		settings.setNameOYaw("maorr");
		settings.setNameOfPitch("bdika");
		settings.setNameOfRoll("maorr");
		settings.setNameOfThrottle("maorr");
		settings.setNameOfValueOnJoystickX("maorr");
		settings.setNameOfValueOnJoystickY("maorr");
		settings.setNameOfValueOnSteeringWheelX("maorr");
		settings.setNameOfValueOnSteeringWheelY("maorr");
		settings.setMaxLocationOnEarth((float)1.0);
		settings.setMinLocationOnEarth((float)1.0);
		settings.setMaxHeight((float)1.0);
		settings.setMinHeight((float)1.0);
		settings.setMaxDirection((float)1.0);
		settings.setMinDirection((float)1.0);
		settings.setMaxYaw((float)1.0);
		settings.setMinYaw((float)1.0);
		settings.setMaxPitch((float)1.0);
		settings.setMinPitch((float)1.0);
		settings.setMaxRoll((float)1.0);
		settings.setMinRoll((float)1.0);
		settings.setMaxThrottle((float)1.0);
		settings.setMinThrottle((float)1.0);
		settings.setMaxValueOnJoystickX((float)1.0);
		settings.setMinValueOnJoystickX((float)1.0);
		settings.setMaxValueOnJoystickY((float)1.0);
		settings.setMinValueOnJoystickY((float)1.0);
		settings.setMaxValueOnSteeringWheelX((float)1.0);
		settings.setMinValueOnSteeringWheelX((float)1.0);
		settings.setMaxValueOnSteeringWheelY((float)1.0);
		settings.setMinValueOnSteeringWheelY((float)1.0);
		settings.setFileSampleRateInSecond((float)1.0);
		settings.setNameOfNormalFlightFile("maorr");
		settings.setNameOfAnomalyDetectionFile("maorr");
		settings.serializeToXML("settings2.xml");
         */

		 settings.deserializeFromXML("settings.xml");

		//....
		//....
		//....
		//Now, the user asked to browse his XML file:
		String filePath = "settings2.xml";
		if (!settings.deserializeFromXML(filePath))
			System.out.println("XML upload failed");
		else {
			System.out.println("XML uploaded successfully");
		}
	}
}
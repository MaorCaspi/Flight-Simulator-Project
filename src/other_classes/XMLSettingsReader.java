package other_classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class XMLSettingsReader {

        private  String path;

        public  XMLSettingsReader(String path){
                this.path=path;
        }

        public  void write() {
                //Settings settings
                try {
                        Item i1 = new Item();
                        i1.Max = 1;
                        i1.Min =-1;
                        i1.NewName = "aileron";
                        i1.OldName="aileron";
                        i1.category="Aileron";

                        Item i2 = new Item();
                        i2.Max = 200;
                        i2.Min =-200;
                        i2.NewName = "elevator";
                        i2.OldName="elevator";
                        i2.category="Elevator";

                        Item i3 = new Item();
                        i3.Max = 200;
                        i3.Min =-200;
                        i3.NewName = "categorycategory";
                        i3.OldName="catcat";
                        i3.category="Aileron";

                        Item i4 = new Item();
                        i4.Max = 200;
                        i4.Min =-200;
                        i4.NewName = "flaps";
                        i4.OldName="flaps";
                        i4.category="Flaps";


                        Item i6 = new Item();
                        i6.Max = 200;
                        i6.Min =-200;
                        i6.NewName = "slats";
                        i6.OldName="slats";
                        i6.category="Slats";

                        Item i7 = new Item();
                        i7.Max = 200;
                        i7.Min =-200;
                        i7.NewName = "speedbrake";
                        i7.OldName="speedbrake";
                        i7.category="Speedbrake";

                        Item i8 = new Item();
                        i8.Max = 200;
                        i8.Min =-200;
                        i8.NewName = "throttle";
                        i8.OldName="throttle";
                        i8.category="Throttle";

                        Item i9 = new Item();
                        i9.Max = 200;
                        i9.Min =-200;
                        i9.NewName = "throttle";
                        i9.OldName="throttle";
                        i9.category="Throttle";

                        Item i10 = new Item();
                        i10.Max = 200;
                        i10.Min =-200;
                        i10.NewName = "engine-pump";
                        i10.OldName="engine-pump";
                        i10.category="Engine-pump";

                        Item i11 = new Item();
                        i11.Max = 200;
                        i11.Min =-200;
                        i11.NewName = "engine-pump";
                        i11.OldName="engine-pump";
                        i11.category="Engine-pump";


                        Item i12 = new Item();
                        i12.Max = 200;
                        i12.Min =-200;
                        i12.NewName = "electric-pump";
                        i12.OldName="electric-pump";
                        i12.category="Electric-pump";

                        Item i13 = new Item();
                        i13.Max = 200;
                        i13.Min =-200;
                        i13.NewName = "electric-pump";
                        i13.OldName="electric-pump";
                        i13.category="Electric-pump";

                        Item i14 = new Item();
                        i14.Max = 200;
                        i14.Min =-200;
                        i14.NewName = "external-power";
                        i14.OldName="external-power";
                        i14.category="External-power";

                        Item i15 = new Item();
                        i15.Max = 200;
                        i15.Min =-200;
                        i15.NewName = "APU-generator";
                        i15.OldName="APU-generator";
                        i15.category="APU-generator";

                        Item i16 = new Item();
                        i16.Max = 200;
                        i16.Min =-200;
                        i16.NewName = "latitude-deg";
                        i16.OldName="latitude-deg";
                        i16.category="Latitude-deg";

                        Item i17 = new Item();
                        i17.Max = 200;
                        i17.Min =-200;
                        i17.NewName = "longitude-deg";
                        i17.OldName="longitude-deg";
                        i17.category="Longitude-deg";

                        Item i18 = new Item();
                        i18.Max = 200;
                        i18.Min =-200;
                        i18.NewName = "altitude-ft";
                        i18.OldName="altitude-ft";
                        i18.category="Altitude-ft";

                        Item i19 = new Item();
                        i19.Max = 200;
                        i19.Min =-200;
                        i19.NewName = "roll-deg";
                        i19.OldName="roll-deg";
                        i19.category="Roll-deg";

                        Item i20 = new Item();
                        i20.Max = 200;
                        i20.Min =-200;
                        i20.NewName = "pitch-deg";
                        i20.OldName="pitch-deg";
                        i20.category="Pitch-deg";

                        Item i21 = new Item();
                        i21.Max = 200;
                        i21.Min =-200;
                        i21.NewName = "heading-deg";
                        i21.OldName="heading-deg";
                        i21.category="Heading-deg";

                        Item i22 = new Item();
                        i22.Max = 200;
                        i22.Min =-200;
                        i22.NewName = "side-slip-deg";
                        i22.OldName="side-slip-deg";
                        i22.category="Side-slip-deg";

                        Item i23 = new Item();
                        i23.Max = 200;
                        i23.Min =-200;
                        i23.NewName = "airspeed-kt";
                        i23.OldName="airspeed-kt";
                        i23.category="Airspeed-kt";


                        Item i25 = new Item();
                        i25.Max = 200;
                        i25.Min =-200;
                        i25.NewName = "glideslope";
                        i25.OldName="glideslope";
                        i25.category="Glideslope";

                        Item i26 = new Item();
                        i26.Max = 200;
                        i26.Min =-200;
                        i26.NewName = "vertical-speed-fps";
                        i26.OldName="vertical-speed-fps";
                        i26.category="Vertical-speed-fps";

                        Item i27 = new Item();
                        i27.Max = 200;
                        i27.Min =-200;
                        i27.NewName = "airspeed-indicator_indicated-speed-kt";
                        i27.OldName="airspeed-indicator_indicated-speed-kt";
                        i27.category="Airspeed-indicator_indicated-speed-kt";

                        Item i28 = new Item();
                        i28.Max = 200;
                        i28.Min =-200;
                        i28.NewName = "altimeter_indicated-altitude-ft";
                        i28.OldName="altimeter_indicated-altitude-ft";
                        i28.category="Altimeter_indicated-altitude-ft";

                        Item i29 = new Item();
                        i29.Max = 200;
                        i29.Min =-200;
                        i29.NewName = "altimeter_pressure-alt-ft";
                        i29.OldName="altimeter_pressure-alt-ft";
                        i29.category="Altimeter_pressure-alt-ft";

                        Item i30 = new Item();
                        i30.Max = 200;
                        i30.Min =-200;
                        i30.NewName = "attitude-indicator_indicated-pitch-deg";
                        i30.OldName="attitude-indicator_indicated-pitch-deg";
                        i30.category="Attitude-indicator_indicated-pitch-deg";

                        Item i31 = new Item();
                        i31.Max = 200;
                        i31.Min =-200;
                        i31.NewName = "attitude-indicator_indicated-roll-deg";
                        i31.OldName="attitude-indicator_indicated-roll-deg";
                        i31.category="Attitude-indicator_indicated-roll-deg";

                        Item i32 = new Item();
                        i32.Max = 200;
                        i32.Min =-200;
                        i32.NewName = "attitude-indicator_internal-pitch-deg";
                        i32.OldName="attitude-indicator_internal-pitch-deg";
                        i32.category="Attitude-indicator_internal-pitch-deg";

                        Item i33 = new Item();
                        i33.Max = 200;
                        i33.Min =-200;
                        i33.NewName = "attitude-indicator_internal-roll-deg";
                        i33.OldName="attitude-indicator_internal-roll-deg";
                        i33.category="Attitude-indicator_internal-roll-deg";

                        Item i34 = new Item();
                        i34.Max = 200;
                        i34.Min =-200;
                        i34.NewName = "encoder_indicated-altitude-ft";
                        i34.OldName="encoder_indicated-altitude-ft";
                        i34.category="Encoder_indicated-altitude-ft";

                        Item i35 = new Item();
                        i35.Max = 200;
                        i35.Min =-200;
                        i35.NewName = "gps_indicated-altitude-ft";
                        i35.OldName="gps_indicated-altitude-ft";
                        i35.category="Gps_indicated-altitude-ft";

                        Item i36 = new Item();
                        i36.Max = 200;
                        i36.Min =-200;
                        i36.NewName = "gps_indicated-ground-speed-kt";
                        i36.OldName="gps_indicated-ground-speed-kt";
                        i36.category="Gps_indicated-ground-speed-kt";

                        Item i37 = new Item();
                        i37.Max = 200;
                        i37.Min =-200;
                        i37.NewName = "gps_indicated-vertical-speed";
                        i37.OldName="gps_indicated-vertical-speed";
                        i37.category="Gps_indicated-vertical-speed";

                        Item i38 = new Item();
                        i38.Max = 200;
                        i38.Min =-200;
                        i38.NewName = "indicated-heading-deg";
                        i38.OldName="indicated-heading-deg";
                        i38.category="Indicated-heading-deg";

                        Item i39 = new Item();
                        i39.Max = 200;
                        i39.Min =-200;
                        i39.NewName = "categorycategory";
                        i39.OldName="catcat";
                        i39.category="Aileron";

                        Item i40 = new Item();
                        i40.Max = 200;
                        i40.Min =-200;
                        i40.NewName = "magnetic-compass_indicated-heading-deg";
                        i40.OldName="magnetic-compass_indicated-heading-deg";
                        i40.category="Magnetic-compass_indicated-heading-deg";

                        Item i41 = new Item();
                        i41.Max = 200;
                        i41.Min =-200;
                        i41.NewName = "slip-skid-ball_indicated-slip-skid";
                        i41.OldName="slip-skid-ball_indicated-slip-skid";
                        i41.category="Slip-skid-ball_indicated-slip-skid";

                        Item i42 = new Item();
                        i42.Max = 200;
                        i42.Min =-200;
                        i42.NewName = "turn-indicator_indicated-turn-rate";
                        i42.OldName="turn-indicator_indicated-turn-rate";
                        i42.category="Turn-indicator_indicated-turn-rate";

                        Item i43 = new Item();
                        i43.Max = 200;
                        i43.Min =-200;
                        i43.NewName = "vertical-speed-indicator_indicated-speed-fpm";
                        i43.OldName="vertical-speed-indicator_indicated-speed-fpm";
                        i43.category="Vertical-speed-indicator_indicated-speed-fpm";

                        Item i44 = new Item();
                        i44.Max = 200;
                        i44.Min =-200;
                        i44.NewName = "engine_rpm";
                        i44.OldName="engine_rpm";
                        i44.category="Engine_rpm";

                        //*** it 44 but have 42

                        Settings s1 = new Settings();
                        s1.Item.add(i1);
                        s1.Item.add(i2);
                        s1.Item.add(i3);
                        s1.Item.add(i4);
                        s1.Item.add(i6);
                        s1.Item.add(i7);
                        s1.Item.add(i8);
                        s1.Item.add(i9);
                        s1.Item.add(i10);
                        s1.Item.add(i11);

                        s1.Item.add(i12);
                        s1.Item.add(i13);
                        s1.Item.add(i14);
                        s1.Item.add(i15);
                        s1.Item.add(i16);
                        s1.Item.add(i17);
                        s1.Item.add(i18);
                        s1.Item.add(i19);
                        s1.Item.add(i20);
                        s1.Item.add(i21);

                        s1.Item.add(i22);
                        s1.Item.add(i23);
                        s1.Item.add(i25);
                        s1.Item.add(i26);
                        s1.Item.add(i27);
                        s1.Item.add(i28);
                        s1.Item.add(i29);
                        s1.Item.add(i30);
                        s1.Item.add(i31);
                        s1.Item.add(i32);

                        s1.Item.add(i33);
                        s1.Item.add(i34);
                        s1.Item.add(i35);
                        s1.Item.add(i36);
                        s1.Item.add(i37);
                        s1.Item.add(i38);
                        s1.Item.add(i39);
                        s1.Item.add(i40);
                        s1.Item.add(i41);
                        s1.Item.add(i42);

                        s1.Item.add(i43);
                        s1.Item.add(i44);

                        s1.AlgoFileName="algo";
                        s1.ProperFlightFileName="bla";
                        s1.RateSeconds=10;



                        FileOutputStream fos = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        XMLEncoder xmlEncoder = new XMLEncoder(bos);
                        xmlEncoder.writeObject(s1);// settings
                        xmlEncoder.close();
                        fos.close();

//            Settings b = deserializeFromXML();
//
//            System.out.println(b.Item.get(0).NewName);
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        private  Settings read() throws IOException {
                FileInputStream fis = new FileInputStream(path);
                XMLDecoder decoder = new XMLDecoder(fis);
                Settings decodedSettings = (Settings) decoder.readObject();
                decoder.close();
                fis.close();
                return decodedSettings;
        }
        public static void main(String[] args) {
                XMLSettingsReader j=new XMLSettingsReader("C:\\Users\\Administrator\\Desktop\\haha.xml");
                //j.write();
                try {
                        Settings set=j.read();
                        System.out.println(set.Item.get(0).category);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}




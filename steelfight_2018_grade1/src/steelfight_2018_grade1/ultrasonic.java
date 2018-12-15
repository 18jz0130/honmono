package steelfight_2018_grade1;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;

public class ultrasonic {

    static final EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
	static SensorMode sonic = sonicSensor.getMode(0);
	static float value[] = new float[sonic.sampleSize()];
	ultrasonic(){
		
	}
	public float get_ss_metor() {
		sonic.fetchSample(value, 0);
		/*for (int k = 0 ; k < sonic.sampleSize() ; k++) {
			LCD.drawString("val[" + k +"] :" + value[k] +" m", 1, k + 5);
		}*/
		return value[0];
	}
}

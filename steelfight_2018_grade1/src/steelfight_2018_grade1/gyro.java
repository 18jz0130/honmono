package steelfight_2018_grade1;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;

public class gyro {

    static final EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S3);
    static final SensorMode gyro = gyroSensor.getMode(1);
	static float value[] = new float[gyro.sampleSize()];

    public static float get_angle() {
    	gyro.fetchSample(value, 0);
    	
		return value[0];
	}
}

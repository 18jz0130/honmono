package steelfight_2018_grade1;

import lejos.hardware.Button;

public class Robotanien {

	/*static RegulatedMotor armMotor = Motor.A;
    static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
    static final EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
    static final EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S3);*/

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		run_motor run_motor = new run_motor();
		arm_motor arm = new arm_motor();
		ultrasonic sonic = new ultrasonic();
		gyro gyro = new gyro();
		color color = new color();
		while ( ! Button.ESCAPE.isDown()){
			arm.arm_control();
			//LCD.drawString("" + arm.arm_control(), 0, 0);
		}
	}

}

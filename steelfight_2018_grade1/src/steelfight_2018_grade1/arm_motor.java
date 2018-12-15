package steelfight_2018_grade1;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class arm_motor {

	static RegulatedMotor armMotor = Motor.A;
	static boolean is_catch = false;


	public void arm_control() {
		if(is_catch == false) {
			armMotor.rotateTo(0);
			is_catch = true;
		}
		else {
			armMotor.rotateTo(-360);
			is_catch = false;
		}
		Delay.msDelay(1000);

	}
}

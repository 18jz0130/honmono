package steelfight_2018_grade1;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class run_motor {

    static RegulatedMotor leftMotor = Motor.B;
    static RegulatedMotor rightMotor = Motor.C;

	public run_motor() {
		leftMotor.resetTachoCount();
		leftMotor.rotateTo(0);
		rightMotor.resetTachoCount();
		rightMotor.rotateTo(0);
	}
	public static void motor_set (int l_motor_pow, int r_motor_pow) {
		leftMotor.setSpeed(l_motor_pow);
		rightMotor.setSpeed(r_motor_pow);
		if (l_motor_pow < 0) {
			leftMotor.backward();
		}else if(l_motor_pow > 0){
			leftMotor.forward();
		}else {
			leftMotor.stop();
		}
		if(r_motor_pow < 0) {
			rightMotor.backward();
		}else if(r_motor_pow > 0){
			rightMotor.forward();
		}else {
			rightMotor.stop();
		}
	}
}

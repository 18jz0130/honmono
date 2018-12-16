package steelfight_2018_grade1;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class run_motor {

    static RegulatedMotor leftMotor = Motor.B;
    static RegulatedMotor rightMotor = Motor.C;
    static int story_memory = -1;
	public run_motor() {
		leftMotor.resetTachoCount();
		leftMotor.rotateTo(0);
		rightMotor.resetTachoCount();
		rightMotor.rotateTo(0);
	}
	int turn_memory_l = 0;
	int turn_memory_r = 0;
	public void motor_set (int l_motor_pow, int r_motor_pow, int now_story) {
		if(story_memory == now_story) {

		}else {
			story_memory = now_story;
			motor_wheel(l_motor_pow, r_motor_pow);
		}
	}
	public static void motor_wheel(int l_motor_pow, int r_motor_pow) {
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
	public void reset_ran_metor() {
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
	}

	public void line_trace(int l_motor_pow, int r_motor_pow, int color, int trace_color) {
		if( color == trace_color) {
			motor_set(l_motor_pow,r_motor_pow, color);
		}else{
			motor_set(r_motor_pow,l_motor_pow, color);
		}
	}
	public void turning_set(int l_motor_pow, int r_motor_pow) {

		turn_memory_l = l_motor_pow;
		turn_memory_r = r_motor_pow;
	}
	//即席だよ？直したほうがいいと思うよ？

	public void turning() {
		if( turn_memory_l  > turn_memory_r) {
			if(turn_memory_l > turn_memory_r / 2) {
				turn_memory_l -= 1;
			}
		}else if( turn_memory_l < turn_memory_r) {
			if(turn_memory_l / 2 < turn_memory_r) {
				turn_memory_l -= 1;
			}
		}
		motor_wheel(turn_memory_l, turn_memory_r);
	}
	public int get_ran_metor() {
		int ran_metor = 0;
		ran_metor = (leftMotor.getTachoCount() + rightMotor.getTachoCount()) / 2;

		return ran_metor;
	}
}

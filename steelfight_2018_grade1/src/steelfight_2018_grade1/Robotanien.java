package steelfight_2018_grade1;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Robotanien {

	/*static RegulatedMotor armMotor = Motor.A;
    static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
    static final EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
    static final EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S3);*/

	static final int white = 0;
	static final int yellow = 1;
	static final int green = 2;
	static final int blue = 3;
	static final int red = 4;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		run_motor run_motor = new run_motor();
		arm_motor arm = new arm_motor();
		ultrasonic sonic = new ultrasonic();
		gyro gyro = new gyro();
		color color = new color();
		int now_story = 0;

		reader reader = new reader();


		while ( ! Button.ESCAPE.isDown()){
			//LCD.drawString("" + arm.arm_control(), 0, 0);

			switch(now_story) {
			case 0:		/////////////スタート キャリブレーション必要か判定////////////////////////
				if(color.color_value[0][0] == 0) {
					now_story = 1;
				}else {
					now_story = 5;
				}
				break;
			case 1:		////////////////////スタート前/////////////////////////
				if(reader.story(Button.DOWN.isDown(), true)) {
					color.colors_init();
					now_story = 5;
				}
				break;
			case 5:		///////////////////////白を見つけるまで直進///////////////////////
				///*直進*///
				if (reader.story(color.get_color(), white)) {
					now_story = 10;
				}
				LCD.drawString("Ready",0,0);
				break;
			case 10:	///////////////////////ライントレース/////////////////

				if(reader.story(color.get_color(), blue)) {
					now_story = 15;
				}
				break;
			case 15:
				run_motor.reset_ran_metor();
				now_story = 16;
			case 16:	//////////////////////ワーク置くまで直進///////////////////
				if(reader.story(run_motor.get_ran_metor(), 20, true)) {
					now_story = 20;
				}
				break;
			case 20:	///////////////////////ワーク置いてバック
				if(reader.story(color.get_color(), green)) {
					now_story = 25;
				}
				break;
			case 25:
				run_motor.reset_ran_metor();
				now_story = 26;
			case 26:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				if(reader.story(run_motor.get_ran_metor(), 500)) {
					now_story = 30;
				}
				break;
			case 30:	////////////////////////90度回転/////////////////////////////////
				if(reader.story(gyro.get_angle(), 90, true)) {
					now_story = 35;
				}
			case 35:	///////////////////////ワークまで直進/////////////////
				break;
			}
			LCD.drawString("" + now_story, 0, 7);

		}
	}


}

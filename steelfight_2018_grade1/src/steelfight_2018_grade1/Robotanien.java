package steelfight_2018_grade1;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

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
	static final int greenwhite = 5;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		run_motor run_motor = new run_motor();
		arm_motor arm = new arm_motor();	//左がプラス、右がマイナス
		ultrasonic sonic = new ultrasonic();
		color color = new color();
		gyro gyro = new gyro();
		int now_story = 0;

		reader reader = new reader();
		int speeds[][] = {
			//ライントレースまとめ 	正方向 L > R の場合 右側トレース 緑の場合Lが強く、白の場合Rが強くなります
			//						正方向 R > L の場合 左側トレース 緑の場合Rが強く、白の場合Lが強くなります
			//						逆方向 L > R の場合 左側トレース 緑の場合Rが強く、白の場合Lが強くなります
			//						逆方向 R < L の場合 右側トレース 緑の場合Lが強く、白の場合Rが強くなります
			//       L,    R
		/*[0]*/	{  360,  360},//スタート ～ 白色の地面まで
				{  720,  690},//ライントレース時の速度 カラー見て左右反転させます 青を見つけるまで ここから[4]まで使いまわす
				{  720,  720},//青発見～ワーク設置まで
				{ -360, -300},//ワーク設置～ライントレース開始まで  バック → 回転 → バック
				{ -690, -720},//バックしながらライントレース
		/*[5]*/	{  360,  360},//ワーク探して進行
				{  360,  360},//ワーク捕獲後、旋回しながらラインへ



		};

		while ( ! Button.ESCAPE.isDown()){
			//LCD.drawString("" + arm.arm_control(), 0, 0);
			int get_uls_metor = 0;
			switch(now_story) {
/**************************************************************************/
			case 0:		/////////////スタート キャリブレーション必要か判定////////////////////////
				arm.arm_set();
				if(color.color_value[0][0] == 0) {
					now_story = 1;
				}else {
					if(reader.story(Button.ENTER.isDown(), true)) {
						now_story = 5;
					}else if(reader.story(Button.UP.isDown(), true)) {
						now_story = 1;
					}
				}
				break;
			case 1:	////////////////キャリブレーションの表示////////////
				LCD.clear();
				LCD.drawString("calibration", 0, 7);
				now_story = 2;
				break;
			case 2:		////////////////////スタート前/////////////////////////
				color.colors_init();
				if(reader.story(Button.DOWN.isDown(), true)) {
					now_story = 3;
				}
				break;
			case 3:
				LCD.clear();
				LCD.drawString("ready", 0, 7);
				now_story = 4;
				break;
			case 4:
				if(reader.story(Button.UP.isDown(), true)) {
					now_story = 1;
				}
				if(reader.story(Button.ENTER.isDown(), true)) {
					color.colors_init();
					now_story = 5;
				}
				break;
/**************************************************************************/
			case 5:
				run_motor.reset_ran_metor();
				now_story = 6;
				break;
			case 6:		///////////////////////白・緑・中間色のどれかを見つけるまで直進///////////////////////
				///*直進*///
				run_motor.motor_set(speeds[0][0],speeds[0][1],now_story);
				if (reader.story(color.get_color(), white		  )||
					reader.story(color.get_color(), green		  )||
					reader.story(color.get_color(), greenwhite  )) {
					now_story = 10;
				}
				LCD.drawString("Ready",0,0);
				break;
/**************************************************************************/
			case 10:	///////////////////////ライントレース/////////////////
				run_motor.line_trace(speeds[1][0],speeds[1][1], color.get_color(), greenwhite ,green);
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				if(reader.story(color.get_color(), blue) && reader.story(run_motor.get_ran_metor(), 1200, true)) {
					now_story = 15;
				}
				break;
/**************************************************************************/
			case 15:	///////////////////////直進用タコメーターのリセット///////////////////
				run_motor.reset_ran_metor();
				now_story = 16;
			case 16:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 500, true)) {
					now_story = 20;
				}
				break;
			case 20:
				run_motor.rota(false);
				if(gyro.get_angle() < -150) {
					now_story = 21;
				}
				break;
			case 21:
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 22;
				break;
			case 22:
				run_motor.motor_set(720, 720, now_story);
				if(run_motor.get_ran_metor() > 1552) {
					run_motor.motor_set(0, 0, now_story);
					now_story = 25;
				}
				break;
			case 25:
				arm.arm_control();
				Delay.msDelay(500);
				gyro.reset();
				now_story = 26;
				break;
			case 26:
				now_story = 27;
				break;
			case 27:
				run_motor.rota(false);
				if(gyro.get_angle() < -180) {
					now_story = 30;
				}
				break;
			case 30:
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 31;
				break;
			case 31:
				run_motor.motor_set(720, 720, now_story);
				if(run_motor.get_ran_metor() > 1552) {
					run_motor.motor_set(0, 0, now_story);
					now_story = 35;
				}
				break;
			case 35:
				gyro.reset();
				run_motor.reset_ran_metor();
				now_story = 36;
				break;
			case 36:
				run_motor.rota(true);
				if(gyro.get_angle() > 150) {
					now_story = 40;
				}
				break;
			case 40:
				run_motor.motor_set(720, 720, now_story);
				if(run_motor.get_ran_metor() > 1552) {
					run_motor.motor_set(0, 0, now_story);
					now_story = 45;
				}
				break;
			case 46:
				arm.arm_control();
				Delay.msDelay(500);
				gyro.reset();
				now_story = 50;
				break;
			case 50:
				run_motor.rota(false);
				if(gyro.get_angle() < -180) {
					run_motor.reset_ran_metor();
					now_story = 55;
				}
				break;
			case 55:
				run_motor.motor_set(720, 720, now_story);
				if(run_motor.get_ran_metor() > 1552) {
					run_motor.motor_set(0, 0, now_story);
					now_story = 60;
				}
				break;
			case 60:
				run_motor.motor_set(0, 0, now_story);
				Delay.msDelay(500);
				now_story = 65;
				break;
			case 65:
				gyro.reset();
				now_story = 66;
				break;
			case 66:
				run_motor.rota(false);
				if(gyro.get_angle() < -15 ){
					run_motor.reset_ran_metor();
					now_story = 70;
				}
				break;
			case 70:
				run_motor.motor_set(720, 720, now_story);
				if(color.get_color() == yellow) {
					run_motor.motor_set(0, 0, now_story);
					now_story = 75;
				}
				break;
			case 75:
				run_motor.motor_set(0, 0, now_story);
				break;
			}
		}
	}
}
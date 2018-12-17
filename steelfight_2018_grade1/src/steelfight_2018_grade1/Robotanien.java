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

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		run_motor run_motor = new run_motor();
		arm_motor arm = new arm_motor();
		ultrasonic sonic = new ultrasonic();
		gyro gyro = new gyro();
		color color = new color();
		int now_story = 0;

		reader reader = new reader();
		int speeds[][] = {
			//ライントレースまとめ 	正方向 L > R の場合 右側トレース 緑の場合Lが強く、白の場合Rが強くなります
			//						正方向 R > L の場合 左側トレース 緑の場合Rが強く、白の場合Lが強くなります
			//						逆方向 L > R の場合 左側トレース 緑の場合Rが強く、白の場合Lが強くなります
			//						逆方向 R < L の場合 右側トレース 緑の場合Lが強く、白の場合Rが強くなります
			//       L,    R
		/*[0]*/	{  360,  360},//スタート ～ 白色の地面まで
				{   60,   45},//ライントレース時の速度 カラー見て左右反転させます 青を見つけるまで ここから[4]まで使いまわす
				{   60,   60},//青発見～ワーク設置まで
				{  -30,  -30},//ワーク設置～ライントレース開始まで  バック → 回転 → バック
				{  -45,  -60},//バックしながらライントレース
		/*[5]*/	{   60,   60},//ワーク探して進行
				{   60,   45},//ワーク捕獲後、旋回しながらラインへ



		};

		while ( ! Button.ESCAPE.isDown()){
			//LCD.drawString("" + arm.arm_control(), 0, 0);

			switch(now_story) {
/**************************************************************************/
			case 0:		/////////////スタート キャリブレーション必要か判定////////////////////////
				if(color.color_value[0][0] == 0) {
					now_story = 1;
				}else {
					if(reader.story(Button.ENTER.isDown(), true)) {
						now_story = 5;
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
			case 5:		///////////////////////白を見つけるまで直進///////////////////////
				///*直進*///
				run_motor.motor_set(speeds[0][0],speeds[0][1],now_story);
				if (reader.story(color.get_color(), white)) {
					now_story = 10;
				}
				LCD.drawString("Ready",0,0);
				break;
/**************************************************************************/
			case 10:	///////////////////////ライントレース/////////////////
				run_motor.line_trace(speeds[1][0],speeds[1][1], color.get_color(), green);
				if(reader.story(color.get_color(), blue)) {
					now_story = 15;
				}
				break;
/**************************************************************************/
			case 15:	///////////////////////直進用タコメーターのリセット///////////////////
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 16;
			case 16:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 300, true)) {
					now_story = 20;
				}
				break;
/**************************************************************************/
			case 20:	/////////////////円の内側の白を検知するまでバック///
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), white)) {
					now_story = 21;
				}
				break;
			case 21:	/////////////////////少し曲がるための回転///////////////////////////
				run_motor.motor_set(10, -10, now_story);
				if(reader.story(gyro.get_angle(), 10)) {
					now_story = 22;
				}
				break;
			case 22:	//////////////////////青を検知するまで、ただしモーターは止めないので23も同様の実行結果/////////////////////////////
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), blue)) {
					now_story = 23;
				}
				break;
			case 23:	//////////////////////円の外側の白を検知するまで///////////////////
				if(reader.story(color.get_color(), white)) {
					now_story = 25;
				}
				break;
			case 25:	//////////////////////バックライントレースのためのタコメリセット////////////////
				run_motor.reset_ran_metor();
				now_story = 26;
/**************************************************************************/
			case 26:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.line_trace(speeds[4][0],speeds[4][1], color.get_color(), green);
				if(reader.story(run_motor.get_ran_metor(), -300, false)) {
					now_story = 30;
				}
				break;
/**************************************************************************/
			case 30:	///////////////////90度回転用///////////////////////
				LCD.clear();
				now_story = 31;
				break;
			case 31:	////////////////////////90度回転/////////////////////////////////
				run_motor.motor_set( 10, -10, now_story);
				if(reader.story(gyro.get_angle(), 90, true)) {
					now_story = 35;
				}
				LCD.drawString("angle = " + gyro.get_angle(), 1, 1);
				break;
/**************************************************************************/
			case 35:	/////////////////////ワークまで直進/////////////////////////
				run_motor.reset_ran_metor();
				now_story = 36;
				break;
			case 36:	///////////////////////ワークまで直進/////////////////
				run_motor.motor_set(speeds[5][0], speeds[5][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 200)) {
					now_story = 40;
				}
				break;
/**************************************************************************/
			case 40:	////////////////アームでつかむ////////////////////
                arm.arm_control();
                now_story = 45;
                break;
/**************************************************************************/
            case 45:	////////////////旋回しながら緑のラインへ向かう///////
                if (reader.story(color.get_color(), green)) {
                	now_story = 55;
                }
                break;
/**************************************************************************/
            case 55:	///////////////////モーターを止めて回転///////////
            	run_motor.motor_set(0, 0, now_story);
                arm.arm_control();
                now_story =56;
                break;
            case 56:
            	run_motor.motor_set(10, -10, now_story);
            	if(reader.story(gyro.get_angle(), 0,true)) {
            		now_story = 60;
            	}
            	break;
/**************************************************************************/
            case 60:	/////////////////////ライントレースの開始//////////////////
            	run_motor.line_trace(speeds[1][0],speeds[1][1], color.get_color(), green);
            	if(reader.story(color.get_color(), blue)) {
            		now_story = 65;
            	}
                break;
/**************************************************************************/
			case 70:	///////////////////////直進用タコメーターのリセット///////////////////
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 16;
			case 71:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 300, true)) {
					now_story = 75;
				}
				break;
/**************************************************************************/
			case 75:	/////////////////円の内側の白を検知するまでバック///
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), white)) {
					now_story = 76;
				}
				break;
			case 76:	/////////////////////少し曲がるための回転///////////////////////////
				run_motor.motor_set(10, -10, now_story);
				if(reader.story(gyro.get_angle(), 10)) {
					now_story = 77;
				}
				break;
			case 77:	//////////////////////青を検知するまで、ただしモーターは止めないので23も同様の実行結果/////////////////////////////
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), blue)) {
					now_story = 78;
				}
				break;
			case 78:	//////////////////////円の外側の白を検知するまで///////////////////
				if(reader.story(color.get_color(), white)) {
					now_story = 80;
				}
				break;
/*ここから*************************************************************************/
			case 80:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.line_trace(speeds[4][0],speeds[4][1], color.get_color(), green);
				if(reader.story(run_motor.get_ran_metor(), -300, false)) {
					now_story = 50;
				}
				break;
/**************************************************************************/
			case 85:	///////////////////90度回転用///////////////////////
				LCD.clear();
				now_story = 86;
				break;
			case 86:	////////////////////////90度回転/////////////////////////////////
				run_motor.motor_set( 10, -10, now_story);
				if(reader.story(gyro.get_angle(), 90, true)) {
					now_story = 90;
				}
				LCD.drawString("angle = " + gyro.get_angle(), 1, 1);
				break;
/**************************************************************************/
			case 90:	/////////////////////ワークまで直進/////////////////////////
				run_motor.reset_ran_metor();
				now_story = 91;
				break;
			case 91:	///////////////////////ワークまで直進/////////////////
				run_motor.motor_set(speeds[5][0], speeds[5][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 200)) {
					now_story = 95;
				}
				break;
/**************************************************************************/
			case 95:	////////////////アームでつかむ////////////////////
                arm.arm_control();
                now_story = 100;
                break;
/**************************************************************************/
            case 100:	////////////////旋回しながら緑のラインへ向かう///////
                if (reader.story(color.get_color(), green)) {
                	now_story = 105;
                }
                break;
/**************************************************************************/
            case 105:	///////////////////モーターを止めて回転///////////
            	run_motor.motor_set(0, 0, now_story);
                arm.arm_control();
                now_story =106;
                break;
            case 106:
            	run_motor.motor_set(10, -10, now_story);
            	if(reader.story(gyro.get_angle(), 0,true)) {
            		now_story = 60;
            	}
            	break;
/**************************************************************************/
            case 120: ///////////黄色に帰還//////
                if(reader.story(color.get_color(), yellow)) {

                }
                break;
			}
			LCD.drawString("" + now_story, 0, 5);
			Delay.msDelay(5);
		}
	}


}

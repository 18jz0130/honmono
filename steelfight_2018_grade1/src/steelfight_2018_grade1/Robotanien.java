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
		int now_story = 0;
		gyro gyro = new gyro();

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
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 16;
			case 16:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 500, true)) {
					now_story = 20;
				}
				break;
/**************************************************************************/
			/*case 20:	/////////////////円の内側の白を検知するまでバック///
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), white)) {
					now_story = 21;
				}
				break;
			case 21:	/////////////////////少し曲がるための回転///////////////////////////
				run_motor.motor_set(50, -50, now_story);
				if(reader.story(gyro.get_angle(), 20)) {
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
				break;*/
			case 20:
				run_motor.reset_ran_metor();
				now_story = 21;
				break;
			case 21:
				run_motor.motor_set(50, -50, now_story);
				LCD.drawString("" + gyro.get_angle(), 0, 1);
				if(reader.story(gyro.get_angle(), -30, false)) {
					now_story = 22;
				}
				break;
			case 22:
				run_motor.turning(400, run_motor.get_ran_metor(),speeds[3][0],speeds[3][1] );
				if(reader.story(color.get_color(), green)) {
					now_story = 23;
				}
				break;
			case 23:
				if(-5 < gyro.get_angle() || gyro.get_angle() < 5) {
					now_story = 25;
				}
				break;
/**************************************************************************/
			case 25:	//////////////////////バックライントレースのためのタコメリセット////////////////
				now_story = 26;
			case 26:
				run_motor.motor_set(50, -50, now_story);
				if (reader.story(gyro.get_angle(), 10 , false)) {
					run_motor.reset_ran_metor();
					now_story = 27;
				}
				break;
			case 27:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.line_trace(speeds[4][0],speeds[4][1], color.get_color(), greenwhite, green);
				if(reader.story(run_motor.get_ran_metor(), -1200, false)) {
					now_story = 30;
				}
				break;
/**************************************************************************/
			case 30:	//////////////////右のを取る-90度回転用///////////////////////
				LCD.clear();
				now_story = 31;
				break;
			case 31:	////////////////////////-90度回転/////////////////////////////////
				run_motor.motor_set( 50, -50, now_story);
				if(reader.story(gyro.get_angle(), -90, false)) {
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
				if(reader.story(run_motor.get_ran_metor(),200, true)) {
					run_motor.reset_ran_metor();
					now_story = 37;
				}
				break;
			case 37:
				get_uls_metor = sonic.get_ss_metor();
				LCD.drawString("" + get_uls_metor + "                  ", 0, 1);
				if( get_uls_metor < 30 ) {
					run_motor.reset_ran_metor();
					LCD.clear();
					now_story = 39;
				}else{
					//サーチ
					run_motor.motor_set(50, -50, now_story);
					if(gyro.get_angle() < -360){
							now_story = 38;
					}
				}
				break;
			case 38:
				get_uls_metor = sonic.get_ss_metor();
				LCD.drawString("" + get_uls_metor, 0, 1);
				if( get_uls_metor < 30 ) {
					run_motor.reset_ran_metor();
					LCD.clear();
					now_story = 39;
				}else{
					//サーチ
					run_motor.motor_set(-50, 50, now_story);
					if(gyro.get_angle() > 360){
							now_story = 37;
					}
				}
				break;
			case 39:
				//run_motor.motor_set(speeds[5][0], speeds[5][1], now_story);
				run_motor.motor_set(120,120, now_story);
				LCD.drawString("" + get_uls_metor, 0, 1);
				LCD.drawString("" + run_motor.get_ran_metor(), 0, 2);
				if(reader.story(run_motor.get_ran_metor(), get_uls_metor, true )) {
					run_motor.motor_set(0, 0, now_story);
					now_story = 40;
				}
				break;
/**************************************************************************/
			case 40:	////////////////アームでつかむ////////////////////
                arm.arm_control();
                now_story = 45;
                break;
/**************************************************************************/
			case 45:
				run_motor.motor_set(-50, 50, now_story);
				if(reader.story(gyro.get_angle(), -45, false)){
					now_story = 46;
				}
				break;
            case 46:	////////////////緑のラインへ向かう///////
            	run_motor.motor_set(speeds[6][0], speeds[6][1], now_story);
                if (reader.story(color.get_color(), green)) {
                	now_story = 55;
                }
                break;
/**************************************************************************/
            case 55:	///////////////////モーターを止めて回転///////////
            	run_motor.motor_set(0, 0, now_story);
            	LCD.clear();
                now_story =56;
                break;
            case 56:
            	run_motor.motor_set(50, -50, now_story);
				LCD.drawString("" + gyro.get_angle(), 0, 1);
            	if(-5 < gyro.get_angle() && gyro.get_angle() < 5) {
                    arm.arm_control();
            		now_story = 60;
            	}
            	break;
/**************************************************************************/
			case 60:	///////////////////////ライントレース/////////////////
				run_motor.line_trace(speeds[1][0],speeds[1][1], color.get_color(), greenwhite ,green);
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				if(reader.story(color.get_color(), blue) && reader.story(run_motor.get_ran_metor(), 1200, true)) {
					now_story = 65;
				}
				break;
/**************************************************************************/
			case 65:	///////////////////////直進用タコメーターのリセット///////////////////
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 66;
			case 66:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 320, true)) {
					now_story = 70;
				}
				break;
/**************************************************************************/
			/*case 20:	/////////////////円の内側の白を検知するまでバック///
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), white)) {
					now_story = 21;
				}
				break;
			case 21:	/////////////////////少し曲がるための回転///////////////////////////
				run_motor.motor_set(50, -50, now_story);
				if(reader.story(gyro.get_angle(), 20)) {
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
				break;*/
			case 70:
				run_motor.reset_ran_metor();
				now_story = 71;
				break;
			case 71:
				run_motor.turning(200, run_motor.get_ran_metor(),speeds[3][0],speeds[3][1] );
				if(reader.story(color.get_color(), green)) {
					now_story = 72;
				}
				break;
			case 72:
				if(-5 < gyro.get_angle() || gyro.get_angle() < 5) {
					now_story = 75;
				}
				break;
/**************************************************************************/
			case 75:	//////////////////////バックライントレースのためのタコメリセット////////////////
				run_motor.reset_ran_metor();
				now_story = 76;
			case 76:
				if (reader.story(gyro.get_angle(), 0 , false)) {
					now_story = 77;
				}
				break;
			case 77:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.line_trace(speeds[4][0],speeds[4][1], color.get_color(), greenwhite, green);
				if(reader.story(run_motor.get_ran_metor(), -1200, false)) {
					now_story = 80;
				}
				break;
/**************************************************************************/
			case 80:	///////////////////左のを取る90度回転用///////////////////////
				LCD.clear();
				now_story = 81;
				break;
			case 81:	////////////////////////-90度回転/////////////////////////////////
				run_motor.motor_set( -50, 50, now_story);
				if(reader.story(gyro.get_angle(), 90, true)) {
					now_story = 85;
				}
				LCD.drawString("angle = " + gyro.get_angle(), 1, 1);
				break;
/**************************************************************************/
			case 85:	/////////////////////ワークまで直進/////////////////////////
				run_motor.reset_ran_metor();
				now_story = 86;
				break;
			case 86:	///////////////////////ワークまで直進/////////////////
				run_motor.motor_set(speeds[5][0], speeds[5][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 300, true)) {
					run_motor.reset_ran_metor();
					now_story = 87;
				}
				break;
			case 87:
				get_uls_metor = (int)sonic.get_ss_metor();
				LCD.drawString("" + get_uls_metor, 0, 1);
				Delay.msDelay(5000);
				if(reader.story(get_uls_metor, 15, false)) {
					run_motor.reset_ran_metor();
					now_story = 89;
				}else{
					//サーチ
					run_motor.motor_set(-50, 50, now_story);
					if(gyro.get_angle() < -120){
							now_story = 88;
					}
				}
				break;
			case 88:
				get_uls_metor = (int)sonic.get_ss_metor();
				LCD.drawString("" + get_uls_metor, 0, 1);
				Delay.msDelay(5000);
				if(reader.story(get_uls_metor, 15, false)) {
					run_motor.reset_ran_metor();
					now_story = 89;
				}else{
					//サーチ
					run_motor.motor_set(50, -50, now_story);
					if(gyro.get_angle() > -60){
							now_story = 87;
					}
				}
				break;
			case 89:
				run_motor.motor_set(speeds[5][0], speeds[5][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), get_uls_metor, true )) {
					now_story = 90;
				}
				break;
/**************************************************************************/
			case 90:	////////////////アームでつかむ////////////////////
                arm.arm_control();
                now_story = 95;
                break;
/**************************************************************************/
			case 95:
				run_motor.motor_set(-50, 50, now_story);
				if(reader.story(gyro.get_angle(), 45, true)){
					now_story = 96;
				}
				break;
            case 96:	////////////////緑のラインへ向かう///////
            	run_motor.motor_set(speeds[6][0], speeds[6][1], now_story);
                if (reader.story(color.get_color(), green)) {
                	now_story = 100;
                }
                break;
/**************************************************************************/
            case 100:	///////////////////モーターを止めて回転///////////
            	run_motor.motor_set(0, 0, now_story);
                now_story =101;
                break;
            case 101:
            	run_motor.motor_set(-50, 50, now_story);
            	if(reader.story(gyro.get_angle(), 0, false)) {
                    arm.arm_control();
            		now_story = 105;
            	}
            	break;
/**************************************************************************/
			case 105:	///////////////////////ライントレース/////////////////
				run_motor.line_trace(speeds[1][0],speeds[1][1], color.get_color(), greenwhite ,green);
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				if(reader.story(color.get_color(), blue) && reader.story(run_motor.get_ran_metor(), 1200, true)) {
					now_story = 110;
				}
				break;
/**************************************************************************/
			case 110:	///////////////////////直進用タコメーターのリセット///////////////////
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 111;
			case 111:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 320, true)) {
					now_story = 115;
				}
				break;
/**************************************************************************/
			/*case 20:	/////////////////円の内側の白を検知するまでバック///
				run_motor.motor_set(speeds[3][0],speeds[3][1], now_story);
				if(reader.story(color.get_color(), white)) {
					now_story = 21;
				}
				break;
			case 21:	/////////////////////少し曲がるための回転///////////////////////////
				run_motor.motor_set(50, -50, now_story);
				if(reader.story(gyro.get_angle(), 20)) {
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
				break;*/
			case 115:
				run_motor.reset_ran_metor();
				now_story = 116;
				break;
			case 116:
				run_motor.turning(200, run_motor.get_ran_metor(),speeds[3][0],speeds[3][1] );
				if(reader.story(color.get_color(), green)) {
					now_story = 117;
				}
				break;
			case 117:
				if(-5 < gyro.get_angle() || gyro.get_angle() < 5) {
					now_story = 120;
				}
				break;
/**************************************************************************/
			case 120:
				if (reader.story(gyro.get_angle(), 0 , false)) {
					now_story = 121;
				}
				break;
			case 121:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.line_trace(speeds[4][0],speeds[4][1], color.get_color(), greenwhite, green);
				 if(reader.story(color.get_color(), yellow)) {
					 run_motor.motor_set(0, 0, now_story);
				 }
				break;
/**************************************************************************/
            case 125: ///////////黄色に帰還//////
            	LCD.drawString("end it", 1, 1);
                break;
			}
			LCD.drawString("" + now_story, 0, 5);
			Delay.msDelay(5);
		}
	}


}

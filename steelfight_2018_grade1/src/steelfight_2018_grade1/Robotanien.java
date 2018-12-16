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
			//   L   R
		/*[0]*/	{   60,   30},//スタート ～ 白色の地面まで		発進をまっすぐにしたいから少しずれて進む
				{   60,   45},//ライントレース時の速度 カラー見て左右反転させます 青を見つけるまで l > rで右側トレース
				{   60,   60},//青発見～ワーク設置まで
				{  -30,  -60},//ワーク設置～ライントレース開始まで、ここ旋回しながら戻る
				{  -55,  -60},//バックしながらライントレース
		/*[5]*/	{   60,   60},//ワーク探して進行
				{   60,   45},//ワーク捕獲後、旋回しながらラインへ



		};

		while ( ! Button.ESCAPE.isDown()){
			//LCD.drawString("" + arm.arm_control(), 0, 0);

			switch(now_story) {
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
			case 5:		///////////////////////白を見つけるまで直進///////////////////////
				///*直進*///
				run_motor.motor_set(speeds[0][0],speeds[0][1],now_story);
				if (reader.story(color.get_color(), white)) {
					now_story = 10;
				}
				LCD.drawString("Ready",0,0);
				break;
			case 10:	///////////////////////ライントレース/////////////////
				run_motor.line_trace(speeds[1][0],speeds[1][1], color.get_color(), green);
				if(reader.story(color.get_color(), blue)) {
					now_story = 15;
				}
				break;
			case 15:	///////////////////////直進用タコメーターのリセット///////////////////
				LCD.clear();
				run_motor.reset_ran_metor();
				now_story = 16;
			case 16:	//////////////////////ワーク置くまで直進///////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.motor_set(speeds[2][0], speeds[2][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 100, true)) {
					now_story = 20;
				}
				break;
			case 20:	///////////////////ちょっと曲がりながらバックのためのセット///
				run_motor.turning_set(speeds[3][0], speeds[3][1]);
				now_story = 21;
				break;
			case 21:	//////////////////////ワーク置いてバック///////////////////////////
				run_motor.turning();
				if(reader.story(color.get_color(), green)) {
					now_story = 25;
				}
				break;
			case 25:	//////////////////////バックライントレースのためのタコメリセット////////////////
				run_motor.reset_ran_metor();
				now_story = 26;
			case 26:	////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
				LCD.drawString("ran " + run_motor.get_ran_metor(), 1, 1);
				run_motor.line_trace(speeds[4][0],speeds[4][1], color.get_color(), green);
				if(reader.story(run_motor.get_ran_metor(), -300, false)) {
					now_story = 30;
				}
				break;
			case 30:
				LCD.clear();
				now_story = 31;
				break;
			case 31:	////////////////////////90度回転/////////////////////////////////
				run_motor.motor_set( 10, -10, now_story);
				if(reader.story(gyro.get_angle(), 90, true)) {
					now_story = 35;
				}
				LCD.drawString("angle = " + gyro.get_angle(), 1, 1);
			case 35:
				run_motor.reset_ran_metor();
				now_story = 36;
				break;
			case 36:	///////////////////////ワークまで直進/////////////////
				run_motor.motor_set(speeds[5][0], speeds[5][1], now_story);
				if(reader.story(run_motor.get_ran_metor(), 40)) {
					now_story = 40;
				}
				break;
			case 40:	////////////////アームでつかむ////////////////////
                arm.arm_control();
                now_story = 45;
                break;
            case 45:	////////////////////////90度回転////////////////
                if(reader.story(gyro.get_angle(), 90, true)) {
                    now_story = 50;
                }
                break;
            case 50:
                if (reader.story(color.get_color(), red)) {
                    now_story = 55;
                }
            case 55:
                arm.arm_control();
                now_story = 30;
                break;
            case 60:
                arm.arm_control();
                now_story = 62;
                break;
            case 62:///////////////////////ワーク置いてバック
                if(reader.story(color.get_color(), green)) {
                    now_story = 65;
                }
            case 65:    ////////////////バックでライントレース,ワークの並ぶ位置まで//////////////////
                if(reader.story(run_motor.get_ran_metor(), 500)) {
                    now_story = 70;
                }
                break;
            case 70:    ////////////////////////90度回転/////////////////////////////////
                if(reader.story(gyro.get_angle(), 90, true)) {
                    now_story = 75;
                }
            case 75:    ///////////////////////ワークまで直進/////////////////
                run_motor.reset_ran_metor();
                now_story = 80;

                break;
            case 80:
                if(reader.story(run_motor.get_ran_metor(),  40)) {
                    now_story = 85;
                }
                break;
            case 85:
                arm.arm_control();
                now_story = 90;
                break;
            case 95:///////////緑を探して次に行く///////
                now_story = 100;
                break;
            case 100:
                if(reader.story(gyro.get_angle(), 0, true)) {
                    now_story = 105;
                }
                break;
            case 105:
                if (reader.story(color.get_color(), blue)) {
                    now_story = 110;
                }
            case 110:
                arm.arm_control();
                now_story = 112;
                break;
            case 112:///////////////////////ワーク置いてバック
                if(reader.story(color.get_color(), green)) {
                    now_story = 65;
                }
            case 115:    ////////////////バックでライントレース//////////////////
                if(reader.story(run_motor.get_ran_metor(), 500)) {
                    now_story = 120;
                }
                break;
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

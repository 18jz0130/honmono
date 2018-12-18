package steelfight_2018_grade1;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class color {
	static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
 	static SensorMode color = colorSensor.getMode(2);
 	static float color_value[][] = new float[7][color.sampleSize()];// 追加
 	static int now_color = 0;
 	static int k;
 	color(){
 	 	color_value[0][0] = 34.0f;
 	 	color_value[0][1] = 44.0f;
 	 	color_value[0][2] = 33.0f;
 	 	color_value[1][0] = 34.0f;
 	 	color_value[1][1] = 26.0f;
 	 	color_value[1][2] = 3.0f;
 	 	color_value[2][0] = 5.0f;
 	 	color_value[2][1] = 20.0f;
 	 	color_value[2][2] = 5.0f;
 	 	color_value[3][0] = 14.0f;
 	 	color_value[3][1] = 12.0f;
 	 	color_value[3][2] = 19.0f;
 	 	color_value[4][0] = 27.0f;
 	 	color_value[4][1] = 8.0f;
 	 	color_value[4][2] = 3.0f;
 	 	color_value[5][0] = 15.0f;
        color_value[5][1] = 28.0f;
        color_value[5][2] = 15.0f;
 	}
 	public static int get_color() {
        color.fetchSample(color_value[6], 0);

	    String[] colors = new String[]{"white   ", "yellow  ", "green   ", "blue    ", "red     ", "エラー"};
        color_value[6][0] = (float)((int)(color_value[6][0] * 100.0F));
        color_value[6][1] = (float)((int)(color_value[6][1] * 100.0F));
        color_value[6][2] = (float)((int)(color_value[6][2] * 100.0F));
// 赤の判定
        if ((color_value[4][0] - 5.0F) <= color_value[6][0] &&
            (color_value[4][1] + 2.0F) >  color_value[6][1] &&
            (color_value[4][2] + 4.0F) >  color_value[6][2]) {
            now_color = 4;
        }
     // 緑の判定
        if ((color_value[2][0] + 2.0F) >  color_value[6][0] &&
            (color_value[2][1] - 2.0F) <= color_value[6][1] &&
            (color_value[2][2] + 2.0F) >  color_value[6][2]) {
            now_color = 2;
        }
        // 青の判定
        if ((color_value[3][0] + 3.0F) >  color_value[6][0] &&
            (color_value[3][1] + 2.0F) >  color_value[6][1] &&
            (color_value[3][2] - 4.0F) <= color_value[6][2]) {
            now_color = 3;
        }
        // 黄の判定
        if ((color_value[1][0] - 5.0F) <= color_value[6][0] &&
            (color_value[1][1] - 2.0F) <= color_value[6][1] &&
            (color_value[1][2] + 5.0F) >  color_value[6][2]) {
            now_color = 1;
        }
     // 白の判定
        if ((color_value[0][0] - 5.0F) <= color_value[6][0] &&
            (color_value[0][1] - 5.0F) <= color_value[6][1] &&
            (color_value[0][2] - 5.0F) <= color_value[6][2]) { //(float)
            now_color = 0;
        }
    //	緑と白の間の判定
        if ((color_value[5][0] + 3.0F) >  color_value[6][0] &&
            (color_value[5][1] - 2.0F) <= color_value[6][1] &&
            (color_value[5][2] - 2.0F) <  color_value[6][2]) {
            now_color = 5;
        }

        LCD.drawString(colors[now_color], 0, 6);

         return now_color;
      }
 	/*public int get_color() {
	    String[] colors = new String[]{"white   ", "yellow  ", "green   ", "blue    ", "red     ", "エラー"};

	 	int[] gosa = new int[5];
	 	int min = 1000;11

	    for(int i = 0; i < 5; ++i) {
			color.fetchSample(Value, 0);

		    for(int j = 0; j < 3; ++j) {
			     if ((int)color_value[i][j] - (int)(Value[j] * 100.0F) < 0) {
			         gosa[i] += ((int)color_value[i][j] - (int)(Value[j] * 100.0F)) * -1;

			     } else {
			         gosa[i] += (int)color_value[i][j] - (int)(Value[j] * 100.0F);
			     }
		     }
		    if (min > gosa[i]) {
		    	min = gosa[i];
		    	now_color = i;
		 	}
		 }

	    LCD.drawString(colors[now_color], 0, 6);
 		return now_color;
 	 }*/


 	public void colors_init() {
       String[] colors = new String[]{"white", "yellow", "green", "blue", "red"};

       if (Button.RIGHT.isDown()) {
			LCD.clear();
			++now_color;
 	        if (now_color > 4) {
 	          now_color = 0;
 	        }
 	        LCD.drawString(colors[now_color], 0, 0);
 	        for( k = 0; k < color.sampleSize(); ++k) {
 	        	LCD.drawString("val[" + k + "]" + color_value[now_color][k], 3, 1 + k);
 	        }
       }


       if (Button.LEFT.isDown()) {
			LCD.clear();
			--now_color;
			if (now_color < 0) {
				now_color = 4;
			}
 			LCD.drawString(colors[now_color], 0, 0);
 			for( k = 0; k < color.sampleSize(); ++k) {
 				LCD.drawString("val[" + k + "]" + color_value[now_color][k], 3, 1 + k);
 			}
		}


		if (Button.ENTER.isDown()) {
 			LCD.clear();
 			color.fetchSample(color_value[now_color], 0);
 			color_value[now_color][0] = (float)((int)(color_value[now_color][0] * 100.0F));
 			color_value[now_color][1] = (float)((int)(color_value[now_color][1] * 100.0F));
 			color_value[now_color][2] = (float)((int)(color_value[now_color][2] * 100.0F));
 			LCD.drawString(colors[now_color], 0, 0);
 			for(k= 0; k < color.sampleSize(); ++k) {
 				LCD.drawString("val[" + k + "]" + color_value[now_color][k], 3, 1 + k);
 		    }
		}
   }
}

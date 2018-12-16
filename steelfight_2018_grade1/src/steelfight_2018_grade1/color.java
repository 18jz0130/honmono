package steelfight_2018_grade1;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class color {
	static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
 	static SensorMode color = colorSensor.getMode(2);
 	float color_value[][] = new float[6][color.sampleSize()];// 追加
    static float[] Value = new float[color.sampleSize()];
 	static int now_color = 0;
 	static int k;
 	public int get_color() {
	    String[] colors = new String[]{"white   ", "yellow  ", "green   ", "blue    ", "red     ", "エラー"};

	 	int[] gosa = new int[5];
	 	int min = 1000;

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
 	 }


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

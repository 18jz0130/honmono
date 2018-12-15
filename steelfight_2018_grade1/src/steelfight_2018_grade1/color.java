package steelfight_2018_grade1;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class color {
	static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
 	static SensorMode color = colorSensor.getMode(2);
 	static float color_value[][] = new float[6][color.sampleSize()];// 追加
    static float[] Value = new float[color.sampleSize()];
 	static int now_color = 0;

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
		 	}
		 }

	    LCD.drawString(colors[now_color], 0, 7);
	    Delay.msDelay(10L);
 		return now_color;
 	 }


 	public void colors_init() {
 		/*  94 */       String[] colors = new String[]{"white", "yellow", "green", "blue", "red"};
 		/*     */
 		/* 103 */       if (Button.RIGHT.isDown()) {
 		/* 104 */          ++now_color;
 		/* 105 */          if (now_color > 4) {
 		/* 106 */             now_color = 0;
 		/*     */          }      }
 		/*     */
 		/* 109 */       if (Button.LEFT.isDown()) {
 		/* 110 */          --now_color;
 		/* 111 */          if (now_color < 0) {
 		/* 112 */             now_color = 4;
 		/*     */          }      }
 		/*     */
 		/* 115 */       if (Button.ENTER.isDown()) {
 		/* 116 */          color.fetchSample(color_value[now_color], 0);
 		/* 117 */          color_value[now_color][0] = (float)((int)(color_value[now_color][0] * 100.0F));
 		/* 118 */          color_value[now_color][1] = (float)((int)(color_value[now_color][1] * 100.0F));
 		/* 119 */          color_value[now_color][2] = (float)((int)(color_value[now_color][2] * 100.0F));
 		/*     */       }
 		/*     */
 		/* 122 */       LCD.clear();
 		/* 123 */       LCD.drawString(colors[now_color], 0, 0);
 		/* 124 */       for(int k = 0; k < color.sampleSize(); ++k) {
 		/* 125 */          LCD.drawString("val[" + k + "]" + color_value[now_color][k], 3, 1 + k);
 		/*     */       }
 		/* 127 */       Delay.msDelay(50L);
 		/* 128 */    }
}

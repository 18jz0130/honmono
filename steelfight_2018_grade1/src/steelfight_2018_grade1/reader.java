package steelfight_2018_grade1;

public class reader {

	private static final int story_color = 0;
	private static final int story_gyro = 1;
	private static final int story_sonic = 2;
	private static final int story_button = 3;
	private static final int story_ran_metor = 4;

	static final int white = 0;
	static final int yellow = 1;
	static final int green = 2;
	static final int blue = 3;
	static final int red = 4;


	public boolean story(int trans_type, int trans_sample) {
		boolean next = false;
		if(trans_type == trans_sample) {
			next = true;
		}
		return next;
    }public boolean story(boolean trans_type, boolean trans_sample) {
		boolean next = false;
		if(trans_type == trans_sample) {
			next = true;
		}
		return next;
    }
	public boolean story(int trans_type, int trans_sample, boolean is_big) {
		boolean next = false;
    	if(is_big == true) {
    		if(trans_type > trans_sample) {
    			next = true;
    		}
    	}else{
    		if(trans_type < trans_sample) {
    			next = true;
    		}
    	}
		return next;
    }
}
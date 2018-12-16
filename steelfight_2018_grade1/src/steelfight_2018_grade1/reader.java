package steelfight_2018_grade1;

public class reader {

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

	public boolean story(boolean trans_type, boolean trans_sample) {
		boolean next = false;
		if(trans_type == trans_sample) {
			next = true;
		}
		return next;
    }
}
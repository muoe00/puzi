package com.puzi.puzi.utils;

import android.widget.ImageView;
import com.puzi.puzi.R;

/**
 * Created by JangwonPark on 2017. 10. 3..
 */

public class UIUtils {

	public static void setEvaluateStarScoreImageNone(ImageView star1, ImageView star2, ImageView star3, ImageView star4,
		ImageView star5) {
		star1.setImageResource(R.drawable.oval_4_copy_2);
		star2.setImageResource(R.drawable.oval_4_copy_2);
		star3.setImageResource(R.drawable.oval_4_copy_2);
		star4.setImageResource(R.drawable.oval_4_copy_2);
		star5.setImageResource(R.drawable.oval_4_copy_2);
	}

	public static void setEvaluateStarScoreImage(int score, ImageView star1, ImageView star2, ImageView star3, ImageView star4,
		ImageView star5) {
		if(score <= 1) {
			star1.setImageResource(R.drawable.oval_4);
			star2.setImageResource(R.drawable.oval_4_copy_2);
			star3.setImageResource(R.drawable.oval_4_copy_2);
			star4.setImageResource(R.drawable.oval_4_copy_2);
			star5.setImageResource(R.drawable.oval_4_copy_2);
		} else if(score == 2) {
			star1.setImageResource(R.drawable.oval_4);
			star2.setImageResource(R.drawable.oval_4);
			star3.setImageResource(R.drawable.oval_4_copy_2);
			star4.setImageResource(R.drawable.oval_4_copy_2);
			star5.setImageResource(R.drawable.oval_4_copy_2);
		} else if(score == 3) {
			star1.setImageResource(R.drawable.oval_4);
			star2.setImageResource(R.drawable.oval_4);
			star3.setImageResource(R.drawable.oval_4);
			star4.setImageResource(R.drawable.oval_4_copy_2);
			star5.setImageResource(R.drawable.oval_4_copy_2);
		} else if(score == 4) {
			star1.setImageResource(R.drawable.oval_4);
			star2.setImageResource(R.drawable.oval_4);
			star3.setImageResource(R.drawable.oval_4);
			star4.setImageResource(R.drawable.oval_4);
			star5.setImageResource(R.drawable.oval_4_copy_2);
		} else {
			star1.setImageResource(R.drawable.oval_4);
			star2.setImageResource(R.drawable.oval_4);
			star3.setImageResource(R.drawable.oval_4);
			star4.setImageResource(R.drawable.oval_4);
			star5.setImageResource(R.drawable.oval_4);
		}
	}

}

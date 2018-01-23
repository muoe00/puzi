package com.puzi.puzi.utils;

import android.app.Activity;
import android.os.Build;
import android.widget.ImageView;
import com.puzi.puzi.R;

/**
 * Created by JangwonPark on 2017. 10. 3..
 */

public class UIUtils {

	public static void setStatusBarColor(Activity activity) {
		if (Build.VERSION.SDK_INT >= 21) {
			activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
		}
	}

	public static void setEvaluateStarScoreImageNone(ImageView star1, ImageView star2, ImageView star3, ImageView star4,
		ImageView star5) {
		star1.setImageResource(R.drawable.oval_4_copy_2);
		star2.setImageResource(R.drawable.oval_4_copy_2);
		star3.setImageResource(R.drawable.oval_4_copy_2);
		star4.setImageResource(R.drawable.oval_4_copy_2);
		star5.setImageResource(R.drawable.oval_4_copy_2);
	}

	public static void setEvaluateStarScoreImage(int score, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5) {
		setEvaluateStarScoreImage(score, star1, star2, star3, star4, star5, R.drawable.oval_4, R.drawable.oval_4_copy_2);
	}

	public static void setEvaluateStarScoreImage(int score, ImageView star1, ImageView star2, ImageView star3, ImageView star4,
		ImageView star5, int onResource, int offResource) {
		if(score <= 0) {
			star1.setImageResource(offResource);
			star2.setImageResource(offResource);
			star3.setImageResource(offResource);
			star4.setImageResource(offResource);
			star5.setImageResource(offResource);
		}else if(score == 1) {
			star1.setImageResource(onResource);
			star2.setImageResource(offResource);
			star3.setImageResource(offResource);
			star4.setImageResource(offResource);
			star5.setImageResource(offResource);
		} else if(score == 2) {
			star1.setImageResource(onResource);
			star2.setImageResource(onResource);
			star3.setImageResource(offResource);
			star4.setImageResource(offResource);
			star5.setImageResource(offResource);
		} else if(score == 3) {
			star1.setImageResource(onResource);
			star2.setImageResource(onResource);
			star3.setImageResource(onResource);
			star4.setImageResource(offResource);
			star5.setImageResource(offResource);
		} else if(score == 4) {
			star1.setImageResource(onResource);
			star2.setImageResource(onResource);
			star3.setImageResource(onResource);
			star4.setImageResource(onResource);
			star5.setImageResource(offResource);
		} else {
			star1.setImageResource(onResource);
			star2.setImageResource(onResource);
			star3.setImageResource(onResource);
			star4.setImageResource(onResource);
			star5.setImageResource(onResource);
		}
	}

}

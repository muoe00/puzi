package kr.puzi.puzi.ui.fcm;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by JangwonPark on 2018. 1. 19..
 */

public class ScreenOnTopView {

	public static View createTopView(Activity activity, int imageResource, String title, String comment, final Listener listener) {
		final View v = activity.getLayoutInflater().inflate(kr.puzi.puzi.R.layout.view_screen_on_alert, null);

		ImageView ivTop = (ImageView) v.findViewById(kr.puzi.puzi.R.id.iv_top);
		ivTop.setImageResource(imageResource);

		TextView tvTopTitle = (TextView) v.findViewById(kr.puzi.puzi.R.id.tv_top_title);
		tvTopTitle.setText(title);

		TextView tvTopComment = (TextView) v.findViewById(kr.puzi.puzi.R.id.tv_top_comment);
		tvTopComment.setText(comment);

		Button btnTop = (Button) v.findViewById(kr.puzi.puzi.R.id.btn_top);
		btnTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onClick(v);
			}
		});

		return v;
	}

	public interface Listener {
		void onClick(View v);
	}

}

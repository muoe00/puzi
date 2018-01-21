package com.puzi.puzi.ui.customview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by JangwonPark on 2017. 11. 6..
 */
@SuppressLint("AppCompatCustomView")
public class RobotoTextView extends TextView {

	public RobotoTextView(Context context) {
		super(context);
		setType(context);
	}

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setType(context);
	}

	public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setType(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		setType(context);
	}

	private void setType(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.otf"));
	}
}

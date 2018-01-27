package kr.puzi.puzi.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import kr.puzi.puzi.R;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class CustomViewPager extends ViewPager {

	private int childId = R.id.hlv_channel_filter;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (childId > 0) {
			View scroll = findViewById(childId);
			if (scroll != null) {
				Rect rect = new Rect();
				scroll.getHitRect(rect);
				if (rect.contains((int) event.getX(), (int) event.getY())) {
					return false;
				}
			}
		}
		return super.onInterceptTouchEvent(event);
	}
}

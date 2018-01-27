package kr.puzi.puzi.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 당기면 상단의 이미지 등이 작아지는 효과를 가진 뷰
 * Created by JangwonPark on 2017. 10. 9..
 */
public class PullToMoveListView extends ListView implements View.OnTouchListener {
	private int max = 300;

	public PullToMoveListView(Context context) {
		super(context);
		init();
	}

	public PullToMoveListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToMoveListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * Pixel단위, 해당 픽셀동안 이동함
	 * @param max
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * 이동하는동안 Listener
	 * @param onMoveListener
	 */
	public void setOnMoveListener(OnMoveListener onMoveListener) {
		this.onMoveListener = onMoveListener;
	}

	private void init(){
		this.setOnTouchListener(this);
	}

	private OnMoveListener onMoveListener;

	private int nowTouchY = 0;

	public void delegateTouch(View v, MotionEvent e) {
		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;

			case MotionEvent.ACTION_MOVE:
//				nowTouchY = super.getChildAt(0).getTop();
//				Log.d("TEST", "+++ count : " + nowTouchY);
				if(super.getFirstVisiblePosition() == 0){
//					onMoveListener.OnAction(count);
				}
				break;

			case MotionEvent.ACTION_UP:
				nowTouchY = super.getChildAt(0).getTop();
//				Log.d("TEST", "+++ count : " + nowTouchY);
				break;
		}

	}

	public interface OnMoveListener {
		public abstract void OnAction(int count);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		delegateTouch(v, event);
		return false;
	}
}

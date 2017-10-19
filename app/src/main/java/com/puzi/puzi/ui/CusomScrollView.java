package com.puzi.puzi.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import lombok.Setter;

/**
 * Created by JangwonPark on 2017. 10. 14..
 */

public class CusomScrollView extends ScrollView {

	public static final int SCROLL_UP = 0;
	public static final int SCROLL_DOWN = 1;

	/// 스크롤 맨 아래까지 되면 처리할 이벤트 전달용 핸들
	@Setter
	OnEndedScrolledListener onEndedScrolledListener = null;

	@Setter
	OnScrollListener onScrollListener = null;
	/// 스크롤뷰 영역 체크하려고 두는 변수에용
	Rect m_rect ;

	public CusomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/// 그리기가 끝나면 체크하기 위해 오버라이드
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		checkIsLocatedAtFooter( ) ; /// 여기서 그리기 끝나면 함수 콜
	}

	private void checkIsLocatedAtFooter() {
		if( m_rect == null ) {
			m_rect = new Rect( ) ;    /// new합니다.
			getLocalVisibleRect( m_rect ) ;  /// 스크롤 영역 구합니다.(저는 0,480,0,696 이던가 했네요)
			return ;       /// 그리고 걍 리턴합니다.
		}
		int oldBottom = m_rect.bottom;   /// 이전 bottom저장 이유는 맨아래인 상태에서 아래로 스크롤 했을떄 쌩까려구요

		getLocalVisibleRect( m_rect ) ;   /// 현재 스크롤뷰의 영역을 구합니다.
		/// 이때 스크롤 이동시켰으면 top와 bottom값이 이동한 만큼 변합니다.

		int height = getMeasuredHeight( ) ;  /// 스크롤 뷰의 높이를 구합니다.

		View v = getChildAt( 0 ) ;    /// 스크롤 뷰 안에 들어있는 내용물의 높이도 구합니다.

		if (oldBottom > 0 && height > 0)   /// 스크롤 뷰나 이전 bottom이 0 이상이어야만 처리
		{
			/// bottom값의 변화가 없으면 처리 안해요
			/// 그리고 현재 bottom이 내용물의 맨 아래까지 왔으면 맨 아래까지 스크롤 한겁니다.
			if (oldBottom != m_rect.bottom && m_rect.bottom == v.getMeasuredHeight( ) )
			{
				// 끝에 왔을 때의 처리
				Log.d("ghlab", "끝에 왔을 때의 처리");

				/// 핸들러가 처음에는 널인데 사용자가 셋팅해주면 그 핸들러로 메세지 날립니다.
				if( onEndedScrolledListener != null ) {
					/// 핸들러에 이벤트 날리면 끗납니다.
					onEndedScrolledListener.onEnded() ;
				}
			}
		}
	}

	@Override
	protected void onScrollChanged (int l, int t, int oldl, int oldt) {
		int direction = (oldt > t)?SCROLL_DOWN:SCROLL_UP;

		if(onScrollListener != null)
			Log.d("TAG", "direction : " + direction + " / t : " + t);
			onScrollListener.onScroll(direction, t);
	}

	public interface OnEndedScrolledListener {
		void onEnded();
	}

	public interface OnScrollListener {
		void onScroll(int direction, float scrollY);
	}

}

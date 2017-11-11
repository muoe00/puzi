package com.puzi.puzi.ui.channel.editorspage;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class EditorsPageActivity extends BaseActivity {

	private Unbinder unbinder;

	@BindView(R.id.tv_channel_editors_page_title)
	TextView tvTitle;
	@BindView(R.id.wv_channel_editors_page_container)
	WebView wvContainer;
	@BindView(R.id.ll_channel_editors_page_top_container)
	LinearLayout llTopContainer;
	@BindView(R.id.ll_channel_editors_page_bottom_container)
	LinearLayout llBottomContainer;

	private ChannelEditorsPageVO channelEditorsPageVO;
	private boolean pageError = false;
	private int count = 0;
	private boolean start = false;

	private int layoutHeight;
	private int layoutHeight2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editors_page_webview);

		unbinder = ButterKnife.bind(this);

		channelEditorsPageVO = (ChannelEditorsPageVO) getIntent().getSerializableExtra("channelEditorsPageVO");

		initChannelEditorsPage();
	}

	private void initChannelEditorsPage() {
		tvTitle.setText("불러오는 중입니다..");
		wvContainer.getSettings().setJavaScriptEnabled(true);
		if(!channelEditorsPageVO.getLink().startsWith("http")) {
			channelEditorsPageVO.setLink("http://" + channelEditorsPageVO.getLink());
		}
		wvContainer.loadUrl(channelEditorsPageVO.getLink());
		wvContainer.setWebViewClient(new WebViewClient(){

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				super.onReceivedError(view, request, error);
				tvTitle.setText("불러오기를 실패하였습니다.");
				pageError = true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(!pageError) {
					tvTitle.setText(channelEditorsPageVO.getTitle());
				}
			}
		});

		layoutHeight = llTopContainer.getLayoutParams().height;
		layoutHeight2 = llBottomContainer.getLayoutParams().height;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			wvContainer.setOnScrollChangeListener(new View.OnScrollChangeListener() {

				@Override
				public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
					if(scrollY <= 10) {
						showBar();
						return;
					}

					if(start) {
						int gap = oldScrollY - scrollY;
						count += (gap >= 0) ? 3 : -3;

						if(llTopContainer.getLayoutParams().height > layoutHeight || llTopContainer.getLayoutParams().height < 0) {
							start = false;
							return;
						}

						ViewGroup.LayoutParams params = llTopContainer.getLayoutParams();
						int willHeight = params.height + (count*3);
						if(willHeight >= layoutHeight) {
							showTopBar();
						} else if(willHeight > 0) {
							params.height = willHeight;
							llTopContainer.setLayoutParams(params);
						} else {
							hideTopBar();
						}
						ViewGroup.LayoutParams params2 = llBottomContainer.getLayoutParams();
						int willHeight2 = params2.height + (count*3);
						if(willHeight2 >= layoutHeight2) {
							showBottomBar();
						} else if(willHeight2 > 0) {
							params2.height = willHeight2;
							llBottomContainer.setLayoutParams(params2);
						} else {
							hideBottomBar();
						}
					}
				}
			});

			wvContainer.setOnTouchListener(new View.OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							start = true;
							count = 0;
							Log.d("TAG", "+++ ACTION_DOWN");

							break;
						case MotionEvent.ACTION_UP:
							Log.d("TAG", "+++ ACTION_UP");
							if(start) {
								start = false;
								if(alreadySetShowOrHide()) {
									break;
								}

								if(count >= 0) {
									showBar();
								} else {
									hideBar();
								}
							}
							count = 0;
							break;
					}
					return false;
				}
			});
		}
	}

	private boolean alreadySetShowOrHide() {
		return (llTopContainer.getLayoutParams().height == 0 || llTopContainer.getLayoutParams().height == layoutHeight) &&
				(llBottomContainer.getLayoutParams().height == 0 || llBottomContainer.getLayoutParams().height == layoutHeight2);
	}

	private void showBar() {
		showTopBar();
		showBottomBar();
	}

	private void showTopBar() {
		ViewGroup.LayoutParams params = llTopContainer.getLayoutParams();
		params.height = layoutHeight;
		llTopContainer.setLayoutParams(params);
	}

	private void showBottomBar() {
		ViewGroup.LayoutParams params2 = llBottomContainer.getLayoutParams();
		params2.height = layoutHeight2;
		llBottomContainer.setLayoutParams(params2);
	}

	private void hideBar() {
		hideTopBar();
		hideBottomBar();
	}

	private void hideTopBar() {
		ViewGroup.LayoutParams params = llTopContainer.getLayoutParams();
		params.height = 0;
		llTopContainer.setLayoutParams(params);
	}

	private void hideBottomBar() {
		ViewGroup.LayoutParams params2 = llBottomContainer.getLayoutParams();
		params2.height = 0;
		llBottomContainer.setLayoutParams(params2);
	}

	@OnClick(R.id.ibtn_channel_editors_page_web_back)
	public void webBackPress() {
		if(wvContainer.canGoBack()) {
			wvContainer.goBack();
		}
	}

	@OnClick(R.id.ibtn_channel_editors_page_web_forward)
	public void webForwardPress() {
		if(wvContainer.canGoForward()){
			wvContainer.goForward();
		}
	}

	@OnClick(R.id.ibtn_channel_editors_page_web_refresh)
	public void webRefreshPress() {
		wvContainer.reload();
	}

	@OnClick(R.id.ibtn_channel_editors_page_back)
	public void backPress() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

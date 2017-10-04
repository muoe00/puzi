package com.puzi.puzi.ui.channel.editorspage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
	@BindView(R.id.fl_channel_editors_page_container)
	FrameLayout flContainer;
	@BindView(R.id.pb_channel_editors_page_progress)
	ProgressBar pbProgress;
	@BindView(R.id.tv_channel_editors_page_message_1)
	TextView tvMessage1;
	@BindView(R.id.tv_channel_editors_page_message_2)
	TextView tvMessage2;

	private ChannelEditorsPageVO channelEditorsPageVO;
	private boolean pageError = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editors_page_webview);

		unbinder = ButterKnife.bind(this);

		channelEditorsPageVO = (ChannelEditorsPageVO) getIntent().getSerializableExtra("channelEditorsPageVO");

		initChannelEditorsPage();
	}

	private void initChannelEditorsPage() {
		tvTitle.setText(channelEditorsPageVO.getTitle());
		wvContainer.getSettings().setJavaScriptEnabled(true);
		if(!channelEditorsPageVO.getLink().startsWith("http")) {
			channelEditorsPageVO.setLink("http://" + channelEditorsPageVO.getLink());
		}
		wvContainer.loadUrl(channelEditorsPageVO.getLink());
		wvContainer.setWebViewClient(new WebViewClient(){

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				super.onReceivedError(view, request, error);
				pbProgress.setVisibility(View.GONE);
				tvMessage2.setVisibility(View.GONE);
				tvMessage1.setText("죄송합니다. 불러오기가 실패했습니다.");
				flContainer.setVisibility(View.VISIBLE);
				pageError = true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(!pageError) {
					flContainer.setVisibility(View.GONE);
				}
			}
		});
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

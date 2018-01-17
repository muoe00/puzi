package com.puzi.puzi.ui.user;

import android.os.Bundle;
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
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
public class EventWebViewActivity extends BaseActivity {

	private Unbinder unbinder;

	@BindView(R.id.tv_event_page_title)
	TextView tvTitle;
	@BindView(R.id.wv_event_page_container)
	WebView wvContainer;
	@BindView(R.id.ll_event_page_top_container)
	LinearLayout llTopContainer;

	private boolean pageError = false;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_page_webview);

		unbinder = ButterKnife.bind(this);

		url = getIntent().getStringExtra("url");

		initComponent();
	}

	private void initComponent() {
		tvTitle.setText("불러오는 중입니다..");
		wvContainer.getSettings().setJavaScriptEnabled(true);
		if(!url.startsWith("http")) {
			url = "http://" + url;
		}
		Map<String, String> extraHeaders = new HashMap<String, String>();
		extraHeaders.put("token", Preference.getProperty(getActivity(), "token"));

		wvContainer.loadUrl(url, extraHeaders);
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
					tvTitle.setText("푸지-카카오톡 이모티콘 증정 이벤트");
				}
			}
		});

	}

	@OnClick(R.id.ibtn_event_page_back)
	public void backPress() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

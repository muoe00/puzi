package kr.puzi.puzi.ui.user;

import android.os.Bundle;
import android.webkit.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
public class EventWebViewActivity extends BaseFragmentActivity {

	private Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_event_page_title)
	TextView tvTitle;
	@BindView(kr.puzi.puzi.R.id.wv_event_page_container)
	WebView wvContainer;
	@BindView(kr.puzi.puzi.R.id.ll_event_page_top_container)
	LinearLayout llTopContainer;

	private boolean pageError = false;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_event_page_webview);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llTopContainer;

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

		wvContainer.getSettings().setJavaScriptEnabled(true);
		wvContainer.setWebChromeClient(new WebChromeClient());
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
					tvTitle.setText("카카오톡 이모티콘 증정 이벤트");
				}
			}
		});

	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_event_page_back)
	public void backPress() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

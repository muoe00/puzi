package com.puzi.puzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by muoe0 on 2017-07-30.
 */

public class WebActivity extends Activity implements View.OnClickListener{

	private EditText editUrl;
	private ProgressBar progress;
	private Intent intent;
	private Button btnHome, btnChannel, btnDialogCancel, btnDialogOk;
	private WebView webView;
	private WebSettings webSettings;
	private String url;
	private LinearLayout llDialog;
	private Boolean bool = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_web);
		initComponents();

		intent = getIntent();
		url = intent.getStringExtra("url");

		webView.setWebViewClient(new WebViewClient());
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(url);
		editUrl.setText(url);
		llDialog.setVisibility(View.GONE);

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				// 실행이 끝난후 확인 가능
				Bundle bd = msg.getData();
				if(bd.getBoolean("END_AUTH")){
					// 메시지를 받고 처리할 부분
				}
			}
		};

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				bool = true;
				transAnimation(bool);
				progress.setVisibility(View.GONE);

				btnDialogCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						llDialog.setVisibility(View.GONE);
					}
				});

				btnDialogOk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						llDialog.setVisibility(View.GONE);
					}
				});

				// 실행할 동작 코딩
				Message message = mHandler.obtainMessage();
				//사용할 핸들러를 이용해서 보낼 메시지 객체 생성
				Bundle bundle = new Bundle();
				//메시지를 담을 번들 생성
				bundle.putBoolean("END_AUTH", true);
				//번들에 메시지 추가
				message.setData(bundle);
				//메세지에 번들을 넣는다.
				mHandler.sendMessage(message);
				//메세지를 핸들러로 넘긴다.
			}
		}, 6000);
	}

	private void transAnimation(boolean bool){
		AnimationSet aniInSet = new AnimationSet(true);
		AnimationSet aniOutSet = new AnimationSet(true);
		aniInSet.setInterpolator(new AccelerateInterpolator());
		Animation transInAni = new TranslateAnimation(0,0,200.0f,0);
		Animation transOutAni = new TranslateAnimation(0,0,0,200.0f);
		transInAni.setDuration(300);
		transOutAni.setDuration(300);
		aniInSet.addAnimation(transInAni);
		aniOutSet.addAnimation(transOutAni);
		if (bool) {
			llDialog.setAnimation(aniInSet);
			llDialog.setVisibility(View.VISIBLE);
		} else {
			llDialog.setAnimation(aniOutSet);
			llDialog.setVisibility(View.GONE);
		}
	}

	private void initComponents() {
		webView = (WebView) findViewById(R.id.web_ad);
		btnHome = (Button) findViewById(R.id.btn_back_web);
		btnChannel = (Button) findViewById(R.id.btn_channel_web);
		btnDialogCancel = (Button) findViewById(R.id.btn_web_cancel);
		btnDialogOk = (Button) findViewById(R.id.btn_web_ok);
		llDialog = (LinearLayout) findViewById(R.id.ll_web_dialog);
		progress = (ProgressBar) findViewById(R.id.progress_circle);
		editUrl = (EditText) findViewById(R.id.editText_url);

		btnHome.setOnClickListener(this);
		btnChannel.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {

		webView.goBack();
	}

	@Override
	public void onClick(View v) {

		Intent intent = null;

		switch (v.getId()) {
			case R.id.btn_back_web:
				intent = new Intent(WebActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;

			case R.id.btn_channel_web:

				break;

		}


	}
}

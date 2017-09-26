package com.puzi.puzi.ui.advertisement;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.MainActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by muoe0 on 2017-07-30.
 */

public class AdvertisementDetailActivity extends Activity {

	private Unbinder unbinder;
	private int seconds, count;
	private Boolean isTime = false;
	private ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();

	@BindView(R.id.ll_web_dialog) public LinearLayout llDialog;
	@BindView(R.id.btn_back_web) public Button btnHome;
	@BindView(R.id.btn_channel_web) public Button btnChannel;
	@BindView(R.id.editText_url) public EditText editUrl;
	@BindView(R.id.progressbar) public ProgressBar progressBar;

	private Intent intent;
	private Button btnDialogCancel, btnDialogOk;
	private WebView webView;
	private WebSettings webSettings;
	private String url;
	private Boolean bool = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_web);

		unbinder = ButterKnife.bind(this);

		initComponents();

		intent = getIntent();
		url = intent.getStringExtra("url");

		webView.setWebViewClient(new WebViewClient());
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(url);
		editUrl.setText(url);

		progressBar.setIndeterminate(true);
		progressBar.setMax(500);
		progressBar.setVisibility(View.GONE);
		llDialog.setVisibility(View.GONE);

		DialogAsync dialogAsync = new DialogAsync();
		dialogAsync.execute();

	}

	class DialogAsync extends AsyncTask<Void, Integer, Void> {

		private boolean isCanceled = false;

		@Override
		protected void onPreExecute() {
			isCanceled = false;
			progressBar.setMax(50);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			for(int i = 1 ; i <= 50 && ! isCanceled ; i++) {
				try {
					publishProgress(i);
					Thread.sleep(100);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			isCanceled = true;
			progressBar.setVisibility(View.GONE);

			bool = true;
			transAnimation(bool);
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
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressBar.setProgress(progress[0].intValue());
		}

		@Override
		protected void onCancelled() {
			isCanceled = true;
		}
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
	}

	@OnClick(R.id.btn_back_web)
	public void back() {
		Intent intent = new Intent(AdvertisementDetailActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.btn_channel_web)
	public void changedChannel() {

	}

	@Override
	public void onBackPressed() {
		if(webView.canGoBack()) {
			webView.goBack();
		}
	}
}

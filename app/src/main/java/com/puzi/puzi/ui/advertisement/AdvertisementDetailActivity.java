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
import com.puzi.puzi.ui.channel.ChannelDetailActivity;

/**
 * Created by muoe0 on 2017-07-30.
 */

public class AdvertisementDetailActivity extends Activity {

	Unbinder unbinder;

	@BindView(R.id.ll_web_dialog) public LinearLayout llDialog;
	@BindView(R.id.btn_back_web) public Button btnHome;
	@BindView(R.id.btn_channel_web) public Button btnChannel;
	@BindView(R.id.editText_url) public EditText editUrl;
	@BindView(R.id.progressbar) public ProgressBar progressBar;
	@BindView(R.id.web_ad) public WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_advertisement_detail);

		unbinder = ButterKnife.bind(this);

		Intent intent = getIntent();
		String url = intent.getStringExtra("url");

		webView.setWebViewClient(new WebViewClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(url);
		editUrl.setText(url);

		progressBar.setIndeterminate(true);
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

			transAnimation(true);
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

	@OnClick(R.id.btn_web_ok)
	public void returnDialogOK() {
		llDialog.setVisibility(View.GONE);
	}

	@OnClick(R.id.btn_web_cancel)
	public void returnDialogCancel() {
		llDialog.setVisibility(View.GONE);
	}

	@OnClick(R.id.btn_back_web)
	public void back() {
		Intent intent = new Intent(AdvertisementDetailActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick(R.id.btn_channel_web)
	public void changedChannel() {
		Intent intent = new Intent(AdvertisementDetailActivity.this, ChannelDetailActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if(webView.canGoBack()) {
			webView.goBack();
		}
	}
}

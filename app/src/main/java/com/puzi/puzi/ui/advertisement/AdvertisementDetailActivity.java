package com.puzi.puzi.ui.advertisement;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.channel.ChannelDetailActivity;
import com.puzi.puzi.ui.company.CompanyActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by muoe0 on 2017-07-30.
 */

public class AdvertisementDetailActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.iv_companyPicture) public SelectableRoundedImageView companyPicture;
	@BindView(R.id.tv_companyId) public TextView companyName;
	@BindView(R.id.ll_web_dialog) public LinearLayout llDialog;
	@BindView(R.id.tv_ad_state) public TextView tvState;
	@BindView(R.id.progressbar) public ProgressBar progressBar;
	@BindView(R.id.web_ad) public WebView webView;
	@BindView(R.id.tv_ad_quiz) public TextView tvQuiz;
	@BindView(R.id.tv_ad_answer_first) public TextView firstAnswer;
	@BindView(R.id.tv_ad_answer_second) public TextView secondAnswer;

	private String url, answerOne, answerTwo;
	private long startTime;
	private int touchCount, channelId;
	private boolean isChanged = false;
	private ReceivedAdvertiseVO receivedAdvertise;
	private ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_advertisement_detail);

		unbinder = ButterKnife.bind(this);
		startTime = System.currentTimeMillis();

		receivedAdvertise = (ReceivedAdvertiseVO) getIntent().getExtras().getSerializable("advertise");

		Log.i(PuziUtils.INFO, "detail.getSaved() : " + receivedAdvertise.getSaved());
		Log.i(PuziUtils.INFO, "detail.getToday() : " + receivedAdvertise.getToday());

		progressBar.setVisibility(View.GONE);
		llDialog.setVisibility(View.GONE);

		if(!receivedAdvertise.getSaved() && receivedAdvertise.getToday()) {
			String quiz = receivedAdvertise.getQuiz();
			answerOne = receivedAdvertise.getAnswerOne();
			answerTwo = receivedAdvertise.getAnswerTwo();

			tvQuiz.setText(quiz);
			firstAnswer.setText(answerOne);
			secondAnswer.setText(answerTwo);

			DialogAsync dialogAsync = new DialogAsync();
			dialogAsync.execute();

		} else if(receivedAdvertise.getSaved()) {
			tvState.setText("이미 적립된 광고입니다.");
		} else {
			tvState.setText("적립 대상이 아닙니다.");
		}

		channelId = receivedAdvertise.getChannelId();
		url = receivedAdvertise.getLink();
		BitmapUIL.load(receivedAdvertise.getCompanyInfoDTO().getPictureUrl(), companyPicture);
		companyName.setText(receivedAdvertise.getCompanyInfoDTO().getCompanyAlias());

		webView.setWebViewClient(new WebViewClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(url);

		executors.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(!webView.getUrl().equals(url)) {
							isChanged = true;
						}
					}
				});
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			touchCount++;
		}

		return super.onTouchEvent(event);
	}

	class DialogAsync extends AsyncTask<Integer, Integer, Integer> {

		private boolean isCanceled = false;

		@Override
		protected void onPreExecute() {
			isCanceled = false;
			progressBar.setProgress(0);
			progressBar.setMax(50);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
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
		protected void onPostExecute(Integer params) {
			isCanceled = true;
			progressBar.setVisibility(View.GONE);

			transAnimation(true);
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			Log.i(PuziUtils.INFO, "progress i : " + progress[0]);
			progressBar.setProgress(progress[0]);
			Log.i(PuziUtils.INFO, "progressbar : " + progressBar.getProgress());
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onCancelled() {
			progressBar.setProgress(0);
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

	@OnClick({R.id.tv_ad_answer_first, R.id.tv_ad_answer_second})
	public void returnAnswer(View view) {
		llDialog.setVisibility(View.GONE);

		String answer = null;

		switch (view.getId()) {
			case R.id.tv_ad_answer_first:
				answer = answerOne;
				break;
			case R.id.tv_ad_answer_second:
				answer = answerTwo;
				break;
		}

		AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);
		String token = Preference.getProperty(AdvertisementDetailActivity.this, "token");
		Call<ResponseVO> call = advertisementNetworkService.pointSave(token, receivedAdvertise.getCmpnId(), answer);
		call.enqueue(new CustomCallback<ResponseVO>(AdvertisementDetailActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				switch(responseVO.getResultType()){
					case SUCCESS:
						Toast.makeText(getBaseContext(), "적립되었습니다.", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent();
						intent.putExtra("advertiseIndex", receivedAdvertise.getReceivedAdvertiseId());
						intent.putExtra("pointSavedState", true);

						Log.i(PuziUtils.INFO, "advertiseIndex : " + receivedAdvertise.getReceivedAdvertiseId());

						setResult(Activity.RESULT_OK, intent);

						break;

					default:
						Log.i("INFO", "point history failed.");
						Toast.makeText(getBaseContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@OnClick(R.id.btn_back_web)
	public void back() {

		long endTime = startTime = System.currentTimeMillis();
		long stayTime = (long)((endTime - startTime)/1000.0);

		Log.i(PuziUtils.INFO, "startTime : " + startTime);
		Log.i(PuziUtils.INFO, "endTime : " + endTime);

		// TODO: Network (cmpnId, touchCount, stayTime, isChanged)
		Log.i(PuziUtils.INFO, "touchCount : " + touchCount + ", stayTime : " + stayTime + ", isChanged : " + isChanged);

		finish();
		doAnimationGoLeft();
	}

	@OnClick(R.id.btn_back_page)
	public void backPage() {
		if(webView.canGoBack()) {
			webView.goBack();
		} else {
			back();
		}
	}

	@OnClick(R.id.btn_channel_web)
	public void changedChannel() {
		Intent intent = new Intent(AdvertisementDetailActivity.this, ChannelDetailActivity.class);
		intent.putExtra("channelId", channelId);
		startActivity(intent);
	}

	@OnClick(R.id.ll_ad_company)
	public void changedCompony() {
		Intent intent = new Intent(AdvertisementDetailActivity.this, CompanyActivity.class);
		Bundle bundle = new Bundle();
		Log.i(PuziUtils.INFO, "receivedAdvertise.getCompanyInfoDTO() : " + receivedAdvertise.getCompanyInfoDTO());
		bundle.putSerializable("company", receivedAdvertise.getCompanyInfoDTO());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		backPage();
	}
}

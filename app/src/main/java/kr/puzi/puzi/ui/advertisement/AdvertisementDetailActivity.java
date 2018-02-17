package kr.puzi.puzi.ui.advertisement;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.joooonho.SelectableRoundedImageView;
import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.AdvertisementNetworkService;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.channel.ChannelDetailActivity;
import kr.puzi.puzi.ui.common.PointDialog;
import kr.puzi.puzi.ui.company.CompanyActivity;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static kr.puzi.puzi.ui.advertisement.AdvertisementFragment.updateSavedPoint;

/**
 * Created by muoe0 on 2017-07-30.
 */

public class AdvertisementDetailActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.iv_companyPicture) public SelectableRoundedImageView companyPicture;
	@BindView(kr.puzi.puzi.R.id.tv_companyId) public TextView companyName;
	@BindView(kr.puzi.puzi.R.id.ll_web_dialog) public LinearLayout llDialog;
	@BindView(kr.puzi.puzi.R.id.web_ad) public WebView webView;
	@BindView(kr.puzi.puzi.R.id.tv_ad_quiz) public TextView tvQuiz;
	@BindView(kr.puzi.puzi.R.id.tv_ad_answer_first) public TextView firstAnswer;
	@BindView(kr.puzi.puzi.R.id.tv_ad_answer_second) public TextView secondAnswer;

	@BindView(kr.puzi.puzi.R.id.tv_advertise_progress) public TextView tvProgress;
	@BindView(kr.puzi.puzi.R.id.tv_advertise_progress_2) public TextView tvProgress2;
	@BindView(kr.puzi.puzi.R.id.pg_advertise) public ProgressBar progressCircle;
	@BindView(kr.puzi.puzi.R.id.iv_advertise_state) public ImageView ivState;

	@BindView(kr.puzi.puzi.R.id.ll_advertise_top_container) LinearLayout llTopContainer;
	@BindView(kr.puzi.puzi.R.id.ll_advertise_bottom_container) LinearLayout llBottomContainer;

	private int layoutHeight;
	private int layoutHeight2;

	private int count = 0;
	private boolean start = false;

	private String url, answerOne, answerTwo;
	private long startTime;
	private int touchCount, channelId;
	private boolean isChanged = false;
	private ReceivedAdvertiseVO receivedAdvertise;
	private ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(kr.puzi.puzi.R.layout.activity_advertisement_detail);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llTopContainer;
		startTime = System.currentTimeMillis();

		layoutHeight = llTopContainer.getLayoutParams().height;
		layoutHeight2 = llBottomContainer.getLayoutParams().height;

		receivedAdvertise = (ReceivedAdvertiseVO) getIntent().getExtras().getSerializable("advertise");

		Log.i(PuziUtils.INFO, "detail.getSaved() : " + receivedAdvertise.isSaved());
		Log.i(PuziUtils.INFO, "detail.getToday() : " + receivedAdvertise.isToday());

		llDialog.setVisibility(View.GONE);

		if(!receivedAdvertise.isSaved() && receivedAdvertise.isToday()) {
			String quiz = receivedAdvertise.getQuiz();
			answerOne = receivedAdvertise.getAnswerOne();
			answerTwo = receivedAdvertise.getAnswerTwo();

			tvQuiz.setText(quiz);
			firstAnswer.setText(answerOne);
			secondAnswer.setText(answerTwo);

			DialogAsync dialogAsync = new DialogAsync();
			dialogAsync.execute();

		} else if(receivedAdvertise.isSaved()) {
			// tvState.setText("이미 적립된 광고입니다.");
			progressCircle.setVisibility(View.GONE);
			tvProgress.setVisibility(View.GONE);
			tvProgress2.setVisibility(View.GONE);
			ivState.setVisibility(View.VISIBLE);
			ivState.setImageResource(kr.puzi.puzi.R.drawable.old);
		} else {
			// tvState.setText("적립 대상이 아닙니다.");
			progressCircle.setVisibility(View.GONE);
			tvProgress.setVisibility(View.GONE);
			tvProgress2.setVisibility(View.GONE);
			ivState.setVisibility(View.VISIBLE);
			ivState.setImageResource(kr.puzi.puzi.R.drawable.wrong);
		}

		channelId = receivedAdvertise.getChannelId();
		url = receivedAdvertise.getLink();
		BitmapUIL.load(receivedAdvertise.getCompanyInfoDTO().getPictureUrl(), companyPicture);
		companyName.setText(receivedAdvertise.getCompanyInfoDTO().getCompanyAlias());

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("https://play.google.com/store/apps/details")) {
					String[] splited = url.split("=");
					if(splited.length != 2) {
						view.loadUrl(url);
						return true;
					}

					Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
					marketLaunch.setData(Uri.parse("market://details?id="+splited[1]));
					startActivity(marketLaunch);

					return true;
				} else {
					view.loadUrl(url);
					return true;
				}
			}
		});
		webView.loadUrl(url);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

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

			webView.setOnTouchListener(new View.OnTouchListener(){

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
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				progressCircle.setVisibility(View.VISIBLE);
				progressCircle.setProgress(0);
				progressCircle.setMax(500);
			} else {
				progressCircle.setVisibility(View.GONE);
			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			for(int i = 1 ; i <= 500 && ! isCanceled ; i++) {
				try {
					publishProgress(i);
					Thread.sleep(10);
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
			transAnimation(true);
			progressCircle.setVisibility(View.GONE);
			tvProgress.setVisibility(View.GONE);
			tvProgress2.setVisibility(View.GONE);
			ivState.setVisibility(View.VISIBLE);
			ivState.setImageResource(kr.puzi.puzi.R.drawable.old);
		}

		@Override
		protected void onProgressUpdate(final Integer... progress) {
			super.onProgressUpdate(progress);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				progressCircle.setProgress(progress[0]);
			}

			if(progress[0] == 1) {
				tvProgress.setText(5 + "");
			} else if(progress[0] == 101) {
				tvProgress.setText(4 + "");
			} else if(progress[0] == 201) {
				tvProgress.setText(3 + "");
			} else if(progress[0] == 301) {
				tvProgress.setText(2 + "");
			} else if(progress[0] == 401) {
				tvProgress.setText(1 + "");
			}
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

	@OnClick({kr.puzi.puzi.R.id.tv_ad_answer_first, kr.puzi.puzi.R.id.tv_ad_answer_second})
	public void returnAnswer(View view) {
		llDialog.setVisibility(View.GONE);

		final String answer = view.getId() == kr.puzi.puzi.R.id.tv_ad_answer_first ? answerOne : answerTwo;

		LazyRequestService service = new LazyRequestService(getActivity(), AdvertisementNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<AdvertisementNetworkService>() {
			@Override
			public Call<ResponseVO> execute(AdvertisementNetworkService advertisementNetworkService, String token) {
				return advertisementNetworkService.pointSave(token, receivedAdvertise.getReceivedAdvertiseId(), answer);
			}
		});
		service.enqueue(new CustomCallback(AdvertisementDetailActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				if(receivedAdvertise.isTest()) {
					Toast.makeText(getBaseContext(), "테스트 광고입니다", Toast.LENGTH_SHORT).show();
				} else {
					int savedPoint = responseVO.getInteger("savedPoint");

					PointDialog.load(getActivity(), savedPoint, true);

					// Toast.makeText(getBaseContext(), savedPoint + "원이 적립되었습니다.", Toast.LENGTH_SHORT).show();

					Intent intent = new Intent();
					intent.putExtra("advertiseIndex", receivedAdvertise.getReceivedAdvertiseId());
					intent.putExtra("pointSavedState", true);

					Log.i(PuziUtils.INFO, "advertiseIndex : " + receivedAdvertise.getReceivedAdvertiseId());

					updateSavedPoint(receivedAdvertise.getReceivedAdvertiseId());
				}
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_channel_web)
	public void changedChannel() {
		if(channelId != 0) {
			Intent intent = new Intent(AdvertisementDetailActivity.this, ChannelDetailActivity.class);
			intent.putExtra("channelId", channelId);
			startActivity(intent);
			doAnimationGoRight();
		} else {
			Toast.makeText(this, "해당하는 채널이 없습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(kr.puzi.puzi.R.id.ll_ad_company)
	public void changedCompony() {
		Intent intent = new Intent(AdvertisementDetailActivity.this, CompanyActivity.class);
		Bundle bundle = new Bundle();
		Log.i(PuziUtils.INFO, "receivedAdvertise.getCompanyInfoDTO() : " + receivedAdvertise.getCompanyInfoDTO());
		bundle.putSerializable("company", receivedAdvertise.getCompanyInfoDTO());
		intent.putExtras(bundle);
		startActivity(intent);
		doAnimationGoRight();
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

	@OnClick(kr.puzi.puzi.R.id.ibtn_advertise_web_back)
	public void webBackPress() {
		if(webView.canGoBack()) {
			webView.goBack();
		}
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_advertise_web_forward)
	public void webForwardPress() {
		if(webView.canGoForward()){
			webView.goForward();
		}
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_advertise_web_refresh)
	public void webRefreshPress() {
		webView.reload();
	}

	@OnClick(kr.puzi.puzi.R.id.btn_back_web)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}

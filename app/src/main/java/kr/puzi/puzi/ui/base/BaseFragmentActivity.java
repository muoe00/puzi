package kr.puzi.puzi.ui.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.fcm.PuziBroadcastReceiver;
import kr.puzi.puzi.fcm.PuziPushManager;
import kr.puzi.puzi.fcm.PuziPushMessageVO;
import kr.puzi.puzi.ui.advertisement.AdvertisementDetailActivity;
import kr.puzi.puzi.ui.fcm.ScreenOnTopView;

import java.util.ArrayList;
import java.util.List;

import static kr.puzi.puzi.fcm.PuziPushType.ADVERTISEMENT;

public class BaseFragmentActivity extends FragmentActivity {

	public static List<Activity> activities = new ArrayList<>();

	protected View targetViewForPush;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activities.remove(this);
	}

	public static void finishAll() {
		for(Activity activity : activities) {
			activity.finish();
		}
	}

	protected Animation outToTop() {
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.slide_out_top);
	}

	protected Animation inFromTop() {
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.slide_in_top);
	}

	protected Animation outStatusImg() {
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.image_out);
	}

	protected Animation outAlpha() {
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.alpha_out);
	}
	
	protected Animation inAlpha(){
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.alpha250);
	}

	protected void doAnimationGoBottom() {
		overridePendingTransition(kr.puzi.puzi.R.anim.top_in, kr.puzi.puzi.R.anim.none);
	}

	protected void doAnimationGoTop() {
		overridePendingTransition(kr.puzi.puzi.R.anim.top_out, kr.puzi.puzi.R.anim.none);
	}

	protected void doAnimationAlphaOut() {
		overridePendingTransition(kr.puzi.puzi.R.anim.alpha_out, kr.puzi.puzi.R.anim.alpha_out);
	}

	protected void doAnimationAlphaOut500() {
		overridePendingTransition(kr.puzi.puzi.R.anim.alpha_out500, kr.puzi.puzi.R.anim.alpha_out500);
	}

	protected void doAnimationAlpha() {
		overridePendingTransition(kr.puzi.puzi.R.anim.alpha250, kr.puzi.puzi.R.anim.alpha250);
	}

	protected void doAnimationGoLeft() {
		overridePendingTransition(kr.puzi.puzi.R.anim.leftin, kr.puzi.puzi.R.anim.rightout);
	}

	public void doAnimationGoRight() {
		overridePendingTransition(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.leftout);
	}

	protected void doAnimationComeRight() {
		overridePendingTransition(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.shrink_back);
	}

	protected void doAnimationBackLeft() {
		overridePendingTransition(kr.puzi.puzi.R.anim.stretch_front, kr.puzi.puzi.R.anim.rightout);
	}

	protected FragmentActivity getActivity() {
		return this;
	}

	protected void closeInputKeyboard(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	protected void loadInputKeyboard(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, editText.length());
	}

	@Override
	protected void onResume() {
		super.onResume();

		checkPush();

		if(targetViewForPush == null) {
			Log.d("PUSH", "MUST SET targetViewForPush for PUSH!");
			return;
		}
		IntentFilter filter = new IntentFilter("com.puzi.puzi.GOT_PUSH");
		filter.setPriority(4);
		registerReceiver(pushReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(targetViewForPush == null) {
			return;
		}
		unregisterReceiver(pushReceiver);
	}

	private BroadcastReceiver pushReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			PuziPushMessageVO messageVO = PuziBroadcastReceiver.convert(intent);
			if(messageVO == null) {
				return;
			}

			switch (messageVO.getType()) {
				case ADVERTISEMENT:
					boolean success = PuziPushManager.addId(ADVERTISEMENT, messageVO.getReceivedAdvertiseDTO().getReceivedAdvertiseId());
					if(!success) {
						return;
					}
					break;

				case NOTICE:

					break;
			}
			showAlertOnTheTop(messageVO, targetViewForPush);
			abortBroadcast();
		}
	};

	protected void showAlertOnTheTop(PuziPushMessageVO messageVO, View target){
		if(messageVO == null) {
			return;
		}

		switch (messageVO.getType()) {
			case ADVERTISEMENT:
				final ReceivedAdvertiseVO receivedAdvertiseVO = messageVO.getReceivedAdvertiseDTO();
				receivedAdvertiseVO.transferComponyInfo();

				// 뷰 새생성
				final View topView = ScreenOnTopView.createTopView(getActivity(), kr.puzi.puzi.R.drawable.push_icon, "새로운 광고가 도착했습니다",
					receivedAdvertiseVO.getSendComment(), new ScreenOnTopView.Listener() {
						@Override
						public void onClick(View v) {
							if(v.getVisibility() == View.GONE) {
								return;
							}
							closeAlertOnTheTop(v);
							showAdvertisement(receivedAdvertiseVO);
						}
					});
				ViewGroup container = ((ViewGroup) target.getRootView());
				container.addView(topView);
				Animation animation = AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.top_in);
				topView.startAnimation(animation);

				// 2초 후 클로징
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						closeAlertOnTheTop(topView);
					}
				}, 2000);
				break;

			case NOTICE:

				break;
		}
	}

	protected void closeAlertOnTheTop(View v) {
		if(v.getVisibility() == View.GONE) {
			return;
		}
		v.setVisibility(View.GONE);
		Animation animation = AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.top_out);
		v.startAnimation(animation);
	}

	protected void checkPush() {
		if(PuziPushManager.isEmptyFianlMessage()) {
			return;
		}

		NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);

		PuziPushMessageVO message = PuziPushManager.getFinalMessage();
		switch (message.getType()) {
			case ADVERTISEMENT:
				PuziPushManager.refreshFinalMessage();
				showAdvertisement(message.getReceivedAdvertiseDTO());
				break;

			case NOTICE:

				break;
		}
	}

	private void showAdvertisement(ReceivedAdvertiseVO receivedAdvertiseVO) {
		Intent intent = new Intent(getActivity(), AdvertisementDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("advertise", receivedAdvertiseVO);
		intent.putExtras(bundle);
		startActivity(intent);
		doAnimationGoRight();
	}
}

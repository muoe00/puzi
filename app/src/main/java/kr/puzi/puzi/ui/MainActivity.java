package kr.puzi.puzi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import kr.puzi.puzi.PuziApplication;
import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.biz.event.EventInfoVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.fcm.PuziBroadcastReceiver;
import kr.puzi.puzi.fcm.PuziPushMessageVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.advertisement.AdvertisementFragment;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.myservice.myworry.MyWorryWriteActivity;
import kr.puzi.puzi.ui.store.coupon.CouponActivity;
import kr.puzi.puzi.ui.store.puzi.challenge.StoreChallengeDetailActivity;
import kr.puzi.puzi.ui.store.puzi.saving.StoreChallengeCompletedDialog;
import kr.puzi.puzi.ui.store.puzi.saving.StoreSavingMineActivity;
import kr.puzi.puzi.ui.user.PointActivity;
import kr.puzi.puzi.ui.user.RecommendActivity;
import kr.puzi.puzi.utils.PuziUtils;
import kr.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

import static android.R.attr.name;

public class MainActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_point) public TextView tvPoint;
	@BindView(kr.puzi.puzi.R.id.tv_todaypoint) public TextView tvTodayPoint;
	@BindView(kr.puzi.puzi.R.id.vp_main) public CustomViewPager viewPager;
	@BindView(kr.puzi.puzi.R.id.main_fragment_container) public LinearLayout llMainBar;
	@BindView(kr.puzi.puzi.R.id.iv_advertise) public ImageView ivAdvertise;
	@BindView(kr.puzi.puzi.R.id.iv_channel) public ImageView ivChannel;
	@BindView(kr.puzi.puzi.R.id.iv_store) public ImageView ivStore;
	@BindView(kr.puzi.puzi.R.id.iv_setting) public ImageView ivSetting;
	@BindView(kr.puzi.puzi.R.id.fl_advertise) public FrameLayout flAdvertise;
	@BindView(kr.puzi.puzi.R.id.fl_channel) public FrameLayout flChannel;
	@BindView(kr.puzi.puzi.R.id.fl_store) public FrameLayout flStore;
	@BindView(kr.puzi.puzi.R.id.fl_setting) public FrameLayout flSetting;
	@BindView(kr.puzi.puzi.R.id.btn_advertise) public Button btnAdvertise;
	@BindView(kr.puzi.puzi.R.id.btn_channel) public Button btnChannel;
	@BindView(kr.puzi.puzi.R.id.btn_store) public Button btnStore;
	@BindView(kr.puzi.puzi.R.id.btn_setting) public Button btnSetting;
	@BindView(kr.puzi.puzi.R.id.ibtn_right_button) public ImageButton ibtnRightButton;
	@BindView(kr.puzi.puzi.R.id.ll_main_bar) public LinearLayout llMain;
	@BindView(kr.puzi.puzi.R.id.ll_main_setting) public LinearLayout llMainSetting;
	@BindView(kr.puzi.puzi.R.id.fl_main_saving) public FrameLayout flMainSaving;
	@BindView(kr.puzi.puzi.R.id.tv_main_saving) public TextView tvMainSaving;

	public static final int FRAGMENT_ADVERTISE = 0;
	public static final int FRAGMENT_CHANNEL = 1;
	public static final int FRAGMENT_STORE = 2;
	public static final int FRAGMENT_SETTING  = 3;

	private long backKeyPressedTime;

	public static boolean needToUpdateUserVO = false;

	private int rightButtonHome = kr.puzi.puzi.R.drawable.add_friend;
	private EventInfoVO eventInfoVO;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_main);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llMain;

		getUser();
		getEventInfo();
		initComponent();
	}

	private void initComponent() {
		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		viewPager.setOffscreenPageLimit(5);
		viewPager.setCurrentItem(FRAGMENT_ADVERTISE);

		ivAdvertise.setImageResource(kr.puzi.puzi.R.drawable.home_on);
		ivChannel.setImageResource(kr.puzi.puzi.R.drawable.channel_off);
		ivStore.setImageResource(kr.puzi.puzi.R.drawable.store_off);
		ivSetting.setImageResource(kr.puzi.puzi.R.drawable.setting_off);

		btnAdvertise.setTag(FRAGMENT_ADVERTISE);
		btnChannel.setTag(FRAGMENT_CHANNEL);
		btnStore.setTag(FRAGMENT_STORE);
		btnSetting.setTag(FRAGMENT_SETTING);

		btnAdvertise.setSelected(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUserInfoOnTitleBar();

		if(needToUpdateUserVO) {
			getUser();
			needToUpdateUserVO = true;
		}

		IntentFilter filter = new IntentFilter("com.puzi.puzi.GOT_PUSH");
		filter.setPriority(4);
		registerReceiver(pushReceiver, filter);
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
					ReceivedAdvertiseVO receivedAdvertiseVO = messageVO.getReceivedAdvertiseDTO();
					for (Fragment fragment : getSupportFragmentManager().getFragments()) {
						if (fragment.isVisible()) {
							if(fragment instanceof AdvertisementFragment){
								AdvertisementFragment advertisementFragment = (AdvertisementFragment) fragment;
								advertisementFragment.addAdvertisement(receivedAdvertiseVO);
							}
						}
					}
					break;
			}
			showAlertOnTheTop(messageVO, llMain);
			abortBroadcast();
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(pushReceiver);
	}

	public void getUser() {
		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.myInfo(token);
			}
		});
		service.enqueue(new CustomCallback(MainActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);

				Preference.saveMyInfo(MainActivity.this, userVO);
				updateUserInfoOnTitleBar();

				PuziApplication application = (PuziApplication) getApplication();
				Tracker tracker = application.getDefaultTracker();
				tracker.set("&uid", userVO.getUserId());
				tracker.send(new HitBuilders.EventBuilder()
					.setCustomDimension(3, userVO.getLevelType())
					.setCategory("UX")
					.setAction("Login")
					.build());
			}
		});
	}

	public void getEventInfo() {
		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.eventEmoticon(token);
			}
		});
		service.enqueue(new CustomCallback(MainActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				eventInfoVO = responseVO.getValue("eventInfoDTO", EventInfoVO.class);
				if(eventInfoVO.getEventStatusType().isShowEvent()) {
					rightButtonHome = kr.puzi.puzi.R.drawable.add_friend_new;
					ibtnRightButton.setImageResource(rightButtonHome);
				}
			}
		});
	}


	public void updateUserInfoOnTitleBar() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		if(userVO == null) {
			return;
		}
		tvPoint.setText(TextUtils.addComma(userVO.getPoint()) + "P");
		tvTodayPoint.setText(TextUtils.addComma(userVO.getTodayPoint()) + "P");
		if(userVO.getUserSavingDTO() == null) {
			flMainSaving.setVisibility(View.GONE);
			return;
		}
		if(!userVO.getUserSavingDTO().isCompleted()) {
			flMainSaving.setVisibility(View.VISIBLE);
			tvMainSaving.setText(TextUtils.addComma(userVO.getUserSavingDTO().getSavedPoint()) + "P");
			return;
		}

		flMainSaving.setVisibility(View.GONE);
		if(userVO.getUserSavingDTO().isCompleted()) {
			StoreChallengeCompletedDialog.load(getActivity(), userVO.getUserSavingDTO().getStoreItemDTO().getPictureUrl(),
				new StoreChallengeDetailActivity.ChallengeSuccessListener() {
					@Override
					public void onSuccess() {
						startActivity(new Intent(getActivity(), CouponActivity.class));
						doAnimationGoRight();
					}
				});
		}
	}

	@OnClick({kr.puzi.puzi.R.id.btn_main_saving})
	public void savingOnClick() {
		startActivity(new Intent(getActivity(), StoreSavingMineActivity.class));
		doAnimationGoRight();
	}

	@OnClick({kr.puzi.puzi.R.id.btn_pointhistory, kr.puzi.puzi.R.id.ibtn_right_button})
	public void changePage(View view) {
		Intent intent = null;
		switch (view.getId()) {
			case kr.puzi.puzi.R.id.btn_pointhistory:
				intent = new Intent(MainActivity.this, PointActivity.class);
				break;
			case kr.puzi.puzi.R.id.ibtn_right_button:
				switch (viewPager.getCurrentItem()) {
					case FRAGMENT_ADVERTISE:
						intent = new Intent(MainActivity.this, RecommendActivity.class);
						intent.putExtra("eventInfoVO", gson.toJson(eventInfoVO));
						break;

					case FRAGMENT_CHANNEL:
						intent = new Intent(MainActivity.this, MyWorryWriteActivity.class);
						break;

					case FRAGMENT_STORE:
						intent = new Intent(MainActivity.this, CouponActivity.class);
						break;
				}
				break;
			default:
				break;
		}
		startActivity(intent);
		doAnimationGoRight();
	}

	// swipe
	@OnPageChange(kr.puzi.puzi.R.id.vp_main)
	public void addPage(int position) {
		int index = FRAGMENT_ADVERTISE;

		while(index < 4) {
			if(position == index) {
				llMainBar.findViewWithTag(index).setSelected(true);
			}
			else {
				llMainBar.findViewWithTag(index).setSelected(false);
			}
			index++;
		}

		changeBottomButton(position);
		Log.i("INFO", "OnPageSelected position : " + position);
	}

	// button
	@OnClick({kr.puzi.puzi.R.id.btn_advertise, kr.puzi.puzi.R.id.btn_channel, kr.puzi.puzi.R.id.btn_store, kr.puzi.puzi.R.id.btn_setting})
	public void movePage(View view){
		int tag = (int) view.getTag();
		int index = FRAGMENT_ADVERTISE;

		while(index < 4) {
			if(tag == index) {
				llMainBar.findViewWithTag(index).setSelected(true);
			}
			else {
				llMainBar.findViewWithTag(index).setSelected(false);
			}
			index++;
		}

		viewPager.setCurrentItem(tag);
		changeBottomButton(tag);

		Log.i("INFO", "movePageListener tag : " + tag);
	}

	@SuppressWarnings("deprecation")
	private void changeBottomButton(int id){
		switch (id) {
			case FRAGMENT_ADVERTISE:
				llMain.setVisibility(View.VISIBLE);
				llMainSetting.setVisibility(View.GONE);
				ivAdvertise.setImageResource(kr.puzi.puzi.R.drawable.home_on);
				ivChannel.setImageResource(kr.puzi.puzi.R.drawable.channel_off);
				ivStore.setImageResource(kr.puzi.puzi.R.drawable.store_off);
				ivSetting.setImageResource(kr.puzi.puzi.R.drawable.setting_off);
				ibtnRightButton.setImageResource(rightButtonHome);
				return;
			case FRAGMENT_CHANNEL:
				llMain.setVisibility(View.VISIBLE);
				llMainSetting.setVisibility(View.GONE);
				ivAdvertise.setImageResource(kr.puzi.puzi.R.drawable.home_off);
				ivChannel.setImageResource(kr.puzi.puzi.R.drawable.channel_on);
				ivStore.setImageResource(kr.puzi.puzi.R.drawable.store_off);
				ivSetting.setImageResource(kr.puzi.puzi.R.drawable.setting_off);
				ibtnRightButton.setImageResource(kr.puzi.puzi.R.drawable.make_survey);
				return;
			case FRAGMENT_STORE:
				llMain.setVisibility(View.VISIBLE);
				llMainSetting.setVisibility(View.GONE);
				ivAdvertise.setImageResource(kr.puzi.puzi.R.drawable.home_off);
				ivChannel.setImageResource(kr.puzi.puzi.R.drawable.channel_off);
				ivStore.setImageResource(kr.puzi.puzi.R.drawable.store_selected);
				ivSetting.setImageResource(kr.puzi.puzi.R.drawable.setting_off);
				ibtnRightButton.setImageResource(kr.puzi.puzi.R.drawable.coupon_archive);
				return;
			case FRAGMENT_SETTING:
				llMain.setVisibility(View.GONE);
				llMainSetting.setVisibility(View.VISIBLE);
				ivAdvertise.setImageResource(kr.puzi.puzi.R.drawable.home_off);
				ivChannel.setImageResource(kr.puzi.puzi.R.drawable.channel_off);
				ivStore.setImageResource(kr.puzi.puzi.R.drawable.store_off);
				ivSetting.setImageResource(kr.puzi.puzi.R.drawable.setting_on);
				return;
		}

	}

	/**
	 * 현재 광고 적립 상태변경 및 채널 필터링에 이용
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
			case RESULT_OK:
				int index = data.getIntExtra("advertiseIndex", 0);
				boolean state = data.getBooleanExtra("pointSavedState", false);
				if(index != 0) {
					for (Fragment fragment : getSupportFragmentManager().getFragments()) {
						if (fragment.isVisible()) {
							if(fragment instanceof AdvertisementFragment){
								AdvertisementFragment advertisementFragment = (AdvertisementFragment) fragment;
								advertisementFragment.refresh(index, state);
								Log.i(PuziUtils.INFO, "index : " + index + ", state : " + state);
							}
						}
					}
				}
				break;
		}
	}

	@Override
	public void onBackPressed() {
		int entrySize = getSupportFragmentManager().getBackStackEntryCount();
		Log.i("INFO", "MainActivity entrySize : " + entrySize);

		if(entrySize <= 1){
			if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
				backKeyPressedTime = System.currentTimeMillis();
				Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
				return;
			} if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
				finish();
			}
		} else {
			getSupportFragmentManager().popBackStack();
		}
	}

	private float convertPixelsToDp(float px, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

}

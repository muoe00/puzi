package com.puzi.puzi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.advertisement.AdvertisementFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.channel.ChannelFilterActivity;
import com.puzi.puzi.ui.channel.ChannelFragment;
import com.puzi.puzi.ui.myworry.MyWorryWriteActivity;
import com.puzi.puzi.ui.store.coupon.CouponActivity;
import com.puzi.puzi.ui.store.puzi.challenge.StoreChallengeDetailActivity;
import com.puzi.puzi.ui.store.puzi.saving.StoreChallengeCompletedDialog;
import com.puzi.puzi.ui.store.puzi.saving.StoreSavingMineActivity;
import com.puzi.puzi.ui.user.PointActivity;
import com.puzi.puzi.ui.user.RecommendActivity;
import com.puzi.puzi.utils.PuziUtils;
import com.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_point) public TextView tvPoint;
	@BindView(R.id.tv_todaypoint) public TextView tvTodayPoint;
	@BindView(R.id.vp_main) public CustomViewPager viewPager;
	@BindView(R.id.main_fragment_container) public LinearLayout llMainBar;
	@BindView(R.id.iv_advertise) public ImageView ivAdvertise;
	@BindView(R.id.iv_channel) public ImageView ivChannel;
	@BindView(R.id.iv_store) public ImageView ivStore;
	@BindView(R.id.iv_setting) public ImageView ivSetting;
	@BindView(R.id.fl_advertise) public FrameLayout flAdvertise;
	@BindView(R.id.fl_channel) public FrameLayout flChannel;
	@BindView(R.id.fl_store) public FrameLayout flStore;
	@BindView(R.id.fl_setting) public FrameLayout flSetting;
	@BindView(R.id.btn_advertise) public Button btnAdvertise;
	@BindView(R.id.btn_channel) public Button btnChannel;
	@BindView(R.id.btn_store) public Button btnStore;
	@BindView(R.id.btn_setting) public Button btnSetting;
	@BindView(R.id.ibtn_right_button) public ImageButton ibtnRightButton;
	@BindView(R.id.ll_main_bar) public LinearLayout llMain;
	@BindView(R.id.ll_main_setting) public LinearLayout llMainSetting;
	@BindView(R.id.fl_main_saving) public FrameLayout flMainSaving;
	@BindView(R.id.tv_main_saving) public TextView tvMainSaving;

	public static final int FRAGMENT_ADVERTISE = 0;
	public static final int FRAGMENT_CHANNEL = 1;
	public static final int FRAGMENT_STORE = 2;
	public static final int FRAGMENT_SETTING  = 3;

	private long backKeyPressedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		unbinder = ButterKnife.bind(this);
		getUser();

		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		viewPager.setOffscreenPageLimit(5);
		viewPager.setCurrentItem(FRAGMENT_ADVERTISE);

		ivAdvertise.setImageResource(R.drawable.home_on);
		ivChannel.setImageResource(R.drawable.channel_off);
		ivStore.setImageResource(R.drawable.store_off);
		ivSetting.setImageResource(R.drawable.setting_off);

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
			}
		});
	}

	private void updateUserInfoOnTitleBar() {
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

	@OnClick({R.id.btn_main_saving})
	public void savingOnClick() {
		startActivity(new Intent(getActivity(), StoreSavingMineActivity.class));
		doAnimationGoRight();
	}

	@OnClick({R.id.btn_pointhistory, R.id.ibtn_right_button})
	public void changePage(View view) {
		Intent intent = null;
		switch (view.getId()) {
			case R.id.btn_pointhistory:
				intent = new Intent(MainActivity.this, PointActivity.class);
				break;
			case R.id.ibtn_right_button:
				switch (viewPager.getCurrentItem()) {
					case FRAGMENT_ADVERTISE:
						intent = new Intent(MainActivity.this, RecommendActivity.class);
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
	@OnPageChange(R.id.vp_main)
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
	@OnClick({R.id.btn_advertise, R.id.btn_channel, R.id.btn_store, R.id.btn_setting})
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
				ivAdvertise.setImageResource(R.drawable.home_on);
				ivChannel.setImageResource(R.drawable.channel_off);
				ivStore.setImageResource(R.drawable.store_off);
				ivSetting.setImageResource(R.drawable.setting_off);
				ibtnRightButton.setImageResource(R.drawable.add_friend);
				return;
			case FRAGMENT_CHANNEL:
				llMain.setVisibility(View.VISIBLE);
				llMainSetting.setVisibility(View.GONE);
				ivAdvertise.setImageResource(R.drawable.home_off);
				ivChannel.setImageResource(R.drawable.channel_on);
				ivStore.setImageResource(R.drawable.store_off);
				ivSetting.setImageResource(R.drawable.setting_off);
				ibtnRightButton.setImageResource(R.drawable.make_survey);
				return;
			case FRAGMENT_STORE:
				llMain.setVisibility(View.VISIBLE);
				llMainSetting.setVisibility(View.GONE);
				ivAdvertise.setImageResource(R.drawable.home_off);
				ivChannel.setImageResource(R.drawable.channel_off);
				ivStore.setImageResource(R.drawable.store_selected);
				ivSetting.setImageResource(R.drawable.setting_off);
				ibtnRightButton.setImageResource(R.drawable.coupon_archive);
				return;
			case FRAGMENT_SETTING:
				llMain.setVisibility(View.GONE);
				llMainSetting.setVisibility(View.VISIBLE);
				ivAdvertise.setImageResource(R.drawable.home_off);
				ivChannel.setImageResource(R.drawable.channel_off);
				ivStore.setImageResource(R.drawable.store_off);
				ivSetting.setImageResource(R.drawable.setting_on);
				return;
		}

	}

	/**
	 * 현재 광고 적립 상태변경
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

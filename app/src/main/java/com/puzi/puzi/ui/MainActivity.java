package com.puzi.puzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.advertisement.AdvertisementFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.channel.ChannelFilterActivity;
import com.puzi.puzi.ui.channel.ChannelFragment;
import com.puzi.puzi.ui.user.PointActivity;
import com.puzi.puzi.ui.user.RecommendActivity;
import com.puzi.puzi.utils.PuziUtils;
import com.puzi.puzi.utils.SerializeUtils;
import retrofit2.Call;

import java.text.NumberFormat;
import java.util.List;

public class MainActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_point) public TextView tvPoint;
	@BindView(R.id.tv_todaypoint) public TextView tvTodayPoint;
	@BindView(R.id.vp_main) public CustomViewPager viewPager;
	@BindView(R.id.main_fragment_container) public LinearLayout llMain;
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

	public static final int FRAGMENT_ADVERTISE = 0;
	public static final int FRAGMENT_CHANNEL = 1;
	public static final int FRAGMENT_STORE = 2;
	public static final int FRAGMENT_SETTING  = 3;

	private UserVO userVO;
	private long backKeyPressedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		unbinder = ButterKnife.bind(this);
		getUser();

		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(FRAGMENT_ADVERTISE);

		ivAdvertise.setBackgroundResource(R.drawable.home_selected);
		ivChannel.setBackgroundResource(R.drawable.channel);
		ivStore.setBackgroundResource(R.drawable.store);
		ivSetting.setBackgroundResource(R.drawable.gear);

		btnAdvertise.setTag(FRAGMENT_ADVERTISE);
		btnChannel.setTag(FRAGMENT_CHANNEL);
		btnStore.setTag(FRAGMENT_STORE);
		btnSetting.setTag(FRAGMENT_SETTING);

		btnAdvertise.setSelected(true);
	}

	public void getUser() {

		Log.i("INFO", "getUser");

		final UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

		String token = Preference.getProperty(MainActivity.this, "token");

		Call<ResponseVO> callUser = userNetworkService.myInfo(token);
		callUser.enqueue(new CustomCallback<ResponseVO>(MainActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				switch(responseVO.getResultType()){
					case SUCCESS:
						userVO = responseVO.getValue("userInfoDTO", UserVO.class);
						Log.i("INFO", "HomeFragment main / userVO : " + userVO.toString());

						NumberFormat numberFormat = NumberFormat.getInstance();

						int point = userVO.getPoint();
						String resultPoint = numberFormat.format(point);
						tvPoint.setText(resultPoint);

						int todayPoint = userVO.getTodayPoint();
						String resultTodayPoint = numberFormat.format(todayPoint);
						tvTodayPoint.setText(resultTodayPoint);
						break;

					default:
						Log.i("INFO", "advertisement getUser failed.");
						Toast.makeText(getBaseContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
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
						intent = new Intent(MainActivity.this, ChannelFilterActivity.class);
						List<ChannelCategoryType> categoryTypeList = null;
						for (Fragment fragment : getSupportFragmentManager().getFragments()) {
							if (fragment != null && fragment.isVisible()) {
								if(fragment instanceof ChannelFragment){
									ChannelFragment channelFragment = (ChannelFragment) fragment;
									categoryTypeList = channelFragment.getCategoryTypeList();
								}
							}
						}
						intent.putStringArrayListExtra("categoryTypeList", SerializeUtils.convertToString(categoryTypeList));
						startActivityForResult(intent, 0);
						doAnimationGoRight();
						return;
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
				llMain.findViewWithTag(index).setSelected(true);
			}
			else {
				llMain.findViewWithTag(index).setSelected(false);
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
				llMain.findViewWithTag(index).setSelected(true);
			}
			else {
				llMain.findViewWithTag(index).setSelected(false);
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
				ivAdvertise.setBackgroundResource(R.drawable.home_selected);
				ivChannel.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				ibtnRightButton.setImageResource(R.drawable.add_friend);
				return;
			case FRAGMENT_CHANNEL:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel_selected);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				ibtnRightButton.setImageResource(R.drawable.filter);
				return;
			case FRAGMENT_STORE:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store_selected);
				ivSetting.setBackgroundResource(R.drawable.gear);
				return;
			case FRAGMENT_SETTING:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
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
				List<ChannelCategoryType> categoryTypeList =
					SerializeUtils.convertToType((List<String>) data.getSerializableExtra("categoryTypeList"));
				if(categoryTypeList != null && categoryTypeList.size() != 0 && viewPager.getCurrentItem() == FRAGMENT_CHANNEL) {
					for (Fragment fragment : getSupportFragmentManager().getFragments()) {
						if (fragment.isVisible()) {
							if(fragment instanceof ChannelFragment){
								ChannelFragment channelFragment = (ChannelFragment) fragment;
								channelFragment.refresh(categoryTypeList);
							}
						}
					}
				} else {
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
}

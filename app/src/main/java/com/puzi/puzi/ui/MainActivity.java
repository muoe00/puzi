package com.puzi.puzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.user.LevelActivity;
import com.puzi.puzi.ui.user.PointActivity;
import com.puzi.puzi.ui.user.RecommendActivity;
import retrofit2.Call;

import java.text.NumberFormat;
import java.util.List;

import static com.puzi.puzi.R.id.btn_advertise;

public class MainActivity extends FragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_point) public TextView tvPoint;
	@BindView(R.id.vp_main) public ViewPager viewPager;
	@BindView(R.id.main_fragment_container) public LinearLayout llMain;
	@BindView(R.id.iv_advertise) public ImageView ivAdvertise;
	@BindView(R.id.iv_channel) public ImageView ivChannel;
	@BindView(R.id.iv_store) public ImageView ivStore;
	@BindView(R.id.iv_setting) public ImageView ivSetting;
	@BindView(R.id.fl_advertise) public FrameLayout flAdvertise;
	@BindView(R.id.fl_channel) public FrameLayout flChannel;
	@BindView(R.id.fl_store) public FrameLayout flStore;
	@BindView(R.id.fl_setting) public FrameLayout flSetting;
	@BindView(btn_advertise) public Button btnAdvertise;
	@BindView(R.id.btn_channel) public Button btnChannel;
	@BindView(R.id.btn_store) public Button btnStore;
	@BindView(R.id.btn_setting) public Button btnSetting;

	public static final int FRAGMENT_ADVERTISE = 0;
	public static final int FRAGMENT_CHANNEL = 1;
	public static final int FRAGMENT_STORE = 2;
	public static final int FRAGMENT_SETTING  = 3;

	private UserVO userVO;
	private long backKeyPressedTime;
	private Fragment currentFragment = null;

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

						int point = userVO.getPoint();
						NumberFormat numberFormat = NumberFormat.getInstance();
						String result = numberFormat.format(point);
						tvPoint.setText(result);
						break;

					default:
						Log.i("INFO", "advertisement getUser failed.");
						Toast.makeText(getBaseContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@OnClick({R.id.btn_pointhistory, R.id.btn_level, R.id.ibtn_recommend})
	public void changePage(View view) {
		Intent intent = null;
		switch (view.getId()) {
			case R.id.btn_level:
				intent = new Intent(MainActivity.this, LevelActivity.class);
				break;
			case R.id.btn_pointhistory:
				intent = new Intent(MainActivity.this, PointActivity.class);
				break;
			case R.id.ibtn_recommend:
				intent = new Intent(MainActivity.this, RecommendActivity.class);
				break;
			default:
				break;
		}
		startActivity(intent);
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
				return;
			case FRAGMENT_CHANNEL:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel_selected);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
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
			List<Fragment> fragments = getSupportFragmentManager().getFragments();
			for(int i=0;i<fragments.size();i++){
				if(fragments.get(i) != null && fragments.get(i).isVisible()){
					if(i != 0){
						currentFragment = (Fragment) fragments.get(i-1);
					} else {
						currentFragment = (Fragment) fragments.get(i);
					}
				}
			}
			getSupportFragmentManager().popBackStack();
		}
	}
}

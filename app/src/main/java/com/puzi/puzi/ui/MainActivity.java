package com.puzi.puzi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;

import java.util.List;

import static com.puzi.puzi.R.id.btn_advertise;

public class MainActivity extends FragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.vp_main) public ViewPager viewPager;
	@BindView(R.id.main_fragment_container) public LinearLayout llMain;
	@BindView(R.id.iv_advertise) public ImageView ivAdvertise;
	@BindView(R.id.iv_channel) public ImageView ivChannel;
	@BindView(R.id.iv_store) public ImageView ivStore;
	@BindView(R.id.iv_setting) public ImageView ivSetting;
	@BindView(R.id.iv_advertise_select) public ImageView ivAdSelected;
	@BindView(R.id.iv_channel_select) public ImageView ivChSelected;
	@BindView(R.id.iv_store_select) public ImageView ivStoreSelected;
	@BindView(R.id.iv_setting_select) public ImageView ivSettingSelected;
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

	private long backKeyPressedTime;
	private Fragment currentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		unbinder = ButterKnife.bind(this);

		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(FRAGMENT_ADVERTISE);
		getFragment(FRAGMENT_ADVERTISE);

		ivAdvertise.setBackgroundResource(R.drawable.home_selected);
		ivChannel.setBackgroundResource(R.drawable.channel);
		ivStore.setBackgroundResource(R.drawable.store);
		ivSetting.setBackgroundResource(R.drawable.gear);

		/*btnAdvertise.setOnClickListener(movePageListener);
		btnChannel.setOnClickListener(movePageListener);
		btnStore.setOnClickListener(movePageListener);
		btnSetting.setOnClickListener(movePageListener);*/

		btnAdvertise.setTag(FRAGMENT_ADVERTISE);
		btnChannel.setTag(FRAGMENT_CHANNEL);
		btnStore.setTag(FRAGMENT_STORE);
		btnSetting.setTag(FRAGMENT_SETTING);

		btnAdvertise.setSelected(true);

		/*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				int index = FRAGMENT_ADVERTISE;

				while(index < 4) {
					if(position == index) {
						llMain.findViewWithTag(position).setSelected(true);
					}
					else {
						llMain.findViewWithTag(position).setSelected(false);
					}
					index++;
				}

				getFragment(position);
				changeBottomButton(position);
				Log.i("INFO", "OnPageSelected position : " + position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});*/
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

		getFragment(position);
		changeBottomButton(position);
		Log.i("INFO", "OnPageSelected index : " + index);
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
		getFragment(tag);

		Log.i("INFO", "movePageListener tag : " + tag);
		Log.i("INFO", "movePageListener index : " + index);
	}



	/*View.OnClickListener movePageListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = (int) v.getTag();
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
			getFragment(tag);
			Log.i("INFO", "movePageListener tag : " + tag);
			Log.i("INFO", "movePageListener index : " + index);
		}
	};*/

	@SuppressWarnings("deprecation")
	private void changeBottomButton(int id){

		flAdvertise.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
		flChannel.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
		flStore.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
		flSetting.setBackgroundColor(getResources().getColor(R.color.colorPuzi));

		switch (id) {
			case FRAGMENT_ADVERTISE:
				ivAdvertise.setBackgroundResource(R.drawable.home_selected);
				ivChannel.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flAdvertise.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
			case FRAGMENT_CHANNEL:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel_selected);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flChannel.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
			case FRAGMENT_STORE:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store_selected);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flStore.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
			case FRAGMENT_SETTING:
				ivAdvertise.setBackgroundResource(R.drawable.home);
				ivChannel.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flSetting.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
		}

	}

	private void getFragment(int index){

		switch(index){
			case FRAGMENT_ADVERTISE:
				ivAdSelected.setVisibility(View.VISIBLE);
				ivChSelected.setVisibility(View.INVISIBLE);
				ivStoreSelected.setVisibility(View.INVISIBLE);
				ivSettingSelected.setVisibility(View.INVISIBLE);
				break;

			case FRAGMENT_CHANNEL:
				ivAdSelected.setVisibility(View.INVISIBLE);
				ivChSelected.setVisibility(View.VISIBLE);
				ivStoreSelected.setVisibility(View.INVISIBLE);
				ivSettingSelected.setVisibility(View.INVISIBLE);
				break;

			case FRAGMENT_STORE:
				ivAdSelected.setVisibility(View.INVISIBLE);
				ivChSelected.setVisibility(View.INVISIBLE);
				ivStoreSelected.setVisibility(View.VISIBLE);
				ivSettingSelected.setVisibility(View.INVISIBLE);
				break;

			case FRAGMENT_SETTING:
				ivAdSelected.setVisibility(View.INVISIBLE);
				ivChSelected.setVisibility(View.INVISIBLE);
				ivStoreSelected.setVisibility(View.INVISIBLE);
				ivSettingSelected.setVisibility(View.VISIBLE);
				break;

			default:
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

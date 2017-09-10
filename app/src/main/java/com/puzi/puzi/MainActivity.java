package com.puzi.puzi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.List;

public class MainActivity extends FragmentActivity {

	public static final int FRAGMENT_ADVERTISE = 0;
	public static final int FRAGMENT_CHANNEL = 1;
	public static final int FRAGMENT_STORE = 2;
	public static final int FRAGMENT_SETTING  = 3;
	private int currentIndex = FRAGMENT_ADVERTISE;

	private ViewPager viewPager;
	private LinearLayout llMain;
	private ImageView ivAd, ivCh, ivStore, ivSetting, ivAdSelected, ivChSelected, ivStoreSelected, ivSettingSelected;
	private FrameLayout flAd, flCh, flStore, flSetting;
	private Button btnAd, btnCh, btnStore, btnSetting;
	private int tag, position;
	private long backKeyPressedTime;
	private Fragment currentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initComponent();

		PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(FRAGMENT_ADVERTISE);
		getFragment(FRAGMENT_ADVERTISE);

		ivAd.setBackgroundResource(R.drawable.home_selected);
		ivCh.setBackgroundResource(R.drawable.channel);
		ivStore.setBackgroundResource(R.drawable.store);
		ivSetting.setBackgroundResource(R.drawable.gear);

		btnAd.setOnClickListener(movePageListener);
		btnAd.setTag(FRAGMENT_ADVERTISE);
		btnCh.setOnClickListener(movePageListener);
		btnCh.setTag(FRAGMENT_CHANNEL);
		btnStore.setOnClickListener(movePageListener);
		btnStore.setTag(FRAGMENT_STORE);
		btnSetting.setOnClickListener(movePageListener);
		btnSetting.setTag(FRAGMENT_SETTING);

		btnAd.setSelected(true);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
				Log.i("DEBUG", "##### OnPageSelected position : " + position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	View.OnClickListener movePageListener = new View.OnClickListener() {
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
			Log.e("DEBUG", "##### movePageListener tag : " + tag);
			Log.e("DEBUG", "##### movePageListener index : " + index);
		}
	};

	@Override
	public void onBackPressed() {

		int entrySize = getSupportFragmentManager().getBackStackEntryCount();
		Log.i("DEBUG", "MainActivity entrySize : " + entrySize);
		if(entrySize <= 1){
			// exit
			if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
				backKeyPressedTime = System.currentTimeMillis();
				Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
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

	private void initComponent() {

		viewPager = (ViewPager)findViewById(R.id.vp_main);
		llMain = (LinearLayout)findViewById(R.id.main_fragment_container);

		btnAd = (Button) findViewById(R.id.btn_advertise);
		ivAd = (ImageView) findViewById(R.id.iv_advertise);
		flAd = (FrameLayout) findViewById(R.id.fl_advertise);
		ivAdSelected = (ImageView) findViewById(R.id.iv_advertise_select);

		btnCh = (Button) findViewById(R.id.btn_channel);
		ivCh = (ImageView) findViewById(R.id.iv_channel);
		flCh = (FrameLayout) findViewById(R.id.fl_channel);
		ivChSelected = (ImageView) findViewById(R.id.iv_channel_select);

		btnStore = (Button) findViewById(R.id.btn_store);
		ivStore = (ImageView) findViewById(R.id.iv_store);
		flStore = (FrameLayout) findViewById(R.id.fl_store);
		ivStoreSelected = (ImageView) findViewById(R.id.iv_store_select);

		btnSetting = (Button) findViewById(R.id.btn_setting);
		ivSetting = (ImageView) findViewById(R.id.iv_setting);
		flSetting = (FrameLayout) findViewById(R.id.fl_setting);
		ivSettingSelected = (ImageView) findViewById(R.id.iv_setting_select);
	}

	@SuppressWarnings("deprecation")
	private void changeBottomButton(int id){

		// back
		flAd.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
		flCh.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
		flStore.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
		flSetting.setBackgroundColor(getResources().getColor(R.color.colorPuzi));

		switch (id) {
			case FRAGMENT_ADVERTISE:
				ivAd.setBackgroundResource(R.drawable.home_selected);
				ivCh.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flAd.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
			case FRAGMENT_CHANNEL:
				ivAd.setBackgroundResource(R.drawable.home);
				ivCh.setBackgroundResource(R.drawable.channel_selected);
				ivStore.setBackgroundResource(R.drawable.store);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flCh.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
			case FRAGMENT_STORE:
				ivAd.setBackgroundResource(R.drawable.home);
				ivCh.setBackgroundResource(R.drawable.channel);
				ivStore.setBackgroundResource(R.drawable.store_selected);
				ivSetting.setBackgroundResource(R.drawable.gear);
				flStore.setBackgroundColor(getResources().getColor(R.color.colorPuzi));
				return;
			case FRAGMENT_SETTING:
				ivAd.setBackgroundResource(R.drawable.home);
				ivCh.setBackgroundResource(R.drawable.channel);
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
}

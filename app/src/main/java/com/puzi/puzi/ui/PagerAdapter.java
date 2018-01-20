package com.puzi.puzi.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.puzi.puzi.ui.today.QuestionFragment;
import com.puzi.puzi.ui.advertisement.AdvertisementFragment;
import com.puzi.puzi.ui.setting.SettingFragment;
import com.puzi.puzi.ui.store.StoreFragment;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

	public PagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		switch(position) {
			case 0:
				return new AdvertisementFragment();
			case 1:
				return new QuestionFragment();
			case 2:
				return new StoreFragment();
			case 3:
				return new SettingFragment();
			default:
				return null;
		}
	}

	@Override
	public int getCount()
	{
		return 4;
	}
}

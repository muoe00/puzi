package com.puzi.puzi;

import android.support.v4.app.FragmentStatePagerAdapter;
import com.puzi.puzi.Fragment.ChannelFragment;
import com.puzi.puzi.Fragment.HomeFragment;
import com.puzi.puzi.Fragment.SettingFragment;
import com.puzi.puzi.Fragment.StoreFragment;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

	public PagerAdapter(android.support.v4.app.FragmentManager fragmentManager)
	{
		super(fragmentManager);
	}
	@Override
	public android.support.v4.app.Fragment getItem(int position)
	{
		switch(position)
		{
			case 0:
				return new HomeFragment();
			case 1:
				return new ChannelFragment();
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

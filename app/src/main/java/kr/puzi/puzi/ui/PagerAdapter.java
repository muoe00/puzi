package kr.puzi.puzi.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.ui.advertisement.AdvertisementFragment;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.setting.SettingFragment;
import kr.puzi.puzi.ui.store.StoreFragment;
import kr.puzi.puzi.ui.today.QuestionFragment;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

	private AdvertisementFragment advertisementFragment = new AdvertisementFragment();
	private QuestionFragment questionFragment = new QuestionFragment();
	private StoreFragment storeFragment = new StoreFragment();
	private SettingFragment settingFragment = new SettingFragment();

	public PagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	public void setPush(ReceivedAdvertiseVO receivedAdvertiseVO) {
		advertisementFragment.setPush(receivedAdvertiseVO);
	}

	public BaseFragment getBaseFragment(int position) {
		switch(position) {
			case 0:
				return advertisementFragment;
			case 1:
				return questionFragment;
			case 2:
				return storeFragment;
			case 3:
				return settingFragment;
			default:
				return null;
		}
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		switch(position) {
			case 0:
				return advertisementFragment;
			case 1:
				return questionFragment;
			case 2:
				return storeFragment;
			case 3:
				return settingFragment;
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

package kr.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.setting.blockTime.BlockTimeFragment;
import kr.puzi.puzi.ui.setting.blockcompany.BlockCompanyFragment;
import kr.puzi.puzi.utils.PuziUtils;

import java.util.ArrayList;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class SettingDetailActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_setting_title)
	TextView tvTitle;
	@BindView(R.id.fl_container_top)
	FrameLayout flContainerTop;

	private int tag;
	private BaseFragment fragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	private ArrayList<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_setting_detail);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		tag = getIntent().getExtras().getInt("TAG");

		Log.i("INFO", "tag : " + tag);

		switch (tag) {
			case 0:
				setTitle("공지사항");
				fragment = new NoticeFragment();
				break;
			case 1:
				setTitle("계정 설정");
				fragment = new UserFragment();
				break;
			case 2:
				setTitle("관심사 설정");
				fragment = new FavoriteFragment();
				break;
			case 3:
				setTitle("알람 시간 제한 설정");
				fragment = new BlockTimeFragment();
				break;
			case 4:
				setTitle("수신 거부 관리");
				fragment = new BlockCompanyFragment();
				break;
			case 5:
				setTitle("고객센터");
				fragment = new CenterFragment();
				break;
		}

		changeView();
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void changeView() {
		addFragment(fragment);
		doAnimationGoRight();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.ll_setting_content, fragment);
		fragmentTransaction.commit();
	}

	public void addFragment(BaseFragment fragment) {
		if(fragment.isAdded()) {
			return;
		} else {
			fragmentList.add(fragment);
			Log.i("INFO", "addFragment");
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.rightin, R.anim.leftout, R.anim.rightin, R.anim.leftout);
			fragmentTransaction.replace(R.id.ll_setting_content, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
			Log.i(PuziUtils.INFO, "fragment list size : " + fragmentList.size());
		}
	}

	public void removeFragment(int position) {
		fragmentList.remove(position);
		fragment = fragmentList.get(position - 1);
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.leftin, R.anim.rightout, R.anim.leftin, R.anim.rightout);
		fragmentTransaction.replace(R.id.ll_setting_content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		Log.i(PuziUtils.INFO, "fragment list size : " + fragmentList.size());
	}

	public void backFragment() {
		int position = fragmentList.size() - 1;

		if(fragmentList.size() == 1) {
			finish();
			doAnimationGoLeft();
		} else if(fragmentList.size() > 1) {
			removeFragment(position);
		}
	}

	@Override
	public void onBackPressed(){
		backFragment();
	}

	@OnClick(R.id.ibtn_back_setting)
	public void back() {
		backFragment();
	}
}

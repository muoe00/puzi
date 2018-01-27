package kr.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.ui.base.BaseFragment;

/**
 * Created by 170605 on 2017-10-26.
 */

public class CenterFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;
	private BaseFragment fragment;
	private SettingDetailActivity settingActivity;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_setting_cs, container, false);
		unbinder = ButterKnife.bind(this, view);

		settingActivity = (SettingDetailActivity) getActivity();
		settingActivity.setTitle("고객센터");

		return view;
	}

	@OnClick({kr.puzi.puzi.R.id.btn_setting_cs_using, kr.puzi.puzi.R.id.btn_setting_cs_service, kr.puzi.puzi.R.id.btn_setting_cs_user, kr.puzi.puzi.R.id.btn_setting_cs_leave})
	public void onClick(View v) {
		Bundle bundle = new Bundle(1);

		switch (v.getId()) {
			case kr.puzi.puzi.R.id.btn_setting_cs_using :
				fragment = new AskFragment();
				bundle.putString("key", "USER");
				fragment.setArguments(bundle);
				changedFragment("문의하기");
				break;
			case kr.puzi.puzi.R.id.btn_setting_cs_service :
				fragment = new UsingFragment();
				bundle.putString("key", "service");
				fragment.setArguments(bundle);
				changedFragment("서비스 이용약관");
				break;
			case kr.puzi.puzi.R.id.btn_setting_cs_user :
				fragment = new PersonalFragment();
				bundle.putString("key", "user");
				fragment.setArguments(bundle);
				changedFragment("개인정보 취급방침");
				break;
			case kr.puzi.puzi.R.id.btn_setting_cs_leave :
				fragment = new OutFragment();
				changedFragment("회원탈퇴");
				break;
			default:
				break;
		}

		doAnimationGoRight();
	}

	public void changedFragment(String title) {
		settingActivity = (SettingDetailActivity) getActivity();

		settingActivity.setTitle(title);
		settingActivity.addFragment(fragment);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

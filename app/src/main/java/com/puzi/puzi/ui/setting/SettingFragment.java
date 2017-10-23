package com.puzi.puzi.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseFragment;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class SettingFragment extends BaseFragment {

	public static final int NOTICE = 0;
	public static final int VERSION = 1;
	public static final int USER = 2;
	public static final int ALARM  = 3;
	public static final int BLOCK = 4;
	public static final int CUSTOMER  = 5;

	Unbinder unbinder;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	@OnClick({R.id.btn_setting_notice, R.id.btn_setting_version, R.id.btn_setting_user,
		R.id.btn_setting_alarm, R.id.btn_setting_block, R.id.btn_setting_customer})
	public void onClick(View v) {

		Intent intent = new Intent(getActivity(), SettingDetailActivity.class);

		switch (v.getId()) {
			case R.id.btn_setting_notice :
				intent.putExtra("TAG", NOTICE);
				break;
			case R.id.btn_setting_version :
				intent.putExtra("TAG", VERSION);
				break;
			case R.id.btn_setting_user :
				intent.putExtra("TAG", USER);
				break;
			case R.id.btn_setting_alarm :
				intent.putExtra("TAG", ALARM);
				break;
			case R.id.btn_setting_block :
				intent.putExtra("TAG", BLOCK);
				break;
			case R.id.btn_setting_customer :
				intent.putExtra("TAG", CUSTOMER);
				break;
			default:
				break;
		}

		doAnimationGoRight();
		startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

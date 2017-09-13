package com.puzi.puzi.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class SettingFragment extends Fragment implements View.OnClickListener{

	public static final int NOTICE = 0;
	public static final int VERSION = 1;
	public static final int USER = 2;
	public static final int ALARM  = 3;
	public static final int BLOCK = 4;
	public static final int CUSTOMER  = 5;

	private Button btnNotice, btnVersion, btnUser, btnAlarm, btnBlock, btnCustomer;

	public SettingFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_setting, container, false);

		initComponent(view);

		return view;
	}

	private void initComponent(View view) {

		btnNotice = (Button) view.findViewById(R.id.btn_setting_notice);
		btnVersion = (Button) view.findViewById(R.id.btn_setting_version);
		btnUser = (Button) view.findViewById(R.id.btn_setting_user);
		btnAlarm = (Button) view.findViewById(R.id.btn_setting_alarm);
		btnBlock = (Button) view.findViewById(R.id.btn_setting_block);
		btnCustomer = (Button) view.findViewById(R.id.btn_setting_customer);

		btnNotice.setOnClickListener(this);
		btnVersion.setOnClickListener(this);
		btnUser.setOnClickListener(this);
		btnAlarm.setOnClickListener(this);
		btnBlock.setOnClickListener(this);
		btnCustomer.setOnClickListener(this);
	}

	@Override
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

		startActivity(intent);
	}
}

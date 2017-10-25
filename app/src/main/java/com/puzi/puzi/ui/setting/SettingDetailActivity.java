package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class SettingDetailActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_setting_title) TextView tvTitle;

	private int tag;
	private BaseFragment fragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_setting_detail);

		unbinder = ButterKnife.bind(this);

		tag = getIntent().getExtras().getInt("TAG");

		Log.i("INFO", "tag : " + tag);

		switch (tag) {
			case 0:
				tvTitle.setText("공지사항");
				fragment = new NoticeFragment();
				break;
			case 1:
				tvTitle.setText("계정 설정");
				fragment = new UserFragment();
				break;
			case 2:
				tvTitle.setText("관심사 설정");
				fragment = new FavoriteFragment();
				break;
			case 3:
				tvTitle.setText("알람 시간 제한 설정");
				fragment = new AlarmFragment();
				break;
			case 4:
				tvTitle.setText("수신 거부 관리");
				fragment = new BlockFragment();
				break;
			case 5:
				tvTitle.setText("고객센터");
				fragment = new FavoriteFragment();
				break;
		}

		changeView();
	}

	public void changeView() {
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.ll_setting_content, fragment);
		fragmentTransaction.commit();
	}

	@OnClick(R.id.ibtn_back_setting)
	public void back() {
		finish();
	}
}

package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_setting_detail);

		unbinder = ButterKnife.bind(this);

		tag = getIntent().getExtras().getInt("TAG");

		BaseFragment fragment = null;

		switch (tag) {
			case 0:
				tvTitle.setText("공지사항");
				fragment = new NoticeFragment();
				break;
			case 1:
				tvTitle.setText("버전정보");
				break;
			case 2:
				tvTitle.setText("계정/관심사 설정");
				fragment = new UserFragment();
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
				fragment = new CustomerFragment();
				break;
		}

		changeView(fragment);
	}

	public void changeView(BaseFragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.ll_setting_content, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

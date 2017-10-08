package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.utils.PuziUtils;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PointActivity extends BaseFragmentActivity {

	Unbinder unbinder;
	private int tag = 0;

	@BindView(R.id.ibtn_back) public ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);

		unbinder = ButterKnife.bind(this);

		changedFragment(tag);
	}

	public void changedFragment(int tag) {

		Bundle bundle = new Bundle(1);
		bundle.putInt("tag", tag);

		BaseFragment fragment = new PointContentsFragment();
		fragment.setArguments(bundle);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.ll_point_content, fragment);
		fragmentTransaction.commit();
	}

	@OnClick({R.id.btn_channelRecommend, R.id.btn_point_level})
	public void changedTag(View view) {

		switch(view.getId()) {
			case R.id.btn_channelRecommend:
				tag = PuziUtils.VIEW_POINT;
				break;
			case R.id.btn_point_level:
				tag = PuziUtils.VIEW_LEVEL;
				break;
		}

		changedFragment(tag);
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
		doAnimationGoLeft();
	}
}

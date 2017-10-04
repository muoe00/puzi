package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PointActivity extends FragmentActivity {

	private static final int VIEW_POINT = 0;
	private static final int VIEW_LEVEL = 1;

	Unbinder unbinder;
	View view;
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

		Fragment fragment = new PointContentsFragment();
		fragment.setArguments(bundle);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.ll_point_content, fragment);
		fragmentTransaction.commit();
	}

	@OnClick({R.id.btn_channelRecommend, R.id.btn_point_level})
	public void changedTag(View view) {

		switch(view.getId()) {
			case VIEW_POINT:
				tag = 0;
				break;
			case VIEW_LEVEL:
				tag = 1;
				break;
		}

		changedFragment(tag);
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

package com.puzi.puzi.ui.store.coupon;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseActivity {

	Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);

	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

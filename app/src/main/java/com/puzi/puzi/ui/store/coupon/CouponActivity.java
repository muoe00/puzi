package com.puzi.puzi.ui.store.coupon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseActivity {

	private ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_store_coupon);

		btnBack = (ImageButton) findViewById(R.id.ibtn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}

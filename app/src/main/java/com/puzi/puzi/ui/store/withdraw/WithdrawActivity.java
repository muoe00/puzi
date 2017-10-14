package com.puzi.puzi.ui.store.withdraw;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by muoe0 on 2017-07-30.
 */

public class WithdrawActivity extends BaseActivity {

	private ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_store_withdraw);

		btnBack = (ImageButton) findViewById(R.id.ibtn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}

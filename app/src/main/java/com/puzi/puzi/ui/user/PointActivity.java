package com.puzi.puzi.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PointActivity extends Activity {

	private ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);

		btnBack = (ImageButton) findViewById(R.id.ibtn_back);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}

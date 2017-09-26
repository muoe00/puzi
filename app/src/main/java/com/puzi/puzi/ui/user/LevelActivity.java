package com.puzi.puzi.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class LevelActivity extends Activity {

	private Unbinder unbinder;

	@BindView(R.id.ibtn_back) public ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);

		unbinder = ButterKnife.bind(this);
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

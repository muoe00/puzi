package com.puzi.puzi.ui.store.puzi.challenge;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by JangwonPark on 2017. 12. 30..
 */

public class StoreChallengeDetailActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.gv_store_challenge)
	GridView gvChallenge;
	@BindView(R.id.sv_store_challenge)
	ScrollView svChallenge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_challenge_detail);

		unbinder = ButterKnife.bind(this);

		initComponent();
	}

	private void initComponent() {

	}

}

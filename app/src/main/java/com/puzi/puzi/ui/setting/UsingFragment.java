package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.base.BaseFragment;

/**
 * Created by 170605 on 2017-10-27.
 */

public class UsingFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;

	@BindView(R.id.fl_signup)
	FrameLayout flTitle;
	@BindView(R.id.fl_signup_line)
	LinearLayout llLine;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_cs_using, container, false);
		unbinder = ButterKnife.bind(this, view);

		if(getActivity() instanceof IntroActivity) {
			flTitle.setVisibility(View.VISIBLE);
			llLine.setVisibility(View.VISIBLE);
		}

		return view;
	}
}

package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.ui.base.BaseFragment;

/**
 * Created by 170605 on 2017-10-27.
 */

public class TermsFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;
	private String state = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_cs_terms, container, false);
		unbinder = ButterKnife.bind(this, view);

		state = getArguments().getString("key");

		return view;
	}
}

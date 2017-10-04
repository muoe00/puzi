package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointContentsFragment extends Fragment {

	Unbinder unbinder;
	int tag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//tag = getArguments().get("tag");
		//Log.i(PuziUtils.INFO, "tag : " + tag);

		View view = inflater.inflate(R.layout.fragment_point_list, container, false);

		unbinder = ButterKnife.bind(this, view);

		return view;
	}



}

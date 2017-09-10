package com.puzi.puzi.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SearchFragment extends Fragment {

	public SearchFragment() {
	}

	private static final String TAG = "SearchFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_search, container, false);

		initComponent(view);

		return view;
	}

	private void initComponent(View view) {

	}
}

package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.puzi.puzi.R;

import java.util.Arrays;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

	private Activity activity;
    private LayoutInflater inflate;
    private List<String> stateList = Arrays.asList("전체", "내가 쓴 글");
	private boolean state;

	public SpinnerAdapter(Activity activity) {
		this.activity = activity;
		this.inflate = activity.getLayoutInflater();
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public int getCount() {
		return stateList.size();
	}

	@Override
	public Object getItem(int position) {
		return stateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if(view == null) {
			view = inflate.inflate(R.layout.item_spinner_normal, viewGroup, false);
		}

		String text = stateList.get(i);
		((TextView) view.findViewById(R.id.spinnerText)).setText(text);

		return view;
	}
}

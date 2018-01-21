package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.AnswerType;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

	private Activity activity;
    private LayoutInflater inflate;
	private List<AnswerType> list;

	public SpinnerAdapter(Activity activity) {
		this.activity = activity;
		this.inflate = activity.getLayoutInflater();
	}

	public void addList(List<AnswerType> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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

		if(list != null){
			String text = list.get(i).getComment();

			((TextView) view.findViewById(R.id.spinnerText)).setText(text);
		}

		return view;
	}
}

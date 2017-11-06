package com.puzi.puzi.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 11. 6..
 */

public class CustonArrayAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Activity activity;
	private List<String> list = newArrayList();

	public CustonArrayAdapter(Activity activity, List<String> list) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.list.addAll(list);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder viewHolder = null;

		if(v == null) {
			v = inflater.inflate(R.layout.item_array_text, null);
			viewHolder = new ViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.text.setText(list.get(position));

		return v;
	}

	class ViewHolder {

		@BindView(R.id.tv_item_array_text) public TextView text;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}
}

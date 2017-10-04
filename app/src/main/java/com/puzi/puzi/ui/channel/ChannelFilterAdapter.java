package com.puzi.puzi.ui.channel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelCategoryType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class ChannelFilterAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Activity activity;
	private List<ChannelCategoryType> list = newArrayList();

	public ChannelFilterAdapter(Activity activity) {
		this.inflater = activity.getLayoutInflater();
		this.activity = activity;
	}

	public void refreshAndAdd(List<ChannelCategoryType> newList) {
		list = newArrayList(newList);
		notifyDataSetChanged();
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

	public int getViewTypeCount() {
		return 1;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder viewHolder;
		if(v == null) {
			v = inflater.inflate(R.layout.item_channel_filter, null);
			viewHolder = new ViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		ChannelCategoryType categoryType = (ChannelCategoryType) getItem(position);
		viewHolder.tvName.setText("#" + categoryType.getComment());

		return v;
	}

	public class ViewHolder {
		@BindView(R.id.tv_item_channel_filter_name) public TextView tvName;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}

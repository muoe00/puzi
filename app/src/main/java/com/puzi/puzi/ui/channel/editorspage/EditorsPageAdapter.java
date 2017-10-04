package com.puzi.puzi.ui.channel.editorspage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.image.BitmapUIL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class EditorsPageAdapter extends BaseAdapter {

	private static final int VIEW_EDITORS_PAGE = 0;
	private static final int VIEW_PROGRESS = 1;

	private LayoutInflater inflater;
	private Activity activity;
	private List<ChannelEditorsPageVO> list = new ArrayList();
	private boolean progressed = false;

	public EditorsPageAdapter(Activity activity) {
		this.inflater = activity.getLayoutInflater();
		this.activity = activity;
	}

	public void add(List<ChannelEditorsPageVO> list) {
		this.list.addAll(list);
	}

	public void startProgress() {
		if(!progressed) {
			progressed = true;
			notifyDataSetChanged();
		}
	}

	public void stopProgress() {
		if(progressed) {
			progressed = false;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if(progressed) {
			return 1;
		}
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
		return 2;
	}

	public int getItemViewType(int position) {
		if(progressed) {
			return VIEW_PROGRESS;
		}
		return VIEW_EDITORS_PAGE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder viewHolder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case VIEW_EDITORS_PAGE:
					v = inflater.inflate(R.layout.item_channel_detail_editors_page, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_EDITORS_PAGE:
				final ChannelEditorsPageVO editorsPageVO = (ChannelEditorsPageVO) getItem(position);
				BitmapUIL.load(editorsPageVO.getPreviewUrl(), viewHolder.ivImage);
				viewHolder.tvTitle.setText(editorsPageVO.getTitle());
				viewHolder.tvName.setText(editorsPageVO.getCreatedBy());
				break;
		}

		return v;
	}

	public class ViewHolder {
		@BindView(R.id.iv_item_channel_detail_editors_page_image) public SelectableRoundedImageView ivImage;
		@BindView(R.id.tv_item_channel_detail_editors_page_title) public TextView tvTitle;
		@BindView(R.id.tv_item_channel_detail_editors_page_name) public TextView tvName;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}

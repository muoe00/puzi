package com.puzi.puzi.ui.channel;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.CustomPagingAdapter;

/**
 * Created by JangwonPark on 2017. 11. 5..
 */

public class ChannelEditorspageAdapter extends CustomPagingAdapter<ChannelEditorsPageVO> {

	public ChannelEditorspageAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, listView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder holder, ChannelEditorsPageVO item, int position) {
		ViewHolder viewHolder = (ViewHolder) holder;

		BitmapUIL.load(item.getPreviewUrl(), viewHolder.ivImage);
		viewHolder.tvTitle.setText(item.getTitle());
		viewHolder.tvName.setText(item.getCreatedBy());
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	class ViewHolder extends Holder {

		@BindView(R.id.iv_item_channel_editor_image) public SelectableRoundedImageView ivImage;
		@BindView(R.id.tv_item_channel_editor_title) public TextView tvTitle;
		@BindView(R.id.tv_item_channel_editor_name) public TextView tvName;

		public ViewHolder(View view) {
			super(view);
		}
	}
}

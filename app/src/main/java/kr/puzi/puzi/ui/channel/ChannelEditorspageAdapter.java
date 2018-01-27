package kr.puzi.puzi.ui.channel;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import kr.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import kr.puzi.puzi.image.BitmapUIL;

import com.joooonho.SelectableRoundedImageView;

import kr.puzi.puzi.ui.CustomPagingAdapter;

/**
 * Created by JangwonPark on 2017. 11. 5..
 */

public class ChannelEditorspageAdapter extends CustomPagingAdapter<ChannelEditorsPageVO> {

	public ChannelEditorspageAdapter(Activity activity, int layoutResource, int layoutResource2, ListView listView, ScrollView scrollView,
		ListHandler listHandler) {
		super(activity, layoutResource, layoutResource2, listView, scrollView, listHandler);
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public int getItemViewType(int position) {
		int type = super.getItemViewType(position);
		if(VIEW_LIST != type) {
			return type;
		}

		ChannelEditorsPageVO channelEditorsPageVO = getItem(position);
		if(channelEditorsPageVO.isShowTitle()){
			return VIEW_LIST;
		} else {
			return VIEW_LIST_2;
		}
	}

	@Override
	protected void setView(Holder holder, ChannelEditorsPageVO item, int position) {
		ViewHolder viewHolder = (ViewHolder) holder;

		BitmapUIL.load(item.getPreviewUrl(), viewHolder.ivImage);
		viewHolder.tvTitle.setText(item.getTitle());
		viewHolder.tvName.setText(item.getCreatedBy());
	}

	@Override
	public void setView2(Holder holder, ChannelEditorsPageVO item, int position) {
		ViewHolder2 viewHolder = (ViewHolder2) holder;
		BitmapUIL.load(item.getPreviewUrl(), viewHolder.ivImage);
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	@Override
	protected Holder createHolder2(View v) {
		return new ViewHolder2(v);
	}

	class ViewHolder extends Holder {

		@BindView(kr.puzi.puzi.R.id.iv_item_channel_editor_image) public SelectableRoundedImageView ivImage;
		@BindView(kr.puzi.puzi.R.id.tv_item_channel_editor_title) public TextView tvTitle;
		@BindView(kr.puzi.puzi.R.id.tv_item_channel_editor_name) public TextView tvName;

		public ViewHolder(View view) {
			super(view);
		}
	}

	class ViewHolder2 extends Holder {

		@BindView(kr.puzi.puzi.R.id.iv_item_channel_editor_image) public SelectableRoundedImageView ivImage;

		public ViewHolder2(View view) {
			super(view);
		}
	}
}

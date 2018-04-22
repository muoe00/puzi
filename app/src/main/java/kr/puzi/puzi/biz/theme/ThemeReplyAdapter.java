package kr.puzi.puzi.biz.theme;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import kr.puzi.puzi.ui.CustomPagingAdapter;

/**
 * Created by 170605 on 2018-03-22.
 */

public class ThemeReplyAdapter extends CustomPagingAdapter<ThemeReplyVO> {

	public ThemeReplyAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, listView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder viewHolder, ThemeReplyVO item, int position) {
		ThemeReplyAdapter.ViewHolder holder = (ThemeReplyAdapter.ViewHolder) viewHolder;

		String writer = "***" + item.getWriter().substring(3);
		holder.tvName.setText(writer);
		holder.tvTime.setText(item.getCreatedAt());
		holder.tvComment.setText(item.getComment());
	}

	@Override
	protected Holder createHolder(View v) {
		return new ThemeReplyAdapter.ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(kr.puzi.puzi.R.id.tv_item_thema_reply_name) public TextView tvName;
		@BindView(kr.puzi.puzi.R.id.tv_item_thema_reply_time) public TextView tvTime;
		@BindView(kr.puzi.puzi.R.id.tv_item_thema_reply_comment) public TextView tvComment;

		public ViewHolder(View view) {
			super(view);
		}
	}
}

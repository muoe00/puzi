package kr.puzi.puzi.ui.myservice.myworry;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.myworry.MyWorryReplyVO;
import kr.puzi.puzi.ui.CustomPagingAdapter;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
public class MyWorryReplyAdapter extends CustomPagingAdapter<MyWorryReplyVO> {

	public MyWorryReplyAdapter(Activity activity, ListView listView, ListHandler listHandler) {
		super(activity, R.layout.item_myworry_reply, listView, listHandler);
	}

	@Override
	protected void setView(Holder viewHolder, MyWorryReplyVO item, int position) {
		ViewHolder holder = (ViewHolder) viewHolder;

		holder.tvName.setText(item.getWriter());
		holder.tvTime.setText(item.getCreatedAt());
		holder.tvComment.setText(item.getComment());
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(kr.puzi.puzi.R.id.tv_item_myworry_reply_name) public TextView tvName;
		@BindView(kr.puzi.puzi.R.id.tv_item_myworry_reply_time) public TextView tvTime;
		@BindView(kr.puzi.puzi.R.id.tv_item_myworry_reply_comment) public TextView tvComment;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

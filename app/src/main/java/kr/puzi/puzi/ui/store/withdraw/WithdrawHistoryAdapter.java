package kr.puzi.puzi.ui.store.withdraw;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.store.WithdrawVO;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.utils.TextUtils;

/**
 * Created by JangwonPark on 2017. 11. 5..
 */
public class WithdrawHistoryAdapter extends CustomPagingAdapter<WithdrawVO> {

	public WithdrawHistoryAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, listView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder holder, WithdrawVO item, int position) {
		ViewHolder viewHolder = (ViewHolder) holder;

		viewHolder.tvTime.setText(item.getCreatedAt());
		viewHolder.tvPoint.setText(TextUtils.addComma(item.getMoney()) + "P");
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	class ViewHolder extends Holder {

		@BindView(R.id.tv_item_store_withdraw_history_time) public TextView tvTime;
		@BindView(R.id.tv_item_store_withdraw_history_point) public TextView tvPoint;

		public ViewHolder(View view) {
			super(view);
		}
	}
}

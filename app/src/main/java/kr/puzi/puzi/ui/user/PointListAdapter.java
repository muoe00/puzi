package kr.puzi.puzi.ui.user;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import kr.puzi.puzi.biz.user.point.history.PointHistoryVO;
import kr.puzi.puzi.ui.CustomPagingAdapter;

import java.text.NumberFormat;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointListAdapter extends CustomPagingAdapter<PointHistoryVO> {

	public PointListAdapter(Activity activity, ListView listView, ListHandler listHandler) {
		super(activity, kr.puzi.puzi.R.layout.fragment_point_detail, listView, listHandler);
		setEmptyMessage("적립 내역이 없습니다.");
	}

	@Override
	protected void setView(Holder viewHolder, final PointHistoryVO pointHistoryVO, final int position) {
		ViewHolder holder = (ViewHolder) viewHolder;

		NumberFormat numberFormat = NumberFormat.getInstance();
		String point = numberFormat.format(pointHistoryVO.getPoint());

		Log.i("pointHistoryVO", pointHistoryVO.toString());

		if (pointHistoryVO.isSaved()) {
			holder.tvPoint.setText(point + "P");
			holder.tvPoint.setTextColor(ContextCompat.getColor(super.activity, kr.puzi.puzi.R.color.colorPuzi));
		} else {
			holder.tvPoint.setText(point + "P");
			holder.tvPoint.setTextColor(ContextCompat.getColor(super.activity, kr.puzi.puzi.R.color.colorTextGray));
		}

		holder.tvType.setText(pointHistoryVO.getPointType().getComment());
		holder.tvTime.setText(pointHistoryVO.getCreatedAt());
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(kr.puzi.puzi.R.id.tv_point_type) public TextView tvType;
		@BindView(kr.puzi.puzi.R.id.tv_point_at) public TextView tvTime;
		@BindView(kr.puzi.puzi.R.id.tv_point) public TextView tvPoint;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

package com.puzi.puzi.ui.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.point.history.PointHistoryVO;
import com.puzi.puzi.utils.PuziUtils;

import java.util.List;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointListAdapter extends BaseAdapter {

	@BindView(R.id.tv_point_type) public TextView tvType;
	@BindView(R.id.tv_point_at) public TextView tvTime;
	@BindView(R.id.tv_point) public TextView tvPoint;

	private Unbinder unbinder;
	private LayoutInflater inflater;
	private Context context;
	private PointHistoryVO pointHistory;
	private List<PointHistoryVO> pointHistoryVOs;

	public PointListAdapter(Context context, List<PointHistoryVO> list) {
		this.context = context;
		this.pointHistoryVOs = list;
		Log.i(PuziUtils.INFO, "PointListAdapter pointHistoryVOs size : " + pointHistoryVOs.size());
	}

	@Override
	public int getCount() {
		return pointHistoryVOs.size();
	}

	@Override
	public Object getItem(int position) {
		return pointHistoryVOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_point_detail, parent, false);
		}

		unbinder = ButterKnife.bind(this, convertView);

		pointHistory = pointHistoryVOs.get(position);

		tvType.setText(pointHistory.getPointType().getComment());
		tvTime.setText(pointHistory.getCreatedAt());
		tvPoint.setText(pointHistory.getPoint());


		return null;
	}
}

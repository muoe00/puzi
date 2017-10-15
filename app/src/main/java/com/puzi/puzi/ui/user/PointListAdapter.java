package com.puzi.puzi.ui.user;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.point.history.PointHistoryVO;
import lombok.Getter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointListAdapter extends BaseAdapter {

	private static final int VIEW_POINT = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private Context context = null;
	private LayoutInflater inflater;
	private List<PointHistoryVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public PointListAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addPointFirst(PointHistoryVO pointHistoryVO) {
		empty = false;
		list.add(0, pointHistoryVO);
	}

	public void addPointList(List<PointHistoryVO> pointHistoryVOs) {
		if(empty && pointHistoryVOs.size() > 0) {
			empty = false;
		}
		list.addAll(pointHistoryVOs);
	}

	public void empty() {
		if(!empty){
			empty = true;
		}
	}

	public int getViewTypeCount() {
		return 3;
	}

	public int getItemViewType(int position) {
		if(empty) {
			return VIEW_EMPTY;
		}
		if(progressed) {
			if(getCount() - 1 == position) {
				return VIEW_PROGRESS;
			}
		}
		return VIEW_POINT;
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
		if(empty) {
			return 1;
		}
		if(progressed) {
			return list.size() + 1;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case VIEW_POINT:
					v = inflater.inflate(R.layout.fragment_point_detail, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_point, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_POINT:
				final PointHistoryVO pointHistoryVO = (PointHistoryVO) getItem(position);

				NumberFormat numberFormat = NumberFormat.getInstance();
				String point = numberFormat.format(pointHistoryVO.getPoint());

				if (pointHistoryVO.isSaved() == true) {
					viewHolder.tvPoint.setText(point);
					viewHolder.tvPoint.setTextColor(ContextCompat.getColor(context, R.color.colorPuzi));
				} else if (pointHistoryVO.isSaved() == false) {
					viewHolder.tvPoint.setText("-" + point);
					viewHolder.tvPoint.setTextColor(ContextCompat.getColor(context, R.color.colorTextGray));
				}

				viewHolder.tvType.setText(pointHistoryVO.getPointType().getComment());
				viewHolder.tvTime.setText(pointHistoryVO.getCreatedAt());


				break;
		}
		return v;
	}

	public class ViewHolder {
		@BindView(R.id.tv_point_type) public TextView tvType;
		@BindView(R.id.tv_point_at) public TextView tvTime;
		@BindView(R.id.tv_point) public TextView tvPoint;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}

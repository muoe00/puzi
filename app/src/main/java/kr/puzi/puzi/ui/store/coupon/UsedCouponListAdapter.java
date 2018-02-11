package kr.puzi.puzi.ui.store.coupon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.puzi.puzi.biz.store.PurchaseHistoryVO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.utils.TextUtils;
import lombok.Getter;

public class UsedCouponListAdapter extends BaseAdapter {

	private static final int VIEW_USED_COUPON = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private Activity activity;
	private LayoutInflater inflater;
	private List<PurchaseHistoryVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public UsedCouponListAdapter(Activity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void addList(List<PurchaseHistoryVO> purchaseHistoryVOs) {
		if(empty && purchaseHistoryVOs.size() > 0) {
			empty = false;
		}
		list.addAll(purchaseHistoryVOs);
	}

	public void clean() {
		list = new ArrayList();
		notifyDataSetChanged();
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
		return VIEW_USED_COUPON;
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case VIEW_USED_COUPON:
					v = inflater.inflate(kr.puzi.puzi.R.layout.item_coupon_used_child, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(kr.puzi.puzi.R.layout.item_list_empty_used_coupon, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(kr.puzi.puzi.R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_USED_COUPON:
				final PurchaseHistoryVO purchaseHistoryVO = (PurchaseHistoryVO) getItem(position);

				BitmapUIL.load(purchaseHistoryVO.getStoreItemDTO().getPictureUrl(), viewHolder.ivImage);
				viewHolder.tvTitle.setText(purchaseHistoryVO.getStoreItemDTO().getName());
				viewHolder.tvPrice.setText(TextUtils.addComma(purchaseHistoryVO.getStoreItemDTO().getPrice()) + "P");
				viewHolder.tvDate.setText(purchaseHistoryVO.getValidEndDate());

				break;
		}
		return v;
	}

	public class ViewHolder {
		@BindView(kr.puzi.puzi.R.id.iv_used_coupon)
		public ImageView ivImage;
		@BindView(kr.puzi.puzi.R.id.tv_used_coupon_name)
		public TextView tvTitle;
		@BindView(kr.puzi.puzi.R.id.tv_used_coupon_price)
		public TextView tvPrice;
		@BindView(kr.puzi.puzi.R.id.tv_used_coupon_date)
		public TextView tvDate;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
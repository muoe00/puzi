package com.puzi.puzi.ui.store.coupon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.image.BitmapUIL;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 170605 on 2017-10-27.
 */

public class CouponListAdapter extends BaseAdapter {

	private static final int VIEW_COUPON = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private Activity activity;
	private LayoutInflater inflater;
	private List<PurchaseHistoryVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public CouponListAdapter(Activity activity) {
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
		return VIEW_COUPON;
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
				case VIEW_COUPON:
					v = inflater.inflate(R.layout.item_coupon_child, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_coupon, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_COUPON:
				final PurchaseHistoryVO purchaseHistoryVO = (PurchaseHistoryVO) getItem(position);

				/*Log.i(PuziUtils.INFO, "getPictureUrl() : " + purchaseHistoryVO.getStoreItemDTO().getPictureUrl());
				Log.i(PuziUtils.INFO, "getName() : " + purchaseHistoryVO.getStoreItemDTO().getName());
				Log.i(PuziUtils.INFO, "getPrice() : " + purchaseHistoryVO.getStoreItemDTO().getPrice());
				Log.i(PuziUtils.INFO, "getValidEndDate() : " + purchaseHistoryVO.getValidEndDate());*/

				BitmapUIL.load(purchaseHistoryVO.getStoreItemDTO().getPictureUrl(), viewHolder.ivImage);
				viewHolder.tvTitle.setText(purchaseHistoryVO.getStoreItemDTO().getName());
				viewHolder.tvPrice.setText(purchaseHistoryVO.getStoreItemDTO().getPrice() + "");
				viewHolder.tvDate.setText(purchaseHistoryVO.getValidEndDate());

				viewHolder.btnCoupon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final CouponDialog couponDialog = new CouponDialog(activity, purchaseHistoryVO.getCouponImageUrl());
						couponDialog.show();
					}
				});

				break;
		}

		return v;
	}


	public class ViewHolder {
		@BindView(R.id.iv_coupon)
		public ImageView ivImage;
		@BindView(R.id.tv_coupon_name)
		public TextView tvTitle;
		@BindView(R.id.tv_coupon_price)
		public TextView tvPrice;
		@BindView(R.id.tv_coupon_date)
		public TextView tvDate;
		@BindView(R.id.btn_coupon)
		public Button btnCoupon;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}

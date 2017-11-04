package com.puzi.puzi.ui.store.coupon;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.company.CompanyVO;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.utils.PuziUtils;
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
					v = inflater.inflate(R.layout.fragment_advertisement_item, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_advertise, null);
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
				final ReceivedAdvertiseVO receivedAdvertise = (ReceivedAdvertiseVO) getItem(position);

				Log.i("INFO", "URL Company : " + receivedAdvertise.getLinkPreviewUrl());

				BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), viewHolder.ivAd);
				BitmapUIL.load(receivedAdvertise.getCompanyInfoDTO().getPictureUrl(), viewHolder.ivComp);

				final CompanyVO company = receivedAdvertise.getCompanyInfoDTO();

				viewHolder.tvAd.setText(receivedAdvertise.getSendComment());
				viewHolder.tvComp.setText(company.getCompanyAlias());

				Log.i(PuziUtils.INFO, "adapter.getSaved() : " + receivedAdvertise.getSaved());
				Log.i(PuziUtils.INFO, "adapter.getToday() : " + receivedAdvertise.getToday());

				viewHolder.btnAd.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// changedDetail(receivedAdvertise);
					}
				});

				/*viewHolder.ivComp.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changedCompany(company);
					}
				});
				*/break;
		}

		return v;
	}

	public class ViewHolder {
		@BindView(R.id.btn_advertiseWv) public Button btnAd;
		@BindView(R.id.iv_home_advertise) public SelectableRoundedImageView ivAd;
		@BindView(R.id.iv_companyPicture) public SelectableRoundedImageView ivComp;
		@BindView(R.id.iv_advertiseNew) public ImageView ivNew;
		@BindView(R.id.tv_advertise) public TextView tvAd;
		@BindView(R.id.tv_companyId) public TextView tvComp;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}

package com.puzi.puzi.ui.advertisement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.company.CompanyVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.company.CompanyDialog;
import com.puzi.puzi.utils.PuziUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementListAdapter extends BaseAdapter {

	private static final int VIEW_ADVERTISE = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private Context context = null;
	private LayoutInflater inflater;
	private List<ReceivedAdvertiseVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public AdvertisementListAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addAdvertiseFirst(ReceivedAdvertiseVO advertiseVO) {
		empty = false;
		list.add(0, advertiseVO);
	}

	public void addAdvertiseList(List<ReceivedAdvertiseVO> advertiseVOList) {
		if(empty && advertiseVOList.size() > 0) {
			empty = false;
		}
		list.addAll(advertiseVOList);
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
		return VIEW_ADVERTISE;
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
				case VIEW_ADVERTISE:
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
			case VIEW_ADVERTISE:
				final ReceivedAdvertiseVO receivedAdvertise = (ReceivedAdvertiseVO) getItem(position);

				Log.i("INFO", "URL Company : " + receivedAdvertise.getLinkPreviewUrl());

				BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), viewHolder.ivAd);
				BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), viewHolder.ivComp);

				final CompanyVO company = receivedAdvertise.getCompanyInfoDTO();

				viewHolder.tvAd.setText(receivedAdvertise.getSendComment());
				viewHolder.tvComp.setText(company.getCompanyAlias());

				Log.i(PuziUtils.INFO, "adapter.getSaved() : " + receivedAdvertise.getSaved());
				Log.i(PuziUtils.INFO, "adapter.getToday() : " + receivedAdvertise.getToday());

				if(receivedAdvertise.getSaved() && receivedAdvertise.getToday()) {
					viewHolder.ivNew.setBackgroundResource(R.drawable.check);
				} else if(!receivedAdvertise.getSaved() && receivedAdvertise.getToday()) {
					viewHolder.ivNew.setBackgroundResource(R.drawable.new_);
				} else {
					viewHolder.ivNew.setVisibility(View.GONE);
				}

				viewHolder.btnAd.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changedDetail(receivedAdvertise);
					}
				});

				viewHolder.btnCompany.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changedCompany(company);
					}
				});
				break;
		}

		return v;
	}

	public void changedDetail(ReceivedAdvertiseVO receivedAdvertise) {
		Intent intent = new Intent(context, AdvertisementDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("advertise", receivedAdvertise);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public void changedCompany(CompanyVO company) {
		Intent intent = new Intent(context, CompanyDialog.class);
		intent.putExtra("name", company.getCompanyAlias());
		intent.putExtra("url", company.getPictureUrl());
		context.startActivity(intent);
	}

	public void changeSaved(int adId, boolean saved) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getReceivedAdvertiseId() == adId) {
				list.get(i).setSaved(saved);
			}
		}
		notifyDataSetChanged();
	}

	public class ViewHolder {
		@BindView(R.id.btn_company) public Button btnCompany;
		@BindView(R.id.btn_advertiseWv) public Button btnAd;
		@BindView(R.id.iv_home_advertise) public ImageView ivAd;
		@BindView(R.id.iv_companyPicture) public ImageView ivComp;
		@BindView(R.id.iv_advertiseNew) public ImageView ivNew;
		@BindView(R.id.tv_advertise) public TextView tvAd;
		@BindView(R.id.tv_companyId) public TextView tvComp;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}

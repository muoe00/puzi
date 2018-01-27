package com.puzi.puzi.ui.advertisement;

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

import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.company.CompanyVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.company.CompanyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementListAdapter extends BaseAdapter {

	private static final int VIEW_ADVERTISE = 0;
	private static final int VIEW_ADVERTISE_NEW = 1;
	private static final int VIEW_ADVERTISE_SAVED = 2;
	private static final int VIEW_EMPTY = 3;
	private static final int VIEW_PROGRESS = 4;

	private BaseFragmentActivity activity;
	private LayoutInflater inflater;
	private List<ReceivedAdvertiseVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public AdvertisementListAdapter(BaseFragmentActivity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void addAdvertiseList(List<ReceivedAdvertiseVO> advertiseVOList) {
		if(empty && advertiseVOList.size() > 0) {
			empty = false;
		}
		list.addAll(advertiseVOList);
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
		return 5;
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

		final ReceivedAdvertiseVO receivedAdvertise = (ReceivedAdvertiseVO) getItem(position);

		if(receivedAdvertise.isSaved() && receivedAdvertise.isToday()) {
			return VIEW_ADVERTISE_SAVED;
		} else if(!receivedAdvertise.isSaved() && receivedAdvertise.isToday()) {
			return VIEW_ADVERTISE_NEW;
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

				case VIEW_ADVERTISE_NEW:
					v = inflater.inflate(R.layout.fragment_advertisement_item_new, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_ADVERTISE_SAVED:
					v = inflater.inflate(R.layout.fragment_advertisement_item_saved, null);
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
			case VIEW_EMPTY:
				break;
			case VIEW_PROGRESS:
				break;
			default:

				final ReceivedAdvertiseVO receivedAdvertise = (ReceivedAdvertiseVO) getItem(position);
				Log.i("INFO", "URL Company : " + receivedAdvertise.getLinkPreviewUrl());

				BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), viewHolder.ivAd);
				BitmapUIL.load(receivedAdvertise.getCompanyInfoDTO().getPictureUrl(), viewHolder.ivComp);

				final CompanyVO company = receivedAdvertise.getCompanyInfoDTO();

				viewHolder.tvAd.setText(receivedAdvertise.getSendComment());
				viewHolder.tvAd.setSelected(true);
				viewHolder.tvComp.setText(company.getCompanyAlias());
				viewHolder.btnAd.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changedDetail(receivedAdvertise);
					}
				});
				viewHolder.ivComp.setOnClickListener(new View.OnClickListener() {
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
		Intent intent = new Intent(activity, AdvertisementDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("advertise", receivedAdvertise);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.doAnimationGoRight();
	}

	public void changedCompany(CompanyVO company) {
		Intent intent = new Intent(activity, CompanyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("company", company);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.doAnimationGoRight();
	}

	public void changeSaved(int adId, boolean saved) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getReceivedAdvertiseId() == adId) {
				list.get(i).setSaved(saved);
			}
		}
		notifyDataSetChanged();
	}

	public void addFirst(ReceivedAdvertiseVO receivedAdvertiseVO) {
		list.add(0, receivedAdvertiseVO);
		notifyDataSetChanged();
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

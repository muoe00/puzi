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

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementListAdapter extends BaseAdapter {

	private static final int VIEW_CHANNEL = 0;
	private static final int VIEW_PROGRESS = 1;

	private Context context = null;
	private LayoutInflater inflater;
	private List<ReceivedAdvertiseVO> advertiseList;

	public AdvertisementListAdapter(Context context, List<ReceivedAdvertiseVO> list) {
		this.context = context;
		this.advertiseList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.i(PuziUtils.INFO, "AdvertisementListAdapter advertiseList size : " + advertiseList.size());
	}

	public int getViewTypeCount() {
		return 1;
	}

	public int getItemViewType(int position) { return 1; }

	/*public void startProgress() {
		if(typeList.size() == 0 || VIEW_PROGRESS != typeList.get(typeList.size()-1)) {
			list.add(new Object());
			typeList.add(VIEW_PROGRESS);
			notifyDataSetChanged();
		}
	}

	public void stopProgress() {
		if(VIEW_PROGRESS == typeList.get(typeList.size()-1)) {
			list.remove(list.size()-1);
			typeList.remove(typeList.size()-1);
			notifyDataSetChanged();
		}
	}*/

	@Override
	public int getCount() {
		return advertiseList.size();
	}

	@Override
	public Object getItem(int position) {
		return advertiseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder = null;

		if(v == null) {
			v = inflater.inflate(R.layout.fragment_advertisement_item, null);
			viewHolder = new ViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

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

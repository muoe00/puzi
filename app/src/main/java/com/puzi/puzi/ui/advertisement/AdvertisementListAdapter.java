package com.puzi.puzi.ui.advertisement;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

	private static final int VIEW_ADVERTISE = 0;
	private static final int VIEW_PROGRESS = 1;

	Unbinder unbinder;

	@BindView(R.id.iv_home_advertise) public ImageView ivAd;
	@BindView(R.id.iv_companyPicture) public ImageView ivComp;
	@BindView(R.id.iv_advertiseNew) public ImageView ivNew;
	@BindView(R.id.tv_advertise) public TextView tvAd;
	@BindView(R.id.tv_companyId) public TextView tvComp;

	private LayoutInflater inflater;
	private Context context = null;
	private CompanyVO company;
	private List<ReceivedAdvertiseVO> advertiseList;
	private ReceivedAdvertiseVO receivedAdvertise;
	private Handler handler = new Handler();

	public AdvertisementListAdapter(Context context, List<ReceivedAdvertiseVO> list) {
		this.context = context;
		this.advertiseList = list;
		Log.i(PuziUtils.INFO, "AdvertisementListAdapter advertiseList size : " + advertiseList.size());
	}

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
		if(convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_advertisement_item, parent, false);
		}

		unbinder = ButterKnife.bind(this, convertView);

		receivedAdvertise = advertiseList.get(position);

		Log.i("INFO", "URL Company : " + receivedAdvertise.getLinkPreviewUrl());

		BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), ivAd);
		BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), ivComp);

		company = receivedAdvertise.getCompanyInfoDTO();

		tvAd.setText(receivedAdvertise.getSendComment());
		tvComp.setText(company.getCompanyAlias());

		if(receivedAdvertise.getIsNew()) {
			ivNew.setVisibility(View.VISIBLE);
		} else {
			ivNew.setVisibility(View.GONE);
		}

		return convertView;
	}

	@OnClick(R.id.btn_advertiseWv)
	public void changedDetail() {
		Intent intent = new Intent(context, AdvertisementDetailActivity.class);
		intent.putExtra("channelId", receivedAdvertise.getChannelId());
		intent.putExtra("quiz", receivedAdvertise.getQuiz());
		intent.putExtra("firstAnswer", receivedAdvertise.getAnswerOne());
		intent.putExtra("secondAnswer", receivedAdvertise.getAnswerTwo());
		intent.putExtra("url", receivedAdvertise.getLink());
		intent.putExtra("companyId", company.getCompanyAlias());
		context.startActivity(intent);
	}

	@OnClick(R.id.btn_company)
	public void changedCompany() {
		Intent intent = new Intent(context, CompanyDialog.class);
		intent.putExtra("name", company.getCompanyAlias());
		intent.putExtra("url", company.getPictureUrl());
		context.startActivity(intent);
	}

}

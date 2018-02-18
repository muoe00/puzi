package kr.puzi.puzi.ui.advertisement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import butterknife.BindView;
import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.biz.company.CompanyVO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.company.CompanyActivity;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementListAdapter extends CustomPagingAdapter<ReceivedAdvertiseVO> {

	private Activity activity;
	private Context context;
	private LinearLayoutManager manager;

	public AdvertisementListAdapter(Activity activity, int layoutResource, int layoutResource2, int layoutResource3, int layoutResource4, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
		super(activity, layoutResource, layoutResource2, layoutResource3, 0, 0, listView, scrollView, listHandler, moreBtn);
		this.activity = activity;
		this.context = activity.getApplicationContext();
	}

	public void changedDetail(ReceivedAdvertiseVO receivedAdvertise) {
		Intent intent = new Intent(activity, AdvertisementDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("advertise", receivedAdvertise);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		((MainActivity)activity).doAnimationGoRight();
	}

	public void changedCompany(CompanyVO company) {
		Intent intent = new Intent(activity, CompanyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("company", company);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		((MainActivity)activity).doAnimationGoRight();
	}

	public void changeSaved(int adId, boolean saved) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getReceivedAdvertiseId() == adId) {
				list.get(i).setSaved(saved);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return 6;
	}

	@Override
	public int getItemViewType(int position) {
		int type = super.getItemViewType(position);
		if(VIEW_LIST != type) {
			return type;
		}

		ReceivedAdvertiseVO receivedAdvertise = getItem(position);

		if(receivedAdvertise.isSaved() && receivedAdvertise.isToday()) {
			return VIEW_LIST_3;
		} else if(!receivedAdvertise.isSaved() && receivedAdvertise.isToday()) {
			return VIEW_LIST_2;
		}

		return VIEW_LIST;
	}

	@Override
	protected void setView(Holder holder, final ReceivedAdvertiseVO item, int position) {
		final AdvertisementListAdapter.ViewHolder viewHolder = (AdvertisementListAdapter.ViewHolder) holder;

		final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

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

	}

	@Override
	public void setView2(Holder holder, final ReceivedAdvertiseVO item, int position) {
		final AdvertisementListAdapter.ViewHolder2 viewHolder = (AdvertisementListAdapter.ViewHolder2) holder;

		final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

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

	}

	@Override
	public void setView3(Holder holder, final ReceivedAdvertiseVO item, int position) {
		final AdvertisementListAdapter.ViewHolder3 viewHolder = (AdvertisementListAdapter.ViewHolder3) holder;

		final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

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

	}

	@Override
	protected Holder createHolder(View v) {
		return new AdvertisementListAdapter.ViewHolder(v);
	}

	@Override
	protected Holder createHolder2(View v) {
		return new AdvertisementListAdapter.ViewHolder2(v);
	}

	@Override
	protected Holder createHolder3(View v) {
		return new AdvertisementListAdapter.ViewHolder3(v);
	}

	class ViewHolder extends Holder {

		@BindView(kr.puzi.puzi.R.id.btn_advertiseWv) public Button btnAd;
		@BindView(kr.puzi.puzi.R.id.iv_home_advertise) public SelectableRoundedImageView ivAd;
		@BindView(kr.puzi.puzi.R.id.iv_companyPicture) public SelectableRoundedImageView ivComp;
		@BindView(kr.puzi.puzi.R.id.iv_advertiseNew) public ImageView ivNew;
		@BindView(kr.puzi.puzi.R.id.tv_advertise) public TextView tvAd;
		@BindView(kr.puzi.puzi.R.id.tv_companyId) public TextView tvComp;

		public ViewHolder(View view) {
			super(view);
		}
	}

	class ViewHolder2 extends Holder {

		@BindView(kr.puzi.puzi.R.id.btn_advertiseWv) public Button btnAd;
		@BindView(kr.puzi.puzi.R.id.iv_home_advertise) public SelectableRoundedImageView ivAd;
		@BindView(kr.puzi.puzi.R.id.iv_companyPicture) public SelectableRoundedImageView ivComp;
		@BindView(kr.puzi.puzi.R.id.iv_advertiseNew) public ImageView ivNew;
		@BindView(kr.puzi.puzi.R.id.tv_advertise) public TextView tvAd;
		@BindView(kr.puzi.puzi.R.id.tv_companyId) public TextView tvComp;

		public ViewHolder2(View view) {
			super(view);
		}
	}

	class ViewHolder3 extends Holder {

		@BindView(kr.puzi.puzi.R.id.btn_advertiseWv) public Button btnAd;
		@BindView(kr.puzi.puzi.R.id.iv_home_advertise) public SelectableRoundedImageView ivAd;
		@BindView(kr.puzi.puzi.R.id.iv_companyPicture) public SelectableRoundedImageView ivComp;
		@BindView(kr.puzi.puzi.R.id.iv_advertiseNew) public ImageView ivNew;
		@BindView(kr.puzi.puzi.R.id.tv_advertise) public TextView tvAd;
		@BindView(kr.puzi.puzi.R.id.tv_companyId) public TextView tvComp;

		public ViewHolder3(View view) {
			super(view);
		}
	}

}

package com.puzi.puzi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.company.CompanyDialog;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.advertisement.AdvertisementDetailActivity;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class HomeGridAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ImageView ivNew, ivAd, ivComp;
	private TextView tvAd, tvComp;
	private Button btnAd, btnComp;

	private Context context = null;
	private List<ReceivedAdvertiseVO> advertiseList;
	private ReceivedAdvertiseVO receivedAdvertise;
	private Handler handler = new Handler();

	public HomeGridAdapter(Context context, List<ReceivedAdvertiseVO> list) {
		this.context = context;
		this.advertiseList = list;
		Log.i("DEBUG", "HomeGridAdapter advertiseList size : " + advertiseList.size());
	}

	public void initComponents(View view) {
		ivNew = (ImageView) view.findViewById(R.id.iv_advertiseNew);
		ivAd = (ImageView) view.findViewById(R.id.iv_home_advertise);

		/*GradientDrawable drawable=
			(GradientDrawable) context.getDrawable(R.drawable.image_line);

		ivAdvertise.setBackground(drawable);*/

		tvAd = (TextView) view.findViewById(R.id.tv_advertise);
		btnAd = (Button) view.findViewById(R.id.btn_advertiseWv);

		ivComp = (ImageView) view.findViewById(R.id.iv_companyPicture);
		tvComp = (TextView) view.findViewById(R.id.tv_companyId);
		btnComp = (Button) view.findViewById(R.id.btn_company);
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

		initComponents(convertView);
		receivedAdvertise = advertiseList.get(position);

		Log.i("DEBUG", "URL Company : " + receivedAdvertise.getPictureUrl());

		BitmapUIL.load(receivedAdvertise.getLinkPreviewUrl(), ivAd);
		BitmapUIL.load(receivedAdvertise.getPictureUrl(), ivComp);

		tvAd.setText(receivedAdvertise.getSendComment());
		btnAd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AdvertisementDetailActivity.class);
				intent.putExtra("url", receivedAdvertise.getLink());
				context.startActivity(intent);
			}
		});

		tvComp.setText(receivedAdvertise.getCompanyName());
		btnComp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CompanyDialog.class);
				intent.putExtra("name", receivedAdvertise.getCompanyName());
				intent.putExtra("url", receivedAdvertise.getPictureUrl());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}

package com.puzi.puzi;

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
import com.puzi.puzi.model.ChannelVO;
import com.puzi.puzi.model.CompanyVO;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-16.
 */

public class ChannelListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<ChannelVO> channelList;
	private ChannelVO channel;
	private CompanyVO company;
	private ImageView ivCompany, ivAd;
	private TextView tvCompany, tvComment;
	private Button btnChannel;
	private Handler handler = new Handler();

	public ChannelListAdapter(Context context, List<ChannelVO> list) {
		this.context = context;
		this.channelList = list;
		Log.i("DEBUG", "ChannelListAdapter advertiseList size : " + channelList.size());
	}

	public void initComponents(View view) {
		ivCompany = (ImageView) view.findViewById(R.id.iv_ch_companyPicture);
		ivAd = (ImageView) view.findViewById(R.id.iv_channelAdvertise);

		tvCompany = (TextView) view.findViewById(R.id.tv_companyId);
		tvComment = (TextView) view.findViewById(R.id.tv_channelComment);

		btnChannel = (Button) view.findViewById(R.id.btn_advertiseWv);
	}

	@Override
	public int getCount() {
		return channelList.size();
	}

	@Override
	public Object getItem(int position) {
		return channelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_channel_item, parent, false);
		}

		initComponents(convertView);

		channel = channelList.get(position);
		company = channel.getCompany();

		tvCompany.setText(company.getCompanyName());

		BitmapUIL.load(company.getPictureUrl(), ivCompany);
		BitmapUIL.load(channel.getPictureUrl(), ivAd);

		Log.i("DEBUG", "channel.getPictureUrl() : " + channel.getPictureUrl());

		tvComment.setText(channel.getComment());
		btnChannel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ChannelDetailActivity.class);
				Log.i("DEBUG", "channel id : " + channel.getChannelId());
				intent.putExtra("Id", channel.getChannelId());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}

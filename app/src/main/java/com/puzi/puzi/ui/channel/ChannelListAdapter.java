package com.puzi.puzi.ui.channel;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-07-16.
 */

public class ChannelListAdapter extends BaseAdapter {

	private static final int VIEW_CHANNEL = 0;
	public static final int VIEW_EDITORSPAGE = 1;
	private static final int VIEW_PROGRESS = 2;

	private LayoutInflater inflater;
	private Activity activity;
	private List<Object> list = new ArrayList();
	private List<Integer> typeList = new ArrayList();

	public ChannelListAdapter(Activity activity) {
		this.inflater = activity.getLayoutInflater();
		this.activity = activity;
	}

	public void startProgress() {
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
	}

	public void removeAll() {
		list = newArrayList();
		typeList = newArrayList();
		notifyDataSetChanged();
	}

	public void addChannel(List<ChannelVO> channelList) {
		synchronized (list) {
			List<ChannelVO> channelSet = newArrayList();
			for(ChannelVO channelVO : channelList) {
				channelSet.add(channelVO);
				if(channelSet.size() >= 2) {
					list.add(newArrayList(channelSet));
					typeList.add(VIEW_CHANNEL);
					channelSet = newArrayList();
				}
			}
			if(channelSet.size() != 0){
				list.add(newArrayList(channelSet));
				typeList.add(VIEW_CHANNEL);
			}
		}
	}

	public void addEditorsPage(List<ChannelEditorsPageVO> editorsPageList) {
		synchronized (list) {
			for(ChannelEditorsPageVO editorsPageVO : editorsPageList) {
				list.add(editorsPageVO);
				typeList.add(VIEW_EDITORSPAGE);
			}
		}
	}

	@Override
	public int getCount() {
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

	public int getViewTypeCount() {
		return 3;
	}

	public int getItemViewType(int position) {
		return typeList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ChannelViewHolder channelHolder = null;
		EditorsPageViewHolder editorsPageHolder = null;
		int viewType = getItemViewType(position);

		if (v == null) {
			switch (viewType) {
				case VIEW_CHANNEL:
					v = inflater.inflate(R.layout.item_channel_list_channel, null);
					channelHolder = new ChannelViewHolder(v);
					v.setTag(channelHolder);
					break;

				case VIEW_EDITORSPAGE:
					v = inflater.inflate(R.layout.item_channel_list_editorspage, null);
					editorsPageHolder = new EditorsPageViewHolder(v);
					v.setTag(editorsPageHolder);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}

		} else {
			switch (viewType) {
				case VIEW_CHANNEL:
					channelHolder = (ChannelViewHolder) v.getTag();
					break;

				case VIEW_EDITORSPAGE:
					editorsPageHolder = (EditorsPageViewHolder) v.getTag();
					break;

				case VIEW_PROGRESS:
					break;
			}
		}

		switch (viewType) {
			case VIEW_CHANNEL:
				final List<ChannelVO> channelList = (List) getItem(position);
				final ChannelVO firstChannel = channelList.get(0);
				BitmapUIL.load(firstChannel.getPictureUrl(), channelHolder.ibtnImage1);
				channelHolder.btnTitle1.setText(firstChannel.getTitle());
				BitmapUIL.load(firstChannel.getCompanyInfoDTO().getPictureUrl(), channelHolder.ibtnCompany1);
				channelHolder.tvScore1.setText(firstChannel.getAverageScore() + "/5");

				View.OnClickListener listener1 = new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity, ChannelDetailActivity.class);
						intent.putExtra("channelVO", firstChannel);
						activity.startActivity(intent);
					}
				};
				channelHolder.ibtnImage1.setOnClickListener(listener1);
				channelHolder.btnTitle1.setOnClickListener(listener1);
				UIUtils.setEvaluateStarScoreImage(firstChannel.getAverageScore(), channelHolder.ivStar1, channelHolder.ivStar2, channelHolder.ivStar3,
					channelHolder.ivStar4, channelHolder.ivStar5, R.drawable.oval_2_copy_3, R.drawable.oval_2_copy_7);

				if(channelList.size() > 1) {
					final ChannelVO secondChannel = channelList.get(1);
					BitmapUIL.load(secondChannel.getPictureUrl(), channelHolder.ibtnImage2);
					channelHolder.btnTitle2.setText(secondChannel.getTitle());
					BitmapUIL.load(secondChannel.getCompanyInfoDTO().getPictureUrl(), channelHolder.ibtnCompany2);
					channelHolder.tvScore2.setText(secondChannel.getAverageScore() + "/5");

					View.OnClickListener listener2 = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(activity, ChannelDetailActivity.class);
							intent.putExtra("channelVO", secondChannel);
							activity.startActivity(intent);
						}
					};
					channelHolder.ibtnImage2.setOnClickListener(listener2);
					channelHolder.btnTitle2.setOnClickListener(listener2);
					UIUtils.setEvaluateStarScoreImage(secondChannel.getAverageScore(), channelHolder.ivStar21, channelHolder.ivStar22, channelHolder.ivStar23,
						channelHolder.ivStar24, channelHolder.ivStar25, R.drawable.oval_2_copy_3, R.drawable.oval_2_copy_7);
				}

				break;

			case VIEW_EDITORSPAGE:
				ChannelEditorsPageVO channelEditorsPageVO = (ChannelEditorsPageVO) getItem(position);
				BitmapUIL.load(channelEditorsPageVO.getPreviewUrl(), editorsPageHolder.ivImage);
				editorsPageHolder.tvTitle.setText(channelEditorsPageVO.getTitle());
				editorsPageHolder.tvName.setText(channelEditorsPageVO.getCreatedBy());
				break;
		}

		return v;
	}

	public class ChannelViewHolder {
		@BindView(R.id.ibtn_item_channel_image_1) public ImageButton ibtnImage1;
		@BindView(R.id.ibtn_item_channel_image_2) public ImageButton ibtnImage2;
		@BindView(R.id.tv_item_channel_title_1) public Button btnTitle1;
		@BindView(R.id.tv_item_channel_title_2) public Button btnTitle2;
		@BindView(R.id.ibtn_item_channel_company_image_1) public ImageButton ibtnCompany1;
		@BindView(R.id.ibtn_item_channel_company_image_2) public ImageButton ibtnCompany2;
		@BindView(R.id.tv_item_channel_score_1) public TextView tvScore1;
		@BindView(R.id.tv_item_channel_score_2) public TextView tvScore2;
		@BindView(R.id.iv_item_channel_company_star_1) public ImageView ivStar1;
		@BindView(R.id.iv_item_channel_company_star_2) public ImageView ivStar2;
		@BindView(R.id.iv_item_channel_company_star_3) public ImageView ivStar3;
		@BindView(R.id.iv_item_channel_company_star_4) public ImageView ivStar4;
		@BindView(R.id.iv_item_channel_company_star_5) public ImageView ivStar5;
		@BindView(R.id.iv_item_channel_company_star2_1) public ImageView ivStar21;
		@BindView(R.id.iv_item_channel_company_star2_2) public ImageView ivStar22;
		@BindView(R.id.iv_item_channel_company_star2_3) public ImageView ivStar23;
		@BindView(R.id.iv_item_channel_company_star2_4) public ImageView ivStar24;
		@BindView(R.id.iv_item_channel_company_star2_5) public ImageView ivStar25;

		public ChannelViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public class EditorsPageViewHolder {
		@BindView(R.id.iv_item_channel_editor_image) public ImageView ivImage;
		@BindView(R.id.tv_item_channel_editor_title) public TextView tvTitle;
		@BindView(R.id.tv_item_channel_editor_name) public TextView tvName;

		public EditorsPageViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}

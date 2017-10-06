package com.puzi.puzi.ui.company;

import android.app.Activity;
import android.content.Intent;
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
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.channel.ChannelDetailActivity;
import com.puzi.puzi.utils.UIUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JangwonPark on 2017. 10. 6..
 */

public class CompanyChannelAdapter extends BaseAdapter {

	private static final int VIEW_CHANNEL = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private LayoutInflater inflater;
	private BaseFragmentActivity activity;
	private List<ChannelVO> list = new ArrayList();

	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public CompanyChannelAdapter(Activity activity) {
		this.inflater = activity.getLayoutInflater();
		this.activity = (BaseFragmentActivity) activity;
	}

	public void addList(List<ChannelVO> newChannelList) {
		if(newChannelList == null || newChannelList.size() == 0) {
			empty();
			return;
		}
		list.addAll(newChannelList);
		notifyDataSetChanged();
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

	public void empty() {
		if(!empty){
			empty = true;
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
		return VIEW_CHANNEL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder viewHolder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case VIEW_CHANNEL:
					v = inflater.inflate(R.layout.item_channel_list_channel, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_reply, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_CHANNEL:
				final List<ChannelVO> channelList = (List) getItem(position);
				final ChannelVO firstChannel = channelList.get(0);
				BitmapUIL.load(firstChannel.getPictureUrl(), viewHolder.ibtnImage1);
				viewHolder.btnTitle1.setText(firstChannel.getTitle());
				BitmapUIL.load(firstChannel.getCompanyInfoDTO().getPictureUrl(), viewHolder.ibtnCompany1);
				viewHolder.ibtnCompany1.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity, CompanyActivity.class);
						intent.putExtra("company", firstChannel.getCompanyInfoDTO());
						activity.startActivity(intent);
						activity.doAnimationGoRight();
					}
				});
				viewHolder.tvScore1.setText(firstChannel.getAverageScore() + "/5");

				View.OnClickListener listener1 = new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity, ChannelDetailActivity.class);
						intent.putExtra("channelVO", firstChannel);
						activity.startActivity(intent);
						activity.doAnimationGoRight();
					}
				};
				viewHolder.ibtnImage1.setOnClickListener(listener1);
				viewHolder.btnTitle1.setOnClickListener(listener1);
				UIUtils.setEvaluateStarScoreImage(firstChannel.getAverageScore(), viewHolder.ivStar1, viewHolder.ivStar2, viewHolder.ivStar3,
					viewHolder.ivStar4, viewHolder.ivStar5, R.drawable.oval_2_copy_3, R.drawable.oval_2_copy_7);

				if(channelList.size() > 1) {
					final ChannelVO secondChannel = channelList.get(1);
					BitmapUIL.load(secondChannel.getPictureUrl(), viewHolder.ibtnImage2);
					viewHolder.btnTitle2.setText(secondChannel.getTitle());
					BitmapUIL.load(secondChannel.getCompanyInfoDTO().getPictureUrl(), viewHolder.ibtnCompany2);
					viewHolder.ibtnCompany2.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(activity, CompanyActivity.class);
							intent.putExtra("company", secondChannel.getCompanyInfoDTO());
							activity.startActivity(intent);
							activity.doAnimationGoRight();
						}
					});
					viewHolder.tvScore2.setText(secondChannel.getAverageScore() + "/5");

					View.OnClickListener listener2 = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(activity, ChannelDetailActivity.class);
							intent.putExtra("channelVO", secondChannel);
							activity.startActivity(intent);
							activity.doAnimationGoRight();
						}
					};
					viewHolder.ibtnImage2.setOnClickListener(listener2);
					viewHolder.btnTitle2.setOnClickListener(listener2);
					UIUtils.setEvaluateStarScoreImage(secondChannel.getAverageScore(), viewHolder.ivStar21, viewHolder.ivStar22, viewHolder.ivStar23,
						viewHolder.ivStar24, viewHolder.ivStar25, R.drawable.oval_2_copy_3, R.drawable.oval_2_copy_7);
				}
				break;
		}

		return v;
	}

	public class ViewHolder {
		@BindView(R.id.ibtn_item_channel_image_1) public SelectableRoundedImageView ibtnImage1;
		@BindView(R.id.ibtn_item_channel_image_2) public SelectableRoundedImageView ibtnImage2;
		@BindView(R.id.tv_item_channel_title_1) public Button btnTitle1;
		@BindView(R.id.tv_item_channel_title_2) public Button btnTitle2;
		@BindView(R.id.ibtn_item_channel_company_image_1) public SelectableRoundedImageView ibtnCompany1;
		@BindView(R.id.ibtn_item_channel_company_image_2) public SelectableRoundedImageView ibtnCompany2;
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

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}

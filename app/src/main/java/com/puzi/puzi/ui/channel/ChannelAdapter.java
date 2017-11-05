package com.puzi.puzi.ui.channel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.company.CompanyActivity;
import com.puzi.puzi.utils.UIUtils;

/**
 * Created by JangwonPark on 2017. 11. 5..
 */
public class ChannelAdapter extends CustomPagingAdapter<ChannelVO> {

	public ChannelAdapter(Activity activity, int layoutResource, GridView gridView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, gridView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder holder, final ChannelVO item, int position) {
		ViewHolder viewHolder = (ViewHolder) holder;

		final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

		BitmapUIL.load(item.getPictureUrl(), viewHolder.ibtnImage1);
		viewHolder.btnTitle1.setText(item.getTitle());
		viewHolder.btnCompanyName1.setText(item.getCompanyInfoDTO().getCompanyAlias());
		viewHolder.btnCompanyName1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, CompanyActivity.class);
				intent.putExtra("company", item.getCompanyInfoDTO());
				baseFragmentActivity.startActivity(intent);
				baseFragmentActivity.doAnimationGoRight();
			}
		});
		viewHolder.tvScore1.setText(item.getAverageScore() + "/5");

		UIUtils.setEvaluateStarScoreImage(item.getAverageScore(), viewHolder.ivStar1, viewHolder.ivStar2, viewHolder.ivStar3,
			viewHolder.ivStar4, viewHolder.ivStar5, R.drawable.star, R.drawable.star_copy_4);
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	class ViewHolder extends Holder {

		@BindView(R.id.ibtn_item_channel_image_1) public SelectableRoundedImageView ibtnImage1;
		@BindView(R.id.tv_item_channel_title_1) public Button btnTitle1;
		@BindView(R.id.tv_item_channel_company_name_1) public Button btnCompanyName1;
		@BindView(R.id.tv_item_channel_score_1) public TextView tvScore1;
		@BindView(R.id.iv_item_channel_company_star_1) public ImageView ivStar1;
		@BindView(R.id.iv_item_channel_company_star_2) public ImageView ivStar2;
		@BindView(R.id.iv_item_channel_company_star_3) public ImageView ivStar3;
		@BindView(R.id.iv_item_channel_company_star_4) public ImageView ivStar4;
		@BindView(R.id.iv_item_channel_company_star_5) public ImageView ivStar5;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

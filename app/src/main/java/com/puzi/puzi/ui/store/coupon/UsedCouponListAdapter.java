package com.puzi.puzi.ui.store.coupon;

import android.app.Activity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragmentActivity;

public class UsedCouponListAdapter extends CustomPagingAdapter<PurchaseHistoryVO> {

	public UsedCouponListAdapter(Activity activity, int layoutResource, ListView gridView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, gridView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder holder, final PurchaseHistoryVO item, int position) {
		UsedCouponListAdapter.ViewHolder viewHolder = (UsedCouponListAdapter.ViewHolder) holder;

		final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;
	}

	@Override
	protected Holder createHolder(View v) {
		return new UsedCouponListAdapter.ViewHolder(v);
	}

	class ViewHolder extends Holder {

		@BindView(R.id.ibtn_item_channel_image_1)
		public SelectableRoundedImageView ibtnImage1;
		@BindView(R.id.tv_item_channel_title_1)
		public Button btnTitle1;
		@BindView(R.id.tv_item_channel_company_name_1)
		public Button btnCompanyName1;
		@BindView(R.id.tv_item_channel_score_1)
		public TextView tvScore1;
		@BindView(R.id.iv_item_channel_company_star_1)
		public ImageView ivStar1;
		@BindView(R.id.iv_item_channel_company_star_2)
		public ImageView ivStar2;
		@BindView(R.id.iv_item_channel_company_star_3)
		public ImageView ivStar3;
		@BindView(R.id.iv_item_channel_company_star_4)
		public ImageView ivStar4;
		@BindView(R.id.iv_item_channel_company_star_5)
		public ImageView ivStar5;

		public ViewHolder(View view) {
			super(view);
		}
	}

}
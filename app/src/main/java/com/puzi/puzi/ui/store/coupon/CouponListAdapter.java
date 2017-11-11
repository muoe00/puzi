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

/**
 * Created by 170605 on 2017-10-27.
 */

public class CouponListAdapter extends CustomPagingAdapter<PurchaseHistoryVO> {

	public CouponListAdapter(Activity activity, int layoutResource, ListView gridView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, gridView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder holder, final PurchaseHistoryVO item, int position) {
		CouponListAdapter.ViewHolder viewHolder = (CouponListAdapter.ViewHolder) holder;

		final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;


	}

	@Override
	protected Holder createHolder(View v) {
		return new CouponListAdapter.ViewHolder(v);
	}

	class ViewHolder extends Holder {

		@BindView(R.id.iv_coupon_1)
		public SelectableRoundedImageView ibtnImage1;
		@BindView(R.id.tv_coupon_title_1)
		public Button btnTitle1;
		@BindView(R.id.tv_coupon_price_1)
		public Button btnCompanyName1;
		@BindView(R.id.tv_coupon_date_1)
		public TextView tvScore1;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

package com.puzi.puzi.ui.store.puzi.saving;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.StoreSavingItemVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.utils.TextUtils;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */

public class StoreSavingAdapter extends CustomPagingAdapter<StoreSavingItemVO> {

	public StoreSavingAdapter(Activity activity, GridView gridView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, R.layout.item_store_saving_item, gridView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder viewHolder, StoreSavingItemVO item, int position) {
		ViewHolder holder = (ViewHolder) viewHolder;

		BitmapUIL.load(item.getStoreDTO().getPictureUrl(), holder.ivLogo);
		holder.tvLogoName.setText(item.getStoreDTO().getName());
		holder.tvDiscount.setText((item.getDiscountRate()/10) + "%");
		BitmapUIL.load(item.getStoreItemDTO().getPictureUrl(), holder.ivItem);
		holder.tvItemName.setText(item.getStoreItemDTO().getName());
		holder.tvTarget.setText(TextUtils.addComma(item.getTargetPoint()) + "P/" + item.getTargetMyToday() + "회");
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(R.id.iv_store_saving_logo) public ImageView ivLogo;
		@BindView(R.id.tv_store_saving_logo_name) public TextView tvLogoName;
		@BindView(R.id.iv_store_saving_discount) public TextView tvDiscount;
		@BindView(R.id.iv_store_saving_item) public ImageView ivItem;
		@BindView(R.id.tv_store_saving_item_name) public TextView tvItemName;
		@BindView(R.id.tv_store_saving_item_target) public TextView tvTarget;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

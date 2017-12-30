package com.puzi.puzi.ui.store.puzi.challenge;

import android.app.Activity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.StoreChallengeItemVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.utils.TextUtils;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */

public class StoreChallengeAdapter extends CustomPagingAdapter<StoreChallengeItemVO> {

	public StoreChallengeAdapter(Activity activity, GridView gridView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, R.layout.item_store_challenge_item, gridView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder viewHolder, StoreChallengeItemVO item, int position) {
		ViewHolder holder = (ViewHolder) viewHolder;

		BitmapUIL.load(item.getStoreDTO().getPictureUrl(), holder.ivLogo);
		holder.tvLogoName.setText(item.getStoreDTO().getName());
		holder.tvCount.setText(TextUtils.addComma(item.getChallengeCount()) + "명");
		BitmapUIL.load(item.getStoreItemDTO().getPictureUrl(), holder.ivItem);
		holder.tvItemName.setText(item.getStoreItemDTO().getName() + "(" + item.getQuantity() + "개)");
		holder.tvItemPrice.setText(TextUtils.addComma(item.getPrice()) + "P");
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(R.id.iv_store_challenge_logo) public ImageView ivLogo;
		@BindView(R.id.tv_store_challenge_logo_name) public TextView tvLogoName;
		@BindView(R.id.tv_store_challenge_count) public TextView tvCount;
		@BindView(R.id.iv_store_challenge_item) public ImageView ivItem;
		@BindView(R.id.iv_store_challenge_item_name) public TextView tvItemName;
		@BindView(R.id.iv_store_challenge_item_price) public TextView tvItemPrice;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

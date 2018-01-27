package kr.puzi.puzi.ui.store.puzi.challenge;

import android.app.Activity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import kr.puzi.puzi.biz.store.puzi.StoreChallengeItemVO;
import kr.puzi.puzi.utils.TextUtils;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.ui.CustomPagingAdapter;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */

public class StoreChallengeAdapter extends CustomPagingAdapter<StoreChallengeItemVO> {

	public StoreChallengeAdapter(Activity activity, GridView gridView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, kr.puzi.puzi.R.layout.item_store_challenge_item, gridView, scrollView, listHandler);
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

		@BindView(kr.puzi.puzi.R.id.iv_store_challenge_logo) public ImageView ivLogo;
		@BindView(kr.puzi.puzi.R.id.tv_store_challenge_logo_name) public TextView tvLogoName;
		@BindView(kr.puzi.puzi.R.id.tv_store_challenge_count) public TextView tvCount;
		@BindView(kr.puzi.puzi.R.id.iv_store_challenge_item) public ImageView ivItem;
		@BindView(kr.puzi.puzi.R.id.iv_store_challenge_item_name) public TextView tvItemName;
		@BindView(kr.puzi.puzi.R.id.iv_store_challenge_item_price) public TextView tvItemPrice;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

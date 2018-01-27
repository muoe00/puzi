package kr.puzi.puzi.ui.store.puzi;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;

import kr.puzi.puzi.biz.store.puzi.StorePuziItemVO;
import kr.puzi.puzi.ui.CustomPagingAdapter;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
public class StoreItemAdapter extends CustomPagingAdapter<StorePuziItemVO> {

	public StoreItemAdapter(Activity activity, ListView listView, ListHandler listHandler) {
		super(activity, kr.puzi.puzi.R.layout.item_block_company, listView, listHandler);
	}

	@Override
	protected void setView(Holder viewHolder, StorePuziItemVO item, int position) {
		ViewHolder holder = (ViewHolder) viewHolder;

	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(kr.puzi.puzi.R.id.ibtn_block_company_preview) public SelectableRoundedImageView ibtnPreview;
		@BindView(kr.puzi.puzi.R.id.tv_block_company_name) public TextView tvName;
		@BindView(kr.puzi.puzi.R.id.ibtn_block_company_remove) public ImageButton ibtnRemove;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

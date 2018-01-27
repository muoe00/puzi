package kr.puzi.puzi.ui.store.item;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.utils.TextUtils;
import kr.puzi.puzi.biz.store.StoreItemVO;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class StoreItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Activity activity;
	private List<StoreItemVO> storeItemList = newArrayList();

	public StoreItemAdapter(Activity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void addList(List<StoreItemVO> storeItemList) {
		this.storeItemList.addAll(storeItemList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return storeItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return storeItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder = null;

		StoreItemVO storeItemVO = (StoreItemVO) getItem(position);

		if(v == null) {
			v = inflater.inflate(kr.puzi.puzi.R.layout.item_store_item, null);
			viewHolder = new ViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		BitmapUIL.load(storeItemVO.getPictureUrl(), viewHolder.ivPreview);
		viewHolder.tvName.setText(storeItemVO.getName());
		viewHolder.tvPrice.setText(TextUtils.addComma(storeItemVO.getPrice()) + "원");

		return v;
	}

	public class ViewHolder {
		@BindView(kr.puzi.puzi.R.id.iv_store_item_preview) public ImageView ivPreview;
		@BindView(kr.puzi.puzi.R.id.tv_store_item_name) public TextView tvName;
		@BindView(kr.puzi.puzi.R.id.tv_store_item_price) public TextView tvPrice;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}


}

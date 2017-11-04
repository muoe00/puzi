package com.puzi.puzi.ui.store.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreItemVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.utils.TextUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class StoreItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseActivity activity;
	private List<StoreItemVO> storeItemList = newArrayList();

	public StoreItemAdapter(BaseActivity activity) {
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
			v = inflater.inflate(R.layout.item_store_item, null);
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
		@BindView(R.id.iv_store_item_preview) public ImageView ivPreview;
		@BindView(R.id.tv_store_item_name) public TextView tvName;
		@BindView(R.id.tv_store_item_price) public TextView tvPrice;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}


}

package com.puzi.puzi.ui.store;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.image.BitmapUIL;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class StoreGroupAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Activity activity;
	private List<StoreVO> storeList = newArrayList();

	public StoreGroupAdapter(FragmentActivity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void add(StoreVO store) {
		storeList.add(store);
	}

	@Override
	public int getCount() {
		return storeList.size();
	}

	@Override
	public Object getItem(int position) {
		return storeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder = null;

		StoreVO store = (StoreVO) getItem(position);

		if(v == null) {
			v = inflater.inflate(R.layout.item_store_brand_single, null);
			viewHolder = new ViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvBrand.setText(store.getName());
		if(store.getStoreType().isWithdraw()) {
			viewHolder.ivBrand.setImageResource(R.drawable.withdraw_img);
		} else {
			BitmapUIL.load(store.getPictureUrl(), viewHolder.ivBrand);
		}

		return v;
	}

	public class ViewHolder {
		@BindView(R.id.tv_item_store_brand_single) public TextView tvBrand;
		@BindView(R.id.iv_item_store_brand_single) public ImageView ivBrand;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}


}

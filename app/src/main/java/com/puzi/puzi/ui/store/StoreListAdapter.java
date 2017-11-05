package com.puzi.puzi.ui.store;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreType;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.store.item.StoreItemActivity;
import com.puzi.puzi.ui.store.withdraw.WithdrawActivity;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class StoreListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseFragmentActivity activity;
	private List<StoreType> storeTypeList = newArrayList();
	private Map<StoreType, StoreGroupAdapter> storeMap = newHashMap();

	public StoreListAdapter(Activity activity) {
		this.activity = (BaseFragmentActivity) activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void addList(List<StoreVO> storeList) {
		for(StoreVO storeVO : storeList) {
			if(storeMap.containsKey(storeVO.getStoreType())) {
				StoreGroupAdapter adapter = storeMap.get(storeVO.getStoreType());
				adapter.add(storeVO);
			} else {
				StoreGroupAdapter adapter = new StoreGroupAdapter(activity);
				adapter.add(storeVO);
				storeMap.put(storeVO.getStoreType(), adapter);

				storeTypeList.add(storeVO.getStoreType());
			}
		}
		addWithdraw();
	}

	private void addWithdraw() {
		StoreGroupAdapter adapter = new StoreGroupAdapter(activity);
		adapter.add(StoreVO.createWithdraw());
		storeMap.put(StoreType.WITHDRAW, adapter);
		storeTypeList.add(StoreType.WITHDRAW);
	}

	@Override
	public int getCount() {
		return storeTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return storeTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder viewHolder = null;

		final StoreType storeType = (StoreType) getItem(position);

		if(v == null) {
			v = inflater.inflate(R.layout.item_store_brand_group, null);
			viewHolder = new ViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvTitle.setText(storeType.getComment());
		viewHolder.gvGroup.setAdapter(storeMap.get(storeType));
		viewHolder.gvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				StoreVO storeVO = (StoreVO) storeMap.get(storeType).getItem(position);
				if(storeVO.getStoreType().isWithdraw()) {
					Intent intent = new Intent(activity, WithdrawActivity.class);
					activity.startActivity(intent);
					activity.doAnimationGoRight();
				} else {
					Intent intent = new Intent(activity, StoreItemActivity.class);
					intent.putExtra("storeVOJson", new Gson().toJson(storeVO));
					activity.startActivity(intent);
					activity.doAnimationGoRight();
				}
			}
		});

		return v;
	}

	public class ViewHolder {
		@BindView(R.id.tv_store_brand_group_title) public TextView tvTitle;
		@BindView(R.id.gv_store_brand_group) public GridView gvGroup;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}


}

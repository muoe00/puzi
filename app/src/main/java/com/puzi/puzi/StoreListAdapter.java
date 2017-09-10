package com.puzi.puzi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.puzi.puzi.model.StoreVO;

import java.util.*;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class StoreListAdapter extends BaseAdapter {

	private final static String MOVIE = "영화",
		CONVENIENCE_STORE = "편의점",
		BAKERY = "베이커리",
		GIFT_CARD = "상품권",
		EAT_OUT = "외식",
		CAR_REFUEL = "주유권",
		CAFE = "카페",
		COSMETICS = "화장품";
	private TextView tvCategory;
	private GridView gvBrand;
	private StoreGridAdapter storeGridAdapter;
	private LayoutInflater inflater;
	private Context context;
	private Map<String, List<StoreVO>> storeMap;
	private List<StoreVO> storeList;
	private List<String> categoryList = new ArrayList<String>();

	public StoreListAdapter(Context context, Map<String, List<StoreVO>> map) {
		this.context = context;
		this.storeMap = map;

		Iterator<String> iterator = storeMap.keySet().iterator();
		while(iterator.hasNext()) {
			this.categoryList.add(iterator.next());
		}

		Log.i("DEBUG", "StoreListAdapter storeMap size : " + storeMap.size());
	}

	public void initComponents(View view) {
		tvCategory = (TextView) view.findViewById(R.id.tv_store_category);
		gvBrand = (GridView) view.findViewById(R.id.gv_storeBrand);
	}

	@Override
	public int getCount() {
		return storeMap.size();
	}

	@Override
	public Object getItem(int position) {
		return storeMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_store_list, parent, false);
		}

		initComponents(convertView);

		storeList = storeMap.get(categoryList.get(position));
		String type = storeList.get(0).getStoreType().toString();

		tvCategory.setText(type);

		Log.i("DEBUG", "StoreListAdapter storeList : " + storeList.toString());

		storeGridAdapter = new StoreGridAdapter(convertView.getContext(), storeList);
		gvBrand.setAdapter(storeGridAdapter);

		return convertView;
	}
}

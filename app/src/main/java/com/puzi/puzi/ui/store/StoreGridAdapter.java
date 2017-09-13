package com.puzi.puzi.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.puzi.puzi.R;
import com.puzi.puzi.model.StoreVO;

import java.util.List;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class StoreGridAdapter extends BaseAdapter {

	private ImageView ivImage;
	private TextView tvBrand;
	private Button btnItem;
	private LayoutInflater inflater;
	private Context context;
	private List<StoreVO> storeList;

	public StoreGridAdapter(Context context, List<StoreVO> list) {
		this.context = context;
		this.storeList = list;
	}

	public void initComponents(View view) {
		ivImage = (ImageView) view.findViewById(R.id.iv_store_image);
		tvBrand = (TextView) view.findViewById(R.id.tv_store_brand);
		btnItem = (Button) view.findViewById(R.id.btn_store_item);
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
		if(convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_store_item, parent, false);
		}

		initComponents(convertView);

		return convertView;
	}
}

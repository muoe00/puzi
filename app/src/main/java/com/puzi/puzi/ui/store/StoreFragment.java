package com.puzi.puzi.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class StoreFragment extends BaseFragment implements View.OnClickListener {

	public static final int STORE = 0;
	public static final int COUPON = 1;
	private List<StoreVO> storeList;
	private Map<String, List<StoreVO>> storeMap = new HashMap<String, List<StoreVO>>();
	private Button btnStore, btnCoupon;
	private ListView lvStore;
	private int index = STORE;
	private String type = "STORE";

	public StoreFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_store, container, false);

		initComponents(view);
		getStoreList(view);

		return view;
	}

	private void initComponents(final View view) {

		lvStore = (ListView) view.findViewById(R.id.lv_store);
		btnStore = (Button) view.findViewById(R.id.btn_store_list);
		btnCoupon = (Button) view.findViewById(R.id.btn_coupon);

		btnStore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				index = STORE;
				type = "STORE";
				getStoreList(view);
			}
		});
		btnCoupon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				index = COUPON;
				type = "COUPON";
				getCouponList(view);
			}
		});
	}

	@Override
	public void onClick(View v) {

		/*Intent intent = null;

		switch (v.getId()) {
			case R.id.btn_withdraw:
				intent = new Intent(getActivity(), WithdrawActivity.class);
				startActivity(intent);
				break;

			case R.id.btn_coupon:
				intent = new Intent(getActivity(), CouponActivity.class);
				startActivity(intent);
				break;
		}*/
	}

	public void getCouponList(final View view) {

	}

	public void getStoreList(final View view) {

		final StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);

		String token = Preference.getProperty(getActivity(), "token");
/*
		final Call<ResponseVO<List<StoreVO>>> call = storeNetworkService.brandList(token);
		call.enqueue(new CustomCallback<ResponseVO<List<StoreVO>>>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO<List<StoreVO>> responseVO) {
				ResultType resultType = responseVO.getResultType();

				if (resultType.isSuccess()) {

					Type type = new TypeToken<List<StoreVO>>(){}.getType();
					storeList = responseVO.getValue("storeList");
					Log.i("DEBUG", "Store Fragment storeList : " + storeList.toString());

					for(StoreVO store : storeList) {
						if(storeMap.containsKey(store.getStoreType())) {
							storeMap.get(store.getStoreType()).add(store);
						} else {
							List<StoreVO> newStoreList = new ArrayList<StoreVO>();
							newStoreList.add(store);
							storeMap.put(store.getStoreType(), newStoreList);
						}
					}
					Log.i("DEBUG", "Store Fragment entry Set : " + storeMap.entrySet());

					StoreListAdapter storeListAdapter = new StoreListAdapter(getContext(), storeMap);
					lvStore.setAdapter(storeListAdapter);
				}
			}
		});*/
	}
}

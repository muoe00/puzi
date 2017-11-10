package com.puzi.puzi.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class StoreFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(R.id.lv_store_brand)
	public ListView lvStoreBrand;

	private StoreListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_store, container, false);
		unbinder = ButterKnife.bind(this, view);

		initComponent();

		getStoreList();

		return view;
	}

	private void initComponent() {
		adapter = new StoreListAdapter(getActivity());
		lvStoreBrand.setAdapter(adapter);
	}

	public void getStoreList() {

		LazyRequestService service = new LazyRequestService(getActivity(), StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.brandList(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				List<StoreVO> storeList = responseVO.getList("storeDTOList", StoreVO.class);
				adapter.addList(storeList);
				adapter.notifyDataSetChanged();
			}
		});

	}
}

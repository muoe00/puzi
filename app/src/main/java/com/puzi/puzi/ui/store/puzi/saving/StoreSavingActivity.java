package com.puzi.puzi.ui.store.puzi.saving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.StoreSavingItemVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StorePuziNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseActivity;
import retrofit2.Call;

import java.util.List;

/**
 * Created by JangwonPark on 2017. 12. 25..
 */
public class StoreSavingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	private StoreSavingAdapter adapter;

	Unbinder unbinder;

	@BindView(R.id.gv_store_saving)
	GridView gvSaving;
	@BindView(R.id.sv_store_saving)
	ScrollView svSaving;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_saving);

		unbinder = ButterKnife.bind(this);

		initComponent();
	}

	private void initComponent() {
		adapter = new StoreSavingAdapter(getActivity(), gvSaving, svSaving, new CustomPagingAdapter.ListHandler() {
			@Override
			public void getList() {
				getPuziSavingList();
			}
		});
		gvSaving.setAdapter(adapter);
		gvSaving.setOnItemClickListener(this);
		adapter.getList();
	}

	public void getPuziSavingList() {
		adapter.startProgressWithScrollDown();

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.getSavingList(token, adapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				adapter.stopProgress();

				List<StoreSavingItemVO> storeSavingList = responseVO.getList("storeSavingItemDTOList", StoreSavingItemVO.class);
				int totalCount = responseVO.getInteger("totalCount");

				adapter.addListWithTotalCount(storeSavingList, totalCount);
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		StoreSavingItemVO storeSavingItemVO = adapter.getItem(position);
		Intent intent = new Intent();
		intent.putExtra("storeSavingItemVO", new Gson().toJson(storeSavingItemVO));
		startActivity(intent);
		doAnimationGoRight();
	}

	@OnClick(R.id.btn_store_item_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

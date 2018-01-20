package com.puzi.puzi.ui.store.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreItemVO;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.store.purchase.PurchaseItemActivity;
import retrofit2.Call;

import java.util.List;

public class StoreItemActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_store_item_title)
	TextView tvTitle;
	@BindView(R.id.iv_store_item_logo)
	ImageView ivLogo;
	@BindView(R.id.tv_store_item_name)
	TextView tvName;
	@BindView(R.id.gv_store_item)
	GridView gvStoreItem;
	@BindView(R.id.tv_store_item_progressBar)
	ProgressBar progressBar;
	@BindView(R.id.ll_container_top)
	LinearLayout llContainerTop;

	private StoreVO storeVO;
	private StoreItemAdapter storeItemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_item);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llContainerTop;

		Intent intent = getIntent();
		storeVO = new Gson().fromJson(intent.getStringExtra("storeVOJson"), StoreVO.class);

		initComponent();

		getStoreItemList();
	}

	private void initComponent() {
		tvTitle.setText(storeVO.getName());
		tvName.setText(storeVO.getName());
		BitmapUIL.load(storeVO.getPictureUrl(), ivLogo);
		storeItemAdapter = new StoreItemAdapter(this);
		gvStoreItem.setAdapter(storeItemAdapter);
	}

	private void getStoreItemList() {
		LazyRequestService service = new LazyRequestService(getActivity(), StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.itemList(token, storeVO.getStoreId(), 1);
			}
		});
		service.enqueue(new CustomCallback(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				List<StoreItemVO> storeItemList = responseVO.getList("storeItemDTOList", StoreItemVO.class);
				storeItemAdapter.addList(storeItemList);
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onFail(ResponseVO responseVO) {
				super.onFail(responseVO);
				progressBar.setVisibility(View.GONE);
			}
		});
	}

	@OnItemClick(R.id.gv_store_item)
	public void onItemClick(int position) {
		StoreItemVO storeItemVO = (StoreItemVO) storeItemAdapter.getItem(position);
		Intent intent = new Intent(this, PurchaseItemActivity.class);
		Gson gson = new Gson();
		intent.putExtra("storeItemVOJson", gson.toJson(storeItemVO));
		intent.putExtra("storeVOJson", gson.toJson(storeVO));
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

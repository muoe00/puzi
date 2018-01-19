package com.puzi.puzi.ui.store.puzi.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.StoreChallengeItemVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StorePuziNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import retrofit2.Call;

import java.util.List;

/**
 * Created by JangwonPark on 2017. 12. 25..
 */
public class StoreChallengeActivity extends BaseFragmentActivity implements AdapterView.OnItemClickListener {

	private StoreChallengeAdapter adapter;

	Unbinder unbinder;

	@BindView(R.id.gv_store_challenge)
	GridView gvChallenge;
	@BindView(R.id.sv_store_challenge)
	ScrollView svChallenge;
	@BindView(R.id.ll_container_top)
	LinearLayout llContainerTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_challenge);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llContainerTop;

		initComponent();
	}

	private void initComponent() {
		adapter = new StoreChallengeAdapter(getActivity(), gvChallenge, svChallenge, new CustomPagingAdapter.ListHandler() {
			@Override
			public void getList() {
				getPuziChallengeList();
			}
		});
		gvChallenge.setAdapter(adapter);
		gvChallenge.setOnItemClickListener(this);
		adapter.getList();
	}

	public void getPuziChallengeList() {
		adapter.startProgressWithScrollDown();

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.getChallengeList(token, adapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				adapter.stopProgress();

				List<StoreChallengeItemVO> challengeItemList = responseVO.getList("storeChallengeItemDTOList", StoreChallengeItemVO.class);
				int totalCount = responseVO.getInteger("totalCount");

				adapter.addListWithTotalCount(challengeItemList, totalCount);
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		StoreChallengeItemVO storeChallengeItemVO = adapter.getItem(position);
		Intent intent = new Intent(this, StoreChallengeDetailActivity.class);
		intent.putExtra("storeChallengeItemVO", new Gson().toJson(storeChallengeItemVO));
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

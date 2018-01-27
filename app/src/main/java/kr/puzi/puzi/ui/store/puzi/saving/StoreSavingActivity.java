package kr.puzi.puzi.ui.store.puzi.saving;

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

import kr.puzi.puzi.biz.store.puzi.StoreSavingItemVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.StorePuziNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import retrofit2.Call;

import java.util.List;

/**
 * Created by JangwonPark on 2017. 12. 25..
 */
public class StoreSavingActivity extends BaseFragmentActivity implements AdapterView.OnItemClickListener {

	private StoreSavingAdapter adapter;

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.gv_store_saving)
	GridView gvSaving;
	@BindView(kr.puzi.puzi.R.id.sv_store_saving)
	ScrollView svSaving;
	@BindView(kr.puzi.puzi.R.id.ll_container_top)
	LinearLayout llContainerTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_store_saving);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llContainerTop;

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
		Intent intent = new Intent(getActivity(), StoreSavingDetailActivity.class);
		intent.putExtra("storeSavingItemVO", new Gson().toJson(storeSavingItemVO));
		startActivity(intent);
		doAnimationGoRight();
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_item_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

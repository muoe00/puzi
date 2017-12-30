package com.puzi.puzi.ui.store.puzi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.StorePuziItemVO;
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
public class StoreItemActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	private StoreItemAdapter adapter;

	Unbinder unbinder;

	@BindView(R.id.lv_company_block)
	ListView lvPuziItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_item);

		unbinder = ButterKnife.bind(this);

		initComponent();
	}

	private void initComponent() {
		adapter = new StoreItemAdapter(getActivity(), lvPuziItem, new CustomPagingAdapter.ListHandler() {
			@Override
			public void getList() {
				getPuziItemList();
			}
		});
		lvPuziItem.setAdapter(adapter);
		lvPuziItem.setOnItemClickListener(this);
		adapter.getList();
	}

	public void getPuziItemList() {
		adapter.startProgressWithScrollDown();

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.getPuziItemList(token, adapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				adapter.stopProgress();

				List<StorePuziItemVO> puziItemList = responseVO.getList("storePuziItemDTOList", StorePuziItemVO.class);
				int totalCount = responseVO.getInteger("totalCount");

				adapter.addListWithTotalCount(puziItemList, totalCount);
			}

		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

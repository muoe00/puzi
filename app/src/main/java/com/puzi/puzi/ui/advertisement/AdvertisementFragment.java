package com.puzi.puzi.ui.advertisement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.lv_advertise) public ListView lvAd;
	@BindView(R.id.srl_advertisement_container) public SwipeRefreshLayout srlContainer;

	private View view = null;
	private boolean more = false;
	private int pagingIndex = 1;
	boolean lastestScrollFlag = false;
	private AdvertisementListAdapter advertiseListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_advertisement, container, false);
		unbinder = ButterKnife.bind(this, view);

		initComponent();
		getAdvertiseList();
		initScrollAction();

		return view;
	}

	private void initComponent() {
		advertiseListAdapter = new AdvertisementListAdapter((BaseFragmentActivity) getActivity());
		lvAd.setAdapter(advertiseListAdapter);

		srlContainer.setColorSchemeResources(R.color.colorPuzi);
		srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
				srlContainer.setRefreshing(false);
			}
		});
	}

	private void refresh() {
		pagingIndex = 1;
		advertiseListAdapter.clean();
		getAdvertiseList();
	}

	public void getAdvertiseList() {
		advertiseListAdapter.startProgress();
		lvAd.setSelection(advertiseListAdapter.getCount() - 1);

		LazyRequestService service = new LazyRequestService(getActivity(), AdvertisementNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<AdvertisementNetworkService>() {
			@Override
			public Call<ResponseVO> execute(AdvertisementNetworkService advertisementNetworkService, String token) {
				return advertisementNetworkService.adList(token, pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				advertiseListAdapter.stopProgress();

				List<ReceivedAdvertiseVO> advertiseList = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
				Log.d(PuziUtils.INFO, "Advertise main / advertiseList : " + advertiseList.toString());
				Log.d(PuziUtils.INFO, "advertiseList totalCount : " + responseVO.getInteger("totalCount"));

				if(advertiseList.size() == 0) {
					advertiseListAdapter.empty();
					more = false;
					return;
				}

				advertiseListAdapter.addAdvertiseList(advertiseList);
				advertiseListAdapter.notifyDataSetChanged();

				if(advertiseListAdapter.getCount() == responseVO.getInteger("totalCount")) {
					more = false;
					return;
				}
				more = true;
			}
		});
	}

	private void initScrollAction() {
		lvAd.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.i(PuziUtils.INFO, "scrollState : " + scrollState + ", more : " + more);

				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
					if(more) {
						pagingIndex = pagingIndex + 1;
						getAdvertiseList();
						advertiseListAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastestScrollFlag = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
			}
		});
	}

	public synchronized void refresh(int adId, boolean saved) {
		pagingIndex = 1;
		advertiseListAdapter.changeSaved(adId, saved);
		advertiseListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

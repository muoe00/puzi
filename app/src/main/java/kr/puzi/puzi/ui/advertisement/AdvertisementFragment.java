package kr.puzi.puzi.ui.advertisement;

import android.content.Intent;
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
import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.AdvertisementNetworkService;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.utils.PuziUtils;
import lombok.NoArgsConstructor;
import retrofit2.Call;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-07-08.
 */
@NoArgsConstructor
public class AdvertisementFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.lv_advertise) public ListView lvAd;
	@BindView(kr.puzi.puzi.R.id.srl_advertisement_container) public SwipeRefreshLayout srlContainer;

	private boolean more = false;
	private int pagingIndex = 1;
	boolean lastestScrollFlag = false;
	private AdvertisementListAdapter advertiseListAdapter;
	private ReceivedAdvertiseVO startReceivedAdvertiseVO = null;

	public static List<Integer> needToUpdateIds = newArrayList();

	public void setPush(ReceivedAdvertiseVO receivedAdvertiseVO) {
		startReceivedAdvertiseVO = receivedAdvertiseVO;
	}

	public static void updateSavedPoint(int id) {
		needToUpdateIds.add(id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(kr.puzi.puzi.R.layout.fragment_advertisement, container, false);
		unbinder = ButterKnife.bind(this, v);

		initComponent();
		getAdvertiseList();
		initScrollAction();

		if(startReceivedAdvertiseVO != null) {
			Log.d("PUSH", "++++startReceivedAdvertiseVO : " + startReceivedAdvertiseVO);
			Intent intent = new Intent(getActivity(), AdvertisementDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("advertise", startReceivedAdvertiseVO);
			intent.putExtras(bundle);
			startActivity(intent);
			doAnimationGoRight();
		}

		return v;
	}

	@Override
	public void onResume() {
		if(needToUpdateIds.size() > 0) {
			for(int index : needToUpdateIds) {
				advertiseListAdapter.changeSaved(index, true);
			}

			needToUpdateIds.clear();
		}

		super.onResume();
	}

	private void initComponent() {
		advertiseListAdapter = new AdvertisementListAdapter((BaseFragmentActivity) getActivity());
		lvAd.setAdapter(advertiseListAdapter);

		srlContainer.setColorSchemeResources(kr.puzi.puzi.R.color.colorPuzi);
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

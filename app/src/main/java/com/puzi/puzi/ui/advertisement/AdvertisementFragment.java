package com.puzi.puzi.ui.advertisement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementFragment extends BaseFragment implements AbsListView.OnScrollListener {

	Unbinder unbinder;

	@BindView(R.id.lv_advertise) public ListView lvAd;

	private View view = null;
	private boolean more = false;
	private int pagingIndex = 1;
	private boolean lastVisible = false;
	private AdvertisementListAdapter advertiseListAdapter;
	private AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

	private static final String TAG = "AdvertisementFragment";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_advertisement, container, false);

		unbinder = ButterKnife.bind(this, view);
		advertiseListAdapter = new AdvertisementListAdapter(view.getContext());
		lvAd.setAdapter(advertiseListAdapter);

		getAdvertiseList(view);
		lvAd.setOnScrollListener(this);

		return view;
	}

	public void getAdvertiseList(final View view) {

		advertiseListAdapter.startProgress();
		lvAd.setSelection(advertiseListAdapter.getCount() - 1);

		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> callList = advertisementNetworkService.adList(token, pagingIndex);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				advertiseListAdapter.stopProgress();

				switch(responseVO.getResultType()){
					case SUCCESS:
						List<ReceivedAdvertiseVO> advertiseList = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
						Log.i(PuziUtils.INFO, "Advertise main / advertiseList : " + advertiseList.toString());

						if(advertiseList.size() == 0) {
							advertiseListAdapter.empty();
							more = false;
							return;
						}

						advertiseListAdapter.addAdvertiseList(advertiseList);
						advertiseListAdapter.notifyDataSetChanged();

						if(advertiseListAdapter.getCount() == advertiseList.size()) {
							more = false;
							return;
						}
						more = true;

						break;

					default:
						Log.i("INFO", "advertisement getAdvertiseList failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) && lastVisible) {
			if(pagingIndex < 10) {
				pagingIndex++;
				getAdvertiseList(view);
			}
			Log.i(PuziUtils.INFO, "pagingIndex : " + pagingIndex);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastVisible = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
	}

	public void refresh(int adId, boolean saved) {
		advertiseListAdapter.changeSaved(adId, saved);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

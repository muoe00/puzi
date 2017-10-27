package com.puzi.puzi.ui.store.coupon;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.channel.ChannelFilterAdapter;
import com.puzi.puzi.ui.channel.ChannelListAdapter;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseActivity {

	Unbinder unbinder;

	private List<PurchaseHistoryVO> list = new ArrayList();
	private CouponListAdapter usedListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);



	}

	public void getCouponList() {
		usedListAdapter.startProgress();
		lvAd.setSelection(usedListAdapter.getCount() - 1);

		String token = Preference.getProperty(getActivity(), "token");

		final StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);

		Call<ResponseVO> callList = storeNetworkService.purchaseHistory(token, pagingIndex);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				advertiseListAdapter.stopProgress();

				switch(responseVO.getResultType()){
					case SUCCESS:
						List<ReceivedAdvertiseVO> advertiseList = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
						Log.i(PuziUtils.INFO, "Advertise main / advertiseList : " + advertiseList.toString());
						Log.i(PuziUtils.INFO, "advertiseList totalCount : " + responseVO.getInteger("totalCount"));

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

						break;

					case NO_AUTH:
						PuziUtils.renewalToken(getActivity());

						break;

					default:
						Log.i("INFO", "advertisement getAdvertiseList failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});

	private void initScrollAction() {
		lvChannel.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
					if(more) {
						pagingIndex = pagingIndex + 1;
						getChannelWithEditorsPageList();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastestScrollFlag = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
			}
		});
	}

	private void initAdapter() {
		// 필터
		channelFilterAdapter = new ChannelFilterAdapter(getActivity());
		hlvChannelFilter.setAdapter(channelFilterAdapter);
		// 리스트
		channelListAdapter = new ChannelListAdapter(getActivity());
		lvChannel.setAdapter(channelListAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

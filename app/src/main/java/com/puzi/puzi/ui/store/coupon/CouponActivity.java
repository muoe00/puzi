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

	private boolean more = false;
	private int pagingIndex = 1;
	boolean lastestScrollFlag = false;
	private List<PurchaseHistoryVO> list = new ArrayList();
	private CouponListAdapter couponListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);

	}

	public void getCouponList() {
		couponListAdapter.startProgress();
		lvAd.setSelection(couponListAdapter.getCount() - 1);

		String token = Preference.getProperty(getActivity(), "token");

		final StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);

		Call<ResponseVO> callList = storeNetworkService.purchaseHistory(token, pagingIndex);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				couponListAdapter.stopProgress();

				switch(responseVO.getResultType()){
					case SUCCESS:
						List<PurchaseHistoryVO> purchaseHistoryVOS = responseVO.getList("PurchaseHistoryDTO", PurchaseHistoryVO.class);
						Log.i(PuziUtils.INFO, "purchaseHistoryVOS : " + purchaseHistoryVOS.toString());
						Log.i(PuziUtils.INFO, "purchaseHistory totalCount : " + responseVO.getInteger("totalCount"));

						if(purchaseHistoryVOS.size() == 0) {
							couponListAdapter.empty();
							more = false;
							return;
						}

						couponListAdapter.addAdvertiseList(purchaseHistoryVOS);
						couponListAdapter.notifyDataSetChanged();

						if(couponListAdapter.getCount() == responseVO.getInteger("totalCount")) {
							more = false;
							return;
						}
						more = true;

						break;

					case NO_AUTH:
						PuziUtils.renewalToken(getActivity());

						break;

					default:
						Log.i("INFO", "getCouponList failed.");
						Toast.makeText(this, responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
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
		couponListAdapter = new CouponListAdapter(getActivity());
		hlvChannelFilter.setAdapter(couponListAdapter);
	}

	@Override
	public void onDestroyView() {
		unbinder.unbind();
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

package com.puzi.puzi.ui.store.coupon;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.CouponVO;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.lv_coupon) ListView lvCoupon;

	private boolean more = false;
	private int pagingIndex = 1;
	boolean lastestScrollFlag = false;
	private List<PurchaseHistoryVO> list = new ArrayList();
	private CouponListAdapter couponListAdapter;

	public Map<String, CouponVO> createItem(String category, CouponVO couponVO) {
		Map<String, CouponVO> item = new HashMap<String, CouponVO>();
		item.put(category, couponVO);

		return item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);

	}



	public void getCouponList() {
		couponListAdapter.startProgress();
		lvCoupon.setSelection(couponListAdapter.getCount() - 1);

		String token = Preference.getProperty(getActivity(), "token");

		final StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);

		Call<ResponseVO> callList = storeNetworkService.purchaseHistory(token, pagingIndex);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "coupon responseVO : " + responseVO.toString());
				couponListAdapter.stopProgress();

				switch (responseVO.getResultType()) {
					case SUCCESS:
						List<PurchaseHistoryVO> purchaseHistoryVOS = responseVO.getList("PurchaseHistoryDTO", PurchaseHistoryVO.class);
						Log.i(PuziUtils.INFO, "purchaseHistoryVOS : " + purchaseHistoryVOS.toString());
						Log.i(PuziUtils.INFO, "purchaseHistory totalCount : " + responseVO.getInteger("totalCount"));

						if (purchaseHistoryVOS.size() == 0) {
							couponListAdapter.empty();
							more = false;
							return;
						}

						couponListAdapter.addAdvertiseList(purchaseHistoryVOS);
						couponListAdapter.notifyDataSetChanged();

						if (couponListAdapter.getCount() == responseVO.getInteger("totalCount")) {
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
	}

	private void initScrollAction() {
		lvCoupon.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
					if(more) {
						pagingIndex = pagingIndex + 1;
						getCouponList();
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
		lvCoupon.setAdapter(couponListAdapter);
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
	}
}

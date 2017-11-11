package com.puzi.puzi.ui.store.coupon;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.CouponStatusType;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.sv_coupon) ScrollView scView;
	@BindView(R.id.lv_coupon) ListView lvCoupon;
	@BindView(R.id.lv_used_coupon) ListView lvUsedCoupon;

	private boolean more = false;
	private int pagingIndex = 1;
	boolean lastestScrollFlag = false;
	private List<PurchaseHistoryVO> couponList = new ArrayList();
	private List<PurchaseHistoryVO> usedCouponList = new ArrayList();
	private CouponListAdapter couponListAdapter;
	private UsedCouponListAdapter usedCouponListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);

		initAdapter();
	}

	public void getCouponList() {
		couponListAdapter.startProgress();

		LazyRequestService service = new LazyRequestService(this, StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.purchaseHistory(token, pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				couponListAdapter.stopProgress();

				List<PurchaseHistoryVO> purchaseHistoryVOS = responseVO.getList("PurchaseHistoryDTO", PurchaseHistoryVO.class);
				Log.i(PuziUtils.INFO, "purchaseHistoryVOS : " + purchaseHistoryVOS.toString());
				Log.i(PuziUtils.INFO, "purchaseHistory totalCount : " + responseVO.getInteger("totalCount"));

				for(PurchaseHistoryVO purchaseHistoryVO : purchaseHistoryVOS) {
					if(purchaseHistoryVO.getCouponStatusType().equals(CouponStatusType.NOT_USE)) {
						couponList.add(purchaseHistoryVO);
					} else if(purchaseHistoryVO.getCouponStatusType().equals(CouponStatusType.USED)) {
						usedCouponList.add(purchaseHistoryVO);
					}
				}

				couponListAdapter.addList(couponList);
				usedCouponListAdapter.addList(usedCouponList);
				couponListAdapter.notifyDataSetChanged();
				usedCouponListAdapter.notifyDataSetChanged();

			}
		});

	}

	private void initAdapter() {
		couponListAdapter = new CouponListAdapter(getActivity(), R.layout.item_coupon_child, lvCoupon, scView, new CustomPagingAdapter.ListHandler() {

			@Override
			public void getList() {
				getCouponList();
			}
		});
		couponListAdapter.setMore(false);
		couponListAdapter.getList();

		usedCouponListAdapter = new UsedCouponListAdapter(getActivity(), R.layout.item_coupon_used_child, lvUsedCoupon, scView, new CustomPagingAdapter.ListHandler() {

			@Override
			public void getList() {
				getCouponList();
			}
		});
		usedCouponListAdapter.setMore(false);
		usedCouponListAdapter.getList();
	}

	@OnClick(R.id.ibtn_back_point)
	public void back() {
		finish();
	}
}

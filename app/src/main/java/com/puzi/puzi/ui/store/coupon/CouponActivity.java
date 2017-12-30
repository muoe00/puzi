package com.puzi.puzi.ui.store.coupon;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.CustomScrollView;
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

	@BindView(R.id.sv_coupon)
	CustomScrollView scView;
	@BindView(R.id.gv_coupon) GridView gvCoupon;
	@BindView(R.id.gv_used_coupon) GridView gvUsedCoupon;

	private boolean isUsedCoupon = false;
	private boolean more = false;
	private int pagingIndex = 1;
	private int totalCount = 0;
	private boolean lastestScrollFlag = false;

	private CouponListAdapter couponListAdapter;
	private UsedCouponListAdapter usedCouponListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);

		initScrollAction();
		initAdapter();
		getCouponList();
	}

	public void getCouponList() {
		couponListAdapter.startProgress();
		usedCouponListAdapter.startProgress();
		gvCoupon.setSelection(couponListAdapter.getCount() - 1);
		gvUsedCoupon.setSelection(usedCouponListAdapter.getCount() - 1);

		final LazyRequestService service = new LazyRequestService(this, StoreNetworkService.class);
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
				usedCouponListAdapter.stopProgress();

				totalCount = responseVO.getInteger("totalCount");

				if(totalCount == 0) {
					return;
				}

				List<PurchaseHistoryVO> purchaseHistoryVOS = responseVO.getList("purchaseHistoryDTOList", PurchaseHistoryVO.class);
				Log.i(PuziUtils.INFO, "purchaseHistoryVOS : " + purchaseHistoryVOS.toString());
				Log.i(PuziUtils.INFO, "purchaseHistoryVOS size : " + purchaseHistoryVOS.size());
				Log.i(PuziUtils.INFO, "purchaseHistory totalCount : " + totalCount);

				List<PurchaseHistoryVO> couponList = new ArrayList();
				List<PurchaseHistoryVO> usedCouponList = new ArrayList();

				for(PurchaseHistoryVO purchaseHistoryVO : purchaseHistoryVOS) {
					if(purchaseHistoryVO.getCouponStatusType().isNotUsed()) {
						couponList.add(purchaseHistoryVO);
						isUsedCoupon = false;
					} else {
						usedCouponList.add(purchaseHistoryVO);
						isUsedCoupon = true;
					}
				}

				Log.i(PuziUtils.INFO, "couponList size : " + couponList.size());
				Log.i(PuziUtils.INFO, "usedCouponList size : " + usedCouponList.size());

				if(totalCount > 0 && couponList.size() == 0) {
					couponListAdapter.empty();
					more = false;
				}

				couponListAdapter.addList(couponList);
				couponListAdapter.notifyDataSetChanged();
				setGridViewHeightBasedOnChildren(couponListAdapter, gvCoupon);

				if(isUsedCoupon) {
					Log.i(PuziUtils.INFO, "isUsedCoupon : " + isUsedCoupon);
					if(usedCouponList.size() == 0) {
						if(!lastestScrollFlag) {
							usedCouponListAdapter.empty();
							more = false;
							return;
						}
					}

					usedCouponListAdapter.addList(usedCouponList);
					usedCouponListAdapter.notifyDataSetChanged();
					setGridViewHeightBasedOnChildren(usedCouponListAdapter, gvUsedCoupon);

					Log.i(PuziUtils.INFO, "couponListAdapter.getCount() : " + couponListAdapter.getCount());
					Log.i(PuziUtils.INFO, "usedCouponListAdapter.getCount() : " + usedCouponListAdapter.getCount());

					if(couponListAdapter.getCount() + usedCouponListAdapter.getCount() == totalCount) {
						more = false;
						lastestScrollFlag = true;
						Log.i(PuziUtils.INFO, "lastestScrollFlag : " + lastestScrollFlag);
						return;
					}
					more = true;
				}

				if(purchaseHistoryVOS.size() < responseVO.getInteger("totalCount")) {
					more = true;
				}

			}
		});

	}

	private void setGridViewHeightBasedOnChildren(BaseAdapter baseAdapter, GridView gridView) {
		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.AT_MOST);

		for (int i = 0; i < baseAdapter.getCount(); i++) {
			View listItem = baseAdapter.getView(i, null, gridView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
		gridView.requestLayout();
	}

	private void initScrollAction() {
		scView.setOnEndedScrolledListener(new CustomScrollView.OnEndedScrolledListener() {
			@Override
			public void onEnded() {
				if(more) {
					pagingIndex = pagingIndex + 1;
					Log.i(PuziUtils.INFO, "pagingIndex : " + pagingIndex);
					getCouponList();
				}
			}
		});

		scView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
			@Override
			public void onScroll(int direction, float scrollY) {

			}
		});
	}

	private void initAdapter() {
		couponListAdapter = new CouponListAdapter(this);
		gvCoupon.setAdapter(couponListAdapter);
		gvCoupon.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE)
				{
					return true;
				}
				return false;
			}
		});
		usedCouponListAdapter = new UsedCouponListAdapter(this);
		gvUsedCoupon.setAdapter(usedCouponListAdapter);
		gvUsedCoupon.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE)
				{
					return true;
				}
				return false;
			}
		});
	}

	@OnClick(R.id.ibtn_back_point)
	public void back() {
		finish();
	}
}

package com.puzi.puzi.ui.store.coupon;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.store.PurchaseHistoryVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.CustomScrollView;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.sv_coupon) CustomScrollView scView;
	@BindView(R.id.ll_used_coupon) LinearLayout llUsed;
	@BindView(R.id.ll_not_use_coupon) LinearLayout llNotUsed;
	@BindView(R.id.gv_coupon) GridView gvCoupon;
	@BindView(R.id.gv_used_coupon) GridView gvUsedCoupon;

	private boolean isUsedCoupon = false;
	private boolean more = false;
	private int pagingIndex = 1;
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
		getNotUseCouponList();
		getUsedCouponList();
	}

	public void getUsedCouponList () {
		usedCouponListAdapter.startProgress();
		gvUsedCoupon.setSelection(usedCouponListAdapter.getCount() - 1);

		final LazyRequestService service = new LazyRequestService(this, StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.purchaseHistoryUsed(token, pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				usedCouponListAdapter.stopProgress();

				Log.i("Coupon", "responseVO : " + responseVO.toString());

				List<PurchaseHistoryVO> usedCouponList = responseVO.getList("purchaseHistoryDTOList", PurchaseHistoryVO.class);

				Log.i(PuziUtils.INFO, "used history : " + usedCouponList.toString());
				Log.i(PuziUtils.INFO, "used history size : " + usedCouponList.size());
				Log.i(PuziUtils.INFO, "used history totalCount : " + responseVO.getInteger("totalCount"));

				if(usedCouponList.size() == 0) {
					if(!lastestScrollFlag) {
						// usedCouponListAdapter.empty();
						gvUsedCoupon.setVisibility(View.GONE);
						LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View emptyView = inflater.inflate(R.layout.item_list_empty_used_coupon, null);
						llUsed.addView(emptyView);
						more = false;
						return;
					}
				}

				usedCouponListAdapter.addList(usedCouponList);
				usedCouponListAdapter.notifyDataSetChanged();
				setGridViewHeightBasedOnChildren(usedCouponListAdapter, gvUsedCoupon);

				Log.i(PuziUtils.INFO, "usedCouponListAdapter.getCount() : " + usedCouponListAdapter.getCount());

				if(usedCouponListAdapter.getCount() == responseVO.getInteger("totalCount")) {
					more = false;
					lastestScrollFlag = true;
					Log.i(PuziUtils.INFO, "lastestScrollFlag : " + lastestScrollFlag);
					return;
				}
				more = true;
			}
		});
	}

	public void getNotUseCouponList() {
		couponListAdapter.startProgress();
		gvCoupon.setSelection(couponListAdapter.getCount() - 1);

		final LazyRequestService service = new LazyRequestService(this, StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.purchaseHistoryNotUse(token, pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				couponListAdapter.stopProgress();

				Log.i("Coupon", "responseVO : " + responseVO.toString());

				List<PurchaseHistoryVO> couponList = responseVO.getList("purchaseHistoryDTOList", PurchaseHistoryVO.class);

				Log.i(PuziUtils.INFO, "not use history : " + couponList.toString());
				Log.i(PuziUtils.INFO, "not use history size : " + couponList.size());
				Log.i(PuziUtils.INFO, "not use history totalCount : " + responseVO.getInteger("totalCount"));

				if(couponList.size() == 0) {
					if(!lastestScrollFlag) {
						// couponListAdapter.empty();
						gvCoupon.setVisibility(View.GONE);
						LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View emptyView = inflater.inflate(R.layout.item_list_empty_coupon, null);
						llNotUsed.addView(emptyView);
						more = false;
						return;
					}
				}

				couponListAdapter.addList(couponList);
				couponListAdapter.notifyDataSetChanged();
				setGridViewHeightBasedOnChildren(couponListAdapter, gvCoupon);

				Log.i(PuziUtils.INFO, "usedCouponListAdapter.getCount() : " + couponListAdapter.getCount());

				if(couponListAdapter.getCount() == responseVO.getInteger("totalCount")) {
					more = false;
					lastestScrollFlag = true;
					Log.i(PuziUtils.INFO, "lastestScrollFlag : " + lastestScrollFlag);
					return;
				}
				more = true;
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
					getNotUseCouponList();
					getUsedCouponList();
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
		couponListAdapter = new CouponListAdapter(getActivity());
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
		usedCouponListAdapter = new UsedCouponListAdapter(getActivity());
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
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}

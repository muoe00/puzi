package kr.puzi.puzi.ui.store.coupon;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.store.PurchaseHistoryVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.StoreNetworkService;
import kr.puzi.puzi.ui.CustomScrollView;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class CouponActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.sv_coupon) CustomScrollView scView;
	@BindView(R.id.ll_coupon_container) LinearLayout llView;
	@BindView(kr.puzi.puzi.R.id.ll_used_coupon) LinearLayout llUsed;
	@BindView(kr.puzi.puzi.R.id.ll_not_use_coupon) LinearLayout llNotUsed;
	@BindView(R.id.ll_coupon_container_used) LinearLayout llUsedContainer;
	@BindView(R.id.ll_coupon_container_not_used) LinearLayout llNotUsedContainer;
	@BindView(kr.puzi.puzi.R.id.gv_coupon) GridView gvCoupon;
	@BindView(kr.puzi.puzi.R.id.gv_used_coupon) GridView gvUsedCoupon;

	private boolean notUsedNull = false;
	private boolean notUsedMore = false;
	private boolean useMore = false;
	private int notUsedpagingIndex = 1;
	private int usepagingIndex = 1;
	private boolean lastestScrollFlag = false;
	private View usedEmptyView, notUsedEmptyView, container;
	private CouponListAdapter couponListAdapter;
	private UsedCouponListAdapter usedCouponListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_store_coupon);

		unbinder = ButterKnife.bind(this);

		initAdapter();
		initScrollAction();
		getNotUseCouponList();
	}

	public void getNotUseCouponList() {
		couponListAdapter.startProgress();
		gvCoupon.setSelection(couponListAdapter.getCount() - 1);

		final LazyRequestService service = new LazyRequestService(this, StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.purchaseHistoryNotUse(token, notUsedpagingIndex);
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

				if(notUsedpagingIndex == 1 && couponList.size() == 0) {
					gvCoupon.setVisibility(View.GONE);
					LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					if(usedEmptyView == null) {
						notUsedEmptyView = inflater.inflate(kr.puzi.puzi.R.layout.item_list_empty_coupon, null);
						llNotUsed.addView(notUsedEmptyView);
					}
					notUsedNull = true;
					notUsedMore = false;

				} else {
					couponListAdapter.addList(couponList);
					if (couponListAdapter.getCount() == responseVO.getInteger("totalCount")) {
						notUsedMore = false;
						lastestScrollFlag = true;
						Log.i(PuziUtils.INFO, "lastestScrollFlag : " + lastestScrollFlag);

					} else {
						notUsedMore = true;
					}

					couponListAdapter.notifyDataSetChanged();
					setGridViewHeightBasedOnChildren(couponListAdapter, gvCoupon);
				}

				getUsedCouponList();

			}
		});
	}

	public void getUsedCouponList () {
		usedCouponListAdapter.startProgress();
		gvUsedCoupon.setSelection(usedCouponListAdapter.getCount() - 1);

		final LazyRequestService service = new LazyRequestService(this, StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.purchaseHistoryUsed(token, usepagingIndex);
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

				LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				if(usepagingIndex == 1 && usedCouponList.size() == 0) {
					if(notUsedNull) {
						llUsed.setVisibility(View.GONE);
						llNotUsed.setVisibility(View.GONE);
						llUsedContainer.setVisibility(View.GONE);
						llNotUsedContainer.setVisibility(View.GONE);
						if (container == null) {
							container = inflater.inflate(kr.puzi.puzi.R.layout.item_list_empty_coupon, null);
							llView.addView(container);
						}
					} else {
						gvUsedCoupon.setVisibility(View.GONE);
						if (usedEmptyView == null) {
							usedEmptyView = inflater.inflate(kr.puzi.puzi.R.layout.item_list_empty_used_coupon, null);
							llUsed.addView(usedEmptyView);
						}
					}
					useMore = false;
					return;
				}

				usedCouponListAdapter.addList(usedCouponList);

				Log.i(PuziUtils.INFO, "usedCouponListAdapter.getCount() : " + usedCouponListAdapter.getCount());

				if(usedCouponListAdapter.getCount() == responseVO.getInteger("totalCount")) {
					useMore = false;
					lastestScrollFlag = true;
					Log.i(PuziUtils.INFO, "lastestScrollFlag : " + lastestScrollFlag);
				} else {
					useMore = true;
				}

				usedCouponListAdapter.notifyDataSetChanged();
				setGridViewHeightBasedOnChildren(usedCouponListAdapter, gvUsedCoupon);

			}
		});
	}

	private void setGridViewHeightBasedOnChildren(BaseAdapter baseAdapter, GridView gridView) {
		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.AT_MOST);

		Log.i("CouponActivity", "baseAdapter.getCount() : " + baseAdapter.getCount());

		int count = baseAdapter.getCount() % 2;

		for (int i = 0; i < baseAdapter.getCount(); i++) {
			View listItem = baseAdapter.getView(i, null, gridView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();

			if((i == 0) && (count != 0)) {
				totalHeight += listItem.getMeasuredHeight();
			}
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight / 2;
		gridView.setLayoutParams(params);
		gridView.requestLayout();
	}

	private void initScrollAction() {
		scView.setOnEndedScrolledListener(new CustomScrollView.OnEndedScrolledListener() {
			@Override
			public void onEnded() {
				/*if(more) {
					pagingIndex = pagingIndex + 1;
					Log.i(PuziUtils.INFO, "pagingIndex : " + pagingIndex);
					getNotUseCouponList();
					getUsedCouponList();
				}*/
				if(notUsedMore) {
					notUsedpagingIndex = notUsedpagingIndex + 1;
					getNotUseCouponList();
				} else if (!notUsedMore && useMore) {
					usepagingIndex = usepagingIndex + 1;
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

	@OnClick(kr.puzi.puzi.R.id.ibtn_back_point)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}

package kr.puzi.puzi.ui.store.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.gson.Gson;

import kr.puzi.puzi.biz.store.StoreItemVO;
import kr.puzi.puzi.biz.store.StoreVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.StoreNetworkService;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.ui.store.coupon.CouponActivity;
import kr.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 11. 3..
 */
public class PurchaseItemActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_store_purchase_title)
	TextView tvTitle;
	@BindView(kr.puzi.puzi.R.id.iv_store_purchase_preview)
	ImageView ivPreview;
	@BindView(kr.puzi.puzi.R.id.tv_store_purchase_name)
	TextView tvName;
	@BindView(kr.puzi.puzi.R.id.tv_store_purchase_price)
	TextView tvPrice;
	@BindView(kr.puzi.puzi.R.id.tv_store_purchase_comment)
	TextView tvComment;
	@BindView(kr.puzi.puzi.R.id.tv_store_purchase_expiryDay)
	TextView tvExpiryDay;
	@BindView(kr.puzi.puzi.R.id.tv_store_purchase_count)
	TextView tvCount;
	@BindView(kr.puzi.puzi.R.id.fl_container_top)
	FrameLayout flContainerTop;

	private StoreVO storeVO;
	private StoreItemVO storeItemVO;
	private int selectedCount = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_store_purchase);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		Intent intent = getIntent();
		storeVO = new Gson().fromJson(intent.getStringExtra("storeVOJson"), StoreVO.class);
		storeItemVO = new Gson().fromJson(intent.getStringExtra("storeItemVOJson"), StoreItemVO.class);

		initComponent();
	}

	private void initComponent() {
		tvTitle.setText(storeVO.getName());
		BitmapUIL.load(storeItemVO.getPictureUrl(), ivPreview);
		tvName.setText(storeItemVO.getName());
		tvPrice.setText(TextUtils.addComma(storeItemVO.getPrice()) + "P");
		tvComment.setText(storeItemVO.getComment());
		tvExpiryDay.setText("구매시점으로 부터 " + storeItemVO.getExpiryDay() + "일");

	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_store_purchase_minus)
	public void minusClick(View v) {
		if(selectedCount == 1) {
			return;
		}
		selectedCount -= 1;
		tvCount.setText(""+selectedCount);
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_store_purchase_plus)
	public void plusClick(View v) {
		if(selectedCount == 5) {
			Toast.makeText(getActivity(), "구매 최대수량을 초과하였습니다.", Toast.LENGTH_SHORT).show();
			return;
		}
		selectedCount += 1;
		tvCount.setText(""+selectedCount);
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_purchase)
	public void purchaseClick(View v) {
		if(selectedCount == 0) {
			Toast.makeText(this, "수량을 선택해주세요", Toast.LENGTH_SHORT).show();
			return;
		}

		OneButtonDialog.show(getActivity(), "상품구매", "구매하시겠습니까?", "구매", new DialogButtonCallback() {
			@Override
			public void onClick() {

				ProgressDialog.show(getActivity());

				LazyRequestService service = new LazyRequestService(getActivity(), StoreNetworkService.class);
				service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
					@Override
					public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
						return storeNetworkService.purchase(token, storeVO.getStoreId(), storeItemVO.getStoreItemId(), selectedCount);
					}
				});
				service.enqueue(new CustomCallback(getActivity()) {

					@Override
					public void onSuccess(ResponseVO responseVO) {
						Preference.updateMyInfoMinusPoint(getActivity(), storeItemVO.getPrice());

						OneButtonDialog.show(getActivity(), "구매완료", "쿠폰함으로 이동하시겠습니까?", "이동", new DialogButtonCallback() {
							@Override
							public void onClick() {
								startActivity(new Intent(getActivity(), CouponActivity.class));
								doAnimationGoRight();
							}
						});
					}
				});
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_purchase_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}

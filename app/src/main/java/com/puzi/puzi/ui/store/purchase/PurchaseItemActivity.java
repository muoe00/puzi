package com.puzi.puzi.ui.store.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreItemVO;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 11. 3..
 */
public class PurchaseItemActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_store_purchase_title)
	TextView tvTitle;
	@BindView(R.id.iv_store_purchase_preview)
	ImageView ivPreview;
	@BindView(R.id.tv_store_purchase_name)
	TextView tvName;
	@BindView(R.id.tv_store_purchase_price)
	TextView tvPrice;
	@BindView(R.id.tv_store_purchase_comment)
	TextView tvComment;
	@BindView(R.id.tv_store_purchase_expiryDay)
	TextView tvExpiryDay;
	@BindView(R.id.tv_store_purchase_count)
	TextView tvCount;

	private StoreVO storeVO;
	private StoreItemVO storeItemVO;
	private int selectedCount = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_purchase);

		unbinder = ButterKnife.bind(this);

		Intent intent = getIntent();
		storeVO = new Gson().fromJson(intent.getStringExtra("storeVOJson"), StoreVO.class);
		storeItemVO = new Gson().fromJson(intent.getStringExtra("storeItemVOJson"), StoreItemVO.class);

		initComponent();
	}

	private void initComponent() {
		tvTitle.setText(storeVO.getName());
		BitmapUIL.load(storeItemVO.getPictureUrl(), ivPreview);
		tvName.setText(storeItemVO.getName());
		tvPrice.setText(TextUtils.addComma(storeItemVO.getPrice()) + "원");
		tvComment.setText(storeItemVO.getComment());
		tvExpiryDay.setText("구매시점으로 부터 " + storeItemVO.getExpiryDay() + "일");

	}

	@OnClick(R.id.ibtn_store_purchase_minus)
	public void minusClick(View v) {
		if(selectedCount == 1) {
			return;
		}
		selectedCount -= 1;
		tvCount.setText(""+selectedCount);
	}

	@OnClick(R.id.ibtn_store_purchase_plus)
	public void plusClick(View v) {
		if(selectedCount == 5) {
			Toast.makeText(getActivity(), "구매 최대수량을 초과하였습니다.", Toast.LENGTH_SHORT).show();
			return;
		}
		selectedCount += 1;
		tvCount.setText(""+selectedCount);
	}

	@OnClick(R.id.btn_store_purchase)
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
						UserVO myInfo = Preference.getMyInfo(getActivity());
						myInfo.setPoint(myInfo.getPoint() - storeItemVO.getPrice());
						Preference.saveMyInfo(getActivity(), myInfo);

						Toast.makeText(getActivity(), "구매완료, 쿠폰함으로 이동 개발해야함!", Toast.LENGTH_SHORT).show();
						//TODO:쿠폰 상세함으로 이동
					}
				});
			}
		});
	}

	@OnClick(R.id.btn_store_purchase_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}

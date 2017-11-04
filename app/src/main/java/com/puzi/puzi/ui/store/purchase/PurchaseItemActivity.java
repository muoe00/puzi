package com.puzi.puzi.ui.store.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreItemVO;
import com.puzi.puzi.biz.store.StoreVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

import static com.google.common.collect.Lists.newArrayList;

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
	@BindView(R.id.sp_store_purchase_count)
	Spinner countSpinner;

	private StoreVO storeVO;
	private StoreItemVO storeItemVO;
	private int selectedCount = 0;

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

		ArrayAdapter<String> countAdapter = new ArrayAdapter<String>
			(getActivity(), android.R.layout.simple_spinner_dropdown_item, newArrayList("1개", "2개", "3개", "4개", "5개"));
		countSpinner.setPrompt("수량을 선택해주세요");
		countSpinner.setAdapter(countAdapter);
	}

	@OnItemSelected(R.id.sp_age)
	public void countSelected(int position) {
		selectedCount = position + 1;
	}

	@OnClick(R.id.btn_store_purchase)
	public void purchaseClick(View v) {
		if(selectedCount == 0) {
			Toast.makeText(this, "수량을 선택해주세요", Toast.LENGTH_SHORT).show();
			return;
		}

		ProgressDialog.show(this);

		StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);
		String token = Preference.getProperty(this, "token");

		Call<ResponseVO> call = storeNetworkService.purchase(token, storeVO.getStoreId(), storeItemVO.getStoreItemId(), selectedCount);
		call.enqueue(new CustomCallback<ResponseVO>(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				if(responseVO.getResultType().isSuccess()) {
					// 쿠폰 상세함으로 이동
				}
				ProgressDialog.dismiss();
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

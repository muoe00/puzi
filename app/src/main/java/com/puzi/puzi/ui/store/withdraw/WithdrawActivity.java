package com.puzi.puzi.ui.store.withdraw;

import android.os.Bundle;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.BankType;
import com.puzi.puzi.biz.store.WithdrawVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.StoreNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.CustonArrayAdapter;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.ui.customview.NotoTextView;
import com.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-07-30.
 */
public class WithdrawActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_store_withdraw_point)
	NotoTextView tvPoint;
	@BindView(R.id.sp_store_withdraw_bank)
	Spinner spBank;
	@BindView(R.id.et_store_withdraw_bank_account)
	EditText etBankAccount;
	@BindView(R.id.et_store_withdraw_bank_name)
	EditText etBankName;
	@BindView(R.id.sp_store_withdraw_bank_price)
	Spinner spBankPrice;
	@BindView(R.id.lv_store_withdraw_history)
	ListView lvHistory;
	@BindView(R.id.sv_store_withdraw_container)
	ScrollView svContainer;

	private WithdrawHistoryAdapter adapter;
	private ArrayList<String> moneyList;
	private BankType selectedBank = BankType.IBKOKRSE;
	private String selectedMoney = "10,000원";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_withdraw);

		unbinder = ButterKnife.bind(this);

		initComponent();
	}

	private void initComponent() {
		UserVO myInfo = Preference.getMyInfo(this);
		tvPoint.setText(TextUtils.addComma(myInfo.getPoint()) + "P");
		adapter = new WithdrawHistoryAdapter(this, R.layout.item_store_withdraw_history, lvHistory, svContainer,
			new CustomPagingAdapter.ListHandler() {
				@Override
				public void getList() {
					getWithdrawHistoryList();
				}
			});
		adapter.setEmptyMessage("출금내역이 없습니다.");
		adapter.getList();
		CustonArrayAdapter bankAdapter = new CustonArrayAdapter(getActivity(), BankType.getBankNameList());
		spBank.setAdapter(bankAdapter);
		moneyList = newArrayList("10,000원", "20,000원", "30,000원", "50,000원", "100,000원");
		CustonArrayAdapter moneyAdapter = new CustonArrayAdapter(getActivity(), moneyList);
		spBankPrice.setAdapter(moneyAdapter);
	}

	private void getWithdrawHistoryList() {
		adapter.startProgressWithScrollDown();

		final StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = storeNetworkService.withdrawResult(token, adapter.getPagingIndex());
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				adapter.stopProgress();

				if (responseVO.getResultType().isSuccess()) {
					List<WithdrawVO> withdrawVOList = responseVO.getList("withdrawDTOList", WithdrawVO.class);
					int totalCount = responseVO.getInteger("totalCount");
					adapter.addListWithTotalCount(withdrawVOList, totalCount);
				}
			}
		});
	}

	@OnClick(R.id.btn_store_withdraw_confirm)
	public void clickConfirm() {
		final String accountNumber = etBankAccount.getText().toString();
		final String accountName = etBankName.getText().toString();
		final int money = Integer.parseInt(selectedMoney.replace(",", "").replace("원", ""));

		if(money > Preference.getMyInfo(this).getPoint()) {
			Toast.makeText(this, "출금 가능 포인트가 부족합니다.", Toast.LENGTH_LONG).show();
			return;
		}
		if(accountName == null || accountName.length() < 2) {
			Toast.makeText(this, "은행정보를 입력해주세요.", Toast.LENGTH_LONG).show();
			return;
		}
		if(accountNumber == null || accountNumber.length() < 2) {
			Toast.makeText(this, "계좌정보를 입력해주세요.", Toast.LENGTH_LONG).show();
			return;
		}

		OneButtonDialog.show(this, "출금하기", selectedMoney + "을 출금하시겠습니까?", "확인", new DialogButtonCallback() {

			@Override
			public void onClick() {
				ProgressDialog.show(WithdrawActivity.this);

				final StoreNetworkService storeNetworkService = RetrofitManager.create(StoreNetworkService.class);
				String token = Preference.getProperty(getActivity(), "token");

				Call<ResponseVO> call = storeNetworkService.withdraw(token, accountNumber, selectedBank.getCode(), accountName, money);
				call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

					@Override
					public void onSuccess(ResponseVO responseVO) {

						if (responseVO.getResultType().isSuccess()) {
							WithdrawVO withdrawVO = new WithdrawVO();
							withdrawVO.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
							withdrawVO.setMoney(money);
							adapter.addOne(withdrawVO);

							UserVO myInfo = Preference.getMyInfo(WithdrawActivity.this);
							myInfo.setPoint(myInfo.getPoint() - money);
							Preference.saveMyInfo(WithdrawActivity.this, myInfo);

							Toast.makeText(WithdrawActivity.this, "출금요청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}

	@OnClick(R.id.ibtn_store_withdraw_back)
	public void back() {
		super.onBackPressed();
	}

}

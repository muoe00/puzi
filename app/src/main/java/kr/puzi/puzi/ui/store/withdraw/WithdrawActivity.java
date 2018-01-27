package kr.puzi.puzi.ui.store.withdraw;

import android.os.Bundle;
import android.widget.*;
import butterknife.*;
import kr.puzi.puzi.biz.store.BankType;
import kr.puzi.puzi.biz.store.WithdrawVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.StoreNetworkService;
import kr.puzi.puzi.ui.CustomArrayAdapter;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.ui.customview.NotoTextView;
import kr.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-07-30.
 */
public class WithdrawActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_store_withdraw_point)
    NotoTextView tvPoint;
	@BindView(kr.puzi.puzi.R.id.sp_store_withdraw_bank)
	Spinner spBank;
	@BindView(kr.puzi.puzi.R.id.et_store_withdraw_bank_account)
	EditText etBankAccount;
	@BindView(kr.puzi.puzi.R.id.et_store_withdraw_bank_name)
	EditText etBankName;
	@BindView(kr.puzi.puzi.R.id.sp_store_withdraw_bank_price)
	Spinner spBankPrice;
	@BindView(kr.puzi.puzi.R.id.lv_store_withdraw_history)
	ListView lvHistory;
	@BindView(kr.puzi.puzi.R.id.sv_store_withdraw_container)
	ScrollView svContainer;

	private WithdrawHistoryAdapter adapter;
	private List<String> moneyList = newArrayList("10,000원", "20,000원", "30,000원", "50,000원", "100,000원");;
	private List<String> bankTypeList = BankType.getBankNameList();
	private BankType selectedBank = BankType.IBKOKRSE;
	private String selectedMoney = "10,000원";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_store_withdraw);

		unbinder = ButterKnife.bind(this);

		initComponent();
	}

	private void initComponent() {
		UserVO myInfo = Preference.getMyInfo(this);
		tvPoint.setText(TextUtils.addComma(myInfo.getPoint()) + "P");
		adapter = new WithdrawHistoryAdapter(this, kr.puzi.puzi.R.layout.item_store_withdraw_history, lvHistory, svContainer,
			new CustomPagingAdapter.ListHandler() {
				@Override
				public void getList() {
					getWithdrawHistoryList();
				}
			});
		adapter.setEmptyMessage("출금내역이 없습니다.");
		adapter.getList();
		CustomArrayAdapter bankAdapter = new CustomArrayAdapter(getActivity(), bankTypeList);
		spBank.setAdapter(bankAdapter);
		CustomArrayAdapter moneyAdapter = new CustomArrayAdapter(getActivity(), moneyList);
		spBankPrice.setAdapter(moneyAdapter);
	}

	private void getWithdrawHistoryList() {
		adapter.startProgressWithScrollDown();

		LazyRequestService service = new LazyRequestService(getActivity(), StoreNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
				return storeNetworkService.withdrawResult(token, adapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				adapter.stopProgress();

				List<WithdrawVO> withdrawVOList = responseVO.getList("withdrawDTOList", WithdrawVO.class);
				int totalCount = responseVO.getInteger("totalCount");
				adapter.addListWithTotalCount(withdrawVOList, totalCount);
			}
		});
	}

	@OnItemSelected(kr.puzi.puzi.R.id.sp_store_withdraw_bank)
	public void bankSelected(int position) {
		this.selectedBank = BankType.findByName(bankTypeList.get(position));
	}

	@OnItemSelected(kr.puzi.puzi.R.id.sp_store_withdraw_bank_price)
	public void priceSelected(int position) {
		this.selectedMoney = moneyList.get(position);
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_withdraw_confirm)
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

				LazyRequestService service = new LazyRequestService(getActivity(), StoreNetworkService.class);
				service.method(new LazyRequestService.RequestMothod<StoreNetworkService>() {
					@Override
					public Call<ResponseVO> execute(StoreNetworkService storeNetworkService, String token) {
						return storeNetworkService.withdraw(token, accountNumber, selectedBank.getCode(), accountName, money);
					}
				});
				service.enqueue(new CustomCallback(getActivity()) {

					@Override
					public void onSuccess(ResponseVO responseVO) {
						WithdrawVO withdrawVO = new WithdrawVO();
						withdrawVO.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
						withdrawVO.setMoney(money);
						adapter.addFirst(withdrawVO);

						UserVO myInfo = Preference.getMyInfo(WithdrawActivity.this);
						myInfo.setPoint(myInfo.getPoint() - money);
						Preference.saveMyInfo(WithdrawActivity.this, myInfo);

						Toast.makeText(WithdrawActivity.this, "출금요청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_store_withdraw_back)
	public void back() {
		super.onBackPressed();
	}

}

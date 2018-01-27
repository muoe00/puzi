package kr.puzi.puzi.ui.store.puzi.saving;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import butterknife.*;
import com.google.gson.Gson;

import kr.puzi.puzi.biz.store.puzi.StoreSavingItemVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.StorePuziNetworkService;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.CustomArrayAdapter;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.utils.TextUtils;
import kr.puzi.puzi.biz.PuziStaticValue;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class StoreSavingDetailActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.iv_store_saving_preview)
	ImageView ivPreview;
	@BindView(kr.puzi.puzi.R.id.tv_store_saving_name)
	TextView tvName;
	@BindView(kr.puzi.puzi.R.id.tv_store_saving_point_count)
	TextView tvPointCount;
	@BindView(kr.puzi.puzi.R.id.sp_store_saving_daily_point)
	Spinner spDailyPoint;
	@BindView(kr.puzi.puzi.R.id.tv_store_savning_comment)
	TextView tvComment;
	@BindView(kr.puzi.puzi.R.id.tv_store_saving_expiryDay)
	TextView tvExpiryDay;
	@BindView(kr.puzi.puzi.R.id.fl_container_top)
	FrameLayout flContainerTop;

	private StoreSavingItemVO storeSavingItemVO;
	private int selectedDailyPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_store_saving_detail);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		Intent intent = getIntent();
		storeSavingItemVO = new Gson().fromJson(intent.getStringExtra("storeSavingItemVO"), StoreSavingItemVO.class);

		initComponent();
	}

	private void initComponent() {
		BitmapUIL.load(storeSavingItemVO.getStoreItemDTO().getPictureUrl(), ivPreview);
		tvName.setText(storeSavingItemVO.getName());
		tvPointCount.setText("포인트 " + TextUtils.addComma(storeSavingItemVO.getTargetPoint()) + "P / 나의오늘은 "
			+ storeSavingItemVO.getTargetMyToday() + "회");
		tvComment.setText(storeSavingItemVO.getStoreItemDTO().getComment());
		tvExpiryDay.setText(storeSavingItemVO.getStoreItemDTO().getExpiryDay() + "일");
		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), PuziStaticValue.DAILY_POINT_LIST);
		spDailyPoint.setAdapter(adapter);
		spDailyPoint.setSelection(1);
	}

	@OnItemSelected(kr.puzi.puzi.R.id.sp_store_saving_daily_point)
	public void dailyPointSelected(int position) {
		String selected = PuziStaticValue.DAILY_POINT_LIST.get(position);
		this.selectedDailyPoint = Integer.parseInt(selected.substring(0, selected.length() - 1));
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_saving)
	public void onSavingClickListerner() {
		OneButtonDialog.show(getActivity(), "적금가입", "적금에 가입하시겠습니까?", "가입", new DialogButtonCallback() {
			@Override
			public void onClick() {
				UserVO myInfo = Preference.getMyInfo(getActivity());
				if(myInfo.getUserSavingDTO() != null) {
					Toast.makeText(getActivity(), "이미 가입된 적금이 있습니다.", Toast.LENGTH_LONG).show();
					return;
				}

				requestSaving();
			}
		});
	}

	private void requestSaving() {
		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.registerSaving(token, storeSavingItemVO.getStoreSavingItemId(), selectedDailyPoint);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				Toast.makeText(getActivity(), "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
				requestMyInfo();
			}

		});
	}

	private void requestMyInfo() {
		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.myInfo(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);
				Preference.saveMyInfo(getActivity(), userVO);
				startActivity(new Intent(getActivity(), StoreSavingMineActivity.class));
				doAnimationGoRight();
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_saving_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

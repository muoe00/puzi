package com.puzi.puzi.ui.store.puzi.saving;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.UserSavingVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.StorePuziNetworkService;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

import static com.puzi.puzi.biz.PuziStaticValue.DAILY_POINT_LIST;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class StoreSavingMineActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.iv_store_saving_mine_preview)
	ImageView ivPreview;
	@BindView(R.id.tv_store_saving_mine_name)
	TextView tvName;
	@BindView(R.id.pb_store_saving_mine_point)
	ProgressBar pbPoint;
	@BindView(R.id.tv_store_saving_mine_point_text)
	TextView tvPointText;
	@BindView(R.id.tv_store_saving_mine_point_total_text)
	TextView tvPointTotalText;
	@BindView(R.id.pb_store_saving_mine_count)
	ProgressBar pbCount;
	@BindView(R.id.tv_store_saving_mine_count_text)
	TextView tvCountText;
	@BindView(R.id.tv_store_saving_mine_count_total_text)
	TextView tvCountTotalText;
	@BindView(R.id.sp_store_saving_mine_daily_point)
	Spinner spDailyPoint;
	@BindView(R.id.ll_profile_channel_dropdown_container)
	LinearLayout llContainer;
	@BindView(R.id.fl_container_top)
	LinearLayout flContainerTop;

	private int selectedDailyPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_saving_detail_mine);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		initComponent();
	}

	private void initComponent() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		UserSavingVO userSavingVO = userVO.getUserSavingDTO();

		BitmapUIL.load(userSavingVO.getStoreItemDTO().getPictureUrl(), ivPreview);
		tvName.setText(userSavingVO.getStoreSavingItemDTO().getName());
		pbPoint.setMax(userSavingVO.getStoreSavingItemDTO().getTargetPoint());
		pbPoint.setProgress(userSavingVO.getSavedPoint());
		tvPointText.setText(TextUtils.addComma(userSavingVO.getSavedPoint()) + "P ");
		tvPointTotalText.setText("/ " + TextUtils.addComma(userSavingVO.getStoreSavingItemDTO().getTargetPoint()) + "P");
		pbCount.setMax(userSavingVO.getStoreSavingItemDTO().getTargetMyToday());
		pbCount.setProgress(userSavingVO.getSavedMyToday());
		tvCountText.setText(TextUtils.addComma(userSavingVO.getSavedMyToday()) + "P ");
		tvCountTotalText.setText("/ " + TextUtils.addComma(userSavingVO.getStoreSavingItemDTO().getTargetMyToday()) + "P");
		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), DAILY_POINT_LIST);
		spDailyPoint.setAdapter(adapter);
		int count = 0;
		for(String dailyPointString : DAILY_POINT_LIST) {
			int point = Integer.parseInt(dailyPointString.substring(0, dailyPointString.length() - 1));
			if(point == userSavingVO.getDailyPoint()) {
				spDailyPoint.setSelection(count);
			}
			++count;
		}
	}

	@OnItemSelected(R.id.sp_store_saving_mine_daily_point)
	public void dailyPointSelected(int position) {
		String selected = DAILY_POINT_LIST.get(position);
		this.selectedDailyPoint = Integer.parseInt(selected.substring(0, selected.length() - 1));
	}

	@OnClick(R.id.btn_store_saving_mine_daily_point_update)
	public void changeRequestOnClick() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		UserSavingVO userSavingVO = userVO.getUserSavingDTO();
		if(userSavingVO.isModifiedDailyPoint()) {
			Toast.makeText(this, "더이상 하루 적금액을 변경할 수 없습니다.", Toast.LENGTH_SHORT).show();
			return;
		}
		if(userSavingVO.getDailyPoint() == selectedDailyPoint) {
			return;
		}

		OneButtonDialog.show(getActivity(), "나의 푸지적금 변경",
			"하루 적금액을 변경하시겠습니까? \n하루 적금액 변경은 한번만 가능하오니 신중하게 변경해주세요.", "변경", new DialogButtonCallback() {
			@Override
			public void onClick() {
				requestUpdateDailyPoint();
			}
		});
	}

	private void requestUpdateDailyPoint() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		final UserSavingVO userSavingVO = userVO.getUserSavingDTO();

		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.editSaving(token, userSavingVO.getStoreSavingItemId(), selectedDailyPoint);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				Toast.makeText(getActivity(), "변경 완료되었습니다.", Toast.LENGTH_SHORT).show();
				UserVO userVO = Preference.getMyInfo(getActivity());
				UserSavingVO userSavingVO = userVO.getUserSavingDTO();
				userSavingVO.setDailyPoint(selectedDailyPoint);
				Preference.saveMyInfo(getActivity(), userVO);
			}

		});
	}

	@OnClick(R.id.btn_store_saving_mine_block)
	public void more() {
		if(llContainer.getVisibility() == View.INVISIBLE) {
			llContainer.setVisibility(View.VISIBLE);
			llContainer.startAnimation(inFromTop());
		} else {
			llContainer.setVisibility(View.INVISIBLE);
			llContainer.startAnimation(outToTop());
		}
	}

	@OnClick(R.id.btn_profile_channel_dropdown_block)
	public void blockOnClick() {
		OneButtonDialog.show(getActivity(), "나의 푸지적금 해지",
			"해당 적금을 해지하시겠습니까? \n해지하면 되돌릴 수 없으니 신중하게 결정해주세요.", "해지하기", new DialogButtonCallback() {
				@Override
				public void onClick() {
					requestTerminate();
				}
			});
	}

	private void requestTerminate() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		final UserSavingVO userSavingVO = userVO.getUserSavingDTO();

		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.terminateSaving(token, userSavingVO.getStoreSavingItemId());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				Toast.makeText(getActivity(), "정상적으로 해지되었습니다.", Toast.LENGTH_SHORT).show();
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
				back();
			}
		});
	}

	@OnClick(R.id.btn_store_saving_mine_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

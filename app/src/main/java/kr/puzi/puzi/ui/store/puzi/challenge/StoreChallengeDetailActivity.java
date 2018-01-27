package kr.puzi.puzi.ui.store.puzi.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import kr.puzi.puzi.biz.store.puzi.StoreChallengeItemVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.StorePuziNetworkService;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.ui.customview.NotoTextView;
import kr.puzi.puzi.ui.store.coupon.CouponActivity;
import kr.puzi.puzi.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 12. 30..
 */

public class StoreChallengeDetailActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_store_challenge_count)
    NotoTextView tvCount;
	@BindView(kr.puzi.puzi.R.id.tv_store_challenge_name)
	TextView tvName;
	@BindView(kr.puzi.puzi.R.id.tv_store_challenge_price)
	TextView tvPrice;
	@BindView(kr.puzi.puzi.R.id.iv_store_challenge_preview)
	ImageView ivPreview;
	@BindView(kr.puzi.puzi.R.id.sw_store_challenge_item)
	Switch svItem;
	@BindView(kr.puzi.puzi.R.id.tv_store_challenge_comment)
	TextView tvComment;
	@BindView(kr.puzi.puzi.R.id.tv_store_challenge_expiryDay)
	TextView tvExpiryDay;
	@BindView(kr.puzi.puzi.R.id.fl_container_top)
	FrameLayout flContainerTop;

	private StoreChallengeItemVO storeChallengeItemVO;
	private boolean usedItem = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_store_challenge_detail);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		Intent intent = getIntent();
		storeChallengeItemVO = new Gson().fromJson(intent.getStringExtra("storeChallengeItemVO"), StoreChallengeItemVO.class);

		initComponent();
	}

	private void initComponent() {
		tvName.setText(storeChallengeItemVO.getName() + " (" + storeChallengeItemVO.getQuantity() + "개)");
		tvPrice.setText(TextUtils.addComma(storeChallengeItemVO.getPrice()) + "P");
		BitmapUIL.load(storeChallengeItemVO.getStoreItemDTO().getPictureUrl(), ivPreview);
		tvComment.setText(storeChallengeItemVO.getStoreItemDTO().getComment());
		tvCount.setText(storeChallengeItemVO.getChallengeCount() + "명 참여중");
		tvExpiryDay.setText(storeChallengeItemVO.getStoreItemDTO().getExpiryDay() + "일");
		svItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				usedItem = isChecked;
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_challenge)
	public void onChallengeClickListener() {
		OneButtonDialog.show(getActivity(), "푸지응모", "푸지응모에 도전하시겠습니까?", "도전", new DialogButtonCallback() {
			@Override
			public void onClick() {
				UserVO myInfo = Preference.getMyInfo(getActivity());
				if(storeChallengeItemVO.getPrice() > myInfo.getPoint()) {
					Toast.makeText(getActivity(), "포인트가 부족합니다", Toast.LENGTH_LONG).show();
					return;
				}

				requestChallenge();
			}
		});
	}

	private void requestChallenge() {
		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), StorePuziNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<StorePuziNetworkService>() {
			@Override
			public Call<ResponseVO> execute(StorePuziNetworkService storePuziNetworkService, String token) {
				return storePuziNetworkService.purchaseChallenge(token, storeChallengeItemVO.getStoreChallengeItemId(), usedItem);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				boolean result = responseVO.getBoolean("result");
				if(result) {
					StoreChallengeSuccessDialog.load(getActivity(), storeChallengeItemVO.getStoreItemDTO().getPictureUrl(),
						new ChallengeSuccessListener() {
						@Override
						public void onSuccess() {
							startActivity(new Intent(getActivity(), CouponActivity.class));
							doAnimationGoRight();
						}
					});
				} else {
					StoreChallengeFailDialog.load(getActivity());
				}
			}

		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_store_challenge_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

	public interface ChallengeSuccessListener {
		void onSuccess();
	}

}

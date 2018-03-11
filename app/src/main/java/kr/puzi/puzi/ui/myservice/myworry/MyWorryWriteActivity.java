package kr.puzi.puzi.ui.myservice.myworry;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.myworry.QuestionType;
import kr.puzi.puzi.biz.user.AgeType;
import kr.puzi.puzi.biz.user.GenderType;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyWorryNetworkService;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import retrofit2.Call;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class MyWorryWriteActivity extends BaseFragmentActivity {

	private Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.et_myworry_write_question)
	EditText etQuestion;
	@BindView(kr.puzi.puzi.R.id.ibtn_myworry_write_2type)
	ImageButton ibtn2Type;
	@BindView(kr.puzi.puzi.R.id.ibtn_myworry_write_4type)
	ImageButton ibtn4Type;
	@BindView(kr.puzi.puzi.R.id.et_myworry_write_answer_1)
	EditText etAnswer1;
	@BindView(kr.puzi.puzi.R.id.et_myworry_write_answer_2)
	EditText etAnswer2;
	@BindView(kr.puzi.puzi.R.id.et_myworry_write_answer_3)
	EditText etAnswer3;
	@BindView(kr.puzi.puzi.R.id.et_myworry_write_answer_4)
	EditText etAnswer4;
	@BindView(kr.puzi.puzi.R.id.gv_myworry_write_type)
	GridView gvType;
	@BindView(kr.puzi.puzi.R.id.sw_myworry_write_item_top)
	Switch swItemTop;
	@BindView(kr.puzi.puzi.R.id.sw_myworry_write_item_target)
	Switch swItemTarget;
	@BindView(kr.puzi.puzi.R.id.btn_myworry_write_item_target_female)
	Button btnTargetFemale;
	@BindView(kr.puzi.puzi.R.id.btn_myworry_write_item_target_male)
	Button btnTargetMale;
	@BindView(kr.puzi.puzi.R.id.btn_myworry_write_item_target_10)
	Button btnTarget10;
	@BindView(kr.puzi.puzi.R.id.btn_myworry_write_item_target_20)
	Button btnTarget20;
	@BindView(kr.puzi.puzi.R.id.btn_myworry_write_item_target_30)
	Button btnTarget30;
	@BindView(kr.puzi.puzi.R.id.btn_myworry_write_item_target_40)
	Button btnTarget40;
	@BindView(kr.puzi.puzi.R.id.ll_myworry_write_container)
	LinearLayout llContainer;
	@BindView(kr.puzi.puzi.R.id.ll_myworry_write_item_target_container)
	LinearLayout llTargetContainer;
	@BindView(kr.puzi.puzi.R.id.tv_myworry_write_item_top_price)
	TextView tvItemTopPrice;
	@BindView(kr.puzi.puzi.R.id.tv_myworry_write_item_target_price)
	TextView tvItemTargetPrice;
	@BindView(kr.puzi.puzi.R.id.ll_container_top)
	LinearLayout llContainerTop;

	private long mLastClickTime = 0;
	private Map<String, Integer> priceInfoMap;
	private MyWorryWriteAdapter adapter;
	private int answerCount = 2;
	private boolean useTopItem = false;
	private boolean useTargetItem = false;
	private QuestionType questionType = QuestionType.LOW_PRICE;
	private List<GenderType> genderTypeList = newArrayList();
	private List<AgeType> ageTypeList = newArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_myworry_write);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = llContainerTop;

		getPriceInfo();

		initComponent();
	}

	private void initComponent() {
		swItemTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				useTopItem = isChecked;
			}
		});
		swItemTarget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				useTargetItem = isChecked;
				if(useTargetItem) {
					llTargetContainer.setVisibility(View.VISIBLE);
				} else {
					llTargetContainer.setVisibility(View.GONE);
				}
			}
		});
	}

	private void getPriceInfo() {
		ProgressDialog.show(this);

		LazyRequestService service = new LazyRequestService(getActivity(), MyWorryNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<MyWorryNetworkService>() {
			@Override
			public Call<ResponseVO> execute(MyWorryNetworkService myWorryNetworkService, String token) {
				return myWorryNetworkService.getPriceInfo(token);
			}
		});
		service.enqueue(new CustomCallback(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				priceInfoMap = responseVO.getIntegerMap("myWorryPriceMap");
				initPrice();
			}
		});
	}

	private void initPrice() {
		type2OnClick();
		tvItemTopPrice.setText("(+" + priceInfoMap.get("MY_WORRY_SHOW_TOP_PRICE") + "P)");
		tvItemTargetPrice.setText("(+" + priceInfoMap.get("MY_WORRY_TARGETING_PRICE") + "P)");
	}

	@OnItemClick(kr.puzi.puzi.R.id.gv_myworry_write_type)
	public void typeOnItemClick(int position) {
		switch(position) {
			case 0:
				questionType = QuestionType.LOW_PRICE;
				break;
			case 1:
				questionType = QuestionType.STANDARD;
				break;
			case 2:
				questionType = QuestionType.LUXURY;
				break;
			case 3:
				questionType = QuestionType.PREMIUM;
				break;
		}
		adapter.setSelectedType(position);
		adapter.notifyDataSetChanged();
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_myworry_write_2type)
	public void type2OnClick() {
		ibtn2Type.setImageResource(kr.puzi.puzi.R.drawable.radio_on);
		ibtn4Type.setImageResource(kr.puzi.puzi.R.drawable.radio_off);
		answerCount = 2;
		etAnswer3.setVisibility(View.GONE);
		etAnswer4.setVisibility(View.GONE);
		setTypeGridView(true);
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_myworry_write_4type)
	public void type4OnClick() {
		ibtn2Type.setImageResource(kr.puzi.puzi.R.drawable.radio_off);
		ibtn4Type.setImageResource(kr.puzi.puzi.R.drawable.radio_on);
		answerCount = 4;
		etAnswer3.setVisibility(View.VISIBLE);
		etAnswer4.setVisibility(View.VISIBLE);
		setTypeGridView(false);
	}

	private void setTypeGridView(boolean isTwoAnswer) {
		List<Integer> priceForTypes = isTwoAnswer ? newArrayList(
			priceInfoMap.get("MY_WORRY_2Q_LOW_PRICE"),
			priceInfoMap.get("MY_WORRY_2Q_STANDARD_PRICE"),
			priceInfoMap.get("MY_WORRY_2Q_LUXURY_PRICE"),
			priceInfoMap.get("MY_WORRY_2Q_PREMIUM_PRICE")
		) : newArrayList(
			priceInfoMap.get("MY_WORRY_4Q_LOW_PRICE"),
			priceInfoMap.get("MY_WORRY_4Q_STANDARD_PRICE"),
			priceInfoMap.get("MY_WORRY_4Q_LUXURY_PRICE"),
			priceInfoMap.get("MY_WORRY_4Q_PREMIUM_PRICE")
		);

		List<Integer> countForTypes = isTwoAnswer ? newArrayList(
				priceInfoMap.get("MY_WORRY_2Q_LOW_COUNT"),
				priceInfoMap.get("MY_WORRY_2Q_STANDARD_COUNT"),
				priceInfoMap.get("MY_WORRY_2Q_LUXURY_COUNT"),
				priceInfoMap.get("MY_WORRY_2Q_PREMIUM_COUNT")
		) : newArrayList(
				priceInfoMap.get("MY_WORRY_4Q_LOW_COUNT"),
				priceInfoMap.get("MY_WORRY_4Q_STANDARD_COUNT"),
				priceInfoMap.get("MY_WORRY_4Q_LUXURY_COUNT"),
				priceInfoMap.get("MY_WORRY_4Q_PREMIUM_COUNT")
		);

		adapter = new MyWorryWriteAdapter(getActivity(), priceForTypes, countForTypes);
		gvType.setAdapter(adapter);
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_female)
	public void femailOnClick(View v) {
		if(genderTypeList.contains(GenderType.FEMALE)) {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			genderTypeList.remove(GenderType.FEMALE);
		} else {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			genderTypeList.add(GenderType.FEMALE);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_male)
	public void mailOnClick(View v) {
		if(genderTypeList.contains(GenderType.MALE)) {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			genderTypeList.remove(GenderType.MALE);
		} else {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			genderTypeList.add(GenderType.MALE);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_10)
	public void age10OnClick(View v) {
		if(ageTypeList.contains(AgeType.TEN)) {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.TEN);
		} else {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.TEN);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_20)
	public void age20OnClick(View v) {
		if(ageTypeList.contains(AgeType.TWENTY)) {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.TWENTY);
		} else {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.TWENTY);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_30)
	public void age30OnClick(View v) {
		if(ageTypeList.contains(AgeType.THIRTY)) {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.THIRTY);
		} else {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.THIRTY);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_40)
	public void age40OnClick(View v) {
		if(ageTypeList.contains(AgeType.FOURTY)) {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.FOURTY);
		} else {
			v.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.FOURTY);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.btn_myworry_write_item_target_write)
	public void writeOnClick() {

		if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
			return;
		}
		mLastClickTime = SystemClock.elapsedRealtime();

		final String question = etQuestion.getText().toString();
		if(question == null || question.length() == 0) {
			Toast.makeText(getActivity(), "질문을 입력해주세요", Toast.LENGTH_SHORT).show();
			return;
		}
		if(question.length() > 30) {
			Toast.makeText(getActivity(), "질문을 30자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
			return;
		}
		final String answer1 = etAnswer1.getText().toString();
		if(answer1 == null || answer1.length() == 0) {
			Toast.makeText(getActivity(), "답변1을 입력해주세요", Toast.LENGTH_SHORT).show();
			return;
		}
		if(answer1.length() > 10) {
			Toast.makeText(getActivity(), "답변1을 10자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
			return;
		}
		final String answer2 = etAnswer2.getText().toString();
		if(answer2 == null || answer2.length() == 0) {
			Toast.makeText(getActivity(), "답변2을 입력해주세요", Toast.LENGTH_SHORT).show();
			return;
		}
		if(answer2.length() > 10) {
			Toast.makeText(getActivity(), "답변2을 10자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
			return;
		}
		final String answer3 = etAnswer3.getText().toString();
		final String answer4 = etAnswer4.getText().toString();
		if(answerCount == 4) {
			if(answer3 == null || answer3.length() == 0) {
				Toast.makeText(getActivity(), "답변3을 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			}
			if(answer3.length() > 10) {
				Toast.makeText(getActivity(), "답변3을 10자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			}
			if(answer4 == null || answer4.length() == 0) {
				Toast.makeText(getActivity(), "답변4을 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			}
			if(answer4.length() > 10) {
				Toast.makeText(getActivity(), "답변4을 10자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if(useTargetItem) {
			if(ageTypeList.size() == 0 && genderTypeList.size() == 0) {
				Toast.makeText(getActivity(), "타겟팅을 선택해주세요", Toast.LENGTH_SHORT).show();
				return;
			}
			if(ageTypeList.size() == 4 && genderTypeList.size() == 2) {
				Toast.makeText(getActivity(), "타겟팅을 올바르게 선택해주세요(전체를 선택할 수 없습니다)", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		int price = adapter.getPrice();
		if(useTopItem) {
			price += priceInfoMap.get("MY_WORRY_SHOW_TOP_PRICE");
		}
		if(useTargetItem) {
			price += priceInfoMap.get("MY_WORRY_TARGETING_PRICE");
		}
		final int finalPrice = price;

		MyWorryWriteConfirmDialog.load(getActivity(), price, new MyWorryWriteConfirmDialog.ConfirmListener() {
			@Override
			public void onConfirm() {
				ProgressDialog.show(getActivity());

				LazyRequestService service = new LazyRequestService(getActivity(), MyWorryNetworkService.class);
				service.method(new LazyRequestService.RequestMothod<MyWorryNetworkService>() {
					@Override
					public Call<ResponseVO> execute(MyWorryNetworkService myWorryNetworkService, String token) {
						return myWorryNetworkService.write(token, question, answerCount, answer1, answer2, answer3, answer4, questionType,
							useTopItem, useTargetItem, genderTypeList, ageTypeList);
					}
				});
				service.enqueue(new CustomCallback(getActivity()) {

					@Override
					public void onSuccess(ResponseVO responseVO) {
						Toast.makeText(getActivity(), "질문 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
						Preference.updateMyInfoMinusPoint(getActivity(), finalPrice);
						onBackPressed();
					}
				});
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_back)
	public void backPress() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}
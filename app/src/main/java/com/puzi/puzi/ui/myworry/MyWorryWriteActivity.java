package com.puzi.puzi.ui.myworry;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.myworry.QuestionType;
import com.puzi.puzi.biz.user.AgeType;
import com.puzi.puzi.biz.user.GenderType;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.MyWorryNetworkService;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseActivity;
import retrofit2.Call;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class MyWorryWriteActivity extends BaseActivity {

	private Unbinder unbinder;

	@BindView(R.id.et_myworry_write_question)
	EditText etQuestion;
	@BindView(R.id.ibtn_myworry_write_2type)
	ImageButton ibtn2Type;
	@BindView(R.id.ibtn_myworry_write_4type)
	ImageButton ibtn4Type;
	@BindView(R.id.et_myworry_write_answer_1)
	EditText etAnswer1;
	@BindView(R.id.et_myworry_write_answer_2)
	EditText etAnswer2;
	@BindView(R.id.et_myworry_write_answer_3)
	EditText etAnswer3;
	@BindView(R.id.et_myworry_write_answer_4)
	EditText etAnswer4;
	@BindView(R.id.gv_myworry_write_type)
	GridView gvType;
	@BindView(R.id.sw_myworry_write_item_top)
	Switch swItemTop;
	@BindView(R.id.sw_myworry_write_item_target)
	Switch swItemTarget;
	@BindView(R.id.btn_myworry_write_item_target_female)
	Button btnTargetFemale;
	@BindView(R.id.btn_myworry_write_item_target_male)
	Button btnTargetMale;
	@BindView(R.id.btn_myworry_write_item_target_10)
	Button btnTarget10;
	@BindView(R.id.btn_myworry_write_item_target_20)
	Button btnTarget20;
	@BindView(R.id.btn_myworry_write_item_target_30)
	Button btnTarget30;
	@BindView(R.id.btn_myworry_write_item_target_40)
	Button btnTarget40;
	@BindView(R.id.ll_myworry_write_container)
	LinearLayout llContainer;
	@BindView(R.id.ll_myworry_write_item_target_container)
	LinearLayout llTargetContainer;
	@BindView(R.id.tv_myworry_write_item_top_price)
	TextView tvItemTopPrice;
	@BindView(R.id.tv_myworry_write_item_target_price)
	TextView tvItemTargetPrice;

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
		setContentView(R.layout.activity_myworry_write);

		unbinder = ButterKnife.bind(this);

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

	@OnItemClick(R.id.gv_myworry_write_type)
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

	@OnClick(R.id.ibtn_myworry_write_2type)
	public void type2OnClick() {
		ibtn2Type.setImageResource(R.drawable.radio_on);
		ibtn4Type.setImageResource(R.drawable.radio_off);
		answerCount = 2;
		etAnswer3.setVisibility(View.GONE);
		etAnswer4.setVisibility(View.GONE);
		setTypeGridView(true);
	}

	@OnClick(R.id.ibtn_myworry_write_4type)
	public void type4OnClick() {
		ibtn2Type.setImageResource(R.drawable.radio_off);
		ibtn4Type.setImageResource(R.drawable.radio_on);
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
		adapter = new MyWorryWriteAdapter(getActivity(), priceForTypes);
		gvType.setAdapter(adapter);
	}

	@OnClick(R.id.btn_myworry_write_item_target_female)
	public void femailOnClick(View v) {
		if(genderTypeList.contains(GenderType.FEMALE)) {
			v.setBackgroundResource(R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			genderTypeList.remove(GenderType.FEMALE);
		} else {
			v.setBackgroundResource(R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			genderTypeList.add(GenderType.FEMALE);
		}
	}

	@OnClick(R.id.btn_myworry_write_item_target_male)
	public void mailOnClick(View v) {
		if(genderTypeList.contains(GenderType.MALE)) {
			v.setBackgroundResource(R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			genderTypeList.remove(GenderType.MALE);
		} else {
			v.setBackgroundResource(R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			genderTypeList.add(GenderType.MALE);
		}
	}

	@OnClick(R.id.btn_myworry_write_item_target_10)
	public void age10OnClick(View v) {
		if(ageTypeList.contains(AgeType.TEN)) {
			v.setBackgroundResource(R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.TEN);
		} else {
			v.setBackgroundResource(R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.TEN);
		}
	}

	@OnClick(R.id.btn_myworry_write_item_target_20)
	public void age20OnClick(View v) {
		if(ageTypeList.contains(AgeType.TWENTY)) {
			v.setBackgroundResource(R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.TWENTY);
		} else {
			v.setBackgroundResource(R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.TWENTY);
		}
	}

	@OnClick(R.id.btn_myworry_write_item_target_30)
	public void age30OnClick(View v) {
		if(ageTypeList.contains(AgeType.THIRTY)) {
			v.setBackgroundResource(R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.THIRTY);
		} else {
			v.setBackgroundResource(R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.THIRTY);
		}
	}

	@OnClick(R.id.btn_myworry_write_item_target_40)
	public void age40OnClick(View v) {
		if(ageTypeList.contains(AgeType.FOURTY)) {
			v.setBackgroundResource(R.drawable.button_favorite_off);
			((Button) v).setTextColor(Color.parseColor("#bbbbbb"));
			ageTypeList.remove(AgeType.FOURTY);
		} else {
			v.setBackgroundResource(R.drawable.button_favorite_on);
			((Button) v).setTextColor(Color.parseColor("#ff2470"));
			ageTypeList.add(AgeType.FOURTY);
		}
	}

	@OnClick(R.id.btn_myworry_write_item_target_write)
	public void writeOnClick() {
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

	@OnClick(R.id.ibtn_back)
	public void backPress() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}
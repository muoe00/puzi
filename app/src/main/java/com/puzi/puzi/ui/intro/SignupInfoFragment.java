package com.puzi.puzi.ui.intro;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.*;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.setting.PersonalFragment;
import com.puzi.puzi.ui.setting.UsingFragment;
import com.puzi.puzi.utils.EncryptUtils;
import retrofit2.Call;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.fragment;
import static com.puzi.puzi.biz.user.AddressInfo.CITY_MAP;
import static com.puzi.puzi.biz.user.AddressInfo.REGION_LIST;

/**
 * Created by muoe0 on 2017-07-27.
 */

public class SignupInfoFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(R.id.btn_info_beauty) public Button btnBeauty;
	@BindView(R.id.btn_info_shopping) public Button btnShop;
	@BindView(R.id.btn_info_game) public Button btnGame;
	@BindView(R.id.btn_info_eat) public Button btnEat;
	@BindView(R.id.btn_info_tour) public Button btnTour;
	@BindView(R.id.btn_info_finance) public Button btnFinance;
	@BindView(R.id.btn_info_culture) public Button btnCulture;
	@BindView(R.id.iv_signup_all) public ImageView ivConfirm;
	@BindView(R.id.rbtn_male) public RadioButton rbtnMale;
	@BindView(R.id.rbtn_female) public RadioButton rbtnFemale;
	@BindView(R.id.sp_age) public Spinner spAge;
	@BindView(R.id.edit_recommend) public EditText edtiRecommend;
	@BindView(R.id.ibtn_back) public ImageView ibtnBack;
	@BindView(R.id.sp_region) public Spinner spRegion;
	@BindView(R.id.sp_city) public Spinner spCity;

	private boolean isConfirm = false;
	private UserVO userVO;
	private AlertDialog.Builder alert_confirm;
	private ArrayList<String> favoritesList = new ArrayList<String>();
	private ArrayList<String> yearList = new ArrayList<String>();
	private String region = REGION_LIST.get(0);
	private String city = CITY_MAP.get(region).get(0);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_signup_info, container, false);
		unbinder = ButterKnife.bind(this, view);

		rbtnMale.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
		rbtnFemale.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));

		settingYears();

		alert_confirm = new AlertDialog.Builder(this.getContext());
		alert_confirm.setPositiveButton("확인", null);
		userVO = new UserVO();

		String isKakao = Preference.getProperty(getActivity(), "kakao");
		if("K".equals(isKakao)) {
			userVO.setRegisterType(RegisterType.K);
			userVO.setPasswd(Preference.getProperty(getActivity(), "temppasswd"));
		} else {
			userVO.setRegisterType(RegisterType.N);
			userVO.setPasswd(EncryptUtils.sha256(Preference.getProperty(getActivity(), "temppasswd")));
		}

		userVO.setUserId(Preference.getProperty(getActivity(), "tempid"));
		userVO.setEmail(Preference.getProperty(getActivity(), "tempemail"));
		userVO.setNotifyId(Preference.getProperty(getActivity(), "tokenFCM"));
		userVO.setPhoneType(PhoneType.A);
		userVO.setLevelType(LevelType.WELCOME);

		checkInfo();

		return view;
	}

	@OnClick(R.id.btn_signup_ok)
	public void signup() {
		if(userVO.getAgeType() == null) {
			Toast.makeText(getContext(), "출생년도를 선택하세요", Toast.LENGTH_SHORT).show();
			return;
		} else if(userVO.getFavoriteTypeList() == null || userVO.getFavoriteTypeList().size() < 3) {
			Toast.makeText(getContext(), "관심분야를 3가지 이상 선택해주세요", Toast.LENGTH_SHORT).show();
			return;
		} else if(!isConfirm) {
			Toast.makeText(getContext(), "약관에 동의해주세요", Toast.LENGTH_SHORT).show();
			return;
		}

		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.signup(userVO.getUserId(), userVO.getPasswd(), userVO.getRegisterType()
					, userVO.getEmail(), userVO.getNotifyId(), userVO.getGenderType(), userVO.getAge(), userVO.getFavoriteTypeList()
					, userVO.getRecommendId(), userVO.getPhoneType(), userVO.getPhoneKey(), region, city);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				String token = responseVO.getString("token");
				Log.i("INFO", "signup token : " + token);

				Preference.addProperty(getActivity(), "token", token);
				Preference.addProperty(getActivity(), "id", userVO.getUserId());
				Preference.addProperty(getActivity(), "passwd", userVO.getPasswd());

				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
				getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		});
	}

	@OnItemSelected(R.id.sp_age)
	public void checkAge(int position) {
		int year = position + 1940;
		int currentYear = currentYear();
		int age = currentYear - year + 1;

		userVO.setAge(year);

		if(age < 20) {
			userVO.setAgeType(AgeType.TEN);
		} else if(age >= 20 && age < 30) {
			userVO.setAgeType(AgeType.TWENTY);
		} else if(age >= 30 && age < 40) {
			userVO.setAgeType(AgeType.THIRTY);
		} else if(age >= 40) {
			userVO.setAgeType(AgeType.FOURTY);
		}

		Log.i("INFO", "Year : " + year + ", age type : " + userVO.getAgeType());
	}

	@OnItemSelected(R.id.sp_region)
	public void checkRegion(int position) {
		region = REGION_LIST.get(position);
		spCity.setAdapter(new CustomArrayAdapter(getActivity(), CITY_MAP.get(REGION_LIST.get(position))));
		city = CITY_MAP.get(region).get(0);
	}

	@OnItemSelected(R.id.sp_city)
	public void checkCity(int position) {
		city = CITY_MAP.get(region).get(position);
	}

	public int currentYear() {
		long nowTime = System.currentTimeMillis();
		Date date = new Date(nowTime);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String currentYear = simpleDateFormat.format(date);
		int year = Integer.parseInt(currentYear);

		return year;
	}

	public void settingYears() {

		int currentYear = currentYear();

		for(int index = 1940; index <= currentYear; index++){
			yearList.add(String.valueOf(index));
		}

		spAge.setPrompt(getResources().getString(R.string.info_age));
		spAge.setAdapter(new CustomArrayAdapter(getActivity(), yearList));
		spAge.setSelection(1999-1940);
		spRegion.setAdapter(new CustomArrayAdapter(getActivity(), REGION_LIST));
		spRegion.setSelection(0);
		spCity.setAdapter(new CustomArrayAdapter(getActivity(), CITY_MAP.get(REGION_LIST.get(0))));
		spCity.setSelection(0);
	}

	@OnClick({R.id.btn_info_beauty, R.id.btn_info_shopping, R.id.btn_info_game, R.id.btn_info_eat,
		R.id.btn_info_tour, R.id.btn_info_finance, R.id.btn_info_culture})
	public void checkFavorites(View view) {
		switch (view.getId()) {
			case R.id.btn_info_beauty:
				checkList(FavoriteType.BEAUTY.name(), btnBeauty);
				break;
			case R.id.btn_info_shopping:
				checkList(FavoriteType.SHOPPING.name(), btnShop);
				break;
			case R.id.btn_info_game:
				checkList(FavoriteType.GAME.name(), btnGame);
				break;
			case R.id.btn_info_eat:
				checkList(FavoriteType.EAT.name(), btnEat);
				break;
			case R.id.btn_info_tour:
				checkList(FavoriteType.TOUR.name(), btnTour);
				break;
			case R.id.btn_info_finance:
				checkList(FavoriteType.FINANCE.name(), btnFinance);
				break;
			case R.id.btn_info_culture:
				checkList(FavoriteType.CULTURE.name(), btnCulture);
				break;
			default:
				break;
		}

		userVO.setFavoriteTypeList(favoritesList);
	}

	public void checkList(String category, Button btn) {
		if(isFavorites(category)) {
			favoritesList.remove(category);
			btn.setBackgroundResource(R.drawable.button_favorite_off);
			btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextGray));
		} else {
			favoritesList.add(String.valueOf(category));
			btn.setBackgroundResource(R.drawable.button_favorite_on);
			btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
		}

		Log.i("INFO", "favoritesList : " + favoritesList.toString());
	}

	public boolean isFavorites(String item) {
		if(favoritesList.isEmpty()) {
			return false;
		} else {
			for (String favorite : favoritesList) {
				if (favorite.equals(String.valueOf(item))) {
					return true;
				}
			}
			return false;
		}
	}

	@OnClick(R.id.btn_signup_all)
	public void checkConfirm() {
		if(isConfirm) {
			isConfirm = false;
			ivConfirm.setImageResource(R.drawable.radio_off);
		} else if (!isConfirm) {
			isConfirm = true;
			ivConfirm.setImageResource(R.drawable.radio_on);
		}
	}

	@OnClick({R.id.btn_signup_service, R.id.btn_signup_personal})
	public void checkClause(View view) {
		switch (view.getId()) {
			case R.id.btn_signup_service:
				BaseFragment usingFragment = new UsingFragment();
				IntroActivity introActivity = (IntroActivity) getActivity();
				introActivity.addFragment(usingFragment);
				break;
			case R.id.btn_signup_personal:
				BaseFragment personalFragment = new PersonalFragment();
				introActivity = (IntroActivity) getActivity();
				introActivity.addFragment(personalFragment);
				break;
		}
	}

	public void checkInfo() {
		if(rbtnMale.isChecked()) {
			userVO.setGenderType(GenderType.MALE);
		} else if(rbtnFemale.isChecked()) {
			userVO.setGenderType(GenderType.FEMALE);
		}

		String idByANDROID_ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		userVO.setPhoneKey(idByANDROID_ID);
		userVO.setRecommendId(edtiRecommend.getText().toString().trim());
	}

	@OnClick(R.id.ll_signup_info)
	public void layoutClick() {
		closeInputKeyboard(edtiRecommend);
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		getActivity().onBackPressed();
		doAnimationGoLeft();
	}
}

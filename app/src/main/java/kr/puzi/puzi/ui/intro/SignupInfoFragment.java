package kr.puzi.puzi.ui.intro;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.user.AgeType;
import kr.puzi.puzi.biz.user.FavoriteType;
import kr.puzi.puzi.biz.user.GenderType;
import kr.puzi.puzi.biz.user.LevelType;
import kr.puzi.puzi.biz.user.PhoneType;
import kr.puzi.puzi.biz.user.RegisterType;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.CustomArrayAdapter;
import kr.puzi.puzi.ui.IntroActivity;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.setting.PersonalFragment;
import kr.puzi.puzi.ui.setting.UsingFragment;
import kr.puzi.puzi.utils.EncryptUtils;
import retrofit2.Call;

import static kr.puzi.puzi.biz.user.AddressInfo.CITY_MAP;
import static kr.puzi.puzi.biz.user.AddressInfo.REGION_LIST;

/**
 * Created by muoe0 on 2017-07-27.
 */

public class SignupInfoFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.btn_info_beauty) public Button btnBeauty;
	@BindView(kr.puzi.puzi.R.id.btn_info_shopping) public Button btnShop;
	@BindView(kr.puzi.puzi.R.id.btn_info_game) public Button btnGame;
	@BindView(kr.puzi.puzi.R.id.btn_info_eat) public Button btnEat;
	@BindView(kr.puzi.puzi.R.id.btn_info_tour) public Button btnTour;
	@BindView(kr.puzi.puzi.R.id.btn_info_finance) public Button btnFinance;
	@BindView(kr.puzi.puzi.R.id.btn_info_culture) public Button btnCulture;
	@BindView(kr.puzi.puzi.R.id.iv_signup_all) public ImageView ivConfirm;
	@BindView(kr.puzi.puzi.R.id.rbtn_male) public RadioButton rbtnMale;
	@BindView(kr.puzi.puzi.R.id.rbtn_female) public RadioButton rbtnFemale;
	@BindView(kr.puzi.puzi.R.id.sp_age) public Spinner spAge;
	@BindView(kr.puzi.puzi.R.id.edit_recommend) public EditText edtiRecommend;
	@BindView(kr.puzi.puzi.R.id.ibtn_back) public ImageView ibtnBack;
	@BindView(kr.puzi.puzi.R.id.sp_region) public Spinner spRegion;
	@BindView(kr.puzi.puzi.R.id.sp_city) public Spinner spCity;

	private boolean isConfirm = false;
	private UserVO userVO = new UserVO();
	private AlertDialog.Builder alert_confirm;
	private ArrayList<String> favoritesList = new ArrayList<String>();
	private ArrayList<String> yearList = new ArrayList<String>();
	private String region = REGION_LIST.get(0);
	private String city = CITY_MAP.get(region).get(0);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_signup_info, container, false);
		unbinder = ButterKnife.bind(this, view);

		rbtnMale.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
		rbtnFemale.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));

		settingYears();

		alert_confirm = new AlertDialog.Builder(this.getContext());
		alert_confirm.setPositiveButton("확인", null);

		String isKakao = Preference.getProperty(getActivity(), "kakao");
		if("K".equals(isKakao)) {
			userVO.setRegisterType(RegisterType.K);
			userVO.setPasswd(Preference.getProperty(getActivity(), "temppasswd"));
			userVO.setEmail("");
		} else {
			userVO.setRegisterType(RegisterType.N);
			userVO.setPasswd(EncryptUtils.sha256(Preference.getProperty(getActivity(), "temppasswd")));
			userVO.setEmail(Preference.getProperty(getActivity(), "tempemail"));
		}

		userVO.setUserId(Preference.getProperty(getActivity(), "tempid"));
		userVO.setNotifyId(Preference.getProperty(getActivity(), "tokenFCM"));
		userVO.setPhoneType(PhoneType.A);
		userVO.setLevelType(LevelType.WELCOME);

		checkInfo();

		return view;
	}

	@OnClick(kr.puzi.puzi.R.id.btn_signup_ok)
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

		checkInfo();

		ProgressDialog.show(getActivity());

		final String notifyFCM = FirebaseInstanceId.getInstance().getToken();

		Log.i("SignupInfo","userVO.getNotifyId() : " + userVO.getNotifyId());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.signup(userVO.getUserId(), userVO.getPasswd(), userVO.getRegisterType()
					, userVO.getEmail(), notifyFCM, userVO.getGenderType(), userVO.getAge(), userVO.getFavoriteTypeList()
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
				Preference.saveMyInfo(getActivity(), userVO);

				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

	@OnItemSelected(kr.puzi.puzi.R.id.sp_age)
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

	@OnItemSelected(kr.puzi.puzi.R.id.sp_region)
	public void checkRegion(int position) {
		region = REGION_LIST.get(position);
		spCity.setAdapter(new CustomArrayAdapter(getActivity(), CITY_MAP.get(REGION_LIST.get(position))));
		city = CITY_MAP.get(region).get(0);
	}

	@OnItemSelected(kr.puzi.puzi.R.id.sp_city)
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

		spAge.setPrompt(getResources().getString(kr.puzi.puzi.R.string.info_age));
		spAge.setAdapter(new CustomArrayAdapter(getActivity(), yearList));
		spAge.setSelection(1999-1940);
		spRegion.setAdapter(new CustomArrayAdapter(getActivity(), REGION_LIST));
		spRegion.setSelection(0);
		spCity.setAdapter(new CustomArrayAdapter(getActivity(), CITY_MAP.get(REGION_LIST.get(0))));
		spCity.setSelection(0);
	}

	@OnClick({kr.puzi.puzi.R.id.btn_info_beauty, kr.puzi.puzi.R.id.btn_info_shopping, kr.puzi.puzi.R.id.btn_info_game, kr.puzi.puzi.R.id.btn_info_eat,
		kr.puzi.puzi.R.id.btn_info_tour, kr.puzi.puzi.R.id.btn_info_finance, kr.puzi.puzi.R.id.btn_info_culture})
	public void checkFavorites(View view) {
		switch (view.getId()) {
			case kr.puzi.puzi.R.id.btn_info_beauty:
				checkList(FavoriteType.BEAUTY.name(), btnBeauty);
				break;
			case kr.puzi.puzi.R.id.btn_info_shopping:
				checkList(FavoriteType.SHOPPING.name(), btnShop);
				break;
			case kr.puzi.puzi.R.id.btn_info_game:
				checkList(FavoriteType.GAME.name(), btnGame);
				break;
			case kr.puzi.puzi.R.id.btn_info_eat:
				checkList(FavoriteType.EAT.name(), btnEat);
				break;
			case kr.puzi.puzi.R.id.btn_info_tour:
				checkList(FavoriteType.TOUR.name(), btnTour);
				break;
			case kr.puzi.puzi.R.id.btn_info_finance:
				checkList(FavoriteType.FINANCE.name(), btnFinance);
				break;
			case kr.puzi.puzi.R.id.btn_info_culture:
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
			btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			btn.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorTextGray));
		} else {
			favoritesList.add(String.valueOf(category));
			btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			btn.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
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

	@OnClick(kr.puzi.puzi.R.id.btn_signup_all)
	public void checkConfirm() {
		if(isConfirm) {
			isConfirm = false;
			ivConfirm.setImageResource(kr.puzi.puzi.R.drawable.radio_off);
		} else if (!isConfirm) {
			isConfirm = true;
			ivConfirm.setImageResource(kr.puzi.puzi.R.drawable.radio_on);
		}
	}

	@OnClick({kr.puzi.puzi.R.id.btn_signup_service, kr.puzi.puzi.R.id.btn_signup_personal})
	public void checkClause(View view) {
		switch (view.getId()) {
			case kr.puzi.puzi.R.id.btn_signup_service:
				BaseFragment usingFragment = new UsingFragment();
				IntroActivity introActivity = (IntroActivity) getActivity();
				introActivity.addFragment(usingFragment);
				break;
			case kr.puzi.puzi.R.id.btn_signup_personal:
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

	@OnClick(kr.puzi.puzi.R.id.ll_signup_info)
	public void layoutClick() {
		closeInputKeyboard(edtiRecommend);
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_back)
	public void back() {
		getActivity().onBackPressed();
		doAnimationGoLeft();
	}
}

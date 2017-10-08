package com.puzi.puzi.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.EncryptUtils;
import retrofit2.Call;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by muoe0 on 2017-07-27.
 */

public class InfoFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(R.id.rbtn_male) public RadioButton rbtnMale;
	@BindView(R.id.rbtn_female) public RadioButton rbtnFemale;
	@BindView(R.id.sp_age) public Spinner spAge;
	@BindView(R.id.edit_recommend) public EditText edtiRecommend;
	@BindView(R.id.ibtn_back) public ImageView ibtnBack;

	private boolean isClause = false;
	private UserVO userVO;
	private AlertDialog.Builder alert_confirm;
	private ArrayList<String> favoritesList = new ArrayList<String>();
	private ArrayList<String> yearList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_signup_info, container, false);

		unbinder = ButterKnife.bind(this, view);

		settingYears();

		alert_confirm = new AlertDialog.Builder(this.getContext());
		alert_confirm.setPositiveButton("확인", null);
		AlertDialog alert = alert_confirm.create();

		userVO = new UserVO();
		userVO.setUserId(Preference.getProperty(getActivity(), "id"));
		userVO.setPasswd(Preference.getProperty(getActivity(), "passwd"));
		userVO.setRegisterType("N");
		userVO.setEmail(Preference.getProperty(getActivity(), "email"));
		userVO.setNotifyId("NoRegister");
		userVO.setPhoneType("A");
		userVO.setLevelType("BRONZE");

		checkInfo();

		return view;
	}

	@OnClick(R.id.btn_signup_ok)
	public void signup() {

		isChecked();

		Log.i("INFO", "User VO : " + userVO.toString());

		UserNetworkService userService = RetrofitManager.create(UserNetworkService.class);

		Call<ResponseVO> call = userService.signup(userVO.getUserId(), EncryptUtils.sha256(userVO.getPasswd()), userVO.getRegisterType()
			, userVO.getEmail(), userVO.getNotifyId(), userVO.getGenderType(), userVO.getAge(), userVO.getFavoriteTypeList()
			, userVO.getRecommendId(), userVO.getPhoneType(), userVO.getPhoneKey());

		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				Log.i("INFO", "signup responseVO : " + responseVO.toString());

				switch(responseVO.getResultType()){
					case SUCCESS:
						Log.i("INFO", "signup success.");

						String token = responseVO.getValue("token", String.class);
						Log.i("INFO", "signup token : " + token);

						Preference.addProperty(getActivity(), "token", token);
						Preference.addProperty(getActivity(), "id", userVO.getUserId());
						Preference.addProperty(getActivity(), "passwd", userVO.getPasswd());

						Intent intent = new Intent(getActivity(), MainActivity.class);
						startActivity(intent);

						getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

						break;

					default:
						Log.i("INFO", "signup failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
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
			userVO.setAgeType("TEN");
		} else if(age >= 20 && age < 30) {
			userVO.setAgeType("TWENTY");
		} else if(age >= 30 && age < 40) {
			userVO.setAgeType("THI");
		} else if(age >= 40 && age < 50) {
			userVO.setAgeType("TOUR");
		}

		Log.i("INFO", "Year : " + year + ", age type : " + userVO.getAgeType());
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

		ArrayAdapter<String> adapter = new ArrayAdapter<String>
			(getActivity(), android.R.layout.simple_spinner_dropdown_item, yearList);

		spAge.setPrompt(getResources().getString(R.string.info_age));
		spAge.setAdapter(adapter);
	}

	@OnClick({R.id.btn_info_beauty, R.id.btn_info_shopping, R.id.btn_info_game, R.id.btn_info_eat,
		R.id.btn_info_tour, R.id.btn_info_finance, R.id.btn_info_culture})
	public void checkFavorites(View view) {
		switch (view.getId()) {
			case R.id.btn_info_beauty:
				checkList("BEAUTY");
				break;
			case R.id.btn_info_shopping:
				checkList("SHOPPING");
				break;
			case R.id.btn_info_game:
				checkList("GAME");
				break;
			case R.id.btn_info_eat:
				checkList("EAT");
				break;
			case R.id.btn_info_tour:
				checkList("TOUR");
				break;
			case R.id.btn_info_finance:
				checkList("FINANCE");
				break;
			case R.id.btn_info_culture:
				checkList("CULTURE");
				break;
			default:
				break;
		}

		userVO.setFavoriteTypeList(favoritesList);
	}

	public void checkList(String category) {
		if(isFavorites(category)) {
			favoritesList.remove(category);
		} else {
			favoritesList.add(category);
		}
	}

	public boolean isFavorites(String item) {
		if(favoritesList.isEmpty()) {
			return false;
		} else {
			for (String favorite : favoritesList) {
				if (favorite.equals(item)) {
					return true;
				}
			}
			return false;
		}
	}

	@OnClick({R.id.btn_signup_service, R.id.btn_signup_personal, R.id.btn_signup_gps, R.id.btn_signup_all})
	public void checkClause(View view) {

		boolean isService = false, isPersonal = false, isGps = false;

		switch (view.getId()) {
			case R.id.btn_signup_service:
				if(isService) {
					isService = false;
				} else {
					isService = true;
				}
				break;
			case R.id.btn_signup_personal:
				if(isPersonal) {
					isPersonal = false;
				} else {
					isPersonal = true;
				}
				break;
			case R.id.btn_signup_gps:
				if(isGps) {
					isGps = false;
				} else {
					isGps = true;
				}
				break;
			case R.id.btn_signup_all:
				if(isClause) {
					isClause = false;
				} else {
					isClause = true;
				}
				break;
			default:
				break;
		}

		if(!isClause) {
			if(isService && isPersonal && isGps) {
				isClause = true;
			}
		}
	}

	public void checkInfo() {
		if(rbtnMale.isChecked()) {
			userVO.setGenderType("MALE");
		} else if(rbtnFemale.isChecked()) {
			userVO.setGenderType("FEMALE");
		}

		String idByANDROID_ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		userVO.setPhoneKey(idByANDROID_ID);
		userVO.setRecommendId(edtiRecommend.getText().toString().trim());
	}

	public void isChecked() {
		if(userVO.getAgeType() == null) {
			Toast.makeText(getContext(), "출생년도를 선택하세요", Toast.LENGTH_SHORT).show();
		} else if(userVO.getFavoriteTypeList().size() < 3) {
			if(userVO.getFavoriteTypeList().isEmpty()) {
				Toast.makeText(getContext(), "관심분야를 선택하세요", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), "관심분야를 3가지 이상 선택하세요", Toast.LENGTH_SHORT).show();
			}
		} else if(!isClause) {
			Toast.makeText(getContext(), "약관에 동의해주세요", Toast.LENGTH_SHORT).show();
		} else {
			Log.i("INFO", "signup check complete.");
		}
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

package com.puzi.puzi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.puzi.puzi.R;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.model.UserVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.PuziNetworkException;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.service.UserService;
import com.puzi.puzi.util.EncryptUtil;
import com.puzi.puzi.util.PreferenceUtil;
import com.puzi.puzi.util.ValidationUtil;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-27.
 */

public class InfoFragment extends Fragment implements View.OnClickListener{

	public InfoFragment() {

	}

	private static final String TAG = "InfoFragment";

	private UserVO userVO;
	private ValidationUtil validationUtil;
	private AlertDialog.Builder alert_confirm;
	private AlertDialog alert;
	private RadioGroup genderRadioGroup;
	private EditText ageEditText, recommendidEdittext;
	private CheckBox beautyCheckBox, shoppingCheckBox, gameCheckBox, eatCheckBox
		, tourCheckBox, financeCheckBox, cultureCheckBox, agreeCheckBox;
	private Button storeButton, serviceButton, personalButton, gpsButton;
	private RadioButton btnMale, btnFemale;
	private String age;
	private List<String> favoritesList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_signup_info, container, false);

		initComponent(view);

		validationUtil = new ValidationUtil();
		alert_confirm = new AlertDialog.Builder(this.getContext());
		alert_confirm.setPositiveButton("확인", null);
		alert = alert_confirm.create();

		userVO = new UserVO();
		userVO.setUserId(PreferenceUtil.getProperty(getActivity(), "id"));
		userVO.setPasswd(PreferenceUtil.getProperty(getActivity(), "pw"));
		userVO.setRegisterType("N");
		userVO.setEmail(PreferenceUtil.getProperty(getActivity(), "email"));
		userVO.setNotifyId("NoRegister");

		userVO.setPhoneType("A");

		storeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				infoCheck();

				UserService userService = RetrofitManager.create(UserService.class);

				Call<ResponseVO> call = userService.signup(userVO.getUserId(), EncryptUtil.sha256(userVO.getPasswd()), userVO.getRegisterType()
					, userVO.getEmail(), userVO.getNotifyId(), userVO.getGenderType(), userVO.getAgeType(), userVO.getFavoriteTypeList()
					, userVO.getRecommendId(), userVO.getPhoneType(), userVO.getPhoneKey());

				call.enqueue(new CustomCallback<ResponseVO>() {
					@Override
					public void onResponse(ResponseVO response) {
						if (response.getResultCode() == 1000) {

							/*userVO = new UserVO(MemoriesVO.id, MemoriesVO.email, registerType, MemoriesVO.pw,
								MemoriesVO.notifyId, gender, age, favorites);*/

							System.setProperty("userId", userVO.getUserId());
							System.setProperty("passwd", userVO.getPasswd());

							//MemoriesVO.token = response.getToken();
							Intent intent = new Intent(getActivity(), FragmentActivity.class);
							startActivity(intent);

						} else {
							alert.setMessage("잘못된 형식입니다");
							alert.show();
						}
					}

					@Override
					public void onFailure(PuziNetworkException e) {
						Log.e("TAG", "통신 오류(" + e.getCode() + ")");
					}

				});

			}
		});

		return view;
	}

	public void infoCheck() {

		if(btnMale.isChecked()) {
			userVO.setGenderType("MALE");
		} else if(btnFemale.isChecked()) {
			userVO.setGenderType("FEMALE");
		}

		userVO.setAge(Integer.parseInt(ageEditText.getText().toString().trim()));
		int temp = 2017 - userVO.getAge() + 1;
		age = String.valueOf(temp);

		if(temp < 20) {
			userVO.setAgeType("TEN");
		} else if(temp >= 20 && temp < 30) {
			userVO.setAgeType("TWENTY");
		} else if(temp >= 30 && temp < 40) {
			userVO.setAgeType("THI");
		}

		String idByANDROID_ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		userVO.setPhoneKey(idByANDROID_ID);

		if(beautyCheckBox.isChecked() == true) {
			favoritesList.add("BEAUTY");
		} if(shoppingCheckBox.isChecked() == true) {
			favoritesList.add("SHOPPING");
		} if(gameCheckBox.isChecked() == true) {
			favoritesList.add("GAME");
		} if(eatCheckBox.isChecked() == true) {
			favoritesList.add("EAT");
		} if(tourCheckBox.isChecked() == true) {
			favoritesList.add("TOUR");
		} if(financeCheckBox.isChecked() == true) {
			favoritesList.add("FINANCE");
		} if(cultureCheckBox.isChecked() == true) {
			favoritesList.add("CULTURE");
		}

		userVO.setFavoriteTypeList(favoritesList);
		userVO.setRecommendId(recommendidEdittext.getText().toString().trim());
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_join_service:
				break;
			case R.id.btn_join_personal:
				break;
			case R.id.btn_join_gps:
				break;
		}
	}

	private void initComponent(View view) {
		ageEditText = (EditText) view.findViewById(R.id.info_ageEditText);
		recommendidEdittext = (EditText) view.findViewById(R.id.info_recommendidEditText);

		beautyCheckBox = (CheckBox) view.findViewById(R.id.info_beautyRadioButton);
		shoppingCheckBox = (CheckBox) view.findViewById(R.id.info_shoppingRadioButton);
		gameCheckBox = (CheckBox) view.findViewById(R.id.info_gameRadioButton);
		eatCheckBox = (CheckBox) view.findViewById(R.id.info_diningRadioButton);
		tourCheckBox = (CheckBox) view.findViewById(R.id.info_tripRadioButton);
		financeCheckBox = (CheckBox) view.findViewById(R.id.info_financeRadioButton);
		cultureCheckBox = (CheckBox) view.findViewById(R.id.info_cultureRadioButton);

		agreeCheckBox = (CheckBox) view.findViewById(R.id.cb_join_agree);

		genderRadioGroup = (RadioGroup) view.findViewById(R.id.info_genderRadioGroup);
		btnMale = (RadioButton) view.findViewById(R.id.info_mRadioButton);
		btnFemale = (RadioButton) view.findViewById(R.id.info_wRadioButton);

		storeButton = (Button) view.findViewById(R.id.info_storeButton);
		serviceButton = (Button) view.findViewById(R.id.btn_join_service);
		personalButton = (Button) view.findViewById(R.id.btn_join_personal);
		gpsButton = (Button) view.findViewById(R.id.btn_join_gps);

		serviceButton.setOnClickListener(this);
		personalButton.setOnClickListener(this);
		gpsButton.setOnClickListener(this);
	}
}

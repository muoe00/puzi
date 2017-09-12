package com.puzi.puzi.Fragment;

import android.content.Intent;
import android.os.Bundle;
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
	private CheckBox beautyCheckBox, shoppingCheckBox, gameCheckBox, diningCheckBox
		, tripCheckBox, financeCheckBox, cultureCheckBox, agreeCheckBox;
	private Button storeButton, serviceButton, personalButton, gpsButton;
	private RadioButton btnMale, btnFemale;
	private String age, recommendId, favorites, registerType;
	private String[] favoritesList = new String[7];

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

				checkBox();
				infoCheck();
				validationCheck();

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

		// TODO : favoriteTypeList / phoneKey

		userVO.setRecommendId(recommendidEdittext.getText().toString().trim());
	}

	public void checkBox() {
		if (beautyCheckBox.isChecked() == true) {
			favoritesList[0] = "1";
		} else if (beautyCheckBox.isChecked() == false) {
			favoritesList[0] = "0";
		}
		if (shoppingCheckBox.isChecked() == true) {
			favoritesList[1] = "1";
		} else if (shoppingCheckBox.isChecked() == false) {
			favoritesList[1] = "0";
		}
		if (gameCheckBox.isChecked() == true) {
			favoritesList[2] = "1";
		} else if (gameCheckBox.isChecked() == false) {
			favoritesList[2] = "0";
		}
		if (diningCheckBox.isChecked() == true) {
			favoritesList[3] = "1";
		} else if (diningCheckBox.isChecked() == false) {
			favoritesList[3] = "0";
		}
		if (tripCheckBox.isChecked() == true) {
			favoritesList[4] = "1";
		} else if (tripCheckBox.isChecked() == false) {
			favoritesList[4] = "0";
		}
		if (financeCheckBox.isChecked() == true) {
			favoritesList[5] = "1";
		} else if (financeCheckBox.isChecked() == false) {
			favoritesList[5] = "0";
		}
		if (cultureCheckBox.isChecked() == true) {
			favoritesList[6] = "1";
		} else if (cultureCheckBox.isChecked() == false) {
			favoritesList[6] = "0";
		}

		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				favorites = favoritesList[i];
				continue;
			}
			favorites = favorites.concat(favoritesList[i]);
		}
	}

	public void validationCheck() {

		/*if (!validationUtil.checkUserFavorites(favorites)) {
			alert.setMessage("관심사를 선택하세요");
			alert.show();
			return;
		}
		if (age.length() == 0) {
			alert.setMessage("출생년도를 입력하세요");
			alert.show();
			return;
		}*/

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
		diningCheckBox = (CheckBox) view.findViewById(R.id.info_diningRadioButton);
		tripCheckBox = (CheckBox) view.findViewById(R.id.info_tripRadioButton);
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

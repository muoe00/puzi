package com.puzi.puzi.ui.setting;

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
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.util.EncryptUtil;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.util.ValidationUtil;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-07-27.
 */

public class InfoFragment extends Fragment implements View.OnClickListener{

	public InfoFragment() {

	}

	private static final String TAG = "InfoFragment";

	private ValidationUtil validationUtil;
	private AlertDialog.Builder alert_confirm;
	private AlertDialog alert;
	private RadioGroup genderRadioGroup;
	private EditText ageEditText, recommendidEdittext;
	private CheckBox beautyCheckBox, shoppingCheckBox, gameCheckBox, diningCheckBox
		, tripCheckBox, financeCheckBox, cultureCheckBox, agreeCheckBox;
	private Button storeButton, serviceButton, personalButton, gpsButton;
	private RadioButton radioButton;
	private int genderId;
	private String id, passwd, email, notifyId, phoneType, gender, age, recommendId, favorites, registerType;
	private String[] favoritesList = new String[7];;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_signup_info, container, false);

		initComponent(view);

		validationUtil = new ValidationUtil();
		alert_confirm = new AlertDialog.Builder(this.getContext());
		alert_confirm.setPositiveButton("확인", null);
		alert = alert_confirm.create();

		registerType = "N";

		storeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				checkBox();
				infoCheck();
				validationCheck();

				passwd = Preference.getProperty(getActivity(), "pw");
				id = Preference.getProperty(getActivity(), "id");
				email = Preference.getProperty(getActivity(), "email");
				notifyId = "NoRegister";
				phoneType = "A";

				Log.i("DEBUG", "userId : " + id);
				Log.i("DEBUG", "passwd : " + passwd);
				Log.i("DEBUG", "registerType : " + registerType);
				Log.i("DEBUG", "email : " + email);
				Log.i("DEBUG", "notifyId : " + notifyId);
				Log.i("DEBUG", "gender : " + gender);
				Log.i("DEBUG", "age : " + age);
				Log.i("DEBUG", "favorites : " + favorites);
				Log.i("DEBUG", "recommendId : " + recommendId);
				Log.i("DEBUG", "phoneType : " + phoneType);

				UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

				Call<ResponseVO> call = userNetworkService.signup(id, EncryptUtil.sha256(passwd), registerType, email
					, notifyId, gender, age, favorites, recommendId, phoneType);
				call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

					@Override
					public void onSuccess(ResponseVO response) {
						if (response.getResultCode() == 1000) {

							/*userVO = new UserVO(MemoriesVO.id, MemoriesVO.email, registerType, MemoriesVO.pw,
								MemoriesVO.notifyId, gender, age, favorites);*/

							System.setProperty("userId", id);
							System.setProperty("passwd", passwd);

							//MemoriesVO.token = response.getToken();
							Intent intent = new Intent(getActivity(), FragmentActivity.class);
							startActivity(intent);

						} else {
							alert.setMessage("잘못된 형식입니다");
							alert.show();
						}
					}
				});

			}
		});

		return view;
	}

	private void initComponent(View view) {

		genderRadioGroup = (RadioGroup) view.findViewById(R.id.info_genderRadioGroup);

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

		genderId = genderRadioGroup.getCheckedRadioButtonId();
		radioButton = (RadioButton) view.findViewById(genderId);

		storeButton = (Button) view.findViewById(R.id.info_storeButton);
		serviceButton = (Button) view.findViewById(R.id.btn_join_service);
		personalButton = (Button) view.findViewById(R.id.btn_join_personal);
		gpsButton = (Button) view.findViewById(R.id.btn_join_gps);

		serviceButton.setOnClickListener(this);
		personalButton.setOnClickListener(this);
		gpsButton.setOnClickListener(this);
	}

	public void infoCheck() {
		/*String gType = radioButton.getText().toString();

		Log.i("DEBUG", "gender type : " + gType);

		if (gType == "남자") {
			gender = "M";
		} else {
			gender = "W";
		}*/

		gender = "FEMALE";

		age = ageEditText.getText().toString().trim();
		recommendId = recommendidEdittext.getText().toString().trim();
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
}

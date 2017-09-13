package com.puzi.puzi.ui.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.puzi.puzi.R;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.setting.InfoFragment;
import com.puzi.puzi.util.PreferenceUtil;
import com.puzi.puzi.util.ValidationUtil;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SignupFragment extends Fragment implements View.OnClickListener {

	public SignupFragment() {
	}

	private static final String TAG = "SignupFragment";

	private String id, passward, confirm, email;
	private EditText idEdittext, emailEdittext, pwEdittext, repwEdittext;
	private Button overBtn, signupBtn;
	private ValidationUtil validationUtil;
	private AlertDialog.Builder alert_confirm;
	private AlertDialog alert;

	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_signup, container, false);

		alert_confirm = new AlertDialog.Builder(this.getContext());
		alert_confirm.setPositiveButton("확인", null);
		alert = alert_confirm.create();

		initComponent(view);

		repwEdittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				passward = pwEdittext.getText().toString();
				confirm = repwEdittext.getText().toString();

				if( passward.equals(confirm) ) {
					pwEdittext.setTextColor(Color.GREEN);
					repwEdittext.setTextColor(Color.GREEN);
				} else {
					pwEdittext.setTextColor(Color.RED);
					repwEdittext.setTextColor(Color.RED);
				}
			}
		});

		return view;
	}

	private void initComponent(View view) {

		idEdittext = (EditText) view.findViewById(R.id.signup_idEditText);
		emailEdittext = (EditText) view.findViewById(R.id.signup_emailEditText);
		pwEdittext = (EditText) view.findViewById(R.id.signup_pwEditText);
		repwEdittext = (EditText) view.findViewById(R.id.signup_repwEditText);

		overBtn = (Button) view.findViewById(R.id.signup_overButton);
		signupBtn = (Button) view.findViewById(R.id.signupButton);

		overBtn.setOnClickListener(this);
		signupBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.signup_overButton :
				overlapCheck();
				break;
			case R.id.signupButton :

				id = idEdittext.getText().toString().trim();
				email = emailEdittext.getText().toString().trim();

				PreferenceUtil.addProperty(getActivity(), "id", id);
				PreferenceUtil.addProperty(getActivity(), "pw", passward);
				PreferenceUtil.addProperty(getActivity(), "email", email);

				emailCheck();
				fragmentChange();
				break;
		}
	}

	public void fragmentChange() {

		Log.i("DEBUG", "fragment changed.");

		Fragment infoFragment = new InfoFragment();

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.replace(R.id.intro_fragment_container2, infoFragment);
		fragmentTransaction.addToBackStack("INFO");
		fragmentTransaction.commit();
	}

	public void emailCheck() {
		email = emailEdittext.getText().toString();
		if(!validationUtil.checkEmail(email)) {
			alert.setMessage("정확한 이메일 주소를 입력해주세요");	// 이메일 중복 확인도 해야함
			alert.show();
			return;
		}
	}

	public void overlapCheck() {

		UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);
		id = idEdittext.getText().toString().trim();

		if (!validationUtil.checkUserId(id)) {
			alert.setMessage("6-15자 이내로 입력해주세요");
			alert.show();
			return;

		} else {
			Call<ResponseVO> call = userNetworkService.check(id);
			call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

				@Override
				public void onSuccess(ResponseVO response) {
					if (response.getResultCode() == 1000) {
						alert.setMessage("사용할 수 있는 아이디입니다");
						alert.show();
					} else if (response.getResultCode() == 2002) {
						alert.setMessage("중복된 아이디입니다");
						alert.show();
					} else {
						alert.setMessage("잘못된 형식의 아이디입니다");
						alert.show();
					}
				}
			});
		}
	}

}

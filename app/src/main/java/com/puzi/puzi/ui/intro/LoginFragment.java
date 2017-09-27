package com.puzi.puzi.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.common.BasicDialog;
import com.puzi.puzi.utils.EncryptUtils;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class LoginFragment extends Fragment {

	private Unbinder unbinder;

	@BindView(R.id.login_et_id) public EditText etId;
	@BindView(R.id.edit_login_pw) public EditText etPwd;
	@BindView(R.id.login_btn) public Button btnLogin;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_login, container, false);

		unbinder = ButterKnife.bind(this, view);

		startAnimation();

		return view;
	}

	private void startAnimation() {
		Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_1);
		Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_2);
		Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_3);
		etId.startAnimation(animation1);
		etPwd.startAnimation(animation2);
		btnLogin.startAnimation(animation3);
	}

	private boolean isValid(String id, String pwd) {
		if(id == null || id.length() < 6){
			String errorNoid = getActivity().getResources().getString(R.string.login_error_noid);
			Toast.makeText(getActivity(), errorNoid, Toast.LENGTH_SHORT).show();
			return false;
		} else if(pwd == null || pwd.length() < 6){
			String errorNopwd = getActivity().getResources().getString(R.string.login_error_nopwd);
			Toast.makeText(getActivity(), errorNopwd, Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	@OnClick(R.id.login_btn)
	public void login(){
		final String id = etId.getText().toString();
		final String pwd = etPwd.getText().toString();

		// check valid id&pwd
		if(isValid(id, pwd)){

			ProgressDialog.show(getActivity());

			UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);
			String notifyId = "NoRegister";
			String phoneType = "A";
			String phoneKey = "";

			Call<ResponseVO<String>> call = userNetworkService.login(id, EncryptUtils.sha256(pwd), notifyId, phoneType, phoneKey);
			call.enqueue(new CustomCallback<ResponseVO<String>>(getActivity()) {
				@Override
				public void onSuccess(ResponseVO<String> responseVO) {

					switch(responseVO.getResultType()){
						case SUCCESS:
							successLogin(responseVO.getValue("token"), id, pwd);
							break;

						case LOGIN_FAIL:
							BasicDialog.show(getActivity(), "로그인실패", "아이디 또는 비밀번호를 확인해주세요");
							break;
					}
				}
			});
		}
	}

	private void successLogin(String token, String id, String pwd) {
		Log.d("### login token : ", token);

		Preference.addProperty(getActivity(), "autoId", id);
		Preference.addProperty(getActivity(), "autoPw", pwd);

		startActivity(new Intent(getActivity(), MainActivity.class));
		getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
		getActivity().finish();
	}

	@OnClick(R.id.btn_signup)
	public void signup() {
		Fragment signupFragment = new SignupFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container, signupFragment);
		fragmentTransaction.addToBackStack("SIGNUP");
		fragmentTransaction.commit();
	}

	@OnClick(R.id.kakao_login_btn)
	public void kakaoLogin() {

	}

	@OnClick(R.id.btn_srch_id)
	public void searchId() {
		Fragment searchFragment = new SearchIdFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container, searchFragment);
		fragmentTransaction.addToBackStack("SEARCH");
		fragmentTransaction.commit();
	}

	@OnClick(R.id.btn_srch_pw)
	public void searchPw() {
		Fragment searchFragment = new SearchPwFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container, searchFragment);
		fragmentTransaction.addToBackStack("SEARCH");
		fragmentTransaction.commit();
	}

	@OnEditorAction(R.id.edit_login_pw)
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE){
			login();
		}
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

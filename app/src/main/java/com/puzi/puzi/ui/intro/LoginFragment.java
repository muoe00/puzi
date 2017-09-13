package com.puzi.puzi.ui.intro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResultType;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.util.EncryptUtil;
import com.puzi.puzi.util.PreferenceUtil;
import com.puzi.puzi.util.tokenUtil;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class LoginFragment extends Fragment implements TextView.OnEditorActionListener {

	private static final String TAG = "LoginFragment";

	private Unbinder unbinder;

	@BindView(R.id.login_et_id)
	public EditText etId;
	@BindView(R.id.login_et_pwd)
	public EditText etPwd;
	@BindView(R.id.login_btn)
	public Button btnLogin;

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

		if(id == null || id.length() < 5){
			String errorNoid = getActivity().getResources().getString(R.string.login_error_noid);
			Toast.makeText(getActivity(), errorNoid, Toast.LENGTH_SHORT).show();
			return false;
		} else if(pwd == null || pwd.length() < 5){
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

			// progress
			ProgressDialog.show(getActivity());

			UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);
			String notifyId = "NoRegister";
			String phoneType = "A";

			Call<ResponseVO<String>> call = userNetworkService.login(id, EncryptUtil.sha256(pwd), notifyId, phoneType);
			call.enqueue(new CustomCallback<ResponseVO<String>>(getActivity()) {
				@Override
				public void onSuccess(ResponseVO<String> responseVO) {

					ProgressDialog.dismiss();
					ResultType resultType = responseVO.getResultType();

					switch(resultType){
						case SUCCESS:
							String token = responseVO.getValue("token");
							tokenUtil.TOKEN = token;
							Log.e("### login token : ", ""+token);

							PreferenceUtil.addProperty(getActivity(), "autoId", id);
							PreferenceUtil.addProperty(getActivity(), "autoPw", pwd);
							PreferenceUtil.addProperty(getActivity(),"token", token);

							startActivity(new Intent(getActivity(), MainActivity.class));
							getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
							getActivity().finish();
							break;

						case LOGIN_FAIL:
							PreferenceUtil.addProperty(getActivity(), "loginPwd", "");

							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("로그인실패");
							builder.setMessage("아이디 또는 비밀번호를 확인해주세요");
							builder.setNeutralButton("닫기", new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});
							builder.show();
							break;
					}
				}
			});
		}
	}

	@OnClick(R.id.signup_btn)
	public void signup() {
		Fragment signupFragment = new SignupFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container2, signupFragment);
		fragmentTransaction.addToBackStack("SIGNUP");
		fragmentTransaction.commit();
	}

	@OnClick(R.id.kakao_login_btn)
	public void kakaoLogin() {
	}

	@OnClick(R.id.search_btn)
	public void search() {
		Fragment searchFragment = new SearchFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container2, searchFragment);
		fragmentTransaction.addToBackStack("SEARCH");
		fragmentTransaction.commit();
	}

	@OnEditorAction(R.id.login_et_pwd)
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

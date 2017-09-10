package com.puzi.puzi.Fragment;

import android.app.Activity;
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
import com.puzi.puzi.MainActivity;
import com.puzi.puzi.ProgressDialog;
import com.puzi.puzi.R;
import com.puzi.puzi.ResultCode;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.PuziNetworkException;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.service.UserService;
import com.puzi.puzi.util.EncryptUtil;
import com.puzi.puzi.util.PreferenceUtil;
import com.puzi.puzi.util.tokenUtil;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class LoginFragment extends Fragment implements TextView.OnEditorActionListener, View.OnClickListener {

	public LoginFragment() {

	}

	private static final String TAG = "LoginFragment";

	private EditText etId, etPwd;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_login, container, false);

		initComponent(view);

		return view;
	}

	private void initComponent(View view) {

		// edittext
		etId = (EditText) view.findViewById(R.id.login_et_id);
		String id = PreferenceUtil.getProperty(getActivity(), "autoId");
		etId.setText(id == null ? "" : id);

		etPwd = (EditText) view.findViewById(R.id.login_et_pwd);
		String pwd = PreferenceUtil.getProperty(getActivity(), "autoPw");
		etPwd.setText(pwd == null ? "" : pwd);
		etPwd.setOnEditorActionListener(this);

		// button
		Button btnLogin = (Button) view.findViewById(R.id.login_btn);
		btnLogin.setOnClickListener(this);

		Button btnSignup = (Button) view.findViewById(R.id.signup_btn);
		btnSignup.setOnClickListener(this);

		Button btnKakao = (Button) view.findViewById(R.id.kakao_login_btn);
		btnKakao.setOnClickListener(this);

		Button btnSearch = (Button) view.findViewById(R.id.search_btn);
		btnSearch.setOnClickListener(this);

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

	public void login(){

		final String id = etId.getText().toString();
		final String pwd = etPwd.getText().toString();

		// check valid id&pwd
		if(isValid(id, pwd)){

			// progress
			ProgressDialog.show(getActivity());

			// set id and password in preference
			PreferenceUtil.addProperty(getActivity(), "autoId", id);
			PreferenceUtil.addProperty(getActivity(), "autoPw", pwd);

			Log.d("PreferenceUtil # ", "Id : " + PreferenceUtil.getProperty(getActivity(), "autoId"));
			Log.d("PreferenceUtil # ", "Pwd : " + PreferenceUtil.getProperty(getActivity(), "autoPw"));

			// request to server
			login(getActivity(), id, pwd);
		}
	}

	public void login(final Activity activity, String id, String pwd) {

		UserService userService = RetrofitManager.create(UserService.class);
		String notifyId = "NoRegister";
		String phoneType = "A";

		Call<ResponseVO<String>> call = userService.login(id, EncryptUtil.sha256(pwd), notifyId, phoneType);
		call.enqueue(new CustomCallback<ResponseVO<String>>() {
			@Override
			public void onResponse(ResponseVO<String> responseVO) {

				ProgressDialog.dismiss();
				int resultCode = responseVO.getResultCode();
				String resultMsg = responseVO.getResultMsg();

				switch(resultCode){
					case ResultCode.SUCCESS:
						String token = responseVO.getValue("token");

						tokenUtil.TOKEN = token;

						Log.e("### login token : ", ""+token);

						PreferenceUtil.addProperty(getActivity(),"token", token);

						startActivity(new Intent(getActivity(), MainActivity.class));
						activity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
						activity.finish();

						break;

					case ResultCode.FAIL_LOGIN_INFO:
						PreferenceUtil.addProperty(getActivity(), "loginPwd", "");

						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder = new AlertDialog.Builder(activity);
						builder.setTitle("로그인실패");
						builder.setMessage("아이디 또는 비밀번호를 확인해주세요");
						builder.setNeutralButton("닫기", new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						builder.show();
						break;

					default:
						break;

				}
			}

			@Override
			public void onFailure(PuziNetworkException e) {
				Log.e("TAG", "통신 오류(" + e.getCode()+")");
			}

		});
	}

	public void signup() {
		Fragment signupFragment = new SignupFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container2, signupFragment);
		fragmentTransaction.addToBackStack("SIGNUP");
		fragmentTransaction.commit();
	}

	public void kakaoLogin() {
	}

	public void search() {
		Fragment searchFragment = new SearchFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container2, searchFragment);
		fragmentTransaction.addToBackStack("SEARCH");
		fragmentTransaction.commit();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

		if(v.getId()==R.id.login_et_pwd && actionId== EditorInfo.IME_ACTION_DONE){
			login();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.login_btn) {
			login();
		} else if (v.getId() == R.id.signup_btn){
			signup();
		} else if (v.getId() == R.id.kakao_login_btn){
			kakaoLogin();
		} else if (v.getId() == R.id.search_btn){
			search();
		}
	}
}

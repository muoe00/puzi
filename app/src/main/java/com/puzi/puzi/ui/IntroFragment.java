package com.puzi.puzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.intro.LoginFragment;
import com.puzi.puzi.ui.intro.SignupFragment;

/**
 * Created by muoe0 on 2017-11-05.
 */

public class IntroFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(R.id.btn_signup) public Button btnSignup;
	@BindView(R.id.btn_kakao_login) public Button btnKakao;
	@BindView(R.id.btn_login) public Button btnLogin;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_intro, container, false);
		unbinder = ButterKnife.bind(this, view);
		startAnimation();

		return view;
	}

	private void startAnimation() {
		Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_1);
		Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_2);
		Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_3);
		btnSignup.startAnimation(animation1);
		btnKakao.startAnimation(animation2);
		btnLogin.startAnimation(animation3);
	}

	@OnClick(R.id.btn_login)
	public void login(){
		BaseFragment fragment = new LoginFragment();

		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(fragment);
	}

	@OnClick(R.id.btn_signup)
	public void changedFragment(View view) {
		BaseFragment fragment = new SignupFragment();

		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(fragment);
	}

	@OnClick(R.id.btn_kakao_login)
	public void kakaoLogin() {
		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.isKakaoLogin();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

}

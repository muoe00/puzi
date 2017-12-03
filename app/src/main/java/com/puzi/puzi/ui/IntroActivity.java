package com.puzi.puzi.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.common.BasicDialog;
import com.puzi.puzi.ui.intro.LoginFragment;
import com.puzi.puzi.ui.intro.SignupInfoFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.security.MessageDigest;
import java.util.ArrayList;

import static com.puzi.puzi.utils.PuziUtils.getDevicesUUID;

public class IntroActivity extends BaseFragmentActivity {

	private SessionCallback mKakaocallback;

	private String tokenFCM = null;
	private String uuid = null;
	private String tempId = null;
	private long backKeyPressedTime;
	private boolean isKakao = false;

	private ArrayList<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private BaseFragment fragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		initFragment();
		getAppKeyHash();
	}

	private void initFragment() {
		fragment = new IntroFragment();
		fragmentList.add(fragment);
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
	}

	public void isKakaoLogin() {
		Log.i("TAG", "isKakaoLogin");

		// 카카오 세션 오픈
		mKakaocallback = new SessionCallback();
		com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
		com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
		// com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, IntroActivity.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private class SessionCallback implements ISessionCallback {

		@Override
		public void onSessionOpened() {

			Log.i("TAG" , "onSessionOpened");

			UserManagement.requestMe(new MeResponseCallback() {

				@Override
				public void onFailure(ErrorResult errorResult) {
					int ErrorCode = errorResult.getErrorCode();
					int ClientErrorCode = -777;

					if (ErrorCode == ClientErrorCode) {
						Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
					} else {
						Log.i("TAG" , "오류로 카카오로그인 실패 ");
					}
				}

				@Override
				public void onSessionClosed(ErrorResult errorResult) {
					Log.i("TAG" , "onSessionClosed : " + errorResult.getErrorMessage());
				}

				@Override
				public void onNotSignedUp() {
					Log.i("TAG" , "onNotSignedUp");
				}

				@Override
				public void onSuccess(UserProfile userProfile) {
					//로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
					//사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
					Log.i("UserProfile", userProfile.toString());

					checkUser();

					if(isKakao) {
						kakaoIdLogin(tempId, uuid);
					} else {
						kakaoIdSignup(tempId, uuid);
					}
				}
			});

		}

		@Override
		public void onSessionOpenFailed(KakaoException exception) {
			Log.i("TAG" , "onSessionOpenFailed");

			exception.printStackTrace();
		}
	}

	public void kakaoIdLogin(final String id, final String pwd) {
		final String notifyId = Preference.getProperty(getActivity(), "tokenFCM");
		final String phoneType = "A";
		final String phoneKey = "ABC";

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.login(id, pwd, notifyId, phoneType, phoneKey);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				LoginFragment loginFragment = new LoginFragment();
				loginFragment.successLogin(responseVO.getString("token"), id, pwd);
			}

			@Override
			public void onFail(ResponseVO responseVO) {
				BasicDialog.show(getActivity(), "로그인실패", "아이디 또는 비밀번호를 확인해주세요");
			}
		});
	}

	public void kakaoIdSignup(final String id, final String pw) {
		Preference.addProperty(getActivity(), "id", id);
		Preference.addProperty(getActivity(), "passwd", pw);
		Preference.addProperty(getActivity(), "kakao", "K");
		// Preference.addProperty(getActivity(), "email", email);

		BaseFragment infoFragment = new SignupInfoFragment();

		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(infoFragment);
	}

	public void checkUser() {
		uuid = getDevicesUUID(getApplicationContext());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.checkKakao(uuid);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				isKakao = responseVO.getBoolean("registered");
				tempId = responseVO.getString("tempId");
			}
		});
	}

	private void getAppKeyHash() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				Log.i("Hash key", something);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("name not found", e.toString());
		}
	}

	public void addFragment(BaseFragment fragment) {
		if(fragment.isAdded()) {
			return;
		} else {
			fragmentList.add(fragment);
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.rightin, R.anim.leftout, R.anim.rightin, R.anim.leftout);
			fragmentTransaction.replace(R.id.intro_fragment_container, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
			Log.i(PuziUtils.INFO, "fragment list size : " + fragmentList.size());
		}
	}

	public void removeFragment(int position) {
		fragmentList.remove(position);
		fragment = fragmentList.get(position - 1);
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.leftin, R.anim.rightout, R.anim.leftin, R.anim.rightout);
		fragmentTransaction.replace(R.id.intro_fragment_container, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		Log.i(PuziUtils.INFO, "fragment list size : " + fragmentList.size());
	}

	public void backFragment() {
		int position = fragmentList.size() - 1;

		if(fragmentList.size() == 1) {
			if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
				backKeyPressedTime = System.currentTimeMillis();
				Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
				finish();
			}
		} else if(fragmentList.size() > 1) {
			removeFragment(position);
		}
	}

	@Override
	public void onBackPressed(){
		backFragment();
	}
}

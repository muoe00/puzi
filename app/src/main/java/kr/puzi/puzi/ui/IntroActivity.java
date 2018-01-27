package kr.puzi.puzi.ui;

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
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.BasicDialog;
import kr.puzi.puzi.ui.intro.LoginFragment;
import kr.puzi.puzi.ui.intro.SignupInfoFragment;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.security.MessageDigest;
import java.util.ArrayList;

import static kr.puzi.puzi.utils.PuziUtils.getDevicesUUID;

public class IntroActivity extends BaseFragmentActivity {

	public static int TEST_COUNT = 0;

	private SessionCallback mKakaocallback;

	private String uuid = null;
	private long backKeyPressedTime;
	private boolean isCheckingKakaoTempId = false;

	private ArrayList<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private BaseFragment fragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TEST_COUNT += 1;
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_intro);
		initFragment();
		getAppKeyHash();
	}

	private void initFragment() {
		fragment = new IntroFragment();
		fragmentList.add(fragment);
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(kr.puzi.puzi.R.id.intro_fragment_container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
	}

	public void isKakaoLogin() {
		ProgressDialog.show(getActivity());
		Log.i("TAG", "isKakaoLogin");

		// 카카오 세션 오픈
		mKakaocallback = new SessionCallback();
		com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
		com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
		com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, IntroActivity.this);
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
			ProgressDialog.dismiss();

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
				}
			});

		}

		@Override
		public void onSessionOpenFailed(KakaoException e) {
			Log.e("TAG" , "onSessionOpenFailed", e);
		}
	}

	public void kakaoIdLogin(final String id, final String pwd) {
		final String notifyId = Preference.getProperty(getActivity(), "tokenFCM");
		final String phoneType = "A";
		final String phoneKey = DeviceKeyFinder.find(getActivity());

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
		Preference.addProperty(getActivity(), "tempid", id);
		Preference.addProperty(getActivity(), "temppasswd", pw);
		Preference.addProperty(getActivity(), "kakao", "K");
		// Preference.addProperty(getActivity(), "email", email);

		BaseFragment infoFragment = new SignupInfoFragment();

		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(infoFragment);
	}

	public void checkUser() {
		if(isCheckingKakaoTempId) {
			return;
		}
		isCheckingKakaoTempId = true;

		ProgressDialog.show(getActivity());

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
				isCheckingKakaoTempId = false;

				ProgressDialog.dismiss();

				boolean isKakao = responseVO.getBoolean("registered");
				String tempId = responseVO.getString("tempId");
				if(isKakao) {
					kakaoIdLogin(tempId, uuid);
				} else {
					kakaoIdSignup(tempId, uuid);
				}
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
			fragmentTransaction.setCustomAnimations(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.leftout, kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.leftout);
			fragmentTransaction.replace(kr.puzi.puzi.R.id.intro_fragment_container, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
			Log.i(PuziUtils.INFO, "fragment list size : " + fragmentList.size());
		}
	}

	public void removeFragment(int position) {
		fragmentList.remove(position);
		fragment = fragmentList.get(position - 1);
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(kr.puzi.puzi.R.anim.leftin, kr.puzi.puzi.R.anim.rightout, kr.puzi.puzi.R.anim.leftin, kr.puzi.puzi.R.anim.rightout);
		fragmentTransaction.replace(kr.puzi.puzi.R.id.intro_fragment_container, fragment);
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

package com.puzi.puzi.ui;

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
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.puzi.puzi.R;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
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
		// 카카오 세션을 오픈한다
//		mKakaocallback = new SessionCallback();
//		com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
//		com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
//		com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, IntroActivity.this);
	}

	private class SessionCallback implements ISessionCallback {
		@Override
		public void onSessionOpened() {
			Log.d("TAG" , "세션 오픈됨");
			// 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
			kakaoRequestMe();
		}

		@Override
		public void onSessionOpenFailed(KakaoException exception) {
			if(exception != null) {
				Log.i("TAG" , exception.getMessage());
			}
		}
	}

	protected void kakaoRequestMe() {
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
				Log.i("TAG" , "오류로 카카오로그인 실패 ");
			}

			@Override
			public void onSuccess(UserProfile userProfile) {
				checkUser();
				if(tempId != null) {
				}
			}

			@Override
			public void onNotSignedUp() {
				// 카톡 회원이 아닐 경우
			}
		});
	}

	public void checkUser() {
		uuid = getDevicesUUID(getApplicationContext());

		final UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);
		Call<ResponseVO> call = userNetworkService.checkKakao(uuid);
		call.enqueue(new CustomCallback(this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				isKakao = responseVO.getValue("registered", boolean.class);

				if(isKakao) {
					tempId = responseVO.getString("tempId");
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

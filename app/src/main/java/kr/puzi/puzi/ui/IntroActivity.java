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
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.user.RegisterType;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.BasicDialog;
import kr.puzi.puzi.ui.intro.SignupInfoFragment;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.security.MessageDigest;
import java.util.ArrayList;

public class IntroActivity extends BaseFragmentActivity {

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
		com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK, IntroActivity.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ProgressDialog.dismiss();
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

			if(Session.getCurrentSession().isOpened()) {
				UserManagement.requestMe(new MeResponseCallback() {
					@Override
					public void onFailure(ErrorResult errorResult) {
						ProgressDialog.dismiss();
						int ErrorCode = errorResult.getErrorCode();
						int ClientErrorCode = -777;

						if (ErrorCode == ClientErrorCode) {
							Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "카카오톡 로그인에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
							Log.i("TAG", "오류로 카카오로그인 실패 ");
						}
					}

					@Override
					public void onSessionClosed(ErrorResult errorResult) {
						Log.i("TAG", "onSessionClosed : " + errorResult.getErrorMessage());
						ProgressDialog.dismiss();
					}

					@Override
					public void onNotSignedUp() {
						Toast.makeText(getApplicationContext(), "카카오톡에 가입되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
						Log.i("TAG", "onNotSignedUp");
						ProgressDialog.dismiss();
					}

					@Override
					public void onSuccess(UserProfile userProfile) {
						//로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
						//사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
						Log.i("UserProfile", userProfile.toString());
						ProgressDialog.dismiss();
						checkUser(userProfile.getUUID());
					}
				});
			}

		}

		@Override
		public void onSessionOpenFailed(KakaoException e) {
			ProgressDialog.dismiss();
			Log.e("TAG" , "onSessionOpenFailed", e);
		}
	}

	public void kakaoIdLogin(final String id, final String pwd) {
		ProgressDialog.show(getActivity());

		final String notifyId = FirebaseInstanceId.getInstance().getToken();
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
				ProgressDialog.dismiss();
				Preference.addProperty(getActivity(), "token", responseVO.getString("token"));
				Preference.addProperty(getActivity(), "id", id);
				Preference.addProperty(getActivity(), "passwd", pwd);

				UserVO userVO = new UserVO();
				userVO.setRegisterType(RegisterType.K);
				Preference.saveMyInfo(getActivity(), userVO);

				startActivity(new Intent(IntroActivity.this, MainActivity.class));
				getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
				getActivity().finish();
			}

			@Override
			public void onFail(ResponseVO responseVO) {
				ProgressDialog.dismiss();
				BasicDialog.show(getActivity(), "로그인실패", "아이디 또는 비밀번호를 확인해주세요");
			}
		});
	}

	public void kakaoIdSignup(final String id, final String pw) {
		Preference.addProperty(getActivity(), "tempid", id);
		Preference.addProperty(getActivity(), "temppasswd", pw);
		Preference.addProperty(getActivity(), "kakao", RegisterType.K.name());

		UserVO userVO = new UserVO();
		userVO.setRegisterType(RegisterType.K);
		Preference.saveMyInfo(getActivity(), userVO);

		BaseFragment infoFragment = new SignupInfoFragment();
		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(infoFragment);
	}

	public void checkUser(final String kakaoUUID) {
		if(isCheckingKakaoTempId) {
			ProgressDialog.dismiss();
			return;
		}
		isCheckingKakaoTempId = true;

		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.checkKakao(kakaoUUID);
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
					kakaoIdLogin(tempId, kakaoUUID);
				} else {
					kakaoIdSignup(tempId, kakaoUUID);
				}
			}

			@Override
			public void onFail(ResponseVO responseVO) {
				super.onFail(responseVO);
				isCheckingKakaoTempId = false;
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

	public void addFragment(BaseFragment fragment, UserVO userVO) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("userVO", userVO);
		// fragment.putEx

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
		ProgressDialog.dismiss();
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

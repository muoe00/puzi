package com.puzi.puzi.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.firebase.iid.FirebaseInstanceId;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.ResultType;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.security.MessageDigest;

public class LaunchActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.splash_progress_Bar) public ProgressBar pbSplash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);

		unbinder = ButterKnife.bind(this);

		FirebaseInstanceId.getInstance().getToken();
		String tokenFCM = FirebaseInstanceId.getInstance().getToken();
		Log.i(PuziUtils.INFO, "FCM_Token : " + tokenFCM);
		Preference.addProperty(LaunchActivity.this, "tokenFCM", tokenFCM);

		getAppKeyHash();

		pbSplash.setIndeterminate(true);
		pbSplash.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
		pbSplash.setVisibility(View.VISIBLE);

		checkLogin();
	}

	public void checkLogin() {
		final String autoId = Preference.getProperty(this, "id");
		final String autoPw = Preference.getProperty(this, "passwd");
		final String tokenFCM = Preference.getProperty(LaunchActivity.this, "tokenFCM");

		// 메인 화면으로 갈지(자동로그인 성공), 로그인 화면으로 갈지(자동로그인 실패) 결정 (변수 : auto_login)
		if (autoId != null && autoPw != null) {
			login(autoId, autoPw, tokenFCM, "");
		} else {
			new Handler() {
				@Override
				public void handleMessage(Message message) {
					goLoginPage();
				}
			}.sendEmptyMessageDelayed(0, 800);
		}
	}

	private void goLoginPage() {
		startActivity(new Intent(this, IntroActivity.class));
		doAnimationAlpha1000();
		finish();
	}

	public void login(final String id, final String sha256Pw, final String notifyId, final String phoneKey) {
		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.login(id, sha256Pw, notifyId, "A", phoneKey);
			}
		});
		service.enqueue(new CustomCallback(this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				ResultType resultCode = responseVO.getResultType();

				switch (resultCode) {
					case SUCCESS:
						Log.i("INFO", "AUTO LOGIN SUCCESS");
						String token = responseVO.getString("token");
						Log.i("INFO", "AUTO TOKEN : " + token);
						Preference.addProperty(getActivity(), "id", id);
						Preference.addProperty(getActivity(), "passwd", sha256Pw);
						Preference.addProperty(getActivity(), "token", token);

						startActivity(new Intent(LaunchActivity.this, MainActivity.class));
						finish();
						break;
					case LOGIN_FAIL:
						goLoginPage();
						break;
					default:
						break;
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

}

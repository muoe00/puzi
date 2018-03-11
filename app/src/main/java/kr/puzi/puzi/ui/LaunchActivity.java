package kr.puzi.puzi.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.igaworks.IgawCommon;
import com.kakao.util.helper.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.ResultType;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.base.BaseActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

public class LaunchActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.splash_progress_Bar) public ProgressBar pbSplash;

	private boolean goAlarmSetting = false;
	private String tokenFCM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		Log.d("TAG", "+++ onCreate");

		Log.d("KeyHash", Utility.getKeyHash(getActivity()));

		unbinder = ButterKnife.bind(this);

		FirebaseInstanceId.getInstance().getToken();
		tokenFCM = FirebaseInstanceId.getInstance().getToken();
		Log.i(PuziUtils.INFO, "FCM_Token : " + tokenFCM);
		Preference.addProperty(LaunchActivity.this, "tokenFCM", tokenFCM);

		pbSplash.setIndeterminate(true);
		pbSplash.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
		pbSplash.setVisibility(View.VISIBLE);

		if(!NotificationManagerCompat.from(getActivity()).areNotificationsEnabled()) {
			OneButtonDialog.show(getActivity(), "설정", "알람설정이 꺼져있습니다.\n더 많은 적립을 받기위해 알람을 켜시겠습니까?", "켜기",
				new DialogButtonCallback() {
					@Override
					public void onClick() {
						goAlarmSetting = true;
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						startActivityForResult(intent, 0);
						return;
					}
				}, new DialogButtonCallback() {
					@Override
					public void onClick() {
						checkLogin();
					}
				});
			return;
		}
		checkLogin();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("TAG", "+++ onResume");
		if(goAlarmSetting) {
			checkLogin();
		}
		IgawCommon.startSession(LaunchActivity.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		IgawCommon.endSession();
	}

	public void checkLogin() {
		final String autoId = Preference.getProperty(this, "id");
		final String autoPw = Preference.getProperty(this, "passwd");

		// 메인 화면으로 갈지(자동로그인 성공), 로그인 화면으로 갈지(자동로그인 실패) 결정
		if (!android.text.TextUtils.isEmpty(autoId) && !android.text.TextUtils.isEmpty(autoPw)) {
			login(autoId, autoPw, tokenFCM, DeviceKeyFinder.find(getActivity()));
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
						Preference.removeProperty(getActivity(), "id");
						Preference.removeProperty(getActivity(), "passwd");
						Preference.removeProperty(getActivity(), "token");
						goLoginPage();
						break;
					default:
						break;
				}
			}

			@Override
			public void onFail(ResponseVO responseVO) {
				Preference.removeProperty(getActivity(), "id");
				Preference.removeProperty(getActivity(), "passwd");
				Preference.removeProperty(getActivity(), "token");
				goLoginPage();
			}
		});
	}

}

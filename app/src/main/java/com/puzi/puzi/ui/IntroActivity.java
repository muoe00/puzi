package com.puzi.puzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.iid.FirebaseInstanceId;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.ResultType;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.intro.LoginFragment;
import com.puzi.puzi.utils.EncryptUtils;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.ArrayList;

public class IntroActivity extends BaseFragmentActivity {

	private long backKeyPressedTime;
	public boolean AUTO_LOGIN = false;

	private ArrayList<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private BaseFragment fragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);

		FirebaseInstanceId.getInstance().getToken();
		String tokenFCM = FirebaseInstanceId.getInstance().getToken();
		Log.i(PuziUtils.INFO, "FCM_Token : " + tokenFCM);

		Preference.addProperty(IntroActivity.this, "tokenFCM", tokenFCM);

		final String autoId = Preference.getProperty(this, "id");
		final String autoPw = Preference.getProperty(this, "passwd");

		// 메인 화면으로 갈지(자동로그인 성공), 로그인 화면으로 갈지(자동로그인 실패) 결정 (변수 : auto_login)
		if(autoId != null && autoPw != null) {
			UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

			Call<ResponseVO> call = userNetworkService.login(autoId, EncryptUtils.sha256(autoPw), tokenFCM, "A", "");
			call.enqueue(new CustomCallback<ResponseVO>(this) {
				@Override
				public void onSuccess(ResponseVO responseVO) {

					ProgressDialog.dismiss();
					ResultType resultCode = responseVO.getResultType();

					switch(resultCode){
						case SUCCESS:
							Log.i("INFO", "AUTO LOGIN SUCCESS");
							AUTO_LOGIN = true;
							String token = responseVO.getString("token");
							if(token != null) {
								Log.i("INFO", "AUTO TOKEN : " + token);
								Preference.addProperty(IntroActivity.this, "token", token);
							}
							break;
						case LOGIN_FAIL:
							AUTO_LOGIN = false;
							break;
						default:
							break;
					}
				}
			});
		}

		new Handler(){
			@Override
			public void handleMessage(Message message){
				if(AUTO_LOGIN) {
					startActivity(new Intent(IntroActivity.this, MainActivity.class));
					fragmentList.clear();
					finish();
				}
				else {
					setContentView(R.layout.activity_intro);
					fragment = new LoginFragment();
					addFragment(fragment);
				}
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

	public void addFragment(BaseFragment fragment) {
		if(fragment.isAdded()) {
			return;
		} else {
			fragmentList.add(fragment);
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.rightin, R.anim.shrink_back, R.anim.rightin, R.anim.shrink_back);
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

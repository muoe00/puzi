package com.puzi.puzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.ResultType;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.intro.LoginFragment;
import com.puzi.puzi.utils.EncryptUtils;
import com.puzi.puzi.utils.tokenUtils;
import retrofit2.Call;

import java.util.ArrayList;

public class IntroActivity extends FragmentActivity {

	private long backKeyPressedTime;
	public boolean AUTO_LOGIN = false;

	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private Fragment fragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_intro);

		final String autoId = Preference.getProperty(this, "autoId");
		final String autoPw = Preference.getProperty(this, "autoPw");

		// 메인 화면으로 갈지(자동로그인 성공), 로그인 화면으로 갈지(자동로그인 실패) 결정 (변수 : auto_login)
		if(autoId != null && autoPw != null) {

			UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

			Call<ResponseVO<String>> call = userNetworkService.login(autoId, EncryptUtils.sha256(autoPw), "NoRegister", "A", "");
			call.enqueue(new CustomCallback<ResponseVO<String>>(this) {
				@Override
				public void onSuccess(ResponseVO<String> responseVO) {

					ProgressDialog.dismiss();
					ResultType resultCode = responseVO.getResultType();

					switch(resultCode){
						case SUCCESS:
							Log.i("INFO", "AUTO LOGIN SUCCESS");
							AUTO_LOGIN = true;
							String token = responseVO.getValue("token");
							if(token != null)
								Log.i("INFO", "AUTO TOKEN : " + token);
							tokenUtils.TOKEN = token;
							Preference.addProperty(IntroActivity.this, "token", token);
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

		// delay 2 second
		new Handler(){
			@Override
			public void handleMessage(Message message){
				String temp;

				if(AUTO_LOGIN)
					temp = "true";
				else
					temp = "false";

				if(AUTO_LOGIN) {
					startActivity(new Intent(IntroActivity.this, MainActivity.class));
				}
				else {
					fragment = new LoginFragment();
					fragmentList.add(fragment);
					fragmentTransaction.replace(R.id.intro_fragment_container, fragment);
					fragmentTransaction.commit();
				}
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

	public void backFragment() {

		int size = fragmentList.size();
		int position = size-1;

		Log.i("INFO", "size : " + size + ", position : " + position);

		if(size == 1) {
			fragmentManager.popBackStack();
		} else if(size > 1) {
			fragmentList.remove(position);
			fragment = fragmentList.get(position - 1);
			fragmentTransaction.replace(R.id.intro_fragment_container, fragment);
			fragmentTransaction.commit();
		} else {
			if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
				backKeyPressedTime = System.currentTimeMillis();
				Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
				finish();
			}
		}
	}

	@Override
	public void onBackPressed(){
		backFragment();
	}
}

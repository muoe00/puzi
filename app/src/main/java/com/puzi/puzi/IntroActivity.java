package com.puzi.puzi;

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
import com.puzi.puzi.Fragment.LoginFragment;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.network.PuziNetworkException;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.service.UserService;
import com.puzi.puzi.util.EncryptUtil;
import com.puzi.puzi.util.PreferenceUtil;
import com.puzi.puzi.util.tokenUtil;
import retrofit2.Call;

public class IntroActivity extends FragmentActivity {

	private long backKeyPressedTime;
	private String autoId, autoPw;
	public boolean AUTO_LOGIN = false;

	Fragment fragment = null;
	FragmentManager fragmentManager = getSupportFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}

		setContentView(R.layout.activity_intro);

		autoId = PreferenceUtil.getProperty(this, "autoId");
		autoPw = PreferenceUtil.getProperty(this, "autoPw");

		// 메인 화면으로 갈지(자동로그인 성공), 로그인 화면으로 갈지(자동로그인 실패) 결정 (변수 : auto_login)
		if(autoId != null && autoPw != null) {

			UserService userService = RetrofitManager.create(UserService.class);

			Call<ResponseVO<String>> call = userService.login(autoId, EncryptUtil.sha256(autoPw), "NoRegister", "A");
			call.enqueue(new CustomCallback<ResponseVO<String>>() {
				@Override
				public void onResponse(ResponseVO<String> responseVO) {

					ProgressDialog.dismiss();
					int resultCode = responseVO.getResultCode();

					switch(resultCode){
						case ResultCode.SUCCESS:
							Log.e("######", "AUTO LOGIN SUCCESS");
							AUTO_LOGIN = true;
							String token = responseVO.getValue("token");
							if(token != null)
								Log.e("AUTO TOKEN : ", token);
							tokenUtil.TOKEN = token;
							PreferenceUtil.addProperty(IntroActivity.this, "token", token);
							break;
						case ResultCode.FAIL_LOGIN_INFO:
							AUTO_LOGIN = false;
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

		// delay 2 second
		new Handler(){

			@Override
			public void handleMessage(Message message){

				String temp;

				if(AUTO_LOGIN)
					temp = "true";
				else
					temp = "false";

				Log.d("AUTO_LOGIN # ", temp);

				if(AUTO_LOGIN) {
					Intent intent = new Intent(IntroActivity.this, MainActivity.class);
					startActivity(intent);
				}
				else {
					fragment = new LoginFragment();
					fragmentTransaction.replace(R.id.intro_fragment_container2, fragment);
					fragmentTransaction.commit();
				}
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	public void onBackPressed(){
		// exit
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

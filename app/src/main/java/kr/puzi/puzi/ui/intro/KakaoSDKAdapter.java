package kr.puzi.puzi.ui.intro;

import android.app.Activity;
import android.content.Context;
import com.kakao.auth.*;
import kr.puzi.puzi.PuziApplication;

/**
 * Created by muoe0 on 2017-11-01.
 */

public class KakaoSDKAdapter extends KakaoAdapter {

	@Override
	public IApplicationConfig getApplicationConfig() {
		return new IApplicationConfig() {
			@Override
			public Activity getTopActivity() {
				return PuziApplication.getCurrentActivity();
			}

			@Override
			public Context getApplicationContext() {
				return PuziApplication.getInstance();
			}
		};
	}

	@Override
	public ISessionConfig getSessionConfig() {
		return new ISessionConfig() {
			@Override
			public AuthType[] getAuthTypes() {
				return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
			}

			@Override
			public boolean isUsingWebviewTimer() {
				return false;
			}

			@Override
			public ApprovalType getApprovalType() {
				return ApprovalType.INDIVIDUAL;
			}

			@Override
			public boolean isSaveFormData() {
				return true;
			}
		};
	}
}

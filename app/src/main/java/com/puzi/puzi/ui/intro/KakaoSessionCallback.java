package com.puzi.puzi.ui.intro;

import android.util.Log;
import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

public class KakaoSessionCallback implements ISessionCallback {

	private KakaoSessionCallback mKakaocallback;

	@Override
	public void onSessionOpened() {
		Log.i("INFO", "onSessionOpened");
	}

	@Override
	public void onSessionOpenFailed(KakaoException exception) {
		if(exception != null) {
			Log.i("INFO", exception.getMessage());
		}
	}

	public void bindKakao() {
		mKakaocallback = new KakaoSessionCallback();
	}
}
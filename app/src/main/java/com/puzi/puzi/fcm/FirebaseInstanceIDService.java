package com.puzi.puzi.fcm;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.puzi.puzi.utils.PuziUtils;

/**
 * Created by muoe0 on 2017-10-15.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

	public FirebaseInstanceIDService() {
		super();
	}

	@Override
	public void onTokenRefresh() {
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.i(PuziUtils.INFO, "Refreshed token: " + refreshedToken);

		sendRegistrationToServer(refreshedToken);
	}

	private void sendRegistrationToServer(String token) {
		// Add custom implementation, as needed.
	}
}

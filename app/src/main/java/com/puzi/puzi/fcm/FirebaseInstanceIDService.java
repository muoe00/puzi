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
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.i(PuziUtils.INFO, "Refreshed token: " + refreshedToken);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
		sendRegistrationToServer(refreshedToken);
	}
	// [END refresh_token]

	/**
	 * Persist token to third-party servers.
	 *
	 * Modify this method to associate the user's FCM InstanceID token with any server-side account
	 * maintained by your application.
	 *
	 * @param token The new token.
	 */
	private void sendRegistrationToServer(String token) {
		// Add custom implementation, as needed.

	}
}

package com.puzi.puzi.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by muoe0 on 2017-10-15.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

	public FirebaseInstanceIDService() {
		super();
	}

	@Override
	public void onTokenRefresh() {
		super.onTokenRefresh();
	}
}

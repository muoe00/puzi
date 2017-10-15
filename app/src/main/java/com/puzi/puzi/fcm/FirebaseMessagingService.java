package com.puzi.puzi.fcm;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by muoe0 on 2017-10-15.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
	public FirebaseMessagingService() {
		super();
	}

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
	}

	@Override
	public void onDeletedMessages() {
		super.onDeletedMessages();
	}

	@Override
	public void onMessageSent(String s) {
		super.onMessageSent(s);
	}

	@Override
	public void onSendError(String s, Exception e) {
		super.onSendError(s, e);
	}
}

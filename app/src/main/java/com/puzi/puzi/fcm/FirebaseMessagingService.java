package com.puzi.puzi.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.IntroActivity;

import java.util.Map;

/**
 * Created by muoe0 on 2017-10-15.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

	private static PowerManager.WakeLock sCpuWakeLock;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
/*      PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
		PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
		wakeLock.acquire(3000);*/

		Map<String, String> data = remoteMessage.getData();
		String defaultData = data.get("default");
		if(defaultData == null || "".equals(defaultData)) {
			return;
		}

		Log.d("PUSH", "+++ defaultData : " + defaultData);

		Intent responseIntent = new Intent("com.puzi.puzi.GOT_PUSH");
		responseIntent.putExtra("default", defaultData);
		sendOrderedBroadcast(responseIntent, null);
	}

	private void sendPushNotification(String message) {
		Intent intent = new Intent(this, IntroActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
			PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.splash_logo).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.splash_logo) )
			.setContentTitle("Puzi messaging test")
			.setContentText(message)
			.setAutoCancel(true)
			.setSound(defaultSoundUri).setLights(000000255,500,2000)
			.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (sCpuWakeLock != null) {
			return;
		}

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		sCpuWakeLock = pm.newWakeLock(
			PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
				PowerManager.ACQUIRE_CAUSES_WAKEUP |
				PowerManager.ON_AFTER_RELEASE, "hi");

		sCpuWakeLock.acquire();

		if (sCpuWakeLock != null) {
			sCpuWakeLock.release();
			sCpuWakeLock = null;
		}

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
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

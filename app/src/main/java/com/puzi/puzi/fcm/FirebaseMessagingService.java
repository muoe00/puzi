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
import com.google.firebase.messaging.RemoteMessage;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.IntroActivity;

/**
 * Created by muoe0 on 2017-10-15.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

	private String msg;
	private static PowerManager.WakeLock sCpuWakeLock;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		// [START_EXCLUDE]
		// There are two types of messages data messages and notification messages. Data messages are handled
		// here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
		// traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
		// is in the foreground. When the app is in the background an automatically generated notification is displayed.
		// When the user taps on the notification they are returned to the app. Messages containing both notification
		// and data payloads are treated as notification messages. The Firebase console always sends notification
		// messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
		// [END_EXCLUDE]

		sendPushNotification(remoteMessage.getData().get("message"));

		/*// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Log.i(PuziUtils.INFO, "From: " + remoteMessage.getFrom());

		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0) {
			Log.i(PuziUtils.INFO, "Message data payload: " + remoteMessage.getData());
		}

		// Check if message contains a notification payload.
		if (remoteMessage.getNotification() != null) {
			Log.i(PuziUtils.INFO, "Message Notification Body: " + remoteMessage.getNotification().getBody());
		}

		msg = remoteMessage.getNotification().getBody();

		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.

		// [END receive_message]

		Intent intent = new Intent(this, IntroActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
			new Intent(this, IntroActivity.class), PendingIntent.FLAG_ONE_SHOT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.splash_logo)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.splash_logo))
			.setContentTitle("Puzi messaging test")
			.setContentText(msg)
			.setAutoCancel(true)
			.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
			.setVibrate(new long[]{1, 1000})
			.setLights(000000255,500,2000)
			.setContentIntent(contentIntent);;

		NotificationManager notificationManager =
			(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		wakelock.acquire(5000);

		notificationManager.notify(0 *//* ID of notification *//*, mBuilder.build());

		mBuilder.setContentIntent(contentIntent);*/
	}

	private void sendPushNotification(String message) {
		System.out.println("received message : " + message);
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

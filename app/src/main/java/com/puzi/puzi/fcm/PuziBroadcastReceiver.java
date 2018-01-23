package com.puzi.puzi.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.fcm.ScreenOffAlertActivity;
import com.puzi.puzi.ui.fcm.ScreenOnAlertActivity;

import static com.puzi.puzi.fcm.PuziPushType.ADVERTISEMENT;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
public class PuziBroadcastReceiver extends BroadcastReceiver {

	private static Gson gson = new Gson();

	@Override
	public void onReceive(Context context, Intent intent) {
		PuziPushMessageVO messageVO = convert(intent);
		Log.d("PUSH", "+++++++ PuziBroadcastReceiver>onReceive messageVO : " + messageVO);
		if(messageVO == null) {
			return;
		}

		switch (messageVO.getType()) {
			case ADVERTISEMENT:
				boolean success = PuziPushManager.addId(ADVERTISEMENT, messageVO.getReceivedAdvertiseDTO().getReceivedAdvertiseId());
				if(!success) {
					Log.d("PUSH", "+++ ALREADY RECEIVED : " + messageVO);
					return;
				}
				messageVO.getReceivedAdvertiseDTO().transferComponyInfo();
				PuziPushManager.setFinalMessage(messageVO);
				break;

			case NOTICE:

				break;
		}

		alertProcess(context, messageVO);
	}

	private void alertProcess(Context context, PuziPushMessageVO messageVO) {
		int icon = R.drawable.puzi_bi;
		NotificationManager notificationManager = (NotificationManager) context
			.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = getNewIntent(context, messageVO, MainActivity.class);
		if(notificationIntent == null) {
			Log.d("PUSH", "+++++++ PuziBroadcastReceiver>alertProcess Type is wrong!");
			return;
		}

		String message = getMessage(messageVO);

		notificationIntent.setFlags(
			Intent.FLAG_ACTIVITY_NEW_TASK |
				Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Notification notification = new Notification.Builder(context)
			.setAutoCancel(true)
			.setSmallIcon(icon)
			.setContentTitle("PUZI")
			.setContentText(message)
			.setContentIntent(intent)
			.build();

		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
			notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}else if(audioManager.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE){
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(500);
		}

		if (!isScreenOn(context)) {
			// 잠겨있을 경우 깨우기
			Intent newViewIntent = getNewIntent(context, messageVO, ScreenOffAlertActivity.class);
			newViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newViewIntent);
		} else {
			Intent newViewIntent = getNewIntent(context, messageVO, ScreenOnAlertActivity.class);
			newViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newViewIntent);
		}

		notificationManager.notify(0, notification);
	}

	private String getMessage(PuziPushMessageVO messageVO) {
		switch (messageVO.getType()) {
			case ADVERTISEMENT: return messageVO.getReceivedAdvertiseDTO().getSendComment();
			case NOTICE: return "[공지]" + messageVO.getNoticeDTO().getTitle();
		}
		return null;
	}

	private Intent getNewIntent(Context context, PuziPushMessageVO messageVO, Class<?> mainActivityClass) {
		Intent intent = null;
		switch (messageVO.getType()) {
			case ADVERTISEMENT:
				intent = new Intent(context, mainActivityClass);
				intent.putExtra("TYPE", "ADVERTISEMENT");
				intent.putExtra("receivedAdvertiseDTO", gson.toJson(messageVO.getReceivedAdvertiseDTO()));
				return intent;

			case NOTICE:
				intent = new Intent(context, mainActivityClass);
				intent.putExtra("TYPE", "NOTICE");
				intent.putExtra("noticeDTO", gson.toJson(messageVO.getNoticeDTO()));
				return intent;
		}
		return null;
	}

	private boolean isScreenOn(Context context) {
		return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

	public static PuziPushMessageVO convert(Intent intent) {
		String defaultData = intent.getStringExtra("default");
		if(defaultData == null || "".equals(defaultData)) {
			Log.d("PUSH", "+++++++ PuziBroadcastReceiver>onReceive defaultData is Null!");
			return null;
		}

		return gson.fromJson(defaultData, PuziPushMessageVO.class);
	}

}

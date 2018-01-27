package kr.puzi.puzi.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by JangwonPark on 2018. 1. 23..
 */

public class DeviceKeyFinder {

	protected static final String PREFS_FILE = "device_id.xml";
	protected static final String PREFS_DEVICE_ID = "device_id";

	public static String find(Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
		final String id = prefs.getString(PREFS_DEVICE_ID, null);
		if (id != null) {
			// Use the ids previously computed and stored in the
			// prefs file
			return UUID.fromString(id).toString();
		} else {
			final String androidId = Settings.Secure.getString(
				context.getContentResolver(), Settings.Secure.ANDROID_ID);
			// Use the Android ID unless it's broken, in which case
			// fallback on deviceId,
			// unless it's not available, then fallback on a random
			// number which we store to a prefs file
			String uuid;
			try {
				if (!"9774d56d682e549c".equals(androidId)) {
					uuid = UUID.nameUUIDFromBytes(androidId
						.getBytes("utf8")).toString();
				} else {
					final String deviceId = (
						(TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE))
						.getDeviceId();
					uuid = deviceId != null ? UUID
						.nameUUIDFromBytes(deviceId
							.getBytes("utf8")).toString() : UUID
						.randomUUID().toString();
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			// Write the value out to the prefs file
			prefs.edit()
				.putString(PREFS_DEVICE_ID, uuid.toString())
				.commit();

			return uuid;
		}
	}
}

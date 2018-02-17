package kr.puzi.puzi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.kakao.auth.KakaoSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import kr.puzi.puzi.ui.intro.KakaoSDKAdapter;

public class PuziApplication extends Application {

	private static PuziApplication instance = null;
	private static volatile Activity currentActivity = null;

	public static PuziApplication getInstance() {
		return instance;
	}

	private static GoogleAnalytics sAnalytics;
	private static Tracker sTracker;
	
	@Override
	public void onCreate() {
		super.onCreate();

		PuziApplication.instance = this;
		KakaoSDK.init(new KakaoSDKAdapter());

		initImageLoader(getApplicationContext());

		sAnalytics = GoogleAnalytics.getInstance(this);
	}

	public static Activity getCurrentActivity() {
		return currentActivity;
	}

	public static void setCurrentActivity(Activity activity) {
		PuziApplication.currentActivity = activity;
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(
					new DisplayImageOptions.Builder()
						.showImageOnFail(R.drawable.basic_picture_preview)
						.showImageForEmptyUri(R.drawable.basic_picture_preview)
						.showImageOnLoading(R.drawable.basic_picture_preview).build())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(30 * 1024 * 1024)
				.discCacheFileCount(500)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		instance = null;
	}

	/**
	 * Gets the default {@link Tracker} for this {@link Application}.
	 * @return tracker
	 */
	synchronized public Tracker getDefaultTracker() {
		// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
		if (sTracker == null) {
			sTracker = sAnalytics.newTracker(R.xml.global_tracker);
		}

		return sTracker;
	}
}

package com.puzi.puzi;

/**
 * Copyright(c) 2014 Chatspit, Inc. All rights reserved.
 * 
 * Author      : Lee Jung Hoon
 * Date         : 2014.03.18
 * Description   :
**/

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class PuziApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader(getApplicationContext());
	}


	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(30 * 1024 * 1024)
				.discCacheFileCount(500)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}
}

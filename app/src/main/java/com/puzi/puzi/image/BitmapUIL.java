package com.puzi.puzi.image;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.puzi.puzi.R;

public class BitmapUIL {
	
	public static Bitmap load(String imageUrl) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		return imageLoader.loadImageSync(imageUrl);
	}
	
	public static void load(String imageUrl, ImageLoadingListener listener){
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(imageUrl, getOption(), listener);
	}
	
	public static void load(String imageUrl, ImageView imageView){
		if(imageUrl == null){
			return;
		}
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageAware imageAware = new ImageViewAware(imageView, false);
		imageLoader.displayImage(imageUrl, imageAware, getOption());	
	}
	
	private static DisplayImageOptions getOption(){		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.puzi_bi_02)
		.showImageForEmptyUri(R.drawable.puzi_bi_02)
		.showImageOnFail(R.drawable.puzi_bi_02)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		return options;
	}
}

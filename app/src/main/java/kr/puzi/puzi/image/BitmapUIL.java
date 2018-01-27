package kr.puzi.puzi.image;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

	public static void loadExactly(String imageUrl, ImageView imageView){
		if(imageUrl == null){
			return;
		}

		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageAware imageAware = new ImageViewAware(imageView, false);
		imageLoader.displayImage(imageUrl, imageAware, getOptionEXACTLY());
	}


	private static DisplayImageOptions getOption(){		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(kr.puzi.puzi.R.drawable.basic_picture_preview)
		.showImageForEmptyUri(kr.puzi.puzi.R.drawable.basic_picture_preview)
		.showImageOnFail(kr.puzi.puzi.R.drawable.basic_picture_preview)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		return options;
	}

	private static DisplayImageOptions getOptionEXACTLY(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(kr.puzi.puzi.R.drawable.basic_picture_preview)
			.showImageForEmptyUri(kr.puzi.puzi.R.drawable.basic_picture_preview)
			.showImageOnFail(kr.puzi.puzi.R.drawable.basic_picture_preview)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.NONE_SAFE)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		return options;
	}
}

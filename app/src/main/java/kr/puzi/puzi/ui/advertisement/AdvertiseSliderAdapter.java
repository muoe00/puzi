package kr.puzi.puzi.ui.advertisement;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kr.puzi.puzi.R;


/**
 * Created by juhyun on 2018. 2. 17..
 */

public class AdvertiseSliderAdapter extends PagerAdapter {

    public static int infiniteScroll = 50;

    private Activity activity;
    private int[] images = {R.drawable.ad_01_copy, R.drawable.image2, R.drawable.image3};
    private LayoutInflater inflater;

    public AdvertiseSliderAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return images.length * infiniteScroll;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        position %= images.length;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_slider, container, false);

        ImageView imageView = (ImageView)v.findViewById(R.id.iv_slider_ad);
        imageView.setImageResource(images[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}

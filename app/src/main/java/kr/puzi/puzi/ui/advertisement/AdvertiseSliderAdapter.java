package kr.puzi.puzi.ui.advertisement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.advertisement.SlidingInfoVO;
import kr.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SlidingNetworkService;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.channel.editorspage.EditorsPageActivity;
import retrofit2.Call;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by juhyun on 2018. 2. 17..
 */

public class AdvertiseSliderAdapter extends PagerAdapter {

    public static int infiniteScroll = 50;

    private Activity activity;
    private LayoutInflater inflater;
    private List<SlidingInfoVO> slidingInfoVOList = newArrayList();

    public AdvertiseSliderAdapter(Activity activity, List<SlidingInfoVO> slidingInfoVOList) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.slidingInfoVOList = slidingInfoVOList;
    }

    @Override
    public int getCount() {
        return slidingInfoVOList.size() * infiniteScroll;
    }

    public int getTotalCount() {
        return slidingInfoVOList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    public SlidingInfoVO getItem(int position) {
        return slidingInfoVOList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final int realPosition = getRealPosition(position);

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_slider, container, false);

        ImageButton ibtnSlidingAd = (ImageButton) v.findViewById(R.id.iv_slider_ad);
        BitmapUIL.load(slidingInfoVOList.get(realPosition).getSlidingPreviewUrl(), ibtnSlidingAd);
        ibtnSlidingAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SlidingInfoVO slidingInfoVO = getItem(realPosition);
                if(!slidingInfoVO.isClicked()) {
                    LazyRequestService service = new LazyRequestService(activity, SlidingNetworkService.class);
                    service.method(new LazyRequestService.RequestMothod<SlidingNetworkService>() {
                        @Override
                        public Call<ResponseVO> execute(SlidingNetworkService slidingNetworkService, String token) {
                            return slidingNetworkService.saveCpc(token, slidingInfoVO.getUserSlidingId());
                        }
                    });
                    service.enqueue(new CustomCallback(activity) {
                        @Override
                        public void onSuccess(ResponseVO responseVO) {
                            updateClicked(realPosition);
                        }
                    });
                }

                ChannelEditorsPageVO channelEditorsPageVO = new ChannelEditorsPageVO();
                channelEditorsPageVO.setLink(slidingInfoVO.getLink());
                channelEditorsPageVO.setTitle(slidingInfoVO.getCompanyAlias());
                Intent intent = new Intent(activity, EditorsPageActivity.class);
                intent.putExtra("channelEditorsPageVO", channelEditorsPageVO);
                activity.startActivity(intent);
                ((MainActivity) activity).doAnimationGoRight();
            }
        });

        container.addView(v);
        return v;
    }

    public void updateSaved(int position) {
        SlidingInfoVO slidingInfoVO = slidingInfoVOList.get(position);
        slidingInfoVO.setSaved(true);
        notifyDataSetChanged();
    }

    public void updateClicked(int position) {
        SlidingInfoVO slidingInfoVO = slidingInfoVOList.get(position);
        slidingInfoVO.setClicked(true);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

	public int getRealPosition(int position) {
        return position % slidingInfoVOList.size();
	}
}

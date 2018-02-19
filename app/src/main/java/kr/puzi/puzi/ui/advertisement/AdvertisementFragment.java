package kr.puzi.puzi.ui.advertisement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.biz.advertisement.SlidingInfoVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.AdvertisementNetworkService;
import kr.puzi.puzi.network.service.SlidingNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.common.PointDialog;
import kr.puzi.puzi.utils.PuziUtils;
import lombok.NoArgsConstructor;
import retrofit2.Call;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-07-08.
 */

@NoArgsConstructor
public class AdvertisementFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.vp_advertise) public ViewPager viewPager;
	@BindView(kr.puzi.puzi.R.id.lv_advertise) public ListView lvAd;
	@BindView(R.id.sv_ad) public ScrollView svAd;
	@BindView(R.id.btn_slide_ad) public Button btnSlide;
	@BindView(kr.puzi.puzi.R.id.srl_advertisement_container) public SwipeRefreshLayout srlContainer;
	@BindView(kr.puzi.puzi.R.id.sliding_container) public FrameLayout llSlidingContainer;
	@BindView(kr.puzi.puzi.R.id.tx_sliding_count) public TextView tvSlidingCount;

	private AdvertiseSliderAdapter advertiseSliderAdapter;
	private AdvertisementListAdapter advertiseListAdapter;
	private ReceivedAdvertiseVO startReceivedAdvertiseVO = null;
	private int selectedSlidingPosition = 0;

	public static List<Integer> needToUpdateIds = newArrayList();

	public void setPush(ReceivedAdvertiseVO receivedAdvertiseVO) {
		startReceivedAdvertiseVO = receivedAdvertiseVO;
	}

	public static void updateSavedPoint(int id) {
		needToUpdateIds.add(id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(kr.puzi.puzi.R.layout.fragment_advertisement, container, false);
		unbinder = ButterKnife.bind(this, v);

		initComponent();

		if(startReceivedAdvertiseVO != null) {
			Log.d("PUSH", "++++startReceivedAdvertiseVO : " + startReceivedAdvertiseVO);
			Intent intent = new Intent(getActivity(), AdvertisementDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("advertise", startReceivedAdvertiseVO);
			intent.putExtras(bundle);
			startActivity(intent);
			doAnimationGoRight();
		}

		return v;
	}

	@Override
	public void onResume() {
		if(needToUpdateIds.size() > 0) {
			for(int index : needToUpdateIds) {
				advertiseListAdapter.changeSaved(index, true);
			}
			needToUpdateIds.clear();
		}

		viewPager.setFocusable(true);
		lvAd.setFocusable(false);

		super.onResume();
	}

	private void initComponent() {
		advertiseListAdapter = new AdvertisementListAdapter(getActivity(), R.layout.fragment_advertisement_item, R.layout.fragment_advertisement_item_new, R.layout.fragment_advertisement_item_saved, 0, lvAd, svAd, new CustomPagingAdapter.ListHandler() {
			@Override
			public void getList() {
				advertiseListAdapter.startProgressWithScrollDown();
				getAdvertiseList();
			}
		}, false);
		lvAd.setAdapter(advertiseListAdapter);
		advertiseListAdapter.getList();

		srlContainer.setColorSchemeResources(kr.puzi.puzi.R.color.colorPuzi);
		srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				advertiseListAdapter.clean();
				advertiseListAdapter.getList();
				srlContainer.setRefreshing(false);
			}
		});

		requestSlidingList();
	}

	private void requestSlidingList() {
		LazyRequestService service = new LazyRequestService(getActivity(), SlidingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SlidingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SlidingNetworkService slidingNetworkService, String token) {
				return slidingNetworkService.slidingList(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				List<SlidingInfoVO> advertiseList = responseVO.getList("slidingInfoDTOList", SlidingInfoVO.class);
				if(advertiseList == null || advertiseList.size() == 0) {
					viewPager.setVisibility(View.GONE);
					return;
				}

				final int size = advertiseList.size();
				tvSlidingCount.setText("1 / " + size);
				SlidingInfoVO slidingInfoVO = advertiseList.get(0);
				if(slidingInfoVO.isSaved()) {
					btnSlide.setText("적립완료");
					btnSlide.setTextColor(Color.parseColor("#666666"));
					btnSlide.setBackgroundResource(R.drawable.bg_sliding_saved);
				} else {
					btnSlide.setText("적립하기");
					btnSlide.setTextColor(Color.parseColor("#ff2470"));
					btnSlide.setBackgroundResource(R.drawable.bg_sliding_save);
				}
				advertiseSliderAdapter = new AdvertiseSliderAdapter(getActivity(), advertiseList);
				viewPager.setAdapter(advertiseSliderAdapter);
				viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageSelected(int position) {
						selectedSlidingPosition = position;
						int realPosition = advertiseSliderAdapter.getRealPosition(selectedSlidingPosition);
						tvSlidingCount.setText((realPosition+1) + " / " + advertiseSliderAdapter.getTotalCount());
						if(position < size)
							viewPager.setCurrentItem(position + size, false);
						else if(position >= size * advertiseSliderAdapter.infiniteScroll)
							viewPager.setCurrentItem(position - size, false);

						SlidingInfoVO slidingInfoVO = advertiseSliderAdapter.getItem(realPosition);
						if(slidingInfoVO.isSaved()) {
							btnSlide.setText("적립완료");
							btnSlide.setTextColor(Color.parseColor("#666666"));
							btnSlide.setBackgroundResource(R.drawable.bg_sliding_saved);
						} else {
							btnSlide.setText("적립하기");
							btnSlide.setTextColor(Color.parseColor("#ff2470"));
							btnSlide.setBackgroundResource(R.drawable.bg_sliding_save);
						}
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});

				llSlidingContainer.setVisibility(View.VISIBLE);
			}
		});

	}

	@OnClick(R.id.btn_slide_ad)
	public void onClickSavedSlidePoint() {
		final int realPosition = advertiseSliderAdapter.getRealPosition(selectedSlidingPosition);
		final SlidingInfoVO slidingInfoVO = advertiseSliderAdapter.getItem(realPosition);
		if(!slidingInfoVO.isSaved()) {
			ProgressDialog.show(getActivity());

			LazyRequestService service = new LazyRequestService(getActivity(), SlidingNetworkService.class);
			service.method(new LazyRequestService.RequestMothod<SlidingNetworkService>() {
				@Override
				public Call<ResponseVO> execute(SlidingNetworkService slidingNetworkService, String token) {
					return slidingNetworkService.save(token, slidingInfoVO.getUserSlidingId());
				}
			});
			service.enqueue(new CustomCallback(getActivity()) {
				@Override
				public void onSuccess(ResponseVO responseVO) {
					ProgressDialog.dismiss();

					int savedPoint = responseVO.getInteger("savedPoint");
					PointDialog.load(getActivity(), savedPoint, true);
					advertiseSliderAdapter.updateSaved(realPosition);
					btnSlide.setText("적립완료");
					btnSlide.setTextColor(Color.parseColor("#666666"));
					btnSlide.setBackgroundResource(R.drawable.bg_sliding_saved);
					Preference.updateMyInfoPlusPoint(getActivity(), savedPoint);
					((MainActivity)getActivity()).updateUserInfoOnTitleBar();
				}
			});
		}
	}

	public void getAdvertiseList() {
		LazyRequestService service = new LazyRequestService(getActivity(), AdvertisementNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<AdvertisementNetworkService>() {
			@Override
			public Call<ResponseVO> execute(AdvertisementNetworkService advertisementNetworkService, String token) {
				return advertisementNetworkService.adList(token, advertiseListAdapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				advertiseListAdapter.stopProgress();

				List<ReceivedAdvertiseVO> advertiseList = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
				Log.d(PuziUtils.INFO, "Advertise main / advertiseList : " + advertiseList.toString());
				Log.d(PuziUtils.INFO, "advertiseList totalCount : " + responseVO.getInteger("totalCount"));

				advertiseListAdapter.addListWithTotalCount(advertiseList, responseVO.getInteger("totalCount"));
			}
		});
	}

	public synchronized void refresh(int adId, boolean saved) {
		advertiseListAdapter.changeSaved(adId, saved);
		advertiseListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	public void addAdvertisement(ReceivedAdvertiseVO receivedAdvertiseVO) {
		advertiseListAdapter.addFirst(receivedAdvertiseVO);
	}
}

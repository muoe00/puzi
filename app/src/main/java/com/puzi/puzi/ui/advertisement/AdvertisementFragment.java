package com.puzi.puzi.ui.advertisement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.user.LevelActivity;
import com.puzi.puzi.ui.user.PointActivity;
import com.puzi.puzi.ui.user.RecommendActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class AdvertisementFragment extends Fragment implements AbsListView.OnScrollListener {

	Unbinder unbinder;

	@BindView(R.id.lv_advertise) public ListView lvAd;
	@BindView(R.id.tv_point) public TextView tvPoint;

	private int pagingIndex = 1;
	private boolean lastVisible = false;
	private UserVO userVO;
	private List<ReceivedAdvertiseVO> advertiseList;
	private AdvertisementListAdapter advertiseGridAdapter;

	private static final String TAG = "AdvertisementFragment";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_advertisement, container, false);

		unbinder = ButterKnife.bind(this, view);

		getAdvertiseList(view);
		getUser();
		lvAd.setOnScrollListener(this);

		return view;
	}

	@OnClick({R.id.btn_pointhistory, R.id.btn_level, R.id.ibtn_recommend})
	public void changePage(View view) {
		Intent intent = null;
		switch (view.getId()) {
			case R.id.btn_level:
				intent = new Intent(getActivity(), LevelActivity.class);
				break;
			case R.id.btn_pointhistory:
				intent = new Intent(getActivity(), PointActivity.class);
				break;
			case R.id.ibtn_recommend:
				intent = new Intent(getActivity(), RecommendActivity.class);
				break;
			default:
				break;
		}
		startActivity(intent);
	}

	public void getUser() {

		Log.i("INFO", "getUser");

		final UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> callUser = userNetworkService.myInfo(token);
		callUser.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				switch(responseVO.getResultType()){
					case SUCCESS:
						userVO = responseVO.getValue("userInfoDTO", UserVO.class);
						Log.i("INFO", "HomeFragment main / userVO : " + userVO.toString());

						int point = userVO.getPoint();
						NumberFormat numberFormat = NumberFormat.getInstance();
						String result = numberFormat.format(point);
						tvPoint.setText(result);
						break;

					default:
						Log.i("INFO", "advertisement getUser failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void getAdvertiseList(final View view) {

		final AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> callList = advertisementNetworkService.adList(token, pagingIndex);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				Log.i("INFO", "advertise responseVO : " + responseVO.toString());

				switch(responseVO.getResultType()){
					case SUCCESS:
						advertiseList = responseVO.getValue("cmpnDTOList", List.class);
						Log.i(PuziUtils.INFO, "Advertise main / advertiseList : " + advertiseList.toString());

						advertiseGridAdapter = new AdvertisementListAdapter(view.getContext(), advertiseList);
						lvAd.setAdapter(advertiseGridAdapter);
						break;

					default:
						Log.i("INFO", "advertisement getAdvertiseList failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) && lastVisible) {
			if(pagingIndex < 10) {
				pagingIndex++;
			}
			Log.i(PuziUtils.INFO, "pagingIndex : " + pagingIndex);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if((totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
			lastVisible = true;
			Log.i(PuziUtils.INFO, "lastVisible : " + lastVisible);
		} else {
			lastVisible = false;
			Log.i(PuziUtils.INFO, "lastVisible : " + lastVisible);
		}
	}
}

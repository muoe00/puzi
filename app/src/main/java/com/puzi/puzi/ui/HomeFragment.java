package com.puzi.puzi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.puzi.puzi.R;
import com.puzi.puzi.model.*;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResultType;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.ui.user.LevelActivity;
import com.puzi.puzi.ui.user.PointActivity;
import com.puzi.puzi.ui.user.RecommendActivity;
import com.puzi.puzi.util.PreferenceUtil;
import retrofit2.Call;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {

	private Context context;
	private GridView gvAd;
	private TextView tvPoint;
	private ImageView ivRangking;
	private ImageButton ibtnRecommend;
	private Button btnPoint, btnLevel;
	private FrameLayout flAd;
	private LinearLayout llText;
	private UserVO userVO;
	private MainVO mainVO;
	private List<ReceivedAdvertiseVO> advertiseList;
	private List testList;
	private List<NoticeVO> noticeList;
	private HomeGridAdapter homeGridAdapter;

	public HomeFragment() {
	}

	private static final String TAG = "HomeFragment";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		context = this.getContext();

		initComponent(view);
		getMainInfomation(view);
		getUser(view);
		gvAd.setOnScrollListener(this);

		return view;
	}

	private void initComponent(View view) {
		gvAd = (GridView) view.findViewById(R.id.gv_advertise);

		btnPoint = (Button) view.findViewById(R.id.btn_pointhistory);
		btnPoint.setOnClickListener(this);
		btnLevel = (Button) view.findViewById(R.id.btn_level);
		btnLevel.setOnClickListener(this);

		ivRangking = (ImageView) view.findViewById(R.id.iv_rankImage);

		ibtnRecommend = (ImageButton) view.findViewById(R.id.ibtn_recommend);
		ibtnRecommend.setOnClickListener(this);

		tvPoint = (TextView) view.findViewById(R.id.tv_point);
		flAd = (FrameLayout) view.findViewById(R.id.fl_advertise);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
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

	public void getUser(final View view) {

		final AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

		String token = PreferenceUtil.getProperty(getActivity(), "token");

		Call<ResponseVO<UserVO>> callUser = advertisementNetworkService.main(token);
		callUser.enqueue(new CustomCallback<ResponseVO<UserVO>>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO<UserVO> responseVO) {
				ResultType resultType = responseVO.getResultType();

				if (resultType.isSuccess()) {

					//mainVO = responseVO.getValue("user");
					userVO = responseVO.getValue("user");
					Log.i("DEBUG", "HomeFragment main / userVO : " + userVO.toString());

					int point = userVO.getPoint();
					NumberFormat numberFormat = NumberFormat.getInstance();
					String result = numberFormat.format(point);
					tvPoint.setText(result);
				}
			}
		});
	}

	public void getMainInfomation(final View view) {

		final AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

		String token = PreferenceUtil.getProperty(getActivity(), "token");

		Call<ResponseVO<List<ReceivedAdvertiseVO>>> callList = advertisementNetworkService.adList(token, 1);
		callList.enqueue(new CustomCallback<ResponseVO<List<ReceivedAdvertiseVO>>>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO<List<ReceivedAdvertiseVO>> responseVO) {
				ResultType resultType = responseVO.getResultType();

				if (resultType.isSuccess()) {

					advertiseList = responseVO.getValue("receivedAdvertiseList");
					Log.i("DEBUG", "HomeFragment main / advertiseList : " + advertiseList.toString());

					homeGridAdapter = new HomeGridAdapter(view.getContext(), advertiseList);
					gvAd.setAdapter(homeGridAdapter);

				}
			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}

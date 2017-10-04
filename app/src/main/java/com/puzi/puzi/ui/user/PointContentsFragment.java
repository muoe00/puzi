package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.point.history.PointHistoryVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointContentsFragment extends Fragment {

	int tag = 0;
	int pagingIndex = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		tag = getArguments().getInt("tag");
		Log.i(PuziUtils.INFO, "tag : " + tag);

		PointViewHolder pointViewHolder = null;
		LevelViewHolder levelViewHolder = null;

		View view = inflater.inflate(R.layout.fragment_point_list, container, false);

		switch (tag) {
			case PuziUtils.VIEW_POINT:
				view = inflater.inflate(R.layout.fragment_point_list, container, false);
				pointViewHolder = new PointViewHolder(view);
				pointList(pointViewHolder);
				break;
			case PuziUtils.VIEW_LEVEL:
				view = inflater.inflate(R.layout.fragment_level, container, false);
				levelViewHolder = new LevelViewHolder(view);
				level(levelViewHolder);
				break;
		}

		return view;
	}

	public void pointList(final PointViewHolder pointViewHolder) {

		AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

		String token = Preference.getProperty(getActivity(), "token");

		pagingIndex = 1;

		Call<ResponseVO> call = advertisementNetworkService.pointHistory(token, pagingIndex);
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				switch(responseVO.getResultType()){
					case SUCCESS:

						List<PointHistoryVO> pointHistoryVOs = responseVO.getList("pointHistoryList", PointHistoryVO.class);
						Log.i(PuziUtils.INFO, "point / pointHistoryVOs : " + pointHistoryVOs.toString());

						PointListAdapter pointListAdapter = new PointListAdapter(getContext(), pointHistoryVOs);
						pointViewHolder.lvPoint.setAdapter(pointListAdapter);

						break;

					default:
						Log.i("INFO", "point history failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void level(LevelViewHolder levelViewHolder) {

	}

	public class PointViewHolder {
		@BindView(R.id.lv_point) public ListView lvPoint;

		public PointViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public class LevelViewHolder {
		@BindView(R.id.iv_level) public ImageView ivLevel;

		public LevelViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}

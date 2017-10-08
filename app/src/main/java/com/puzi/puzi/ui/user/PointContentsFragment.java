package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointContentsFragment extends BaseFragment implements AbsListView.OnScrollListener {

	private int tag = 0, pagingIndex = 1;
	private boolean more = false;
	private boolean lastVisible = false;
	private PointListAdapter pointListAdapter;
	private PointViewHolder pointViewHolder = null;
	private LevelViewHolder levelViewHolder = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		tag = getArguments().getInt("tag");
		Log.i(PuziUtils.INFO, "tag : " + tag);

		View view = inflater.inflate(R.layout.fragment_point_list, container, false);

		switch (tag) {
			case PuziUtils.VIEW_POINT:
				view = inflater.inflate(R.layout.fragment_point_list, container, false);
				pointViewHolder = new PointViewHolder(view);
				pointListAdapter = new PointListAdapter(getContext());
				pointViewHolder.lvPoint.setAdapter(pointListAdapter);
				pointViewHolder.lvPoint.setOnScrollListener(this);
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

		pointListAdapter.startProgress();
		pointViewHolder.lvPoint.setSelection(pointListAdapter.getCount() - 1);

		AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = advertisementNetworkService.pointHistory(token, pagingIndex);
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				pointListAdapter.stopProgress();
				switch(responseVO.getResultType()){
					case SUCCESS:
						List<PointHistoryVO> pointHistoryVOs = responseVO.getList("pointHistoryList", PointHistoryVO.class);
						Log.i(PuziUtils.INFO, "point / pointHistoryVOs : " + pointHistoryVOs.toString());

						if(pointHistoryVOs.size() == 0) {
							pointListAdapter.empty();
							more = false;
							return;
						}

						pointListAdapter.addPointList(pointHistoryVOs);
						pointListAdapter.notifyDataSetChanged();

						if(pointListAdapter.getCount() == pointHistoryVOs.size()) {
							more = false;
							return;
						}
						more = true;


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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) && lastVisible) {
			if(pagingIndex < 10) {
				pagingIndex++;
				pointList(pointViewHolder);
			}
			Log.i(PuziUtils.INFO, "pagingIndex : " + pagingIndex);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastVisible = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
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

package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.point.history.PointHistoryVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-10-04.
 */

public class PointContentsFragment extends BaseFragment {

	private int tag = 0, pagingIndex = 1;
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
				pointListAdapter = new PointListAdapter(getActivity(), pointViewHolder.lvPoint, new CustomPagingAdapter.ListHandler() {
					@Override
					public void getList() {
						pointList();
					}
				});
				pointViewHolder.lvPoint.setAdapter(pointListAdapter);
				pointListAdapter.getList();
				break;
			case PuziUtils.VIEW_LEVEL:
				view = inflater.inflate(R.layout.fragment_level, container, false);
				levelViewHolder = new LevelViewHolder(view);
				break;
		}

		return view;
	}

	public void pointList() {

		pointListAdapter.startProgress();
		pointViewHolder.lvPoint.setSelection(pointListAdapter.getCount() - 1);

		LazyRequestService service = new LazyRequestService(getActivity(), AdvertisementNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<AdvertisementNetworkService>() {
			@Override
			public Call<ResponseVO> execute(AdvertisementNetworkService advertisementNetworkService, String token) {
				return advertisementNetworkService.pointHistory(token, pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				pointListAdapter.stopProgress();

				List<PointHistoryVO> pointHistoryVOs = responseVO.getList("pointHistorieDTOList", PointHistoryVO.class);
				int totalCount = responseVO.getInteger("totalCount");

				pointListAdapter.addListWithTotalCount(pointHistoryVOs, totalCount);
			}
		});
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

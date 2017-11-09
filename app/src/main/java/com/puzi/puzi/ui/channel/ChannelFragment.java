package com.puzi.puzi.ui.channel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.ChannelNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.HorizontalListView;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.channel.editorspage.EditorsPageActivity;
import lombok.Getter;
import retrofit2.Call;

import java.util.Arrays;
import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class ChannelFragment extends BaseFragment {

	private Unbinder unbinder;

	@Getter
	private List<ChannelCategoryType> categoryTypeList = Arrays.asList(ChannelCategoryType.values());
	private String searchType = null;

	private ChannelFilterAdapter channelFilterAdapter = null;
	private ChannelAdapter channelAdapter;
	private ChannelEditorspageAdapter editorsPageAdapter;

	@BindView(R.id.hlv_channel_filter) public HorizontalListView hlvChannelFilter;
	@BindView(R.id.gv_channel) public GridView gvChannel;
	@BindView(R.id.lv_editorspage) public ListView lvEditorspage;
	@BindView(R.id.sv_channel) public ScrollView svContainer;
	@BindView(R.id.fl_channel_more_channel_container) public FrameLayout flMoreChannelContainer;
	@BindView(R.id.fl_channel_more_editorspage_container) public FrameLayout flMoreEditorspageContainer;
	@BindView(R.id.srl_channel) public SwipeRefreshLayout srlChannel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_channel, container, false);
		unbinder = ButterKnife.bind(this, view);

		initComponent();

		return view;
	}

	private void initComponent() {
		// filter
		channelFilterAdapter = new ChannelFilterAdapter(getActivity());
		hlvChannelFilter.setAdapter(channelFilterAdapter);
		//
		channelAdapter = new ChannelAdapter(getActivity(), R.layout.item_channel_list_channel, gvChannel, svContainer, new CustomPagingAdapter.ListHandler() {

			@Override
			public void getList() {
				getChannelList();
			}
		});
		channelAdapter.setMore(false);
//		channelAdapter.getList();

		editorsPageAdapter = new ChannelEditorspageAdapter(getActivity(), R.layout.item_channel_list_editorspage, lvEditorspage, svContainer,
			new CustomPagingAdapter.ListHandler() {

				@Override
				public void getList() {
					getEditorspageList();
				}
			});
		editorsPageAdapter.setMore(false);
//		editorsPageAdapter.getList();

		SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				searchType = null;
				channelAdapter.initPagingIndex();
				channelAdapter.getList();
				editorsPageAdapter.initPagingIndex();
				editorsPageAdapter.getList();
			}
		};
		srlChannel.setColorSchemeResources(R.color.colorPuzi);
		srlChannel.setOnRefreshListener(listener);
		listener.onRefresh();
	}

	private void getChannelList() {
		channelAdapter.startProgress();

		final ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = channelNetworkService.channelList(token, categoryTypeList, searchType, channelAdapter.getPagingIndex());
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				channelAdapter.stopProgress();
				srlChannel.setRefreshing(false);

				if (responseVO.getResultType().isSuccess()) {
					if(channelAdapter.getPagingIndex() <= 1) {
						channelAdapter.clean();
						channelAdapter.increasePagingIndex();

					}

					List<ChannelVO> newChannelList = responseVO.getList("channelDTOList", ChannelVO.class);
					channelAdapter.addList(newChannelList);

					searchType = responseVO.getString("responseVO");
					int totalCount = responseVO.getInteger("totalCount");
					if(channelAdapter.getCount() >= totalCount) {
						flMoreChannelContainer.setVisibility(View.GONE);
					}
				}
			}
		});
	}

	private void getEditorspageList() {
		editorsPageAdapter.startProgress();

		final ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = channelNetworkService.channelEditorsPageList(token, categoryTypeList, editorsPageAdapter.getPagingIndex());
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				editorsPageAdapter.stopProgress();
				srlChannel.setRefreshing(false);

				if (responseVO.getResultType().isSuccess()) {
					if(editorsPageAdapter.getPagingIndex() <= 1) {
						editorsPageAdapter.clean();
						editorsPageAdapter.increasePagingIndex();
					}

					List<ChannelEditorsPageVO> newEditorspageList = responseVO.getList("channelEditorsPageDTOList", ChannelEditorsPageVO.class);
					editorsPageAdapter.addList(newEditorspageList);

					int totalCount = responseVO.getInteger("totalCount");
					if(editorsPageAdapter.getCount() >= totalCount) {
						flMoreEditorspageContainer.setVisibility(View.GONE);
					}

				}
			}
		});
	}

	@OnClick(R.id.btn_channel_more_channel)
	public void clickMoreChannel() {
		channelAdapter.getList();
	}

	@OnClick(R.id.btn_channel_more_editorspage)
	public void clickMoreEditorspage() {
		editorsPageAdapter.getList();
	}

	@OnItemClick(R.id.gv_channel)
	public void channelItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), ChannelDetailActivity.class);
		ChannelVO channelVO = channelAdapter.getItem(position);
		intent.putExtra("channelVO", channelVO);
		startActivity(intent);
		doAnimationGoRight();
	}

	@OnItemClick(R.id.lv_editorspage)
	public void editorsPageItemClick(AdapterView<?> parent, View view, int position, long id) {
		ChannelEditorsPageVO channelEditorsPageVO = editorsPageAdapter.getItem(position);
		Intent intent = new Intent(getActivity(), EditorsPageActivity.class);
		intent.putExtra("channelEditorsPageVO", channelEditorsPageVO);
		startActivity(intent);
		doAnimationGoRight();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	public void refresh(List<ChannelCategoryType> categoryTypeList) {
		this.categoryTypeList = categoryTypeList;
		this.channelAdapter.clean();
		this.editorsPageAdapter.clean();
		if(categoryTypeList.size() != ChannelCategoryType.values().length) {
			channelFilterAdapter.refreshAndAdd(categoryTypeList);
			hlvChannelFilter.setVisibility(View.VISIBLE);
		} else {
			hlvChannelFilter.setVisibility(View.GONE);
		}
		this.channelAdapter.getList();
		this.editorsPageAdapter.getList();
	}

}

package com.puzi.puzi.ui.channel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.ChannelNetworkService;
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
	private int pagingIndex = 1;

	private boolean more = true;
	boolean lastestScrollFlag = false;

	private ChannelListAdapter channelListAdapter = null;
	private ChannelFilterAdapter channelFilterAdapter = null;

	@BindView(R.id.lv_channel) public ListView lvChannel;
	@BindView(R.id.hlv_channel_filter) public HorizontalListView hlvChannelFilter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_channel, container, false);
		unbinder = ButterKnife.bind(this, view);

		initScrollAction();

		initAdapter();

		getChannelWithEditorsPageList();

		return view;
	}

	private void getChannelWithEditorsPageList() {

		channelListAdapter.startProgress();
		lvChannel.setSelection(channelListAdapter.getCount() - 1);

		final ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = channelNetworkService.channelWithEditorsPageList(token, categoryTypeList, searchType, pagingIndex);
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				channelListAdapter.stopProgress();

				if (responseVO.getResultType().isSuccess()) {
					List<ChannelVO> newChannelList = responseVO.getList("channelDTOList", ChannelVO.class);

					if(newChannelList.size() >= 4) {
						List<ChannelEditorsPageVO> editorsPageList = responseVO.getList("channelEditorsPageDTOList", ChannelEditorsPageVO.class);

						// 채널4개 > 에디터스3개 > 채널4개 > 에디터스3개 로 뿌리기 위해
						channelListAdapter.addChannel(newChannelList.subList(0, 4));
						if(editorsPageList.size() >= 3) {
							channelListAdapter.addEditorsPage(editorsPageList.subList(0, 3));
						} else {
							channelListAdapter.addEditorsPage(editorsPageList);
						}
						channelListAdapter.addChannel(newChannelList.subList(4, newChannelList.size()));
						if(editorsPageList.size() >= 4) {
							channelListAdapter.addEditorsPage(editorsPageList.subList(4, editorsPageList.size()));
						}
						channelListAdapter.notifyDataSetChanged();

						searchType = responseVO.getString("searchType");
					} else {
						if(newChannelList.size() != 0) {
							channelListAdapter.addChannel(newChannelList);
							channelListAdapter.notifyDataSetChanged();
						}
						more = false;
					}
				}
			}
		});
	}

	private void initScrollAction() {
		lvChannel.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
					if(more) {
						pagingIndex = pagingIndex + 1;
						getChannelWithEditorsPageList();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastestScrollFlag = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
			}
		});
	}

	private void initAdapter() {
		// 필터
		channelFilterAdapter = new ChannelFilterAdapter(getActivity());
		hlvChannelFilter.setAdapter(channelFilterAdapter);
		// 리스트
		channelListAdapter = new ChannelListAdapter(getActivity());
		lvChannel.setAdapter(channelListAdapter);
	}

	@OnItemClick(R.id.lv_channel)
	public void editorsPageItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(ChannelListAdapter.VIEW_EDITORSPAGE == channelListAdapter.getItemViewType(position)) {
			ChannelEditorsPageVO channelEditorsPageVO = (ChannelEditorsPageVO) channelListAdapter.getItem(position);
			Intent intent = new Intent(getActivity(), EditorsPageActivity.class);
			intent.putExtra("channelEditorsPageVO", channelEditorsPageVO);
			startActivity(intent);
			doAnimationGoRight();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	public void refresh(List<ChannelCategoryType> categoryTypeList) {
		channelListAdapter.removeAll();
		pagingIndex = 1;
		this.categoryTypeList = categoryTypeList;
		if(categoryTypeList.size() != ChannelCategoryType.values().length) {
			channelFilterAdapter.refreshAndAdd(categoryTypeList);
			hlvChannelFilter.setVisibility(View.VISIBLE);
		} else {
			hlvChannelFilter.setVisibility(View.GONE);
		}
		getChannelWithEditorsPageList();
	}

}

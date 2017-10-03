package com.puzi.puzi.ui.channel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import retrofit2.Call;

import java.util.Arrays;
import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class ChannelFragment extends Fragment {

	private Unbinder unbinder;

	private List<ChannelCategoryType> categoryTypeList = Arrays.asList(ChannelCategoryType.values());
	private String searchType = null;
	private int pagingIndex = 1;

	private boolean more = true;
	boolean lastestScrollFlag = false;

	private ChannelListAdapter channelListAdapter = null;

	@BindView(R.id.lv_channel) public ListView lvChannel;

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
		channelListAdapter = new ChannelListAdapter(getActivity());
		lvChannel.setAdapter(channelListAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

}

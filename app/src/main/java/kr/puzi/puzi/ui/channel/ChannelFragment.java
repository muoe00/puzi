package kr.puzi.puzi.ui.channel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import kr.puzi.puzi.biz.channel.ChannelCategoryType;
import kr.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import kr.puzi.puzi.biz.channel.ChannelVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.ChannelNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.HorizontalListView;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.channel.editorspage.EditorsPageActivity;
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

	@BindView(kr.puzi.puzi.R.id.hlv_channel_filter) public HorizontalListView hlvChannelFilter;
	@BindView(kr.puzi.puzi.R.id.gv_channel) public GridView gvChannel;
	@BindView(kr.puzi.puzi.R.id.lv_editorspage) public ListView lvEditorspage;
	@BindView(kr.puzi.puzi.R.id.sv_channel) public ScrollView svContainer;
	@BindView(kr.puzi.puzi.R.id.fl_channel_more_channel_container) public FrameLayout flMoreChannelContainer;
	@BindView(kr.puzi.puzi.R.id.fl_channel_more_editorspage_container) public FrameLayout flMoreEditorspageContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_channel, container, false);
		unbinder = ButterKnife.bind(this, view);

		initComponent();

		return view;
	}

	private void initComponent() {
		if(channelFilterAdapter != null) {
			return;
		}
		// filter
		channelFilterAdapter = new ChannelFilterAdapter(getActivity());
		hlvChannelFilter.setAdapter(channelFilterAdapter);
		//
		channelAdapter = new ChannelAdapter(getActivity(), kr.puzi.puzi.R.layout.item_channel_list_channel, gvChannel, svContainer, new CustomPagingAdapter.ListHandler() {

			@Override
			public void getList() {
				getChannelList();
			}
		});
		channelAdapter.setMore(false);
		channelAdapter.getList();

		editorsPageAdapter = new ChannelEditorspageAdapter(getActivity(), kr.puzi.puzi.R.layout.item_channel_list_editorspage,
			kr.puzi.puzi.R.layout.item_channel_list_editorspage_2, lvEditorspage, svContainer,
			new CustomPagingAdapter.ListHandler() {

				@Override
				public void getList() {
					getEditorspageList();
				}
			});
		editorsPageAdapter.setMore(false);
		editorsPageAdapter.getList();
	}

	private void getChannelList() {
		channelAdapter.startProgress();

		LazyRequestService service = new LazyRequestService(getActivity(), ChannelNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<ChannelNetworkService>() {
			@Override
			public Call<ResponseVO> execute(ChannelNetworkService channelNetworkService, String token) {
				return channelNetworkService.channelList(token, categoryTypeList, searchType, channelAdapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				channelAdapter.stopProgress();

				List<ChannelVO> newChannelList = responseVO.getList("channelDTOList", ChannelVO.class);
				channelAdapter.addList(newChannelList);

				searchType = responseVO.getString("responseVO");
				int totalCount = responseVO.getInteger("totalCount");
				if(channelAdapter.getCount() >= totalCount) {
					flMoreChannelContainer.setVisibility(View.GONE);
				}
			}
		});
	}

	private void getEditorspageList() {
		editorsPageAdapter.startProgress();

		LazyRequestService service = new LazyRequestService(getActivity(), ChannelNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<ChannelNetworkService>() {
			@Override
			public Call<ResponseVO> execute(ChannelNetworkService channelNetworkService, String token) {
				return channelNetworkService.channelEditorsPageList(token, categoryTypeList, editorsPageAdapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				editorsPageAdapter.stopProgress();

				List<ChannelEditorsPageVO> newEditorspageList = responseVO.getList("channelEditorsPageDTOList", ChannelEditorsPageVO.class);
				editorsPageAdapter.addList(newEditorspageList);

				int totalCount = responseVO.getInteger("totalCount");
				if(editorsPageAdapter.getCount() >= totalCount) {
					flMoreEditorspageContainer.setVisibility(View.GONE);
				}
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_channel_more_channel)
	public void clickMoreChannel() {
		channelAdapter.getList();
	}

	@OnClick(kr.puzi.puzi.R.id.btn_channel_more_editorspage)
	public void clickMoreEditorspage() {
		editorsPageAdapter.getList();
	}

	@OnItemClick(kr.puzi.puzi.R.id.gv_channel)
	public void channelItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), ChannelDetailActivity.class);
		ChannelVO channelVO = channelAdapter.getItem(position);
		intent.putExtra("channelVO", channelVO);
		startActivity(intent);
		doAnimationGoRight();
	}

	@OnItemClick(kr.puzi.puzi.R.id.lv_editorspage)
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
		searchType = null;
		this.channelAdapter.getList();
		this.editorsPageAdapter.getList();
	}

}

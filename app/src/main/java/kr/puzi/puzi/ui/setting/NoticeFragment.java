package kr.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.setting.NoticeVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by 170605 on 2017-10-23.
 */

public class NoticeFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.lv_notice) AnimatedExpandableListView lvNotice;

	private View view = null;
	private int pagingIndex = 1;
	private boolean more = false;
	private boolean lastestScrollFlag = false;
	private NoticeListAdapter noticeListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_setting_notice, container, false);
		unbinder = ButterKnife.bind(this, view);

		initAdapter();
		getNoticeList();
		initScrollAction();

		lvNotice.setOnGroupClickListener(new AnimatedExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if (lvNotice.isGroupExpanded(groupPosition)) {
					lvNotice.collapseGroupWithAnimation(groupPosition);
				} else {
					lvNotice.expandGroupWithAnimation(groupPosition);
				}
				noticeListAdapter.notifyDataSetChanged();
				return true;
			}
		});

		return view;
	}

	public void getNoticeList() {
		noticeListAdapter.startProgress();
		lvNotice.setSelection(noticeListAdapter.getGroupCount() - 1);

		String token = Preference.getProperty(getActivity(), "token");

		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.list(token, pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				noticeListAdapter.stopProgress();

				List<NoticeVO> noticeList = responseVO.getList("userNoticeDTOList", NoticeVO.class);

				if(noticeList.size() == 0) {
					noticeListAdapter.empty();
					more = false;
					return;
				}

				noticeListAdapter.addNoticeList(noticeList);
				noticeListAdapter.notifyDataSetChanged();

				if(noticeListAdapter.getGroupCount() == responseVO.getInteger("totalCount")) {
					more = false;
					return;
				}
				more = true;
			}
		});
	}

	private void initScrollAction() {
		lvNotice.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.i(PuziUtils.INFO, "scrollState : " + scrollState + ", more : " + more);

				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
					if(more) {
						pagingIndex = pagingIndex + 1;
						getNoticeList();
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
		noticeListAdapter = new NoticeListAdapter(getActivity());
		lvNotice.setAdapter(noticeListAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

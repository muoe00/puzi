package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.notice.NoticeVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by 170605 on 2017-10-23.
 */

public class NoticeFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.lv_notice) ListView lvNotice;

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
		View view = inflater.inflate(R.layout.fragment_setting_notice, container, false);
		unbinder = ButterKnife.bind(this, view);

		initAdapter();
		getNoticeList();
		initScrollAction();

		return view;
	}

	public void getNoticeList() {
		noticeListAdapter.startProgress();
		lvNotice.setSelection(noticeListAdapter.getCount() - 1);

		String token = Preference.getProperty(getActivity(), "token");

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);

		Call<ResponseVO> callList = settingNetworkService.list(token, pagingIndex);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i(PuziUtils.INFO, "notice responseVO : " + responseVO.toString());
				noticeListAdapter.stopProgress();

				switch(responseVO.getResultType()){
					case SUCCESS:
						List<NoticeVO> noticeList = responseVO.getList("userNoticeDTOList", NoticeVO.class);
						Log.i(PuziUtils.INFO, "noticeList : " + noticeList.toString());
						Log.i(PuziUtils.INFO, "noticeList totalCount : " + responseVO.getInteger("totalCount"));

						if(noticeList.size() == 0) {
							noticeListAdapter.empty();
							more = false;
							return;
						}

						noticeListAdapter.addNoticeList(noticeList);
						noticeListAdapter.notifyDataSetChanged();

						if(noticeListAdapter.getCount() == responseVO.getInteger("totalCount")) {
							more = false;
							return;
						}
						more = true;

						break;

					case NO_AUTH:
						PuziUtils.renewalToken(getActivity());

						break;

					default:
						Log.i("INFO", "setting getNoticeList failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
						break;
				}
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
						noticeListAdapter.notifyDataSetChanged();
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

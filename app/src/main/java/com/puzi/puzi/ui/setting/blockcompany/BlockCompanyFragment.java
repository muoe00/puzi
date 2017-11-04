package com.puzi.puzi.ui.setting.blockcompany;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.setting.RejectCompanyVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.CustomBaseAdapter;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.List;

/**
 * Created by 170605 on 2017-10-23.
 */

public class BlockCompanyFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.lv_company_block)
	ListView lvCompanyBlock;

	private BlockCompanyAdapter adapter;
	private int pagingIndex = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_company_block, container, false);

		unbinder = ButterKnife.bind(this, view);

		initComponent();

		return view;
	}

	private void initComponent() {
		adapter = new BlockCompanyAdapter(getActivity(), lvCompanyBlock, new CustomBaseAdapter.ListHandler() {
			@Override
			public void getList() {
				getBlockedCompanyList();
			}
		});
		lvCompanyBlock.setAdapter(adapter);
		adapter.getList();
	}

	public void getBlockedCompanyList() {
		adapter.startProgressWithScrollDown();

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = settingNetworkService.blockedCompanyList(token, adapter.getPagingIndex());
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				adapter.stopProgress();

				if (responseVO.getResultType().isSuccess()) {
					List<RejectCompanyVO> rejectCompanyVOList = responseVO.getList("rejectCompanyDTOList", RejectCompanyVO.class);
					int totalCount = responseVO.getInteger("totalCount");

					adapter.addListWithTotalCount(rejectCompanyVOList, totalCount);
				}
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

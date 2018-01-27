package kr.puzi.puzi.ui.setting.blockcompany;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.setting.RejectCompanyVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.List;

/**
 * Created by 170605 on 2017-10-23.
 */

public class BlockCompanyFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.lv_company_block)
	ListView lvCompanyBlock;

	private BlockCompanyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_setting_block_company, container, false);

		unbinder = ButterKnife.bind(this, view);

		initComponent();

		return view;
	}

	private void initComponent() {
		adapter = new BlockCompanyAdapter(getActivity(), lvCompanyBlock, new CustomPagingAdapter.ListHandler() {
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

		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.blockedCompanyList(token, adapter.getPagingIndex());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				adapter.stopProgress();

				List<RejectCompanyVO> rejectCompanyVOList = responseVO.getList("rejectCompanyDTOList", RejectCompanyVO.class);
				int totalCount = responseVO.getInteger("totalCount");

				adapter.addListWithTotalCount(rejectCompanyVOList, totalCount);
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

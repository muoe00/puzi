package com.puzi.puzi.ui.setting.blockTime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.setting.RejectTimeVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by 170605 on 2017-10-23.
 */

public class BlockTimeFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.sp_setting_block_time_start)
	Spinner spStart;
	@BindView(R.id.sp_setting_block_time_end)
	Spinner spEnd;
	@BindView(R.id.lv_setting_block_time)
	ListView lvBlockTime;
	@BindView(R.id.sv_setting_block_time)
	ScrollView svBlockTime;

	private BlockTimeAdapter adapter;
	private List<String> timeList;
	private String selectedStartTime;
	private String selectedEndTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_block_time, container, false);

		unbinder = ButterKnife.bind(this, view);

		initComponent();

		return view;
	}

	private void initComponent() {
		adapter = new BlockTimeAdapter(getActivity(), R.layout.item_block_time, lvBlockTime, svBlockTime, new CustomPagingAdapter.ListHandler(){
			@Override
			public void getList() {
				getBlockedTimeList();
			}
		});
		adapter.setEmptyMessage("등록된 제한시간이 없습니다.");
		adapter.getList();

		timeList = newArrayList();
		for(int i=8; i<24; i++){
			if(i > 9) {
				timeList.add(i + "시");
			} else {
				timeList.add("0" + i + "시");
			}
		}

		CustomArrayAdapter startAdapter = new CustomArrayAdapter(getActivity(), timeList);
		spStart.setAdapter(startAdapter);
		spStart.setPrompt("시작시간");
		spStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedStartTime = timeList.get(position).replace("시", "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				parent.setBackgroundResource(R.drawable.button_block_time_off);
			}
		});
		CustomArrayAdapter endAdapter = new CustomArrayAdapter(getActivity(), timeList);
		spEnd.setAdapter(endAdapter);
		spEnd.setPrompt("종료시간");
		spEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedEndTime = timeList.get(position).replace("시", "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				parent.setBackgroundResource(R.drawable.button_block_time_off);
			}
		});
	}

	private void getBlockedTimeList() {
		adapter.startProgressWithScrollDown();

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = settingNetworkService.blockedTimeList(token, adapter.getPagingIndex());
		call.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				adapter.stopProgress();

				if (responseVO.getResultType().isSuccess()) {
					List<RejectTimeVO> rejectTimeVoList = responseVO.getList("rejectTimeDTOList", RejectTimeVO.class);
					adapter.addList(rejectTimeVoList);
					adapter.setMore(false);
				}
			}
		});
	}

	@OnClick(R.id.ibtn_setting_block_time_add)
	public void clickAdd(View v) {
		ProgressDialog.show(getActivity());

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);
		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> call = settingNetworkService.blockTime(token, true, selectedStartTime, selectedEndTime);
		call.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				ProgressDialog.dismiss();

				if (responseVO.getResultType().isSuccess()) {
					Toast.makeText(getActivity(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
					RejectTimeVO newRejectTime = new RejectTimeVO();
					newRejectTime.setStartTime(selectedStartTime);
					newRejectTime.setEndTime(selectedEndTime);
					adapter.addOne(newRejectTime);
				} else {
					Toast.makeText(getActivity(), responseVO.getResultType().getResultMsg(), Toast.LENGTH_LONG).show();
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

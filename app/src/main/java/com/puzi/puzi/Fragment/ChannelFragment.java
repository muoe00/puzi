package com.puzi.puzi.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.puzi.puzi.ChannelListAdapter;
import com.puzi.puzi.R;
import com.puzi.puzi.ResultCode;
import com.puzi.puzi.model.ChannelVO;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.PuziNetworkException;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.service.ChannelService;
import com.puzi.puzi.util.PreferenceUtil;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class ChannelFragment extends Fragment{

	public static final int RECOMMEND = 0;
	public static final int FAVORITS = 1;
	private Button btnReccomend, btnFavorit;
	private ListView lvChannel;
	private List<ChannelVO> channelList;
	private ChannelListAdapter channelListAdapter;
	private int index = RECOMMEND;
	private String type = "RECOMMEND";

	public ChannelFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_channel, container, false);
		initComponent(view);
		getChannelList(view);

		btnReccomend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				index = RECOMMEND;
				type = "RECOMMEND";
				getChannelList(view);
			}
		});
		btnFavorit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				index = FAVORITS;
				type = "FAVORITES";
				getChannelList(view);
			}
		});

		return view;
	}

	private void initComponent(View view) {

		lvChannel = (ListView) view.findViewById(R.id.lv_channel);

		btnReccomend = (Button) view.findViewById(R.id.btn_channelRecommend);
		btnFavorit = (Button) view.findViewById(R.id.btn_channelFavorit);
	}

	public void getChannelList(final View view) {

		final ChannelService channelService = RetrofitManager.create(ChannelService.class);

		String token = PreferenceUtil.getProperty(getActivity(), "token");
		int pagingIndex = 1;

		Call<ResponseVO<List<ChannelVO>>> call = channelService.channelList(token, type, pagingIndex);
		call.enqueue(new CustomCallback<ResponseVO<List<ChannelVO>>>() {
			@Override
			public void onResponse(ResponseVO<List<ChannelVO>> responseVO) {
				int resultCode = responseVO.getResultCode();

				if (resultCode == ResultCode.SUCCESS) {

					channelList = responseVO.getValue("channelList");
					Log.i("DEBUG", "ChannelFragment main / channelList : " + channelList.toString());

					channelListAdapter = new ChannelListAdapter(view.getContext(), channelList);
					lvChannel.setAdapter(channelListAdapter);
				}
			}

			@Override
			public void onFailure(PuziNetworkException e) {
				Log.e("TAG", "통신 오류(" + e.getCode() + ")", e);
			}
		});
	}
}

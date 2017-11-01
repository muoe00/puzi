package com.puzi.puzi.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

/**
 * Created by 170605 on 2017-10-27.
 */

public class OutFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_cs_out, container, false);
		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	@OnClick(R.id.btn_out)
	public void requestOut() {
		String token = Preference.getProperty(getActivity(), "token");
		ProgressDialog.show(getActivity());

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);

		Call<ResponseVO> callList = settingNetworkService.out(token);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i(PuziUtils.INFO, "ask responseVO : " + responseVO.toString());
				ProgressDialog.dismiss();

				switch(responseVO.getResultType()){
					case SUCCESS:
						ProgressDialog.dismiss();
						Intent intent = new Intent(getActivity(), IntroActivity.class);
						getActivity().startActivity(intent);
						break;

					case NO_AUTH:
						PuziUtils.renewalToken(getActivity());
						break;

					default:
						Log.i("INFO", "out failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});
	}
}

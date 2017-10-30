package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

/**
 * Created by 170605 on 2017-10-27.
 */

public class AskFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;
	private String userType, askType, title, comment;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_cs_ask, container, false);
		unbinder = ButterKnife.bind(this, view);

		userType = getArguments().getString("key");


		return view;
	}

//	@OnClick()
	public void sendAsk() {
		if(askType != null) {
			if(title != null) {
				if(comment != null) {
					ask();
				} else {
					Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
		}
	}

	public void ask() {
		String token = Preference.getProperty(getActivity(), "token");

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);

		ProgressDialog.show(getActivity());

		Call<ResponseVO> callList = settingNetworkService.ask(token, userType, askType, title, comment);
		callList.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i(PuziUtils.INFO, "ask responseVO : " + responseVO.toString());

				switch(responseVO.getResultType()){
					case SUCCESS:

						ProgressDialog.dismiss();

						switch(responseVO.getResultType()){
							case SUCCESS:
								Toast.makeText(getContext(), "이메일을 전송하였습니다", Toast.LENGTH_SHORT).show();
								break;
							case NOT_FOUND_USER:
								Toast.makeText(getContext(), "등록되지 않은 사용자입니다", Toast.LENGTH_SHORT).show();
								break;
							case WRONG_PARAMS:
								Toast.makeText(getContext(), "정확한 아이디와 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
								break;
						}

						break;

					case NO_AUTH:
						PuziUtils.renewalToken(getActivity());
						break;

					default:
						Log.i("INFO", "setting ask failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});
	}

}

package com.puzi.puzi.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.AdvertisementNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class SettingFragment extends BaseFragment {

	public static final int NOTICE = 0;
	public static final int USER = 1;
	public static final int FAVORITE = 2;
	public static final int ALARM  = 3;
	public static final int BLOCK = 4;
	public static final int CUSTOMER  = 5;

	Unbinder unbinder;

	@BindView(R.id.tv_setting_versionNum) TextView tvVersion;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		unbinder = ButterKnife.bind(this, view);

		getVersion();

		return view;
	}

	@OnClick({R.id.btn_setting_notice, R.id.btn_setting_favorite, R.id.btn_setting_user,
		R.id.btn_setting_alarm, R.id.btn_setting_block, R.id.btn_setting_customer})
	public void onClick(View v) {

		Intent intent = new Intent(getActivity(), SettingDetailActivity.class);

		switch (v.getId()) {
			case R.id.btn_setting_notice :
				intent.putExtra("TAG", NOTICE);
				break;
			case R.id.btn_setting_user :
				intent.putExtra("TAG", USER);
				break;
			case R.id.btn_setting_favorite :
				intent.putExtra("TAG", FAVORITE);
				break;
			case R.id.btn_setting_alarm :
				intent.putExtra("TAG", ALARM);
				break;
			case R.id.btn_setting_block :
				intent.putExtra("TAG", BLOCK);
				break;
			case R.id.btn_setting_customer :
				intent.putExtra("TAG", CUSTOMER);
				break;
			default:
				break;
		}

		doAnimationGoRight();
		startActivity(intent);
	}

	public void getVersion() {
		Log.i("INFO", "getVersion");
		String token = Preference.getProperty(getActivity(), "token");

		final AdvertisementNetworkService advertisementNetworkService = RetrofitManager.create(AdvertisementNetworkService.class);

		Call<ResponseVO> callList = advertisementNetworkService.main(token);
		callList.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				switch(responseVO.getResultType()){
					case SUCCESS:
						String version = responseVO.getString("version");
						Log.i("INFO", "version : " + version);
						tvVersion.setText(version);

						break;

					default:
						Log.i("INFO", "getVersion failed.");
						break;
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

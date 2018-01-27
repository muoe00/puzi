package com.puzi.puzi.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.utils.PuziUtils;
import com.puzi.puzi.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by 170605 on 2017-10-27.
 */

public class OutFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;

	@BindView(R.id.iv_out_level)
	ImageView ivLevel;
	@BindView(R.id.tv_out_point)
	TextView tvPoint;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_cs_out, container, false);
		unbinder = ButterKnife.bind(this, view);

		UserVO myInfo = Preference.getMyInfo(getActivity());
		tvPoint.setText(TextUtils.addComma(myInfo.getPoint()));
		switch (myInfo.getLevelType()) {
			case "WELCOME":
				ivLevel.setImageResource(R.drawable.welcome_icon);
				break;
			case "SILVER":
				ivLevel.setImageResource(R.drawable.silver_icon);
				break;
			case "GOLD":
				ivLevel.setImageResource(R.drawable.gold_icon);
				break;
			case "VIP":
				ivLevel.setImageResource(R.drawable.vip_icon);
				break;
			case "VVIP":
				ivLevel.setImageResource(R.drawable.vvip_icon);
				break;
		}

		return view;
	}

	@OnClick(R.id.btn_out)
	public void requestOut() {
		OneButtonDialog.show(getActivity(), "회원탈퇴", "정말로 탈퇴하시겠습니까? \n포인트는 복구할 수 없습니다.", "탈퇴", new DialogButtonCallback() {
			@Override
			public void onClick() {
				Preference.removeProperty(getActivity(), "passwd");
				BaseFragmentActivity.finishAll();
				startActivity(new Intent(getActivity(), IntroActivity.class));

				Preference.removeProperty(getActivity(), "tokenFCM");

				String token = Preference.getProperty(getActivity(), "token");
				ProgressDialog.show(getActivity());

				final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);

				Call<ResponseVO> callList = settingNetworkService.out(token);
				callList.enqueue(new CustomCallback(getActivity()) {
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
		});
	}
}

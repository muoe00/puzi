package com.puzi.puzi.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.utils.TextUtils;
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
				ProgressDialog.show(getActivity());

				LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
				service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
					@Override
					public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
						return settingNetworkService.out(token);
					}
				});
				service.enqueue(new CustomCallback(getActivity()) {
					@Override
					public void onSuccess(ResponseVO responseVO) {
						ProgressDialog.dismiss();

						Preference.removeProperty(getActivity(), "id");
						Preference.removeProperty(getActivity(), "passwd");
						Preference.removeProperty(getActivity(), "tokenFCM");
						getActivity().startActivity(new Intent(getActivity(), IntroActivity.class));
					}
				});
			}
		});
	}
}

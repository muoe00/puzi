package kr.puzi.puzi.ui.setting;

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
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.IntroActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.utils.TextUtils;
import retrofit2.Call;

/**
 * Created by 170605 on 2017-10-27.
 */

public class OutFragment extends BaseFragment {

	Unbinder unbinder;

	private View view = null;

	@BindView(kr.puzi.puzi.R.id.iv_out_level)
	ImageView ivLevel;
	@BindView(kr.puzi.puzi.R.id.tv_out_point)
	TextView tvPoint;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_setting_cs_out, container, false);
		unbinder = ButterKnife.bind(this, view);

		UserVO myInfo = Preference.getMyInfo(getActivity());
		tvPoint.setText(TextUtils.addComma(myInfo.getPoint()));
		switch (myInfo.getLevelType()) {
			case "WELCOME":
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.welcome_icon);
				break;
			case "SILVER":
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.silver_icon);
				break;
			case "GOLD":
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.gold_icon);
				break;
			case "VIP":
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.vip_icon);
				break;
			case "VVIP":
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.vvip_icon);
				break;
		}

		return view;
	}

	@OnClick(kr.puzi.puzi.R.id.btn_out)
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

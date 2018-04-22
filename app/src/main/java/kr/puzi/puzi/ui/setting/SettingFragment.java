package kr.puzi.puzi.ui.setting;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
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

	@BindView(R.id.fl_setting_user)
	FrameLayout flUser;
	@BindView(R.id.tv_setting_versionNum)
	TextView tvVersion;
	@BindView(R.id.btn_setting_version)
	Button btnVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		unbinder = ButterKnife.bind(this, view);

		getVersion();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		UserVO userVO = Preference.getMyInfo(getActivity());

		// Toast.makeText(getActivity(), "register type : " + userVO.getRegisterType().toString(), Toast.LENGTH_SHORT).show();

		if(!userVO.getRegisterType().isEmpty()) {
			if ("K".equals(userVO.getRegisterType())) {
				flUser.setVisibility(View.GONE);
			}
		}
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
		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.version(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				String version = responseVO.getString("version");
				Log.i("VERSION", "version : " + version);
				setVersion(version);
			}
		});
	}

	private void setVersion(String version) {
		tvVersion.setText(version);
		try {
			String appVersion = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			Log.i("VERSION", "app version : " + appVersion);
			if(version.equals(appVersion)) {
				btnVersion.setVisibility(View.GONE);
			} else {
				btnVersion.setVisibility(View.VISIBLE);
			}

			String[] splitedCurrentVersion = appVersion.split("\\.");
			String[] splitedServerVersion = version.split("\\.");

			if(splitedCurrentVersion.length != 3 || splitedServerVersion.length != 3) {
				return;
			}

			if(!splitedCurrentVersion[0].equals(splitedServerVersion[0]) || !splitedCurrentVersion[1].equals(splitedServerVersion[1])){
				OneButtonDialog.show(getActivity(), "버전 업데이트", "푸지의 새로운 기능이 추가되었습니다. 더 많은 포인트적립을 위해 업데이트 해주세요 :)",
					"업데이트", "나중에", new DialogButtonCallback() {
						@Override
						public void onClick() {
							versionClick(null);
						}
					});
			}

		} catch (PackageManager.NameNotFoundException e) {
			Log.e("VERSION", e.toString(), e);
		}
	}

	@OnClick(R.id.btn_setting_version)
	public void versionClick(View v) {
		Log.d("VERSION", "+++ PackageName : " + getActivity().getPackageName());
		final String appPackageName = getActivity().getPackageName();
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

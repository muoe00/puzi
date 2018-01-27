package kr.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
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
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.utils.EncryptUtils;
import kr.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

import static kr.puzi.puzi.cache.Preference.getMyInfo;

/**
 * Created by 170605 on 2017-10-23.
 */

public class UserFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.edt_setting_email) public EditText editEmail;
	@BindView(kr.puzi.puzi.R.id.edt_setting_pw) public EditText editPw;
	@BindView(kr.puzi.puzi.R.id.edt_setting_repw) public EditText editRePw;
	@BindView(kr.puzi.puzi.R.id.ll_setting_user) public LinearLayout linearLayout;

	private View view = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_setting_user, container, false);
		unbinder = ButterKnife.bind(this, view);

		UserVO myInfo = getMyInfo(getActivity());

		editEmail.setHint(myInfo.getEmail());

		return view;
	}

	@OnClick(kr.puzi.puzi.R.id.btn_setting_user_modify)
	public void modifyInformation() {

		if(editEmail == null && editPw == null) {
			Toast.makeText(getContext(), "변경하고자 하는 정보를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}

		String email = "";
		if(editEmail != null) {
			email = editEmail.getText().toString();
		}
		String passwd = "";
		if(editPw != null) {
			passwd = editPw.getText().toString();
		}

		String passwd2 = "";
		if(editRePw != null) {
			passwd2 = editRePw.getText().toString();
		}

		if("".equals(email) && "".equals(passwd) && "".equals(passwd2)) {
			Toast.makeText(getContext(), "변경하고자 하는 정보를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}

		if(!"".equals(passwd) && !passwd.equals(passwd2)) {
			Toast.makeText(getContext(), "비밀번호가 서로 다릅니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		if(!"".equals(passwd) && !ValidationUtils.isValidPasswd(passwd)) {
			Toast.makeText(getContext(), "비밀번호가 규칙에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		if(!"".equals(email) && !ValidationUtils.checkEmail(email)) {
			Toast.makeText(getContext(), "정확한 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}

		email = "".equals(email) ? null : email;
		passwd = "".equals(passwd) ? null : EncryptUtils.sha256(passwd);
		requestModification(email, passwd);
	}

	public void requestModification(final String email, final String passwd) {

		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.updateAccount(token, passwd, email);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.d("INFO", "advertisement getAdvertiseList success.");
				MainActivity.needToUpdateUserVO = true;
				Toast.makeText(getContext(), "성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();

				UserVO myInfo = Preference.getMyInfo(getActivity());
				if(email != null) {
					myInfo.setEmail(email);
				}
				Preference.saveMyInfo(getActivity(), myInfo);
				if(passwd != null) {
					Preference.addProperty(getActivity(), "passwd", passwd);
				}
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.ll_setting_user)
	public void layoutClick() {
		closeInputKeyboard(editEmail);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

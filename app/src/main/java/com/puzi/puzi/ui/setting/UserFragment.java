package com.puzi.puzi.ui.setting;

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
import com.puzi.puzi.R;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

/**
 * Created by 170605 on 2017-10-23.
 */

public class UserFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.edt_setting_email) public EditText editEmail;
	@BindView(R.id.edt_setting_pw) public EditText editPw;
	@BindView(R.id.edt_setting_repw) public EditText editRePw;
	@BindView(R.id.ll_setting_user) public LinearLayout linearLayout;

	private View view = null;
	private String modEmail;
	private String modPasswd;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_user, container, false);
		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	@OnClick(R.id.btn_setting_user_modify)
	public void modifyInformation() {

		modEmail = "";
		modPasswd = "";

		if(editEmail != null || editPw != null) {

			if(editEmail != null) {
				String email = editEmail.getText().toString();

				if(ValidationUtils.checkEmail(email)) {
					modEmail = email;
				} else {
					Toast.makeText(getContext(), "정확한 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
					return ;
				}
			}

			if(editPw != null) {
				if(editRePw == null) {
					Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
					return ;
				} else {
					String passwd = editPw.getText().toString();

					if(passwd.equals(editRePw.getText().toString())) {
						modPasswd = passwd;
					} else {
						Toast.makeText(getContext(), "정확한 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
						return ;
					}
				}
			}

			requestModification(modEmail, modPasswd);
		} else {
			Toast.makeText(getContext(), "변경하고자 하는 정보를 입력하세요", Toast.LENGTH_SHORT).show();
		}
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
				Toast.makeText(getContext(), "성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@OnClick(R.id.ll_setting_user)
	public void layoutClick() {
		closeInputKeyboard(editEmail);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
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

	private String modEmail;
	private String modPasswd;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
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

	public void requestModification(String email, String passwd) {

		String token = Preference.getProperty(getActivity(), "token");

		ProgressDialog.show(getActivity());
		SettingNetworkService settingNetworkService  = RetrofitManager.create(SettingNetworkService.class);
		Call<ResponseVO> call = settingNetworkService.updateAccount(token, email, passwd);
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				ProgressDialog.dismiss();

			}
		});
	}
}

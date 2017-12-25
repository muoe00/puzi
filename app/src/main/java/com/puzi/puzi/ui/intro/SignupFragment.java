package com.puzi.puzi.ui.intro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SignupFragment extends BaseFragment {

	private Unbinder unbinder;
	private String id, email, pw, rePw;
	private View progressView;

	@BindView(R.id.edt_signup_id) public EditText ethId;
	@BindView(R.id.edt_signup_email) public EditText ethEmail;
	@BindView(R.id.edt_signup_pw) public EditText ethPw;
	@BindView(R.id.edt_signup_repw) public EditText ethRePw;
	@BindView(R.id.btn_signup_next) public Button btnNext;
	@BindView(R.id.ibtn_back) public ImageButton ibtnBack;

	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_signup, container, false);
		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	public void idCheck() {
		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.check(id);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {

			@Override
			public void onSuccess(ResponseVO response) {
				Preference.addProperty(getActivity(), "id", id);
				Preference.addProperty(getActivity(), "passwd", pw);
				Preference.addProperty(getActivity(), "email", email);
				changedFragment();
			}
		});
	}

	@OnClick(R.id.btn_signup_next)
	public void signupNext() {

		id = ethId.getText().toString().trim();
		email = ethEmail.getText().toString().trim();
		pw = ethPw.getText().toString().trim();
		rePw = ethRePw.getText().toString().trim();

		if((id == null)||(email == null)||(pw == null)||(rePw == null)) {
			if(id == null) {
				Toast.makeText(getContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
			} else if(email == null) {
				Toast.makeText(getContext(), "이메일 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
			} else if(pw == null) {
				Toast.makeText(getContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
			} else if(rePw == null) {
				Toast.makeText(getContext(), "비밀번호를 한번 더 입력하세요.", Toast.LENGTH_SHORT).show();
			}
		} else {
			if(ValidationUtils.checkEmail(email)) {
				if(pw.equals(rePw)) {
					if(ValidationUtils.checkUserId(id)) {
						idCheck();
					} else {
						Toast.makeText(getContext(), "정확한 아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getContext(), "정확한 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getContext(), "정확한 이메일 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void changedFragment() {
		Log.i("DEBUG", "fragment changed.");
		BaseFragment infoFragment = new SignupInfoFragment();

		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(infoFragment);
	}

	@OnClick(R.id.ll_signup)
	public void layoutClick() {
		closeInputKeyboard(ethId);
		closeInputKeyboard(ethEmail);
		closeInputKeyboard(ethPw);
		closeInputKeyboard(ethRePw);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		getActivity().onBackPressed();
		layoutClick();
	}
}

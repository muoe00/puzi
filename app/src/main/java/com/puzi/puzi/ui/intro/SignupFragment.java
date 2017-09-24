package com.puzi.puzi.ui.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.util.ValidationUtil;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SignupFragment extends Fragment {

	public SignupFragment() {
	}

	private static final String TAG = "Signup";
	private Unbinder unbinder;
	private String id, email, pw, rePw;

	@BindView(R.id.edt_signup_id)
	public EditText ethId;
	@BindView(R.id.edt_signup_email)
	public EditText ethEmail;
	@BindView(R.id.edt_signup_pw)
	public EditText ethPw;
	@BindView(R.id.edt_signup_repw)
	public EditText ethRePw;
	@BindView(R.id.btn_signup_next)
	public Button btnNext;
	@BindView(R.id.ibtn_back)
	public ImageButton ibtnBack;

	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_signup, container, false);
		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	public void idCheck() {

		UserNetworkService userService = RetrofitManager.create(UserNetworkService.class);

		Call<ResponseVO> call = userService.check(id);
		call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {

			@Override
			public void onSuccess(ResponseVO response) {
				if (response.getResultCode() == 1000) {
					Preference.addProperty(getActivity(), "id", id);
					Preference.addProperty(getActivity(), "pw", pw);
					Preference.addProperty(getActivity(), "email", email);
					changedFragment();
				} else if (response.getResultCode() == 2002) {
					Toast.makeText(getContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(), "잘못된 형식의 아이디입니다.", Toast.LENGTH_SHORT).show();
				}
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
				Toast.makeText(getContext(), "비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
			}
		} else {
			if(ValidationUtil.checkEmail(email)) {
				if(pw.equals(rePw)) {
					if(ValidationUtil.checkUserId(id)) {
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
		Fragment infoFragment = new InfoFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container, infoFragment);
		fragmentTransaction.addToBackStack("INFO");
		fragmentTransaction.commit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		getActivity().onBackPressed();
	}
}

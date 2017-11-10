package com.puzi.puzi.ui.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SearchPwFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(R.id.edit_search_id) public EditText editId;
	@BindView(R.id.edit_search_email) public EditText editEmail;
	@BindView(R.id.btn_srchpw) public Button btnSearch;
	@BindView(R.id.btn_srch_signup) public Button btnSignup;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_search_pw, container, false);

		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	@OnClick(R.id.btn_srchpw)
	public void searchPw(){

		if(editId.getText() != null && editEmail.getText() != null) {
			String email = editEmail.getText().toString();
			if(ValidationUtils.checkEmail(email)) {
				ProgressDialog.show(getActivity());

				String id = editId.getText().toString();
				UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

				Call<ResponseVO> call = userNetworkService.searchpasswd(id, email);
				call.enqueue(new CustomCallback(getActivity()) {
					@Override
					public void onSuccess(ResponseVO responseVO) {
						ProgressDialog.dismiss();

						switch (responseVO.getResultType()) {
							case SUCCESS:
								Toast.makeText(getContext(), "이메일을 전송하였습니다", Toast.LENGTH_SHORT).show();
								break;
							case NOT_FOUND_USER:
								Toast.makeText(getContext(), "등록되지 않은 사용자입니다", Toast.LENGTH_SHORT).show();
								break;
							case WRONG_PARAMS:
								Toast.makeText(getContext(), "정확한 아이디와 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
								break;
						}
					}
				});
			} else {
				Toast.makeText(getContext(), "정확한 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
			}
		} else if (editId.getText() == null){
			Toast.makeText(getContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
		} else if (editId.getText() == null){
			Toast.makeText(getContext(), "이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "아이디와 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.btn_srch_signup)
	public void changedSignup() {
		BaseFragment signupFragment = new SignupFragment();
		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(signupFragment);
	}

	@OnClick(R.id.ll_searchpw)
	public void layoutClick() {
		closeInputKeyboard(editId);
		closeInputKeyboard(editEmail);
	}

	@OnClick(R.id.ibtn_back_serch_pw)
	public void back() {
		getActivity().onBackPressed();
		layoutClick();
	}
}
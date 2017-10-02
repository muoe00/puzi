package com.puzi.puzi.ui.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SearchPwFragment extends Fragment {

	private Unbinder unbinder;
	private InputMethodManager inputMethodManager;

	@BindView(R.id.edit_search_id) public EditText editId;
	@BindView(R.id.edit_search_email) public EditText editEmail;
	@BindView(R.id.btn_srchpw) public Button btnSearch;
	@BindView(R.id.btn_srch_signup) public Button btnSignup;

	private static final String TAG = "SearchPw";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_search_pw, container, false);

		unbinder = ButterKnife.bind(this, view);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		return view;
	}

	@OnClick(R.id.btn_srchpw)
	public void searchPw(){

		if(editId.getText() != null && editEmail.getText() != null) {
			String id = editId.getText().toString();
			String email = editEmail.getText().toString();

			UserNetworkService userNetworkService  = RetrofitManager.create(UserNetworkService.class);

			Call<ResponseVO> call = userNetworkService.searchpasswd(id, email);
			call.enqueue(new CustomCallback<ResponseVO>(getActivity()) {
				@Override
				public void onSuccess(ResponseVO responseVO) {
					switch(responseVO.getResultType()){
						case SUCCESS:
							Toast.makeText(getContext(), "이메일을 전송하였습니다", Toast.LENGTH_SHORT).show();
							break;
						case NOT_FOUND_USER:
							Toast.makeText(getContext(), "등록되지 않은 사용자입니다", Toast.LENGTH_SHORT).show();
							break;
					}
				}
			});

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
		Fragment signupFragment = new SignupFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.intro_fragment_container, signupFragment);
		fragmentTransaction.addToBackStack("SIGNUP");
		fragmentTransaction.commit();
	}

	@OnClick(R.id.ll_searchpw)
	public void layoutClick() {
		inputMethodManager.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		getActivity().onBackPressed();
	}
}
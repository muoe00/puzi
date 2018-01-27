package kr.puzi.puzi.ui.intro;

import android.os.Bundle;
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
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.IntroActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-05-26.
 */

public class SearchIdFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.edit_search_email) public EditText editEmail;
	@BindView(kr.puzi.puzi.R.id.btn_srchid) public Button btnSearch;
	@BindView(kr.puzi.puzi.R.id.btn_srch_signup) public Button btnSignup;
	@BindView(kr.puzi.puzi.R.id.ibtn_back_serch_id) public ImageButton ibtnBack;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_search_id, container, false);
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@OnClick(kr.puzi.puzi.R.id.btn_srchid)
	public void searchId(){
		if(editEmail.getText() == null) {
			Toast.makeText(getContext(), "이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}

		final String email = editEmail.getText().toString();
		if(!ValidationUtils.checkEmail(email)) {
			Toast.makeText(getContext(), "정확한 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}

		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.searchid(email);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Toast.makeText(getContext(), "이메일을 전송하였습니다", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_srch_signup)
	public void changedSignup() {
		BaseFragment signupFragment = new SignupFragment();
		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(signupFragment);
	}

	@OnClick(kr.puzi.puzi.R.id.ll_searchid)
	public void layoutClick() {
		closeInputKeyboard(editEmail);
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_back_serch_id)
	public void back() {
		getActivity().onBackPressed();
		layoutClick();
	}
}



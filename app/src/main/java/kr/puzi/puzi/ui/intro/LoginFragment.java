package kr.puzi.puzi.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.user.RegisterType;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.UserNetworkService;
import kr.puzi.puzi.ui.DeviceKeyFinder;
import kr.puzi.puzi.ui.IntroActivity;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.common.BasicDialog;
import kr.puzi.puzi.utils.EncryptUtils;
import retrofit2.Call;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class LoginFragment extends BaseFragment {

	private Unbinder unbinder;

	@BindView(R.id.login_et_id) public EditText etId;
	@BindView(R.id.edit_login_pw) public EditText etPwd;
	@BindView(R.id.login_btn) public Button btnLogin;
	@BindView(R.id.ibtn_back_login) public ImageButton ibtnBack;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		unbinder = ButterKnife.bind(this, view);
		startAnimation();

		return view;
	}

	private void startAnimation() {
		Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_1);
		Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_2);
		Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_3);
		etId.startAnimation(animation1);
		etPwd.startAnimation(animation2);
		btnLogin.startAnimation(animation3);
	}

	private boolean isValid(String id, String pwd) {
		if(id == null || id.length() < 6){
			String errorNoid = getActivity().getResources().getString(R.string.login_error_noid);
			Toast.makeText(getActivity(), errorNoid, Toast.LENGTH_SHORT).show();
			return false;
		} else if(pwd == null || pwd.length() < 6){
			String errorNopwd = getActivity().getResources().getString(R.string.login_error_nopwd);
			Toast.makeText(getActivity(), errorNopwd, Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	@OnClick(R.id.login_btn)
	public void login(){
		final String id = etId.getText().toString();
		final String pwd = etPwd.getText().toString();

		if(isValid(id, pwd)){
			ProgressDialog.show(getActivity());

			final String sha256Pw = EncryptUtils.sha256(pwd);
			final String notifyId = FirebaseInstanceId.getInstance().getToken();
			final String phoneType = "A";
			final String phoneKey = DeviceKeyFinder.find(getActivity());

			LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
			service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
				@Override
				public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
					return userNetworkService.login(id, sha256Pw, notifyId, phoneType, phoneKey);
				}
			});
			service.enqueue(new CustomCallback(getActivity()) {
				@Override
				public void onSuccess(ResponseVO responseVO) {
					successLogin(responseVO.getString("token"), id, sha256Pw);
				}

				@Override
				public void onFail(ResponseVO responseVO) {
					BasicDialog.show(getActivity(), "로그인실패", "아이디 또는 비밀번호를 확인해주세요");
				}
			});
		}
	}

	public void successLogin(String token, String id, String pwd) {
		Log.i("INFO", "login token : " + token);

		Preference.addProperty(getActivity(), "token", token);
		Preference.addProperty(getActivity(), "id", id);
		Preference.addProperty(getActivity(), "passwd", pwd);

		UserVO userVO = new UserVO();
		userVO.setRegisterType(RegisterType.N);
		Preference.saveMyInfo(getActivity(), userVO);

		startActivity(new Intent(getActivity(), MainActivity.class));
		getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
		getActivity().finish();
	}

	@OnClick({R.id.btn_srch_id, R.id.btn_srch_pw})
	public void changedFragment(View view) {
		BaseFragment fragment = null;
		switch (view.getId()) {
			case R.id.btn_srch_id:
				fragment = new SearchIdFragment();
				break;
			case R.id.btn_srch_pw:
				fragment = new SearchPwFragment();
				break;
		}

		IntroActivity introActivity = (IntroActivity) getActivity();
		introActivity.addFragment(fragment);
	}

	@OnClick(R.id.ll_login)
	public void layoutClick() {
		closeInputKeyboard(etId);
		closeInputKeyboard(etPwd);
	}

	@OnEditorAction(R.id.edit_login_pw)
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE){
			login();
		}
		return false;
	}

	@OnClick(R.id.ibtn_back_login)
	public void back() {
		getActivity().onBackPressed();
		layoutClick();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.setting.AskType;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.PuziUtils;
import com.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

import java.util.ArrayList;

import static com.puzi.puzi.biz.setting.AskType.*;

/**
 * Created by 170605 on 2017-10-27.
 */

public class AskFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.sp_ask_type) public Spinner spAskType;
	@BindView(R.id.ll_cs_ask) public LinearLayout linearLayout;
	@BindView(R.id.edt_cs_user_email) public EditText editEmail;
	@BindView(R.id.edt_cs_user_title) public EditText editTitle;
	@BindView(R.id.edt_cs_user_content) public EditText editContent;

	private View view = null;
	private AskType type;
	private String userType, askType, email, title, comment;
	private ArrayList<String> askTypes = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_cs_ask, container, false);
		unbinder = ButterKnife.bind(this, view);

		userType = getArguments().getString("key");
		settingAskType();
		getUserEmail();

		return view;
	}

	@OnItemSelected(R.id.sp_ask_type)
	public void checkAskType(int position) {
		askType = askTypes.get(position);
		type = findByComment(askType);
	}

	public void settingAskType() {
		for(AskType askType : USE.values()) {
			askTypes.add(askType.getComment());
		}

		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), askTypes);
		spAskType.setAdapter(adapter);
	}

	@OnClick(R.id.btn_ask)
	public void sendAsk() {
		if(askType != null) {
			email = editEmail.getText().toString();
			title = editTitle.getText().toString();
			comment = editContent.getText().toString();

			Log.i("INFO", "email : " + email + ", title : " + title + ", comment : " + comment);

			if(!email.equals("")) {
				if(ValidationUtils.checkEmail(email)) {
					if (!title.equals("")) {
						if (!comment.equals("")) {
							ask();
						} else {
							Toast.makeText(getContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getContext(), "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getContext(), "정확한 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getContext(), "이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getContext(), "문의 유형을 선택하세요.", Toast.LENGTH_SHORT).show();
		}
	}

	public void ask() {
		String token = Preference.getProperty(getActivity(), "token");
		ProgressDialog.show(getActivity());

		final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);

		ProgressDialog.show(getActivity());

		Log.i("INFO", "ask userType : " + userType + ", askType : " + type + ", email : " + email + ", title : " + title + ", comment : " + comment);

		Call<ResponseVO> callList = settingNetworkService.ask(token, userType, type.toString(), email, title, comment);
		callList.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i(PuziUtils.INFO, "ask responseVO : " + responseVO.toString());
				ProgressDialog.dismiss();

				switch(responseVO.getResultType()){
					case SUCCESS:
						ProgressDialog.dismiss();
						switch(responseVO.getResultType()){
							case SUCCESS:
								Toast.makeText(getContext(), "이메일을 전송하였습니다", Toast.LENGTH_SHORT).show();
								break;
						}
						break;

					case NO_AUTH:
						PuziUtils.renewalToken(getActivity());
						break;

					default:
						Log.i("INFO", "setting ask failed.");
						Toast.makeText(getContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});
	}

	public void getUserEmail() {
		final UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

		String token = Preference.getProperty(getActivity(), "token");

		Call<ResponseVO> callUser = userNetworkService.myInfo(token);
		callUser.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "advertise responseVO : " + responseVO.toString());
				switch(responseVO.getResultType()){
					case SUCCESS:
						UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);
						email = userVO.getEmail();
						editEmail.setText(email);
						break;

					default:
						Log.i("INFO", "advertisement getUser failed.");
						Toast.makeText(getActivity(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@OnClick(R.id.ll_cs_ask)
	public void layoutClick() {
		closeInputKeyboard(editEmail);
		closeInputKeyboard(editTitle);
		closeInputKeyboard(editContent);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		layoutClick();
	}
}

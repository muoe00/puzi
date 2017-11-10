package com.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.setting.AskType;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.utils.ValidationUtils;
import retrofit2.Call;

import java.util.ArrayList;

import static com.puzi.puzi.biz.setting.AskType.USE;
import static com.puzi.puzi.biz.setting.AskType.findByComment;

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
		ProgressDialog.show(getActivity());


		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.ask(token, userType, type.toString(), email, title, comment);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Toast.makeText(getContext(), "이메일을 전송하였습니다", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void getUserEmail() {
		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.myInfo(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);
				email = userVO.getEmail();
				editEmail.setText(email);
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

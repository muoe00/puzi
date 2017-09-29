package com.puzi.puzi.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.notice.NoticeVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class SettingDetailActivity extends Activity {

	private List<NoticeVO> noticeVO;
	private ImageButton btnBack;
	private TextView tvTitle;
	private String token;
	private int tag, pagingIndex = 0;
	private SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);
	private Call<ResponseVO> call = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_setting_detail);

		initComponent();

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Intent intent = getIntent();
		tag = intent.getExtras().getInt("TAG");

		token = Preference.getProperty(SettingDetailActivity.this, "token");
		Log.i("INFO", "token : " + token);

		switch (tag) {
			case 0:
				tvTitle.setText("공지사항");
				call = settingNetworkService.list(token, pagingIndex);
				Log.e("### token : ", ""+token);
				call(tag);
				break;
			case 1:
				tvTitle.setText("버전정보");
				break;
			case 2:
				tvTitle.setText("계정/관심사 설정");
				break;
			case 3:
				tvTitle.setText("알람 시간 제한 설정");
				break;
			case 4:
				tvTitle.setText("수신 거부 관리");
				break;
			case 5:
				tvTitle.setText("고객센터");
				break;
		}

	}

	private void call(int tag) {

		/*call.enqueue(new CustomCallback<ResponseVO>() {
			@Override
			public void onResponse(ResponseVO responseVO) {

				int resultCode = responseVO.getResultCode();

				switch(resultCode){
					case ResultCode.SUCCESS:

						noticeVO = responseVO.test("noticeList", NoticeVO.class);

						Log.e("### noticeVO : ", "" + noticeVO);
						break;

					default:
						break;

				}
			}

			@Override
			public void onFailure(PuziNetworkException e) {
				Log.e("TAG", "통신 오류(" + e.getCode()+")");
			}

		});*/

	}

	private void initComponent() {

		btnBack = (ImageButton) findViewById(R.id.ibtn_back);
		tvTitle = (TextView) findViewById(R.id.tv_setting_title);

	}
}

package com.puzi.puzi.ui.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.gson.Gson;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.setting.NoticeVO;
import com.puzi.puzi.fcm.PuziPushType;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
public class ScreenOffAlertActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.tv_comment)
	TextView tvComment;

	private Gson gson = new Gson();
	private PuziPushType type;
	private String data;
	private Intent nextIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_off_alert);

		getWindow()
			.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		unbinder = ButterKnife.bind(this);

		Intent intent = getIntent();

		initComponent(intent);
	}

	private void initComponent(Intent intent) {
		String typeString = intent.getStringExtra("TYPE");
		if(typeString == null) {
			finish();
			return;
		}

		String title = null;
		String comment = null;
		type = PuziPushType.valueOf(typeString);
		nextIntent = new Intent(getActivity(), MainActivity.class);
		nextIntent.putExtra("TYPE", type.name());
		switch (type) {
			case ADVERTISEMENT:
				data = intent.getStringExtra("receivedAdvertiseDTO");
				ReceivedAdvertiseVO receivedAdvertiseVO = gson.fromJson(data, ReceivedAdvertiseVO.class);
				title = "(광고)";
				comment = receivedAdvertiseVO.getSendComment();
				nextIntent.putExtra("receivedAdvertiseDTO", data);
				break;

			case NOTICE:
				data = intent.getStringExtra("noticeDTO");
				NoticeVO noticeVO = gson.fromJson(data, NoticeVO.class);
				title = "(공지)";
				comment = noticeVO.getTitle();
				nextIntent.putExtra("noticeDTO", data);
				break;
		}

		if(title == null) {
			finish();
			return;
		}

		tvTitle.setText(title);
		tvComment.setText(comment);
	}

	@OnClick(R.id.btn_dialog_close)
	public void closeClick() {
		finish();
	}

	@OnClick(R.id.btn_dialog_confirm)
	public void confirmClick() {
		startActivity(nextIntent);
		finish();
		doAnimationGoRight();
	}

}

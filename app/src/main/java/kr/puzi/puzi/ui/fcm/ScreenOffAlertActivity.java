package kr.puzi.puzi.ui.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.gson.Gson;

import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.fcm.PuziPushType;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
public class ScreenOffAlertActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_title)
	TextView tvTitle;
	@BindView(kr.puzi.puzi.R.id.tv_comment)
	TextView tvComment;

	private Gson gson = new Gson();
	private PuziPushType type;
	private String data;
	private Intent nextIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_screen_off_alert);

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
				title = "새로운 광고가 도착했습니다.";
				comment = receivedAdvertiseVO.getSendComment();
				nextIntent.putExtra("receivedAdvertiseDTO", data);
				break;

			case NOTICE:
				break;
		}

		if(title == null) {
			finish();
			return;
		}

		tvTitle.setText(title);
		tvComment.setText(comment);
	}

	@OnClick(kr.puzi.puzi.R.id.btn_dialog_close)
	public void closeClick() {
		finish();
	}

	@OnClick(kr.puzi.puzi.R.id.btn_dialog_confirm)
	public void confirmClick() {
		startActivity(nextIntent);
		finish();
		doAnimationGoRight();
	}

}

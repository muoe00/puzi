package kr.puzi.puzi.ui.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
public class ScreenOnAlertActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.tv_top_title)
	TextView tvTitle;
	@BindView(kr.puzi.puzi.R.id.tv_top_comment)
	TextView tvComment;
	@BindView(kr.puzi.puzi.R.id.ll_view_container)
	LinearLayout llViewContainer;
	@BindView(kr.puzi.puzi.R.id.fl_container)
	FrameLayout flContainer;
	@BindView(kr.puzi.puzi.R.id.btn_top)
	Button btnTop;

	private Gson gson = new Gson();
	private PuziPushType type;
	private String data;
	private Intent nextIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.view_screen_on_alert);

		getWindow()
			.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		unbinder = ButterKnife.bind(this);

		Intent intent = getIntent();

		initComponent(intent);

		Animation animation = AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.top_in);
		flContainer.startAnimation(animation);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				closeAlertOnTheTop();
			}
		}, 2500);
	}

	private void closeAlertOnTheTop() {
		if(flContainer.getVisibility() == View.GONE) {
			return;
		}
		flContainer.setVisibility(View.GONE);
		Animation animation = AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.top_out);
		flContainer.startAnimation(animation);
		finish();
	}

	private void initComponent(Intent intent) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llViewContainer.getLayoutParams();
		params.topMargin = 0;
		llViewContainer.setLayoutParams(params);
		FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) btnTop.getLayoutParams();
		params2.topMargin = 0;
		btnTop.setLayoutParams(params2);

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

	@OnClick(kr.puzi.puzi.R.id.btn_top)
	public void confirmClick() {
		closeAlertOnTheTop();
		startActivity(nextIntent);
		doAnimationGoRight();
	}

}

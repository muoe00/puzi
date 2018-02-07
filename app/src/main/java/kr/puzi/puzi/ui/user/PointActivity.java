package kr.puzi.puzi.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.user.LevelType;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.utils.PuziUtils;

/**
 * Created by muoe0 on 2017-07-08.
 */
public class PointActivity extends BaseFragmentActivity {

	Unbinder unbinder;
	private int tag = 0;

	@BindView(kr.puzi.puzi.R.id.ibtn_back_point) public ImageButton btnBack;
	@BindView(kr.puzi.puzi.R.id.tv_point) public TextView tvPoint;
	@BindView(kr.puzi.puzi.R.id.tv_point_today) public TextView tvTodayPoint;
	@BindView(kr.puzi.puzi.R.id.tv_point_user_name) public TextView tvUserName;
	@BindView(kr.puzi.puzi.R.id.tv_point_user_level) public TextView tvUserLevel;
	@BindView(kr.puzi.puzi.R.id.tv_point_history) public TextView tvPointHistory;
	@BindView(kr.puzi.puzi.R.id.tv_point_level) public TextView tvPointLevel;
	@BindView(kr.puzi.puzi.R.id.iv_point_background) public ImageView ivLevel;
	@BindView(kr.puzi.puzi.R.id.iv_point_select) public ImageView ivPointBar;
	@BindView(kr.puzi.puzi.R.id.iv_level_select) public ImageView ivLevelBar;
	@BindView(kr.puzi.puzi.R.id.fl_container_top) public FrameLayout flContainerTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_point);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		tvPointHistory.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
		tvPointLevel.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
		ivPointBar.setVisibility(View.VISIBLE);
		ivLevelBar.setVisibility(View.INVISIBLE);

		UserVO userVO = Preference.getMyInfo(getActivity());

		NumberFormat numberFormat = NumberFormat.getInstance();

		LevelType level = LevelType.valueOf(userVO.getLevelType());
		tvUserLevel.setText(level.getComment());

		switch(LevelType.findByComment(level.getComment())) {
			case WELCOME:
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.welcome_grade);
				break;
			case SILVER:
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.silver_bg);
				break;
			case GOLD:
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.gold_grade);
				break;
			case VIP:
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.vip_grade);
				break;
			case VVIP:
				ivLevel.setImageResource(kr.puzi.puzi.R.drawable.vvip_grade);
				break;
		}

		tvUserName.setText(Preference.getProperty(getActivity(), "id"));

		int point = userVO.getPoint();
		String resultPoint = numberFormat.format(point);
		tvPoint.setText(resultPoint);

		int todayPoint = userVO.getTodayPoint();
		String resultTodayPoint = numberFormat.format(todayPoint);
		tvTodayPoint.setText(resultTodayPoint);

		changedFragment(tag);
	}

	public void changedFragment(int tag) {
		Bundle bundle = new Bundle(1);
		bundle.putInt("tag", tag);

		BaseFragment fragment = new PointContentsFragment();
		fragment.setArguments(bundle);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(kr.puzi.puzi.R.id.ll_point_content, fragment);
		fragmentTransaction.commit();
	}

	@OnClick({kr.puzi.puzi.R.id.btn_channelRecommend, kr.puzi.puzi.R.id.btn_point_level})
	public void changedTag(View view) {

		switch(view.getId()) {
			case kr.puzi.puzi.R.id.btn_channelRecommend:
				tag = PuziUtils.VIEW_POINT;
				tvPointHistory.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
				tvPointLevel.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
				ivPointBar.setVisibility(View.VISIBLE);
				ivLevelBar.setVisibility(View.INVISIBLE);
				break;
			case kr.puzi.puzi.R.id.btn_point_level:
				tag = PuziUtils.VIEW_LEVEL;
				tvPointHistory.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
				tvPointLevel.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
				ivPointBar.setVisibility(View.INVISIBLE);
				ivLevelBar.setVisibility(View.VISIBLE);
				break;
		}

		changedFragment(tag);
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_back_point)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}
}

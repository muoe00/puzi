package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.LevelType;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.text.NumberFormat;

import static com.puzi.puzi.biz.user.LevelType.*;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class PointActivity extends BaseFragmentActivity {

	Unbinder unbinder;
	private int tag = 0;

	@BindView(R.id.ibtn_back_point) public ImageButton btnBack;
	@BindView(R.id.tv_point) public TextView tvPoint;
	@BindView(R.id.tv_point_today) public TextView tvTodayPoint;
	@BindView(R.id.tv_point_user_name) public TextView tvUserName;
	@BindView(R.id.tv_point_user_level) public TextView tvUserLevel;
	@BindView(R.id.tv_point_history) public TextView tvPointHistory;
	@BindView(R.id.tv_point_level) public TextView tvPointLevel;
	@BindView(R.id.iv_point_background) public ImageView ivLevel;
	@BindView(R.id.iv_point_select) public ImageView ivPointBar;
	@BindView(R.id.iv_level_select) public ImageView ivLevelBar;
	@BindView(R.id.fl_container_top) public FrameLayout flContainerTop;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		tvPointHistory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
		tvPointLevel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
		ivPointBar.setVisibility(View.VISIBLE);
		ivLevelBar.setVisibility(View.INVISIBLE);

		getUser();
		changedFragment(tag);
	}

	public void getUser() {
		LazyRequestService service = new LazyRequestService(getActivity(), UserNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<UserNetworkService>() {
			@Override
			public Call<ResponseVO> execute(UserNetworkService userNetworkService, String token) {
				return userNetworkService.myInfo(token);
			}
		});
		service.enqueue(new CustomCallback(PointActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);
				Log.i("INFO", "HomeFragment main / userVO : " + userVO.toString());

				NumberFormat numberFormat = NumberFormat.getInstance();

				LevelType level = LevelType.valueOf(userVO.getLevelType());
				tvUserLevel.setText(level.getComment());

				switch(findByComment(level.getComment())) {
					case WELCOME:
						ivLevel.setImageResource(R.drawable.welcome_grade);
						break;
					case SILVER:
						ivLevel.setImageResource(R.drawable.silver_bg);
						break;
					case GOLD:
						ivLevel.setImageResource(R.drawable.gold_grade);
						break;
					case VIP:
						ivLevel.setImageResource(R.drawable.vip_grade);
						break;
					case VVIP:
						ivLevel.setImageResource(R.drawable.vvip_grade);
						break;
				}

				tvUserName.setText(Preference.getProperty(getActivity(), "id"));

				int point = userVO.getPoint();
				String resultPoint = numberFormat.format(point);
				tvPoint.setText(resultPoint);

				int todayPoint = userVO.getTodayPoint();
				String resultTodayPoint = numberFormat.format(todayPoint);
				tvTodayPoint.setText(resultTodayPoint);
			}
		});
	}

	public void changedFragment(int tag) {
		Bundle bundle = new Bundle(1);
		bundle.putInt("tag", tag);

		BaseFragment fragment = new PointContentsFragment();
		fragment.setArguments(bundle);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.ll_point_content, fragment);
		fragmentTransaction.commit();
	}

	@OnClick({R.id.btn_channelRecommend, R.id.btn_point_level})
	public void changedTag(View view) {

		switch(view.getId()) {
			case R.id.btn_channelRecommend:
				tag = PuziUtils.VIEW_POINT;
				tvPointHistory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
				tvPointLevel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
				ivPointBar.setVisibility(View.VISIBLE);
				ivLevelBar.setVisibility(View.INVISIBLE);
				break;
			case R.id.btn_point_level:
				tag = PuziUtils.VIEW_LEVEL;
				tvPointHistory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
				tvPointLevel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
				ivPointBar.setVisibility(View.INVISIBLE);
				ivLevelBar.setVisibility(View.VISIBLE);
				break;
		}

		changedFragment(tag);
	}

	@OnClick(R.id.ibtn_back_point)
	public void back() {
		finish();
		doAnimationGoLeft();
	}
}

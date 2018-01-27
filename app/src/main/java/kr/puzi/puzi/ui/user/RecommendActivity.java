package kr.puzi.puzi.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.puzi.puzi.biz.event.EventInfoVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;

import com.google.gson.Gson;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class RecommendActivity extends BaseFragmentActivity {

	@BindView(kr.puzi.puzi.R.id.tv_recommend_id)
	public TextView tvRecommendId;
	@BindView(kr.puzi.puzi.R.id.recommend_kakaoButton)
	public ImageButton kakao;
	@BindView(kr.puzi.puzi.R.id.ibtn_back)
	public ImageButton btnBack;
	@BindView(kr.puzi.puzi.R.id.ibtn_event_link)
	public ImageButton ibtnEventLink;
	@BindView(kr.puzi.puzi.R.id.fl_container_top)
	public FrameLayout flContainerTop;

	private KakaoLink kakaoLink;
	private KakaoTalkLinkMessageBuilder builder;

	private EventInfoVO eventInfoVO;

	private String recommendMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.puzi.puzi.R.layout.activity_recommend);

		ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		Intent intent = getIntent();
		eventInfoVO = new Gson().fromJson(intent.getStringExtra("eventInfoVO"), EventInfoVO.class);

		initComponent();

		UserVO myInfo = Preference.getMyInfo(getActivity());

		if(eventInfoVO.getEventStatusType().isShowEvent()) {
			recommendMessage = "카카오톡 무료 이모티콘, 너도 받고푸지?\n"
				+ "친구 추천하고 100포인트 적립 받으세요!\n"
				+ "추천인 : " + myInfo.getUserId();
		} else {
			recommendMessage = "친구 추천하고 100포인트 적립 받으세요!\n"
				+ "추천인 : " + myInfo.getUserId();
		}

		try {
			kakaoLink = KakaoLink.getKakaoLink(this);
			builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
			builder.addImage("https://s3.ap-northeast-2.amazonaws.com/puzi/event1.jpg", 1321, 692);
			builder.addText(recommendMessage);
			builder.addAppLink("앱으로 이동");
		} catch (KakaoParameterException e) {
			e.printStackTrace();
		}
	}

	private void initComponent() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		tvRecommendId.setText(userVO.getUserId());
		if(!eventInfoVO.getEventStatusType().isShowEvent()) {
			ibtnEventLink.setVisibility(View.GONE);
		}
	}

	@OnClick(kr.puzi.puzi.R.id.recommend_kakaoButton)
	public void sendMessage() {
		try {
			kakaoLink.sendMessage(builder, this);
		} catch (KakaoParameterException e) {
			e.printStackTrace();
		}
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_event_link)
	public void clickEvent() {
		Intent intent = new Intent(getActivity(), EventWebViewActivity.class);
		intent.putExtra("url", eventInfoVO.getUrl());
		startActivity(intent);
		doAnimationGoRight();
	}

	@OnClick(kr.puzi.puzi.R.id.ibtn_back)
	public void back() {
		finish();
		doAnimationGoLeft();
	}
}

package com.puzi.puzi.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.event.EventInfoVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.ui.base.BaseFragmentActivity;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class RecommendActivity extends BaseFragmentActivity {

	@BindView(R.id.tv_recommend_id)
	public TextView tvRecommendId;
	@BindView(R.id.recommend_kakaoButton)
	public ImageButton kakao;
	@BindView(R.id.ibtn_back)
	public ImageButton btnBack;
	@BindView(R.id.ibtn_event_link)
	public ImageButton ibtnEventLink;
	@BindView(R.id.fl_container_top)
	public FrameLayout flContainerTop;

	private KakaoLink kakaoLink;
	private KakaoTalkLinkMessageBuilder builder;

	private EventInfoVO eventInfoVO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);

		ButterKnife.bind(this);
		targetViewForPush = flContainerTop;

		Intent intent = getIntent();
		eventInfoVO = new Gson().fromJson(intent.getStringExtra("eventInfoVO"), EventInfoVO.class);

		initComponent();

		try {
			kakaoLink = KakaoLink.getKakaoLink(this);
			builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
			builder.addText("TEST");
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

	@OnClick(R.id.recommend_kakaoButton)
	public void sendMessage() {
		try {
			kakaoLink.sendMessage(builder, this);
		} catch (KakaoParameterException e) {
			e.printStackTrace();
		}
	}

	@OnClick(R.id.ibtn_event_link)
	public void clickEvent() {
		Intent intent = new Intent(getActivity(), EventWebViewActivity.class);
		intent.putExtra("url", eventInfoVO.getUrl());
		startActivity(intent);
		doAnimationGoRight();
	}

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
		doAnimationGoLeft();
	}
}

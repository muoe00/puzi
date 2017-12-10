package com.puzi.puzi.ui.user;

import android.os.Bundle;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by muoe0 on 2017-07-08.
 */

public class RecommendActivity extends BaseActivity {

	@BindView(R.id.recommend_kakaoButton)
	public ImageButton kakao;
	@BindView(R.id.ibtn_back)
	public ImageButton btnBack;

	private KakaoLink kakaoLink;
	private KakaoTalkLinkMessageBuilder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);

		ButterKnife.bind(this);

		try {
			kakaoLink = KakaoLink.getKakaoLink(this);
			builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
			builder.addText("TEST");
		} catch (KakaoParameterException e) {
			e.printStackTrace();
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

	@OnClick(R.id.ibtn_back)
	public void back() {
		finish();
		doAnimationGoLeft();
	}
}

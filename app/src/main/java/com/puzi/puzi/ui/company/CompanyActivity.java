package com.puzi.puzi.ui.company;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.biz.company.CompanyVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.CompanyNetworkService;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

import java.util.List;

public class CompanyActivity extends BaseFragmentActivity {

	Unbinder unbinder;

	@BindView(R.id.btn_block) public ImageButton btnBlock;
	@BindView(R.id.iv_companyProfile_picture) public SelectableRoundedImageView companyPicture;
	@BindView(R.id.tv_companyName) public TextView companyName;
	@BindView(R.id.tv_companyComment) public TextView companyComment;
	@BindView(R.id.lv_profile_channel_list) public ListView lvChannelList;
	@BindView(R.id.ll_company_container) public ListView llCompanyContainer;

	private CompanyChannelAdapter companyChannelAdapter;

	private int companyId = 0;
	private int pagingIndex = 1;
	private boolean isBlock = false;
	private boolean more = false;
	private boolean lastestScrollFlag = false;
	private int totalCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		unbinder = ButterKnife.bind(this);

		initScrollAction();
		initAdapter();

		Intent intent = getIntent();
		CompanyVO companyVO = (CompanyVO) intent.getSerializableExtra("company");
		Log.i(PuziUtils.INFO, "companyVO : " + companyVO.toString());

		if(companyVO == null) {
			companyId = intent.getIntExtra("companyId", 0);
			Log.i(PuziUtils.INFO, "companyId : " + companyId);
			getCompany();
		} else {
			BitmapUIL.load(companyVO.getPictureUrl(), companyPicture);
			companyName.setText(companyVO.getCompanyAlias());
			companyComment.setText(companyVO.getComment());
		}

		isBlock = companyVO.getBlocked();

		if(isBlock) {
			// btnBlock.setBackgroundResource();
		} else if(!isBlock) {
			// btnBlock.setBackgroundResource();
		}

		getChannelList();
	}

	private void initAdapter() {
		companyChannelAdapter = new CompanyChannelAdapter(this);
		lvChannelList.setAdapter(companyChannelAdapter);
//		lvChannelList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//				if(scrollY > 100) { // 채널확대
//					llCompanyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
//				} else if(scrollY > 5) { // 줄이기
//					llCompanyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollY));
//				} else { // 기본
//					llCompanyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
//				}
//			}
//		});
	}

	private void initScrollAction() {
		lvChannelList.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
					if(more) {
						pagingIndex = pagingIndex + 1;
						getChannelList();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastestScrollFlag = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
			}
		});
	}

	public void getCompany() {

		CompanyNetworkService companyNetworkService = RetrofitManager.create(CompanyNetworkService.class);
		String token = Preference.getProperty(CompanyActivity.this, "token");

		Call<ResponseVO> call = companyNetworkService.profile(token, companyId);
		call.enqueue(new CustomCallback<ResponseVO>(CompanyActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				switch(responseVO.getResultType()){
					case SUCCESS:
						CompanyVO companyVO = responseVO.getValue("companyInfoDTO", CompanyVO.class);

						BitmapUIL.load(companyVO.getPictureUrl(), companyPicture);
						companyName.setText(companyVO.getCompanyAlias());
						companyComment.setText(companyVO.getComment());
						break;

					default:
						Log.i("INFO", "company profile failed.");
						Toast.makeText(getBaseContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void getChannelList() {
		companyChannelAdapter.startProgress();

		CompanyNetworkService companyNetworkService = RetrofitManager.create(CompanyNetworkService.class);
		String token = Preference.getProperty(CompanyActivity.this, "token");

		Call<ResponseVO> call = companyNetworkService.channelList(token, companyId, pagingIndex);
		call.enqueue(new CustomCallback<ResponseVO>(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				companyChannelAdapter.stopProgress();

				if(responseVO.getResultType().isSuccess()) {
					List<ChannelVO> newChannelList = responseVO.getList("channelDTOList", ChannelVO.class);
					companyChannelAdapter.addList(newChannelList);

					totalCount = responseVO.getInteger("totalCount");
					if(totalCount == companyChannelAdapter.getCount()) {
						more = false;
					} else {
						more = true;
					}
				}
			}
		});
	}

	public void updateBlock(boolean isBlock) {

		SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);
		String token = Preference.getProperty(CompanyActivity.this, "token");

		Call<ResponseVO> call = settingNetworkService.company(token, isBlock, companyId);
		call.enqueue(new CustomCallback<ResponseVO>(CompanyActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				switch(responseVO.getResultType()){
					case SUCCESS:
						Log.i("INFO", "company block success.");
						break;

					default:
						Log.i("INFO", "company block failed.");
						Toast.makeText(getBaseContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@OnClick(R.id.btn_block)
	public void blockCompany() {

		String title = "차단하기";
		String message = "차단하시겠습니까?";

		if(isBlock) {
			// btnBlock.setBackgroundResource();
			isBlock = false;
			title = "차단 해제하기";
			message = "차단을 해제하시겠습니까?";
		} else if(!isBlock) {
			// btnBlock.setBackgroundResource();
			isBlock = true;
		}

		OneButtonDialog.show(CompanyActivity.this, title, message, "확인", new DialogButtonCallback() {
			@Override
			public void onClick() {
				updateBlock(isBlock);
				Log.i(PuziUtils.INFO, "isBlock : " + isBlock);
			}
		});
	}

	@OnClick(R.id.btn_back_profile)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

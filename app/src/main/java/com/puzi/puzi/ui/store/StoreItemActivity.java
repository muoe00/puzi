package com.puzi.puzi.ui.store;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.company.CompanyChannelAdapter;

public class StoreItemActivity extends BaseFragmentActivity {

	Unbinder unbinder;

//	@BindView(R.id.btn_block) public ImageButton btnBlock;

	private CompanyChannelAdapter companyChannelAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);

		unbinder = ButterKnife.bind(this);

//		initScrollAction();
//		initAdapter();
//
//		Intent intent = getIntent();
//		companyVO = (CompanyVO) intent.getSerializableExtra("company");
//		Log.i(PuziUtils.INFO, "companyVO : " + companyVO.toString());
//
//		if(companyVO == null) {
//			companyId = intent.getIntExtra("companyId", 0);
//			Log.i(PuziUtils.INFO, "companyId : " + companyId);
//			getCompany();
//		} else {
//			setCompanyInfo();
//		}
//
//		isBlock = companyVO.getBlocked();
//
//		if(isBlock) {
//			// btnBlock.setBackgroundResource();
//		} else if(!isBlock) {
//			// btnBlock.setBackgroundResource();
//		}
//
//		getChannelList(false);
	}

	private void getChannelList(final boolean scrollToBottom) {
//		companyChannelAdapter.startProgress();
//
//		if(scrollToBottom) {
//			svContainer.post(new Runnable() {
//				@Override
//				public void run() {
//					svContainer.fullScroll(View.FOCUS_DOWN);
//				}
//			});
//		}
//
//		CompanyNetworkService companyNetworkService = RetrofitManager.create(CompanyNetworkService.class);
//		String token = Preference.getProperty(StoreItemActivity.this, "token");
//
//		Call<ResponseVO> call = companyNetworkService.channelList(token, companyVO.getCompanyId(), pagingIndex);
//		call.enqueue(new CustomCallback<ResponseVO>(this) {
//
//			@Override
//			public void onSuccess(ResponseVO responseVO) {
//
//				companyChannelAdapter.stopProgress();
//
//				if(responseVO.getResultType().isSuccess()) {
//					List<ChannelVO> newChannelList = responseVO.getList("channelDTOList", ChannelVO.class);
//					companyChannelAdapter.addList(newChannelList);
//					setListViewHeightBasedOnChildren(lvChannelList);
//
//					totalCount = responseVO.getInteger("totalCount");
//					if(totalCount == companyChannelAdapter.getCount()) {
//						more = false;
//					} else {
//						more = true;
//					}
//
//					if(!scrollToBottom) {
//						svContainer.post(new Runnable() {
//							@Override
//							public void run() {
//								svContainer.fullScroll(View.FOCUS_UP);
//							}
//						});
//					}
//				}
//			}
//		});
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

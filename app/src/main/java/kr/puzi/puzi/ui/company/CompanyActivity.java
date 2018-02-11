package kr.puzi.puzi.ui.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joooonho.SelectableRoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.channel.ChannelVO;
import kr.puzi.puzi.biz.company.CompanyVO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.CompanyNetworkService;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.CustomScrollView;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.utils.PuziUtils;
import retrofit2.Call;

public class CompanyActivity extends BaseFragmentActivity {

	private static final int SCROLL_STATE_FLING = 0;
	private static final int SCROLL_STATE_TOUCH_SCROLL = 1;
	private static final int SCROLL_STATE_IDLE = 2;

	Unbinder unbinder;

	@BindView(R.id.btn_block) public ImageButton btnBlock;
	@BindView(R.id.iv_companyProfile_picture) public SelectableRoundedImageView companyPicture;
	@BindView(R.id.tv_companyName) public TextView companyName;
	@BindView(R.id.tv_companyComment) public TextView companyComment;
	@BindView(R.id.lv_profile_channel_list) public ListView lvChannelList;
	@BindView(R.id.ll_company_container) public LinearLayout llCompanyContainer;
	@BindView(R.id.sv_container) public CustomScrollView svContainer;
	@BindView(R.id.iv_title_companyProfile_picture) public SelectableRoundedImageView titleCompanyPicture;
	@BindView(R.id.tv_title_companyName) public TextView titleCompanyName;
	@BindView(R.id.ll_title_bottom_bar) public LinearLayout llTitleBottomBar;
	@BindView(R.id.ibtn_like) public Button btnLike;
	@BindView(R.id.ll_profile_channel_dropdown_container) public LinearLayout llDropDownContainer;
	@BindView(R.id.btn_profile_channel_dropdown_block) public Button btnDropDownBlock;
	@BindView(R.id.fl_company_container_top) public FrameLayout flCompanyContainerTop;

	private CompanyChannelAdapter companyChannelAdapter;

	private int pagingIndex = 1;
	private CompanyVO companyVO;
	private boolean isBlock = false;
	private boolean more = false;
	private int totalCount = 0;
	private int svContainerState = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);

		unbinder = ButterKnife.bind(this);
		targetViewForPush = flCompanyContainerTop;

		initScrollAction();
		initAdapter();

		Intent intent = getIntent();
		companyVO = (CompanyVO) intent.getSerializableExtra("company");
		Log.i(PuziUtils.INFO, "companyVO : " + companyVO.toString());

		int companyId = 0;
		 if(companyVO == null) {
			companyId = intent.getIntExtra("companyId", 0);
		 } else {
			 companyId = companyVO.getCompanyId();
		}

		getCompany(companyId);
		getChannelList(false);
	}

	private void initAdapter() {
		companyChannelAdapter = new CompanyChannelAdapter(this);
		lvChannelList.setAdapter(companyChannelAdapter);
	}

	private void initScrollAction() {
		svContainer.setOnEndedScrolledListener(new CustomScrollView.OnEndedScrolledListener() {
			@Override
			public void onEnded() {
				if(more) {
					pagingIndex = pagingIndex + 1;
					getChannelList(true);
				}
			}
		});
		svContainer.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_SCROLL:
					case MotionEvent.ACTION_MOVE:
						svContainerState = SCROLL_STATE_FLING;
						break;
					case MotionEvent.ACTION_DOWN:
						svContainerState = SCROLL_STATE_TOUCH_SCROLL;
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						svContainerState = SCROLL_STATE_IDLE;
						break;
				}
				return false;
			}
		});
		final int CHANGE_Y_START = 200;
		final int CHANGE_Y_END = 400;
		svContainer.setOnScrollListener(new CustomScrollView.OnScrollListener() {
			@Override
			public void onScroll(int direction, float scrollY) {
				if(svContainerState == SCROLL_STATE_FLING || svContainerState == SCROLL_STATE_IDLE){
					if(scrollY >= CHANGE_Y_END) {
						// 상단바에 프로필 생성
						titleCompanyPicture.setVisibility(View.VISIBLE);
						titleCompanyName.setVisibility(View.VISIBLE);
						llTitleBottomBar.setVisibility(View.VISIBLE);
						titleCompanyPicture.getDrawable().setAlpha(255);
						titleCompanyName.setTextColor(Color.argb(255, 51, 51, 51));
					} else if(scrollY < CHANGE_Y_START) {
						// 상단바에 프로필 제거
						titleCompanyPicture.setVisibility(View.INVISIBLE);
						titleCompanyName.setVisibility(View.INVISIBLE);
						llTitleBottomBar.setVisibility(View.INVISIBLE);
						titleCompanyPicture.getDrawable().setAlpha(0);
						titleCompanyName.setTextColor(Color.argb(0, 51, 51, 51));
					} else {
						titleCompanyPicture.setVisibility(View.VISIBLE);
						titleCompanyName.setVisibility(View.VISIBLE);
						llTitleBottomBar.setVisibility(View.VISIBLE);
						int alphaColor = (int) (scrollY - CHANGE_Y_START) / 2;
						titleCompanyPicture.getDrawable().setAlpha(alphaColor);
						titleCompanyName.setTextColor(Color.argb(alphaColor, 51, 51, 51));
					}
				}
			}
		});
	}

	public void getCompany(final int companyId) {
		LazyRequestService service = new LazyRequestService(getActivity(), CompanyNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<CompanyNetworkService>() {
			@Override
			public Call<ResponseVO> execute(CompanyNetworkService companyNetworkService, String token) {
				return companyNetworkService.profile(token, companyId);
			}
		});
		service.enqueue(new CustomCallback(CompanyActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				companyVO = responseVO.getValue("companyInfoDTO", CompanyVO.class);
				setCompanyInfo();
			}
		});
	}

	private void setCompanyInfo() {
		BitmapUIL.load(companyVO.getPictureUrl(), companyPicture);
		companyName.setText(companyVO.getCompanyAlias());
		companyComment.setText(companyVO.getComment());
		BitmapUIL.load(companyVO.getPictureUrl(), titleCompanyPicture);
		titleCompanyName.setText(companyVO.getCompanyAlias());
		if(companyVO.getBlocked()) {
			btnDropDownBlock.setText("차단 해제하기");
		} else {
			btnDropDownBlock.setText("차단하기");
		}
		setLike();
	}

	private void setLike() {
		if(companyVO.isLiked()) {
			btnLike.setText("애독중");
			btnLike.setTextColor(getResources().getColor(R.color.colorWhite));
			btnLike.setBackgroundResource(R.drawable.button_like_on);
		} else {
			btnLike.setText("애독하기");
			btnLike.setTextColor(getResources().getColor(R.color.colorPuzi));
			btnLike.setBackgroundResource(R.drawable.button_like_off);
		}
	}

	private void getChannelList(final boolean scrollToBottom) {
		companyChannelAdapter.startProgress();

		if(scrollToBottom) {
			svContainer.post(new Runnable() {
				@Override
				public void run() {
					svContainer.fullScroll(View.FOCUS_DOWN);
				}
			});
		}

		LazyRequestService service = new LazyRequestService(getActivity(), CompanyNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<CompanyNetworkService>() {
			@Override
			public Call<ResponseVO> execute(CompanyNetworkService companyNetworkService, String token) {
				return companyNetworkService.channelList(token, companyVO.getCompanyId(), pagingIndex);
			}
		});
		service.enqueue(new CustomCallback(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				companyChannelAdapter.stopProgress();

				List<ChannelVO> newChannelList = responseVO.getList("channelDTOList", ChannelVO.class);
				companyChannelAdapter.addList(newChannelList);
				setListViewHeightBasedOnChildren(lvChannelList);

				totalCount = responseVO.getInteger("totalCount");
				if(totalCount == companyChannelAdapter.getCount()) {
					more = false;
				} else {
					more = true;
				}

				if(!scrollToBottom) {
					svContainer.post(new Runnable() {
						@Override
						public void run() {
							svContainer.fullScroll(View.FOCUS_UP);
						}
					});
				}
			}
		});
	}

	public void updateBlock(final boolean isBlock) {
		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.blockCompany(token, isBlock, companyVO.getCompanyId());
			}
		});
		service.enqueue(new CustomCallback(CompanyActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("INFO", "company block success.");
				if(companyVO.getBlocked()) {
					companyVO.setBlocked(false);
					btnDropDownBlock.setText("차단하기");
					Toast.makeText(getBaseContext(), "성공적으로 해제되었습니다.", Toast.LENGTH_SHORT).show();
				} else {
					companyVO.setBlocked(true);
					btnDropDownBlock.setText("차단 해제하기");
					Toast.makeText(getBaseContext(), "성공적으로 차단되었습니다.", Toast.LENGTH_SHORT).show();
				}
				showDropdown();
			}
		});
	}

	@OnClick(R.id.btn_profile_channel_dropdown_block)
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

	@OnClick(R.id.btn_block)
	public void showDropdown() {
		if(llDropDownContainer.getVisibility() == View.INVISIBLE) {
			llDropDownContainer.setVisibility(View.VISIBLE);
			llDropDownContainer.startAnimation(inFromTop());
		} else {
			llDropDownContainer.setVisibility(View.INVISIBLE);
			llDropDownContainer.startAnimation(outToTop());
		}
	}

	@OnClick(R.id.ibtn_like)
	public void clickLike() {
		final boolean add = !companyVO.isLiked();

		ProgressDialog.show(this);

		LazyRequestService service = new LazyRequestService(getActivity(), CompanyNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<CompanyNetworkService>() {
			@Override
			public Call<ResponseVO> execute(CompanyNetworkService companyNetworkService, String token) {
				return companyNetworkService.like(token, add, companyVO.getCompanyId());
			}
		});
		service.enqueue(new CustomCallback(this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				companyVO.setLiked(add);
				setLike();
				String message = add ? "애독자로 추가되었습니다." : "애독자가 해제되었습니다.";
				Toast.makeText(CompanyActivity.this, message, Toast.LENGTH_SHORT).show();
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

	/**
	 * 스크롤 뷰 안에 리스트뷰를 넣을 때 height문제로 인해서
	 * notifyDataSetChanged()를 호출 할 때 이 메소드를 같이 호출해 줘야 한다.
	 * @param listView
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

}

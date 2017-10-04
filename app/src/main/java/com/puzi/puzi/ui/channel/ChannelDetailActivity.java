package com.puzi.puzi.ui.channel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.*;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.biz.channel.ChannelReplyVO;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.ChannelNetworkService;
import com.puzi.puzi.ui.HorizontalListView;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.advertisement.AdvertisementDetailActivity;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.channel.editorspage.EditorsPageActivity;
import com.puzi.puzi.ui.channel.editorspage.EditorsPageAdapter;
import com.puzi.puzi.ui.channel.reply.ReplyListAdapter;
import com.puzi.puzi.utils.UIUtils;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class ChannelDetailActivity extends BaseActivity {

	private Unbinder unbinder;

	@BindView(R.id.ibtn_chanenl_detail_back)
	ImageButton ibtnBack;
	@BindView(R.id.ibtn_chanenl_detail_company_image)
	SelectableRoundedImageView ibtnCompanyImage;
	@BindView(R.id.ibtn_chanenl_detail_picture)
	ImageButton ibtnAdvertisementPicture;
	@BindView(R.id.tv_channel_detail_company_name)
	TextView tvCompanyName;
	@BindView(R.id.tv_channel_detail_comment)
	TextView tvComment;
	@BindView(R.id.et_channel_detail_write_reply)
	EditText etWriteReply;
	@BindView(R.id.lv_channel_detail_reply)
	ListView lvReply;
	@BindView(R.id.tv_channel_detail_reply_title)
	TextView tvReplyTitle;
	@BindView(R.id.sv_channel_detail)
	ScrollView svContainer;
	@BindView(R.id.btn_channel_detail_write)
	Button btnWrite;
	@BindView(R.id.tv_channel_detail_evaluate_average)
	TextView tvEvaluateAverage;
	@BindView(R.id.iv_evaluate_star_1)
	ImageView ivEvaluateStar1;
	@BindView(R.id.iv_evaluate_star_2)
	ImageView ivEvaluateStar2;
	@BindView(R.id.iv_evaluate_star_3)
	ImageView ivEvaluateStar3;
	@BindView(R.id.iv_evaluate_star_4)
	ImageView ivEvaluateStar4;
	@BindView(R.id.iv_evaluate_star_5)
	ImageView ivEvaluateStar5;
	@BindView(R.id.hlv_channel_detail_editors_page)
	HorizontalListView hlvEditorsPage;

	private EditorsPageAdapter editorsPageAdapter;
	private ReplyListAdapter replyListAdapter;

	private ChannelVO channelVO;
	private boolean more = false;
	private int pagingIndex = 1;
	private int totalCount = 0;

	private ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_channel_detail);

		unbinder = ButterKnife.bind(this);

		channelVO = (ChannelVO) getIntent().getSerializableExtra("channelVO");
		if(channelVO == null) {
			getChannelDetail(getIntent().getIntExtra("channelId", 1));
		} else {
			initAll();
		}
	}

	private void initAll() {
		initChannel();
		initScrollAction();
		getEditorsPagetList();
		getChannelReplyList(false);
	}

	private void getChannelDetail(int channelId) {
		ProgressDialog.show(this);
		String token = Preference.getProperty(this, "token");
		Call<ResponseVO> call = channelNetworkService.channelDetail(token, channelId);
		call.enqueue(new CustomCallback<ResponseVO>(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				ProgressDialog.dismiss();
				if(responseVO.getResultType().isSuccess()) {
					channelVO = responseVO.getValue("channelDTO", ChannelVO.class);
					initAll();
				}
			}
		});
	}

	private void initChannel() {
		// 에디터스페이지 어뎁터
		editorsPageAdapter = new EditorsPageAdapter(this);
		hlvEditorsPage.setAdapter(editorsPageAdapter);
		// 댓글 어뎁터
		replyListAdapter = new ReplyListAdapter(this);
		lvReply.setAdapter(replyListAdapter);
		setListViewHeightBasedOnChildren(lvReply);
		// 기본 채널 정보
		BitmapUIL.load(channelVO.getPictureUrl(), ibtnAdvertisementPicture);
		BitmapUIL.load(channelVO.getCompanyInfoDTO().getPictureUrl(), ibtnCompanyImage);
		tvCompanyName.setText(channelVO.getCompanyInfoDTO().getCompanyAlias());
		tvComment.setText(channelVO.getComment());
		tvEvaluateAverage.setText(channelVO.getAverageScore() + " / 5");
		UIUtils.setEvaluateStarScoreImage(channelVO.getAverageScore(),
			ivEvaluateStar1, ivEvaluateStar2, ivEvaluateStar3, ivEvaluateStar4, ivEvaluateStar5);
	}

	private void initScrollAction() {
		svContainer.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				int scrollViewPos = svContainer.getScrollY();
				int TextView_lines = svContainer.getChildAt(0).getBottom() - svContainer.getHeight();
				if(TextView_lines == scrollViewPos){
					if(more && !replyListAdapter.isProgressed()) {
						pagingIndex = pagingIndex + 1;
						getChannelReplyList(true);
					}
				}
			}
		});
	}

	private void getEditorsPagetList() {
		editorsPageAdapter.startProgress();

		String token = Preference.getProperty(this, "token");
		Call<ResponseVO> call = channelNetworkService.channelDetailEditorsPageList(token, channelVO.getChannelId());
		call.enqueue(new CustomCallback<ResponseVO>(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				editorsPageAdapter.stopProgress();

				if(responseVO.getResultType().isSuccess()) {
					List<ChannelEditorsPageVO> list = responseVO.getList("channelEditorsPageDTOList", ChannelEditorsPageVO.class);
					if(list.size() > 0) {
						editorsPageAdapter.add(list);
						editorsPageAdapter.notifyDataSetChanged();
						return;
					}
				}
				hlvEditorsPage.setVisibility(View.GONE);
			}
		});
	}

	private void getChannelReplyList(boolean scrollToBottom) {
		replyListAdapter.startProgress();
		setListViewHeightBasedOnChildren(lvReply);
		if(scrollToBottom) {
			svContainer.post(new Runnable() {
				@Override
				public void run() {
					svContainer.fullScroll(View.FOCUS_DOWN);
				}
			});
		}

		String token = Preference.getProperty(this, "token");
		Call<ResponseVO> call = channelNetworkService.replyList(token, channelVO.getChannelId(), pagingIndex);
		call.enqueue(new CustomCallback<ResponseVO>(this) {

			@Override
			public void onSuccess(ResponseVO responseVO) {
				replyListAdapter.stopProgress();
				setListViewHeightBasedOnChildren(lvReply);

				if(responseVO.getResultType().isSuccess()) {
					totalCount = responseVO.getInteger("totalCount");
					tvReplyTitle.setText("댓글 " + totalCount);

					if(totalCount == 0) {
						replyListAdapter.empty();
						more = false;
						return;
					}

					List<ChannelReplyVO> channelReplyList = responseVO.getList("channelReplyDTOList", ChannelReplyVO.class);
					replyListAdapter.addReplyList(channelReplyList);
					replyListAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(lvReply);

					if(replyListAdapter.getCount() == totalCount) {
						more = false;
						return;
					}
					more = true;
				}
			}
		});
	}

	@OnClick(R.id.ibtn_chanenl_detail_back)
	public void backPress() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

	@OnClick(R.id.btn_channel_detail_evaluate)
	public void evaluate() {
		if(channelVO.isScored()){
			Toast.makeText(this, "이미 평가하셨습니다.", Toast.LENGTH_SHORT).show();
			return;
		}
		ChannelEvaluateDialog.load(this, channelVO.getChannelId(), new EvaluateListener() {

			@Override
			public void success() {
				channelVO.setScored(true);
			}
		});
	}

	@OnClick(R.id.ibtn_chanenl_detail_picture)
	public void clickAdvertisement(View v) {
		ReceivedAdvertiseVO receivedAdvertiseVO = new ReceivedAdvertiseVO();
		receivedAdvertiseVO.setChannelId(channelVO.getChannelId());
		receivedAdvertiseVO.setSaved(false);
		receivedAdvertiseVO.setNew(false);
		receivedAdvertiseVO.setLink(channelVO.getLink());
		receivedAdvertiseVO.setCompanyInfoDTO(channelVO.getCompanyInfoDTO());

		Intent intent = new Intent(this, AdvertisementDetailActivity.class);
		intent.putExtra("advertise", receivedAdvertiseVO);
		startActivity(intent);
	}

	@OnClick(R.id.btn_channel_detail_write)
	public void write() {
		String replyText = etWriteReply.getText().toString().replaceAll(" ", "");
		if(replyText != null && replyText.length() != 0) {
			btnWrite.setEnabled(false);
			String token = Preference.getProperty(this, "token");
			Call<ResponseVO> call = channelNetworkService.replyWrite(token, channelVO.getChannelId(), replyText);
			call.enqueue(new CustomCallback<ResponseVO>(this) {

				@Override
				public void onSuccess(ResponseVO responseVO) {
					btnWrite.setEnabled(true);
					if(responseVO.getResultType().isSuccess()) {
						// 초기화
						etWriteReply.setText("");
						// 키보드닫기
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(etWriteReply.getWindowToken(), 0);
						// 댓글 추가
						ChannelReplyVO newReply = responseVO.getValue("channelReplyDTO", ChannelReplyVO.class);
						replyListAdapter.addReplyFirst(newReply);
						replyListAdapter.notifyDataSetChanged();
						setListViewHeightBasedOnChildren(lvReply);
						// 스크롤 처음으로
						int x = lvReply.getLeft();
						int y = lvReply.getTop();
						svContainer.smoothScrollTo(x, y);
						lvReply.smoothScrollToPosition(0);
						// 카운트 추가
						totalCount = totalCount + 1;
						tvReplyTitle.setText("댓글 " + totalCount);
					}
				}
			});
		}
	}

	@OnItemClick(R.id.hlv_channel_detail_editors_page)
	public void editorsPageItemClick(AdapterView<?> parent, View view, int position, long id) {
		ChannelEditorsPageVO channelEditorsPageVO = (ChannelEditorsPageVO) editorsPageAdapter.getItem(position);
		Intent intent = new Intent(this, EditorsPageActivity.class);
		intent.putExtra("channelEditorsPageVO", channelEditorsPageVO);
		startActivity(intent);
		doAnimationGoRight();
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

	public interface EvaluateListener {
		public void success();
	}

}

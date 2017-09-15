package com.puzi.puzi.ui.channel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.puzi.puzi.R;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.biz.channel.ChannelReplyVO;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResultType;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.ChannelNetworkService;
import com.puzi.puzi.ui.channel.reply.ReplyListAdapter;
import com.puzi.puzi.cache.Preference;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-08-06.
 */

public class ChannelDetailActivity extends Activity implements TextView.OnEditorActionListener, View.OnClickListener, AbsListView.OnScrollListener {

	private EditText editReply;
	private TextView tvComment;
	private ImageButton btnBack;
	private Button btnReply;
	private ImageView ivAdvertise;
	private ListView lvReply;
	private ReplyListAdapter replyListAdapter;
	private boolean recommend;

	private int channelId, channelReplyId;
	private ChannelVO channel;
	private ChannelReplyVO channelReplyVO;
	private List<ChannelReplyVO> channelReplyVOList;

	private ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_channel_detail);
		initComponent();

		Intent intent = getIntent();
		channelId = intent.getIntExtra("Id", 0);
		Log.i("DEBUG", "Channel Detail Activity / channel Id : " + channelId);

		getChannelDetail();
		getChannelReply();

		lvReply.setOnScrollListener(this);
	}

	public void getChannelReply() {

		final ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);
		final String token = Preference.getProperty(this, "token");

		Log.i("DEBUG", "Channel Reply List");

		Call<ResponseVO<List<ChannelReplyVO>>> call = channelNetworkService.replyList(token, channelId, 1);
		call.enqueue(new CustomCallback<ResponseVO<List<ChannelReplyVO>>>(this) {
			@Override
			public void onSuccess(ResponseVO<List<ChannelReplyVO>> responseVO) {
				ResultType resultType = responseVO.getResultType();

				if (resultType.isSuccess()) {

					channelReplyVOList = responseVO.getValue("channelReplyList");
					replyListAdapter = new ReplyListAdapter(ChannelDetailActivity.this, channelReplyVOList, channelId, token);
					lvReply.setAdapter(replyListAdapter);
					setListViewHeightBasedOnChildren(lvReply, replyListAdapter);
				}
			}
		});
	}

	public void getChannelDetail() {

		final ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);

		String token = Preference.getProperty(this, "token");

		Log.i("DEBUG", "Channel Detail");

		Call<ResponseVO<ChannelVO>> call = channelNetworkService.channelDetail(token, channelId);
		call.enqueue(new CustomCallback<ResponseVO<ChannelVO>>(this) {
			@Override
			public void onSuccess(ResponseVO<ChannelVO> responseVO) {
				ResultType resultType = responseVO.getResultType();

				if (resultType.isSuccess()) {
					Log.i("DEBUG", "Channel Detail Activity / Success");
					Log.i("DEBUG", "Channel Detail Activity / response : " + responseVO);

					channel = responseVO.getValue("channel");
					Log.i("DEBUG", "Channel Detail Activity / channel : " + channel);
					tvComment.setText(channel.getComment());
					BitmapUIL.load(channel.getPictureUrl(), ivAdvertise);
				}
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
	}

	private void initComponent() {

		tvComment = (TextView) findViewById(R.id.tv_channelPageComment);
		ivAdvertise = (ImageView) findViewById(R.id.iv_channelPageImage);
		lvReply = (ListView) findViewById(R.id.lv_channelReply);

		//btnGood.setOnClickListener(this);
		//btnBad.setOnClickListener(this);

		btnReply = (Button) findViewById(R.id.btn_channelReply);
		btnReply.setOnClickListener(this);

		btnBack = (ImageButton) findViewById(R.id.ibtn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		editReply = (EditText) findViewById(R.id.et_channelReply);
		editReply.setOnEditorActionListener(this);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		return false;
	}

	@Override
	public void onClick(View v) {

		final String reply = editReply.getText().toString();

		switch (v.getId()) {
			case R.id.btn_channelReply:
				if(reply.isEmpty()) {
					Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
				} else {

					final String token = Preference.getProperty(this, "token");

					Call<ResponseVO<Integer>> call = channelNetworkService.replyWrite(token, channelId, reply);
					call.enqueue(new CustomCallback<ResponseVO<Integer>>(this) {
						@Override
						public void onSuccess(ResponseVO<Integer> responseVO) {
							ResultType resultType = responseVO.getResultType();

							if (resultType.isSuccess()) {

								Log.i("DEBUG", "responseVO : " + responseVO);

								channelReplyVO = new ChannelReplyVO();

								channelReplyVO.setChannelReplyId(responseVO.getValue("channelReplyId"));
								channelReplyVO.setComment(reply);

								channelReplyVOList.add(channelReplyVO);
								replyListAdapter = new ReplyListAdapter(ChannelDetailActivity.this, channelReplyVOList, channelId, token);
								lvReply.setAdapter(replyListAdapter);
								setListViewHeightBasedOnChildren(lvReply, replyListAdapter);
								editReply.setText("");
							}
						}
					});
				}
				break;

			case R.id.btn_good:

				/*String token = PreferenceUtil.getProperty(this, "token");

				Call<ResponseVO> call = channelService.replyRecommend(token, channelId, channelReplyId, recommend);
				call.enqueue(new CustomCallback<ResponseVO>() {
					@Override
					public void onResponse(ResponseVO responseVO) {
						int resultCode = responseVO.getResultCode();

						if (resultCode == ResultCode.SUCCESS) {

							Log.i("DEBUG", "responseVO : " + responseVO);

						}
					}

					@Override
					public void onFailure(PuziNetworkException e) {
						Log.e("TAG", "통신 오류(" + e.getCode() + ")", e);
					}
				});*/


				break;

			case R.id.btn_bad:

				break;
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView, ReplyListAdapter replyListAdapter) {
		// ListAdapter listAdapter = listView.getAdapter();
		if (replyListAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;

		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

		Log.i("DEBUG", "desiredWidth : " + desiredWidth);
		Log.i("DEBUG", "replyListAdapter.getCount() : " + replyListAdapter.getCount());

		for (int i = 0; i < replyListAdapter.getCount(); i++) {
			View listItem = replyListAdapter.getView(i, null, listView);
			//listItem.measure(0, 0);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();

		Log.i("DEBUG", "totalHeight : " + totalHeight);
		params.height = totalHeight;
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}

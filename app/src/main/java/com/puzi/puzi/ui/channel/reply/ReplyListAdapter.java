package com.puzi.puzi.ui.channel.reply;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelReplyVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.ChannelNetworkService;

import java.util.List;

/**
 * Created by muoe0 on 2017-08-13.
 */

public class ReplyListAdapter extends BaseAdapter implements View.OnClickListener {

	private TextView tvId, tvComment, tvDate;
	private List<ChannelReplyVO> replyList;
	private ChannelReplyVO channelReplyVO;
	private LayoutInflater inflater;
	private Activity activity;
	private Button btnGood, btnBad;
	private TextView tvGood, tvBad;
	private FrameLayout flGood, flBad;
	private ImageView ivGood, ivBad;
	private String token;
	private int channelId, goodCount = 100, badCount = 50;
	private boolean flag = false;

	public ReplyListAdapter(Activity activity, List<ChannelReplyVO> list, int channelId, String token) {
		this.activity = activity;
		this.replyList = list;
		this.channelId = channelId;
		this.token = token;

		Log.i("DEBUG", "ReplyListAdapter replyList : " + replyList);
	}

	public void initComponents(View view) {
		tvId = (TextView) view.findViewById(R.id.tv_reply_id);
		tvComment = (TextView) view.findViewById(R.id.tv_reply_comment);
		tvDate = (TextView) view.findViewById(R.id.tv_reply_date);

		tvGood = (TextView) view.findViewById(R.id.tv_good);
		tvBad = (TextView) view.findViewById(R.id.tv_bad);
		ivGood = (ImageView) view.findViewById(R.id.iv_good);
		ivBad = (ImageView) view.findViewById(R.id.iv_bad);
		flGood = (FrameLayout) view.findViewById(R.id.fl_good);
		flBad = (FrameLayout) view.findViewById(R.id.fl_bad);

		btnGood = (Button) view.findViewById(R.id.btn_good);
		btnBad = (Button) view.findViewById(R.id.btn_bad);
	}

	@Override
	public int getCount() {
		return replyList.size();
	}

	@Override
	public Object getItem(int position) {
		return replyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_reply_item, parent, false);
		}

		initComponents(convertView);

		channelReplyVO = replyList.get(position);

		Log.i("DEBUG", "channelReply : " + channelReplyVO);

		tvId.setText("" + channelReplyVO.getChannelReplyId());
		tvComment.setText("" + channelReplyVO.getComment());
		tvGood.setText("" + goodCount);
		tvBad.setText("" + badCount);

		btnGood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(flag) {
					Toast.makeText(v.getContext(), "이미 평가하셨습니다.", Toast.LENGTH_SHORT).show();
				} else {
					flag = true;
					flGood.setBackgroundResource(R.drawable.goodbad_line_red);
					ivGood.setBackgroundResource(R.drawable.like_icon_red);
					tvGood.setTextColor(Color.parseColor("#f13d3a"));
					tvGood.setText("" + ++goodCount);
					getChannelReply(token, channelId, channelReplyVO.getChannelReplyId(), true);
				}
			}
		});

		btnBad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(flag) {
					Toast.makeText(v.getContext(), "이미 평가하셨습니다.", Toast.LENGTH_SHORT).show();
				} else {
					flag = true;
					flBad.setBackgroundResource(R.drawable.goodbad_line_red);
					ivBad.setBackgroundResource(R.drawable.dislike_icon_red);
					tvBad.setTextColor(Color.parseColor("#f13d3a"));
					tvBad.setText("" + ++badCount);
					getChannelReply(token, channelId, channelReplyVO.getChannelReplyId(), false);
				}
			}
		});

		return convertView;
	}

	public void getChannelReply(String token, int channelId, int channelReplyId, boolean recommend) {

		final ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);

		Log.i("DEBUG", "Channel Reply List");

//		Call<ResponseVO> call = channelNetworkService.replyRecommend(token, channelId, channelReplyId, recommend);
//		call.enqueue(new CustomCallback<ResponseVO>(activity) {
//			@Override
//			public void onSuccess(ResponseVO responseVO) {
//				ResultType resultType = responseVO.getResultType();
//
//				if (resultType.isSuccess()) {
//
//				}
//			}
//		});
	}

	@Override
	public void onClick(View v) {

	}
}

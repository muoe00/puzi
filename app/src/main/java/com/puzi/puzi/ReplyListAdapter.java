package com.puzi.puzi;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.puzi.puzi.model.ChannelReply;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.PuziNetworkException;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.service.ChannelService;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-08-13.
 */

public class ReplyListAdapter extends BaseAdapter implements View.OnClickListener {

	private TextView tvId, tvComment, tvDate;
	private List<ChannelReply> replyList;
	private ChannelReply channelReply;
	private LayoutInflater inflater;
	private Context context;
	private Button btnGood, btnBad;
	private TextView tvGood, tvBad;
	private FrameLayout flGood, flBad;
	private ImageView ivGood, ivBad;
	private String token;
	private int channelId, goodCount = 100, badCount = 50;
	private boolean flag = false;

	public ReplyListAdapter(Context context, List<ChannelReply> list, int channelId, String token) {
		this.context = context;
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
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_reply_item, parent, false);
		}

		initComponents(convertView);

		channelReply = replyList.get(position);

		Log.i("DEBUG", "channelReply : " + channelReply);

		tvId.setText("" + channelReply.getChannelReplyId());
		tvComment.setText("" + channelReply.getComment());
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
					getChannelReply(token, channelId, channelReply.getChannelReplyId(), true);
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
					getChannelReply(token, channelId, channelReply.getChannelReplyId(), false);
				}
			}
		});

		return convertView;
	}

	public void getChannelReply(String token, int channelId, int channelReplyId, boolean recommend) {

		final ChannelService channelService = RetrofitManager.create(ChannelService.class);

		Log.i("DEBUG", "Channel Reply List");

		Call<ResponseVO> call = channelService.replyRecommend(token, channelId, channelReplyId, recommend);
		call.enqueue(new CustomCallback<ResponseVO>() {
			@Override
			public void onResponse(ResponseVO responseVO) {
				int resultCode = responseVO.getResultCode();

				if (resultCode == ResultCode.SUCCESS) {

				}
			}

			@Override
			public void onFailure(PuziNetworkException e) {
				Log.e("TAG", "통신 오류(" + e.getCode() + ")", e);
			}
		});
	}

	@Override
	public void onClick(View v) {

	}
}

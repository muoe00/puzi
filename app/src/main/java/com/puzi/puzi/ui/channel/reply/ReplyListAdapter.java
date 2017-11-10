package com.puzi.puzi.ui.channel.reply;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelReplyVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.ChannelNetworkService;
import lombok.Getter;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muoe0 on 2017-08-13.
 */

public class ReplyListAdapter extends BaseAdapter {

	private static final int VIEW_CHANNEL_REPLY = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private LayoutInflater inflater;
	private Activity activity;
	private List<ChannelReplyVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public ReplyListAdapter(Activity activity) {
		this.inflater = activity.getLayoutInflater();
		this.activity = activity;
	}

	public void addReplyFirst(ChannelReplyVO channelReplyVO) {
		empty = false;
		list.add(0, channelReplyVO);
	}

	public void addReplyList(List<ChannelReplyVO> channelReplyVOList) {
		if(empty && channelReplyVOList.size() > 0) {
			empty = false;
		}
		list.addAll(channelReplyVOList);
	}

	public void startProgress() {
		if(!progressed) {
			progressed = true;
			notifyDataSetChanged();
		}
	}

	public void stopProgress() {
		if(progressed) {
			progressed = false;
			notifyDataSetChanged();
		}
	}

	public void empty() {
		if(!empty){
			empty = true;
		}
	}

	@Override
	public int getCount() {
		if(empty) {
			return 1;
		}
		if(progressed) {
			return list.size() + 1;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getViewTypeCount() {
		return 3;
	}

	public int getItemViewType(int position) {
		if(empty) {
			return VIEW_EMPTY;
		}
		if(progressed) {
			if(getCount() - 1 == position) {
				return VIEW_PROGRESS;
			}
		}
		return VIEW_CHANNEL_REPLY;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder viewHolder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case VIEW_CHANNEL_REPLY:
					v = inflater.inflate(R.layout.item_channel_detail_reply, null);
					viewHolder = new ViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_reply, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_CHANNEL_REPLY:
				final ChannelReplyVO channelReplyVO = (ChannelReplyVO) getItem(position);
				viewHolder.tvReplyId.setText(channelReplyVO.getWriter());
				viewHolder.tvReplyComment.setText(channelReplyVO.getComment());
				viewHolder.tvRecommend.setText(""+channelReplyVO.getRecommend());
				viewHolder.tvReverse.setText(""+channelReplyVO.getReverse());

				if(channelReplyVO.isScored()) {
					// 선택한 댓글에 on
					recommendUI(viewHolder, channelReplyVO, !channelReplyVO.isRecommended(), false);
					recommendUI(viewHolder, channelReplyVO, channelReplyVO.isRecommended(), true);
				} else {
					// 추천, 반대 둘다 off
					recommendUI(viewHolder, channelReplyVO, true, false);
					recommendUI(viewHolder, channelReplyVO, false, false);
				}
				break;
		}

		return v;
	}

	private void requestRecommendOrReverse(final ViewHolder viewHolder, final ChannelReplyVO channelReplyVO, final boolean recommend) {
		// 미리 +카운트하고 서버에 요청하기
		if(recommend) {
			channelReplyVO.setRecommend(channelReplyVO.getRecommend() + 1);
			viewHolder.tvRecommend.setText(""+channelReplyVO.getRecommend());
		} else {
			channelReplyVO.setReverse(channelReplyVO.getReverse() + 1);
			viewHolder.tvReverse.setText(""+channelReplyVO.getReverse());
		}
		recommendUI(viewHolder, channelReplyVO, !recommend, false);
		recommendUI(viewHolder, channelReplyVO, recommend, true);

		String token = Preference.getProperty(activity, "token");
		ChannelNetworkService channelNetworkService = RetrofitManager.create(ChannelNetworkService.class);
		Call<ResponseVO> call = channelNetworkService.replyRecommend(token, channelReplyVO.getChannelId(), channelReplyVO.getChannelReplyId(),
			recommend);
		call.enqueue(new CustomCallback(activity) {

			@Override
			public void onSuccess(ResponseVO responseVO) {

				// 실패했을 경우 미리 +카운트해놓은 것 원복
				if(!responseVO.getResultType().isSuccess()) {
					if(recommend) {
						channelReplyVO.setRecommend(channelReplyVO.getRecommend() - 1);
						viewHolder.tvRecommend.setText(""+channelReplyVO.getRecommend());
					} else {
						channelReplyVO.setReverse(channelReplyVO.getReverse() - 1);
						viewHolder.tvReverse.setText(""+channelReplyVO.getReverse());
					}
					recommendUI(viewHolder, channelReplyVO, recommend, false);
					recommendUI(viewHolder, channelReplyVO, !recommend, true);
				}
			}
		});
	}

	private void recommendUI(ViewHolder viewHolder, ChannelReplyVO channelReplyVO, boolean recommend, boolean on) {
		if(recommend) {
			if(on) {
				viewHolder.ivRecommend.setImageResource(R.drawable.thumbs_up_copy);
				viewHolder.tvRecommend.setTextColor(Color.parseColor("#ff2470"));
				viewHolder.btnRecommend.setBackgroundResource(R.drawable.button_reply_on);
				viewHolder.btnReverse.setBackgroundResource(R.drawable.button_reply_off);
				viewHolder.btnRecommend.setEnabled(false);
				viewHolder.btnReverse.setEnabled(false);
			} else {
				viewHolder.ivRecommend.setImageResource(R.drawable.thumbs_up);
				viewHolder.tvRecommend.setTextColor(Color.parseColor("#9b9b9b"));
				viewHolder.btnRecommend.setBackgroundResource(R.drawable.button_reply_off);
				viewHolder.btnReverse.setBackgroundResource(R.drawable.button_reply_off);
				viewHolder.btnRecommend.setOnClickListener(recommendListener(viewHolder, channelReplyVO));
				viewHolder.btnRecommend.setEnabled(true);
			}
		} else {
			if(on) {
				viewHolder.ivReverse.setImageResource(R.drawable.thumbs_down);
				viewHolder.tvReverse.setTextColor(Color.parseColor("#ff2470"));
				viewHolder.btnRecommend.setBackgroundResource(R.drawable.button_reply_off);
				viewHolder.btnReverse.setBackgroundResource(R.drawable.button_reply_on);
				viewHolder.btnReverse.setEnabled(false);
				viewHolder.btnRecommend.setEnabled(false);
			} else {
				viewHolder.ivReverse.setImageResource(R.drawable.thumbs_down_copy);
				viewHolder.tvReverse.setTextColor(Color.parseColor("#9b9b9b"));
				viewHolder.btnRecommend.setBackgroundResource(R.drawable.button_reply_off);
				viewHolder.btnReverse.setBackgroundResource(R.drawable.button_reply_off);
				viewHolder.btnReverse.setOnClickListener(reverseListener(viewHolder, channelReplyVO));
				viewHolder.btnReverse.setEnabled(true);
			}
		}
	}

	public View.OnClickListener recommendListener(final ViewHolder viewHolder, final ChannelReplyVO channelReplyVO) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				requestRecommendOrReverse(viewHolder, channelReplyVO, true);
			}
		};
	}

	public View.OnClickListener reverseListener(final ViewHolder viewHolder, final ChannelReplyVO channelReplyVO) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				requestRecommendOrReverse(viewHolder, channelReplyVO, false);
			}
		};
	}

	public class ViewHolder {
		@BindView(R.id.tv_channel_detail_reply_id) public TextView tvReplyId;
		@BindView(R.id.tv_channel_detail_reply_comment) public TextView tvReplyComment;
		@BindView(R.id.iv_channel_detail_reply_recommend) public ImageView ivRecommend;
		@BindView(R.id.tv_channel_detail_reply_recommend) public TextView tvRecommend;
		@BindView(R.id.btn_channel_detail_reply_recommend) public Button btnRecommend;
		@BindView(R.id.iv_channel_detail_reply_reverse) public ImageView ivReverse;
		@BindView(R.id.tv_channel_detail_reply_reverse) public TextView tvReverse;
		@BindView(R.id.btn_channel_detail_reply_reverse) public Button btnReverse;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}

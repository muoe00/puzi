package com.puzi.puzi.ui.today;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.ui.customview.NotoTextView;

import static com.puzi.puzi.biz.myservice.ViewType.BONUS;
import static com.puzi.puzi.biz.myservice.ViewType.INIT;
import static com.puzi.puzi.biz.myservice.ViewType.REMAIN;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

	private int state = 1, hour, minute, second;
    private MyTodayQuestionVO myTodayQuestionVO;
    private QuestionFragment fragment;
	private Context context;

	public TodayAdapter(Context context, QuestionFragment fragment) {
		this.context = context;
		this.fragment = fragment;
	}

	public void setTime(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public void setMyTodayQuestionVO(MyTodayQuestionVO myTodayQuestionVO) {
		this.myTodayQuestionVO = myTodayQuestionVO;

		Log.i("TodayAdapter", "myTodayQuestionVO : " + myTodayQuestionVO.toString());
	}

	public void changedState(int state) {
		this.state = state;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view;

		Log.i("TodayAdapter", "state : " + state);

		if(state == INIT.getIndex()) {
			Log.i("TodayAdapter", "index : " + INIT.getIndex());
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_init, parent, false);
		} else if(state == BONUS.getIndex()) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_bonus, parent, false);
		} else if(state == REMAIN.getIndex()){
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_time, parent, false);
		} else {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_end, parent, false);
		}

		ViewHolder viewHolder = new ViewHolder(view, state);

		return viewHolder;
	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		if(state == REMAIN.getIndex()) {

			if(hour == 0 && minute == 0) {
				holder.tvHour.setVisibility(View.GONE);
				holder.tvHourString.setVisibility(View.GONE);
				holder.tvMinute.setText("" + second);
				holder.tvMinuteString.setText("초");
			} else {
				if(hour == 0) {
					holder.tvHour.setVisibility(View.GONE);
					holder.tvHourString.setVisibility(View.GONE);
				} else if(minute == 0) {
					holder.tvMinute.setVisibility(View.GONE);
					holder.tvMinuteString.setVisibility(View.GONE);
				} else {
					holder.tvHour.setText("" + hour);
					holder.tvMinute.setText("" + minute);
				}
			}
			notifyDataSetChanged();

		} else if(state == INIT.getIndex() || state == BONUS.getIndex()) {
			holder.tvPlusPoint.setText("" + myTodayQuestionVO.getSavePoint());
			holder.button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.i("TodayAdapter", "onClick");
					Intent intent = new Intent(context, QuestionActivity.class);
					intent.putExtra("questionList", myTodayQuestionVO);
					context.startActivity(intent);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return 1;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public NotoTextView tvPlusPoint;
		public NotoTextView tvHour;
		public NotoTextView tvMinute;
		public NotoTextView tvHourString;
		public NotoTextView tvMinuteString;
		public Button button;

		public ViewHolder(View view, int index) {
			super(view);

			if(index == INIT.getIndex() || index == BONUS.getIndex()) {
				tvPlusPoint = (NotoTextView) view.findViewById(R.id.tv_question_plus_point);
				button = (Button) view.findViewById(R.id.btn_rl);
			} else if(index == REMAIN.getIndex()) {
				tvHour = (NotoTextView) view.findViewById(R.id.tv_question_hour);
				tvMinute = (NotoTextView) view.findViewById(R.id.tv_question_minute);
				tvHourString = (NotoTextView) view.findViewById(R.id.tv_question_hour_string);
				tvMinuteString = (NotoTextView) view.findViewById(R.id.tv_question_minute_string);
			}
		}
	}
}
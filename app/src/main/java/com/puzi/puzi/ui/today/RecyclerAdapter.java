package com.puzi.puzi.ui.today;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.test.LoaderTestCase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.ui.customview.NotoTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.puzi.puzi.biz.myservice.ViewType.BONUS;
import static com.puzi.puzi.biz.myservice.ViewType.END;
import static com.puzi.puzi.biz.myservice.ViewType.INIT;
import static com.puzi.puzi.biz.myservice.ViewType.REMAIN;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

	private int state = 1, hour, minute;
    private MyTodayQuestionVO myTodayQuestionVO;
	private Context context;

	public RecyclerAdapter(Context context) {
		this.context = context;
	}

	public void setTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public void setMyTodayQuestionVO(MyTodayQuestionVO myTodayQuestionVO) {
		this.myTodayQuestionVO = myTodayQuestionVO;

		Log.i("RecyclerAdapter", "myTodayQuestionVO : " + myTodayQuestionVO.toString());
	}

	public void changedState(int state) {
		this.state = state;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view;

		Log.i("RecyclerAdapter", "state : " + state);

		if(state == INIT.getIndex()) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_init, parent, false);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, QuestionActivity.class);
					intent.putExtra("questionList", myTodayQuestionVO);
					context.startActivity(intent);
				}
			});
		} else if(state == BONUS.getIndex()) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_bonus, parent, false);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, QuestionActivity.class);
					intent.putExtra("questionList", myTodayQuestionVO);
					context.startActivity(intent);
				}
			});
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
			holder.tvHour.setText("" + hour);
			holder.tvMinute.setText("" + minute);
		} else if(state == INIT.getIndex() || state == BONUS.getIndex()) {
			holder.tvPlusPoint.setText("" + myTodayQuestionVO.getSavePoint());
		}
	}

	@Override
	public int getItemCount() {
		if(myTodayQuestionVO == null) {
			return 0;
		} else {
			return 1;
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public NotoTextView tvPlusPoint;
		public NotoTextView tvHour;
		public NotoTextView tvMinute;

		public ViewHolder(View view, int index) {
			super(view);

			if(index == INIT.getIndex()) {
				tvPlusPoint = (NotoTextView) view.findViewById(R.id.tv_question_plus_point);
			} else if(index == BONUS.getIndex()) {
				tvPlusPoint = (NotoTextView) view.findViewById(R.id.tv_question_plus_point);
			} else if(index == REMAIN.getIndex()) {
				tvHour = (NotoTextView) view.findViewById(R.id.tv_question_hour);
				tvMinute = (NotoTextView) view.findViewById(R.id.tv_question_minute);
			}
		}
	}
}
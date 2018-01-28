package kr.puzi.puzi.ui.myservice.mytoday;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;
import java.util.List;

import kr.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import kr.puzi.puzi.biz.myservice.ViewType;
import kr.puzi.puzi.ui.customview.NotoTextView;
import kr.puzi.puzi.ui.myservice.QuestionFragment;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

	private int state = 1, hour, minute, second;
    private List<MyTodayQuestionVO> myTodayQuestionVOList;
	private Context context;
	private RefreshCallback refreshCallback;

	public TodayAdapter(Context context) {
		this.context = context;
	}

	public void setTime(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public void setMyTodayQuestionVOList(List<MyTodayQuestionVO> myTodayQuestionVOList) {
		this.myTodayQuestionVOList = myTodayQuestionVOList;
	}

	public void changedState(int state) {
		this.state = state;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view;

		Log.i("TodayAdapter", "state : " + state);

		if(state == ViewType.INIT.getIndex()) {
			Log.i("TodayAdapter", "index : " + ViewType.INIT.getIndex());
			view = LayoutInflater.from(parent.getContext()).inflate(kr.puzi.puzi.R.layout.item_question_init, parent, false);
		} else if(state == ViewType.BONUS.getIndex()) {
			view = LayoutInflater.from(parent.getContext()).inflate(kr.puzi.puzi.R.layout.item_question_bonus, parent, false);
		} else if(state == ViewType.REMAIN.getIndex()){
			view = LayoutInflater.from(parent.getContext()).inflate(kr.puzi.puzi.R.layout.item_question_time, parent, false);
		} else {
			view = LayoutInflater.from(parent.getContext()).inflate(kr.puzi.puzi.R.layout.item_question_end, parent, false);
		}

		ViewHolder viewHolder = new ViewHolder(view, state);

		return viewHolder;
	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		if(state == ViewType.REMAIN.getIndex()) {
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

			holder.button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.i("TodayAdapter", "onClick");
					refreshCallback.refresh();
				}
			});

		} else if(state == ViewType.INIT.getIndex() || state == ViewType.BONUS.getIndex()) {
			holder.tvPlusPoint.setText("" + myTodayQuestionVOList.get(QuestionFragment.count).getSavePoint());
			holder.button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.i("TodayAdapter", "onClick");
					Intent intent = new Intent(context, QuestionActivity.class);
					intent.putExtra("questionList", (Serializable) myTodayQuestionVOList);
					context.startActivity(intent);
				}
			});
		} else {
			holder.button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.i("TodayAdapter", "onClick");
					refreshCallback.refresh();
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

			if(index == ViewType.INIT.getIndex() || index == ViewType.BONUS.getIndex()) {
				tvPlusPoint = (NotoTextView) view.findViewById(kr.puzi.puzi.R.id.tv_question_plus_point);
				button = (Button) view.findViewById(kr.puzi.puzi.R.id.btn_rl);
			} else if(index == ViewType.REMAIN.getIndex()) {
				tvHour = (NotoTextView) view.findViewById(kr.puzi.puzi.R.id.tv_question_hour);
				tvMinute = (NotoTextView) view.findViewById(kr.puzi.puzi.R.id.tv_question_minute);
				tvHourString = (NotoTextView) view.findViewById(kr.puzi.puzi.R.id.tv_question_hour_string);
				tvMinuteString = (NotoTextView) view.findViewById(kr.puzi.puzi.R.id.tv_question_minute_string);
				button = (Button) view.findViewById(kr.puzi.puzi.R.id.btn_rl);
			} else {
				button = (Button) view.findViewById(kr.puzi.puzi.R.id.btn_rl);
			}
		}
	}

	public void setRefreshCallback(RefreshCallback refreshCallback) {
		this.refreshCallback = refreshCallback;
	}

	public interface RefreshCallback {
		void refresh();
	}
}
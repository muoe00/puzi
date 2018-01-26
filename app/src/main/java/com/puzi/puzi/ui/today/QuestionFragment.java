package com.puzi.puzi.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import com.puzi.puzi.biz.myservice.OrderType;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.MyServiceNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragment;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Data;
import retrofit2.Call;

import static com.google.common.collect.Lists.newArrayList;
import static com.puzi.puzi.biz.myservice.ViewType.BONUS;
import static com.puzi.puzi.biz.myservice.ViewType.END;
import static com.puzi.puzi.biz.myservice.ViewType.INIT;
import static com.puzi.puzi.biz.myservice.ViewType.REMAIN;

/**
 * Created by muoe0 on 2018-01-06.
 */

public class QuestionFragment extends BaseFragment implements AdapterView.OnItemClickListener {

	private Unbinder unbinder;
	private View view;
	private boolean mine = false, isBonusTime = false;
	private int state = 1, pagingIndex = 1, bonusCount = 0, hour = 0, minute = 0, second = 0, count = 0, max = 0;
	private OrderType orderType = OrderType.RECENTLY;

	private List<MyTodayQuestionVO> myTodayQuestionList;
	private List<MyWorryQuestionDTO> myWorryQuestionList;

	private TodayAdapter todayAdapter;
	private RecyclerView.LayoutManager manager;
	private SpinnerAdapter spinnerAdapter;
	private WorryAdaptor worryAdaptor;
	private List<OrderType> orderTypes;
	private ScheduledExecutorService excutors = Executors.newSingleThreadScheduledExecutor();

	public static List<UpdateLike> needToUpdateLike = newArrayList();

	@BindView(R.id.lv_vote) ListView lvQuestion;
	@BindView(R.id.sv_question) ScrollView svQuestion;
	@BindView(R.id.rv_question) RecyclerView rvQuestion;
	@BindView(R.id.id_worry_spinner) Spinner spinner;
	@BindView(R.id.btn_vote_more) Button btnMore;

	public static void updateLike(int id, boolean isLike, int count) {
		UpdateLike updateLike = new UpdateLike(id, isLike, count);
		needToUpdateLike.add(updateLike);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_question, container, false);
		unbinder = ButterKnife.bind(this, view);

		initComponent();
		getQuestion();

		TodayAdapter.RefreshCallback refreshCallback = new TodayAdapter.RefreshCallback() {
			@Override
			public void refresh() {
				getQuestion();
			}
		};

		todayAdapter.setRefreshCallback(refreshCallback);

		Log.i("MyService", "onCreateView");

		return view;
	}

	@Override
	public void onResume() {
		Log.i("MyService", "onResume");
		if(max > 0) {
			setQuestion();
		} else {
			getQuestion();
		}

		Log.i("QuestionFragment", "needToUpdateLike.size() : " + needToUpdateLike.size());

		if(needToUpdateLike.size() > 0) {
			for(UpdateLike updateLike : needToUpdateLike) {
				worryAdaptor.changedState(updateLike);
			}
			needToUpdateLike.clear();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i("MyService", "onPause");
		excutors.shutdownNow();
		super.onPause();
	}

	public void setQuestion() {
		if(count >= max) {
			getQuestion();
		} else {
			if (isBonusTime) {
				state = BONUS.getIndex();
			} else {
				state = INIT.getIndex();
			}

			todayAdapter.setMyTodayQuestionVO(myTodayQuestionList.get(count));
			count++;

			todayAdapter.changedState(state);
			rvQuestion.setAdapter(todayAdapter);
			todayAdapter.notifyDataSetChanged();
		}
	}

	public void getQuestion() {
		final LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
			@Override
			public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
				return myServiceNetworkService.getQuestion(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Log.i("MyService", responseVO.toString());

				myTodayQuestionList = responseVO.getList("myTodayQuestionDTOList", MyTodayQuestionVO.class);
				max = myTodayQuestionList.size();
				isBonusTime = responseVO.getBoolean("isBonusTime");

				if(myTodayQuestionList.size() > 0) {
					if(excutors != null) {
						excutors.shutdownNow();
					}
					count = 0;
					setQuestion();

				} else {
					boolean isMoreQuestion = responseVO.getBoolean("isMoreQuestion");

					if(isMoreQuestion) {
						state = REMAIN.getIndex();
						hour = responseVO.getInteger("remainHour");
						minute = responseVO.getInteger("remainMinute");
						second = responseVO.getInteger("remainSecond");

						setTime();
					} else {
						excutors.shutdownNow();
						state = END.getIndex();
					}
				}

				Log.i("QuestionFragment", "state : " + state);

				todayAdapter.changedState(state);
				rvQuestion.setAdapter(todayAdapter);
				todayAdapter.notifyDataSetChanged();
			}
		});
	}

	public void setTime() {
		if(excutors == null || excutors.isShutdown()) {
			excutors = Executors.newSingleThreadScheduledExecutor();
			excutors.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(second <= 0) {
								if(minute > 0) {
									minute--;
									second = 60;
								} else {
									if(hour > 0) {
										hour--;
										minute = 59;
										second = 60;
									} else {
										getQuestion();
									}
								}
							} else {
								Log.i("QuestionFragment", "second : " + second);
								todayAdapter.setTime(hour, minute, second);
								todayAdapter.notifyDataSetChanged();
							}
						}
					});
					second--;
				}
			}, 0, 1, TimeUnit.SECONDS);
		}
	}

	public void getWorryList() {

		orderType = OrderType.getRandomType();
		worryAdaptor.startProgressWithScrollDown();

		LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
			@Override
			public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
				return myServiceNetworkService.getWorryList(token, worryAdaptor.getPagingIndex(), mine, orderType.toString());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				worryAdaptor.stopProgress();

				myWorryQuestionList = responseVO.getList("myWorryQuestionDTOList", MyWorryQuestionDTO.class);
				int totalCount = responseVO.getInteger("totalCount");

				Log.i("QuestionFragment", responseVO.toString());

				worryAdaptor.addListWithTotalCount(myWorryQuestionList, totalCount);
				// worryAdaptor.addList(myWorryQuestionList);

				if(totalCount == worryAdaptor.getCount()) {
					btnMore.setEnabled(false);
				} else {
					btnMore.setEnabled(true);
				}

				if(worryAdaptor.getCount() >= totalCount) {
					btnMore.setVisibility(View.GONE);
				}
			}
		});
	}

	@OnClick(R.id.btn_vote_more)
	public void clickMoreVoteList() {
		worryAdaptor.getList();
	}

	public void initComponent() {

		// myToday
		todayAdapter = new TodayAdapter(getContext());
		rvQuestion.setHasFixedSize(true);
		manager = new LinearLayoutManager(getActivity());
		rvQuestion.setLayoutManager(manager);

		// myWorry
		worryAdaptor = new WorryAdaptor(getActivity(), R.layout.item_question_vote, lvQuestion, svQuestion, new CustomPagingAdapter.ListHandler() {
			@Override
			public void getList() {
				getWorryList();
			}
		});
		worryAdaptor.setMore(false);
		worryAdaptor.getList();
		lvQuestion.setAdapter(worryAdaptor);
		lvQuestion.setOnItemClickListener(this);

		List<String> filterType = Arrays.asList("전체", "내가 쓴 글");
		spinnerAdapter = new SpinnerAdapter(getContext(), filterType);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0) {
					mine = false;
				} else {
					mine = true;
				}
				worryAdaptor.initPagingIndex();
				worryAdaptor.increasePagingIndex();
				getWorryList();
				Log.i("QuestionFragment", "mine : " + mine);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mine = false;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Log.i("QuestionFragment", "onItemClick");
		MyWorryQuestionDTO myWorryQuestionDTO = worryAdaptor.getItem(i);
		Intent intent = new Intent(getActivity(), AnswerActivity.class);
		intent.putExtra("myWorryQuestionDTO", myWorryQuestionDTO);
		startActivity(intent);
	}

	@Data
	public static class UpdateLike {
		private int id;
		private boolean isLike;
		private int count;

		public UpdateLike(int id, boolean isLike, int count) {
			this.id = id;
			this.isLike = isLike;
			this.count = count;
		}
	}
}

package com.puzi.puzi.ui.today;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
import com.puzi.puzi.ui.customview.NotoTextView;

import retrofit2.Call;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.puzi.puzi.biz.myservice.ViewType.BONUS;
import static com.puzi.puzi.biz.myservice.ViewType.END;
import static com.puzi.puzi.biz.myservice.ViewType.INIT;
import static com.puzi.puzi.biz.myservice.ViewType.REMAIN;

/**
 * Created by muoe0 on 2018-01-06.
 */

public class QuestionFragment extends BaseFragment {

	private Unbinder unbinder;
	private View view;
	private boolean mine = false;
	private int state = 1, pagingIndex = 1, bonusCount = 0, hour = 0, minute = 0, second = 0;
	private OrderType orderType = OrderType.RECENTLY;

	private List<MyTodayQuestionVO> myTodayQuestionList;
	private List<MyWorryQuestionDTO> myWorryQuestionList;

	private RecyclerAdapter adapter;
	private RecyclerView.LayoutManager manager;
	private WorryAdaptor worryAdaptor;

	private ScheduledExecutorService excutors = Executors.newSingleThreadScheduledExecutor();

	@BindView(R.id.lv_vote) ListView lvQuestion;
	@BindView(R.id.sv_question) ScrollView svQuestion;
	@BindView(R.id.rv_question) RecyclerView rvQuestion;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_question, container, false);
		unbinder = ButterKnife.bind(this, view);

		Log.i("MyService", "onCreateView");

		initComponent();

		return view;
	}

	@Override
	public void onResume() {
		Log.i("MyService", "onResume");
		getQuestion();
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i("MyService", "onPause");
		excutors.shutdownNow();
		super.onPause();
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

				boolean isBonusTime = responseVO.getBoolean("isBonusTime");

				if(myTodayQuestionList.size() > 0) {
					Log.i("MyService", myTodayQuestionList.get(0).toString());

					if(isBonusTime) {
						state = BONUS.getIndex();
						adapter.setMyTodayQuestionVO(myTodayQuestionList.get(0));
					} else {
						state = INIT.getIndex();
						adapter.setMyTodayQuestionVO(myTodayQuestionList.get(0));
					}

				} else {
					boolean isMoreQuestion = responseVO.getBoolean("isMoreQuestion");

					if(isMoreQuestion) {
						state = REMAIN.getIndex();
						hour = responseVO.getInteger("remainHour");
						minute = responseVO.getInteger("remainMinute");
						second = responseVO.getInteger("remainSecond");

						setTime();

						Log.i("QuestionFragment", responseVO.getInteger("remainHour") + ", " + responseVO.getInteger("remainMinute"));
					} else {
						state = END.getIndex();
					}
				}

				Log.i("QuestionFragment", "state : " + state);

				adapter.changedState(state);
				rvQuestion.setAdapter(adapter);
			}
		});
	}

	public void setTime() {
		if(excutors.isShutdown()) {
			excutors = Executors.newSingleThreadScheduledExecutor();
		}
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
								}
							}
						} else {
							adapter.setTime(hour, minute);
							adapter.notifyDataSetChanged();
						}
					}
				});
				Log.i("MainFragment", "setTime second : " + second);
				second--;
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void getWorryList() {
		LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
			@Override
			public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
				return myServiceNetworkService.getWorryList(token, pagingIndex, mine, orderType.toString());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				myWorryQuestionList = responseVO.getList("myWorryQuestionDTOList", MyWorryQuestionDTO.class);
				Log.i("QuestionFragment", myWorryQuestionList.get(0).toString());

				worryAdaptor.addList(myWorryQuestionList);
			}
		});
	}

	@OnClick(R.id.btn_vote_more)
	public void clickMoreVoteList() {
		worryAdaptor.getList();
	}

	public void initComponent() {

		// myToday
		adapter = new RecyclerAdapter(getContext());
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
	}

}

package kr.puzi.puzi.ui.myservice;

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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import kr.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import kr.puzi.puzi.biz.myservice.OrderType;
import kr.puzi.puzi.biz.myservice.ViewType;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyServiceNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.myservice.mytoday.TodayAdapter;
import kr.puzi.puzi.ui.myservice.myworry.AnswerActivity;
import kr.puzi.puzi.ui.myservice.myworry.MyWorryAdaptor;
import lombok.Data;
import retrofit2.Call;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2018-01-06.
 */

public class QuestionFragment extends BaseFragment implements AdapterView.OnItemClickListener {

	private Unbinder unbinder;
	private View view;
	private boolean mine = false, isBonusTime = false;
	private int state = 1, hour = 0, minute = 0, second = 0, max = 0;
	private OrderType orderType = OrderType.RECENTLY;

	private List<MyTodayQuestionVO> myTodayQuestionList;
	private List<MyWorryQuestionDTO> myWorryQuestionList;

	private TodayAdapter todayAdapter;
	private RecyclerView.LayoutManager manager;
	private SpinnerAdapter spinnerAdapter;
	private MyWorryAdaptor myWorryAdaptor;
	private ScheduledExecutorService excutors = Executors.newSingleThreadScheduledExecutor();

	public static int count = 0;
	public static List<UpdateLike> needToUpdateLike = newArrayList();

	@BindView(kr.puzi.puzi.R.id.lv_vote) ListView lvQuestion;
	@BindView(kr.puzi.puzi.R.id.sv_question) ScrollView svQuestion;
	@BindView(kr.puzi.puzi.R.id.rv_question) RecyclerView rvQuestion;
	@BindView(kr.puzi.puzi.R.id.id_worry_spinner) Spinner spinner;
	@BindView(kr.puzi.puzi.R.id.btn_vote_more) Button btnMore;
	@BindView(kr.puzi.puzi.R.id.fl_vote_more) FrameLayout flMore;

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
		view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_question, container, false);
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

		Log.i("QuestionFragment", "onCreateView");

		return view;
	}

	@Override
	public void onResume() {
		Log.i("QuestionFragment", "onResume");
		Log.i("QuestionFragment", "max : " + max);

		if(max == count) {
			count = 0;
			getQuestion();
		} else {
			setQuestion();
		}

		Log.i("QuestionFragment", "needToUpdateLike.size() : " + needToUpdateLike.size());

		if(needToUpdateLike.size() > 0) {
			for(UpdateLike updateLike : needToUpdateLike) {
				myWorryAdaptor.changedState(updateLike);
			}
			needToUpdateLike.clear();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i("QuestionFragment", "onPause");
		excutors.shutdownNow();
		super.onPause();
	}

	public void setQuestion() {
		if(max == count) {
			getQuestion();
		} else {
			if (isBonusTime) {
				state = ViewType.BONUS.getIndex();
			} else {
				state = ViewType.INIT.getIndex();
			}

			todayAdapter.setMyTodayQuestionVOList(myTodayQuestionList);
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
				Log.i("QuestionFragment", responseVO.toString());

				myTodayQuestionList = responseVO.getList("myTodayQuestionDTOList", MyTodayQuestionVO.class);
				max = myTodayQuestionList.size();
				isBonusTime = responseVO.getBoolean("isBonusTime");

				if(myTodayQuestionList.size() > 0) {
					if(excutors != null) {
						excutors.shutdownNow();
					}
					setQuestion();

				} else {
					boolean isMoreQuestion = responseVO.getBoolean("isMoreQuestion");

					if(isMoreQuestion) {
						state = ViewType.REMAIN.getIndex();
						hour = responseVO.getInteger("remainHour");
						minute = responseVO.getInteger("remainMinute");
						second = responseVO.getInteger("remainSecond");

						setTime();
					} else {
						excutors.shutdownNow();
						state = ViewType.END.getIndex();
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

		LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
			@Override
			public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
				return myServiceNetworkService.getWorryList(token, myWorryAdaptor.getPagingIndex(), mine, orderType.toString());
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				myWorryAdaptor.stopProgress();

				myWorryQuestionList = responseVO.getList("myWorryQuestionDTOList", MyWorryQuestionDTO.class);
				int totalCount = responseVO.getInteger("totalCount");

				Log.i("QuestionFragment", responseVO.toString());

				myWorryAdaptor.addListWithTotalCount(myWorryQuestionList, totalCount);

				Log.i("QuestionFragment", "myWorryAdaptor.getCount() : " + myWorryAdaptor.getCount());
				Log.i("QuestionFragment", "totalCount : " + totalCount);

				if((myWorryAdaptor.getCount() == totalCount) || (myWorryAdaptor.isEmpty()) || !(myWorryAdaptor.isMore())) {
					flMore.setVisibility(View.GONE);
				} else {
					flMore.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@OnClick(kr.puzi.puzi.R.id.btn_vote_more)
	public void clickMoreVoteList() {
		myWorryAdaptor.getList();
	}

	public void initComponent() {

		// myToday
		todayAdapter = new TodayAdapter(getContext());
		rvQuestion.setHasFixedSize(true);
		manager = new LinearLayoutManager(getActivity());
		rvQuestion.setLayoutManager(manager);

		// myWorry
		myWorryAdaptor = new MyWorryAdaptor(getActivity(), kr.puzi.puzi.R.layout.item_question_vote, lvQuestion, svQuestion, new CustomPagingAdapter.ListHandler() {
			@Override
			public void getList() {
				myWorryAdaptor.startProgressWithScrollDown();
				getWorryList();
			}
		}, true);
		myWorryAdaptor.setMore(false);
		myWorryAdaptor.getList();
		lvQuestion.setAdapter(myWorryAdaptor);
		lvQuestion.setOnItemClickListener(this);

		List<String> filterType = Arrays.asList("전체", "나의 고민");
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
				myWorryAdaptor.clean();
				myWorryAdaptor.initPagingIndex();
				myWorryAdaptor.increasePagingIndex();
				myWorryAdaptor.startProgress();
				myWorryAdaptor.setMine(mine);
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
		MyWorryQuestionDTO myWorryQuestionDTO = myWorryAdaptor.getItem(i);
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

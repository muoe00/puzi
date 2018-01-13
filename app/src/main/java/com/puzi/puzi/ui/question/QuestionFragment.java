package com.puzi.puzi.ui.question;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.MyServiceNetworkService;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2018-01-06.
 */

public class QuestionFragment extends BaseFragment {

	private Unbinder unbinder;
	private View view;

	@BindView(R.id.layout_question) View layout;
	@BindView(R.id.fl_question) FrameLayout flQuestion;

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
		initScrollAction();

		getQuestion();

		return view;
	}

	public void getQuestion() {

		LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
			@Override
			public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
				return myServiceNetworkService.getQuestion(token);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				List<MyTodayQuestionVO> myTodayQuestionList = responseVO.getList("myTodayQuestionDTOList", MyTodayQuestionVO.class);
				Log.i("MyService", myTodayQuestionList.get(0).toString());

				boolean isMoreQuestion = responseVO.getBoolean("isMoreQuestion");
				boolean isBonusTime = responseVO.getBoolean("isBonusTime");

				if(myTodayQuestionList.size() > 0) {
					if(isBonusTime) {
						// 보너스 타임

					} else {
						// 참여하기
						// flQuestion.setBackgroundResource(getResources().getLayout(R.layout.item_question_init));
					}
				} else {
					if(isMoreQuestion) {
						// 남은 시간 표시

					} else {
						// 질문 끝났다는 멘트 표시

					}
				}
			}
		});
	}

	public void initComponent() {

	}

	public void initScrollAction() {

	}
}

package kr.puzi.puzi.ui.today;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import kr.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyServiceNetworkService;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.base.BaseActivity;
import kr.puzi.puzi.ui.common.PointDialog;
import kr.puzi.puzi.ui.customview.NotoTextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by juhyun on 2018. 1. 14..
 */

public class QuestionActivity extends BaseActivity {

    private Activity activity;
    private Unbinder unbinder;
    private boolean isSelected = false;
    private int selectedCount = 0, savePoint;
    private String answer;
    private MyTodayQuestionVO myTodayQuestionVO = null;

    @BindView(kr.puzi.puzi.R.id.tv_question_q)
    NotoTextView tvQuestion;
    @BindView(kr.puzi.puzi.R.id.tv_answer_plus_point)
    NotoTextView tvPoint;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a1)
    Button btnA1;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a2)
    Button btnA2;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a3)
    Button btnA3;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a4)
    Button btnA4;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a2_a1)
    Button btnA2A1;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a2_a2)
    Button btnA2A2;
    @BindView(kr.puzi.puzi.R.id.ll_question_a4)
    LinearLayout llA4;
    @BindView(kr.puzi.puzi.R.id.ll_question_a2)
    LinearLayout llA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(kr.puzi.puzi.R.layout.activity_question);

        unbinder = ButterKnife.bind(this);
        activity = this;

        myTodayQuestionVO = (MyTodayQuestionVO) getIntent().getExtras().getSerializable("questionList");

        tvQuestion.setText(myTodayQuestionVO.getQuestion());
        tvPoint.setText("" + myTodayQuestionVO.getSavePoint());

        if(myTodayQuestionVO.getAnswerCount() == 2) {
            llA4.setVisibility(View.GONE);
            llA2.setVisibility(View.VISIBLE);
            btnA2A1.setText(myTodayQuestionVO.getAnswerOne());
            btnA2A2.setText(myTodayQuestionVO.getAnswerTwo());
        } else {
            llA4.setVisibility(View.VISIBLE);
            llA2.setVisibility(View.GONE);
            btnA1.setText(myTodayQuestionVO.getAnswerOne());
            btnA2.setText(myTodayQuestionVO.getAnswerTwo());
            btnA3.setText(myTodayQuestionVO.getAnswerThree());
            btnA4.setText(myTodayQuestionVO.getAnswerFour());
        }
    }

    @OnClick({kr.puzi.puzi.R.id.ibtn_question_a1, kr.puzi.puzi.R.id.ibtn_question_a2, kr.puzi.puzi.R.id.ibtn_question_a3, kr.puzi.puzi.R.id.ibtn_question_a4})
    public void checkAnswer(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.ibtn_question_a1:
                selectedCount = 1;
                answer = myTodayQuestionVO.getAnswerOne();
                checkButton(btnA1);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a2:
                selectedCount = 2;
                answer = myTodayQuestionVO.getAnswerTwo();
                checkButton(btnA2);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a3:
                selectedCount = 3;
                answer = myTodayQuestionVO.getAnswerThree();
                checkButton(btnA3);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a4:
                selectedCount = 4;
                answer = myTodayQuestionVO.getAnswerFour();
                checkButton(btnA4);
                break;
        }

        setAnswer();
    }

    public void setAnswer() {
        Log.i("QuestionActivity", "id : " + myTodayQuestionVO.getMyTodayQuestionId());
        Log.i("QuestionActivity", "answer : " + answer);
        Log.i("QuestionActivity", "selectedCount : " + selectedCount);

        LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                Log.i("QuestionActivity", "token : " + token);
                return myServiceNetworkService.setAnswer(token, myTodayQuestionVO.getMyTodayQuestionId(), answer, selectedCount);
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                MainActivity.needToUpdateUserVO = true;
                savePoint = responseVO.getInteger("savedPoint");

                PointDialog.load(activity, savePoint, true);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        closeView();
                    }
                }, 1250);
            }
        });
    }

    public void setEnabled(boolean state) {
        if (selectedCount == 1) {
            btnA2.setEnabled(state);
            btnA3.setEnabled(state);
            btnA4.setEnabled(state);
        } else if(selectedCount == 2) {
            btnA1.setEnabled(state);
            btnA3.setEnabled(state);
            btnA4.setEnabled(state);
        } else if(selectedCount == 3) {
            btnA2.setEnabled(state);
            btnA1.setEnabled(state);
            btnA4.setEnabled(state);
        } else if(selectedCount == 4) {
            btnA2.setEnabled(state);
            btnA3.setEnabled(state);
            btnA1.setEnabled(state);
        }
    }

    public void checkButton(Button btn) {
        if(isSelected) {
            isSelected = false;
            setEnabled(true);
            selectedCount = 0;
            btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_off);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));

        } else {
            isSelected = true;
            setEnabled(false);
            btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
        }
    }

    @OnClick(kr.puzi.puzi.R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
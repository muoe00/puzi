package kr.puzi.puzi.ui.myservice.mytoday;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyServiceNetworkService;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.base.BaseActivity;
import kr.puzi.puzi.ui.common.PointDialog;
import kr.puzi.puzi.ui.customview.NotoTextView;
import kr.puzi.puzi.ui.myservice.QuestionFragment;
import retrofit2.Call;

/**
 * Created by juhyun on 2018. 1. 14..
 */

public class QuestionActivity extends BaseActivity {

    private Activity activity;
    private Unbinder unbinder;
    private boolean isSelected = false;
    private int selectedCount = 0, savePoint, size = 0;
    private String answer;
    private MyTodayQuestionVO myTodayQuestionVO = null;
    private List<MyTodayQuestionVO> myTodayQuestionVOList = null;

    @BindView(kr.puzi.puzi.R.id.tv_question_q)
    NotoTextView tvQuestion;
    @BindView(kr.puzi.puzi.R.id.tv_answer_plus_point)
    NotoTextView tvPoint;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a1)
    LinearLayout btnA1;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a2)
    LinearLayout btnA2;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a3)
    LinearLayout btnA3;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a4)
    LinearLayout btnA4;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a2_a1)
    LinearLayout btnA2A1;
    @BindView(kr.puzi.puzi.R.id.ibtn_question_a2_a2)
    LinearLayout btnA2A2;
    @BindView(kr.puzi.puzi.R.id.tv_question_a1)
    NotoTextView tvA1;
    @BindView(kr.puzi.puzi.R.id.tv_question_a2)
    NotoTextView tvA2;
    @BindView(kr.puzi.puzi.R.id.tv_question_a3)
    NotoTextView tvA3;
    @BindView(kr.puzi.puzi.R.id.tv_question_a4)
    NotoTextView tvA4;
    @BindView(kr.puzi.puzi.R.id.tv_question_a2_a1)
    NotoTextView tvA2A1;
    @BindView(kr.puzi.puzi.R.id.tv_question_a2_a2)
    NotoTextView tvA2A2;
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

        myTodayQuestionVOList = (List<MyTodayQuestionVO>) getIntent().getExtras().getSerializable("questionList");
        size = myTodayQuestionVOList.size();

        Log.i("QuestionActivity", "onCreate size : " + size + " count : " + QuestionFragment.count);

        setComponents();
    }

    public void init() {
        isSelected = false;
        answer = "";
        selectedCount = 0;

        Log.i("QuestionActivity", "init size : " + size + " count : " + QuestionFragment.count);
    }

    public void setComponents() {
        myTodayQuestionVO = myTodayQuestionVOList.get(QuestionFragment.count);

        Log.i("QuestionActivity", "setComponents myTodayQuestionVO : " + myTodayQuestionVO.toString());

        tvQuestion.setText(myTodayQuestionVO.getQuestion());
        tvPoint.setText("" + myTodayQuestionVO.getSavePoint());

        if(myTodayQuestionVO.getAnswerCount() == 2) {
            llA4.setVisibility(View.GONE);
            llA2.setVisibility(View.VISIBLE);
            tvA2A1.setText(myTodayQuestionVO.getAnswerOne());
            tvA2A2.setText(myTodayQuestionVO.getAnswerTwo());
            btnA2A1.setEnabled(true);
            btnA2A2.setEnabled(true);
            btnA2A1.setBackgroundResource(R.drawable.button_question);
            tvA2A1.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
            btnA2A2.setBackgroundResource(R.drawable.button_question);
            tvA2A2.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
        } else {
            llA4.setVisibility(View.VISIBLE);
            llA2.setVisibility(View.GONE);
            tvA1.setText(myTodayQuestionVO.getAnswerOne());
            tvA2.setText(myTodayQuestionVO.getAnswerTwo());
            tvA3.setText(myTodayQuestionVO.getAnswerThree());
            tvA4.setText(myTodayQuestionVO.getAnswerFour());
            btnA1.setEnabled(true);
            btnA2.setEnabled(true);
            btnA3.setEnabled(true);
            btnA4.setEnabled(true);
            btnA1.setBackgroundResource(R.drawable.button_question);
            tvA1.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
            btnA2.setBackgroundResource(R.drawable.button_question);
            tvA2.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
            btnA3.setBackgroundResource(R.drawable.button_question);
            tvA3.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
            btnA4.setBackgroundResource(R.drawable.button_question);
            tvA4.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));
        }
    }

    @OnClick({kr.puzi.puzi.R.id.ibtn_question_a2_a1, kr.puzi.puzi.R.id.ibtn_question_a2_a2})
    public void checkAnswer4(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.ibtn_question_a2_a1:
                selectedCount = 1;
                answer = myTodayQuestionVO.getAnswerOne();
                checkButton(btnA2A1, tvA2A1);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a2_a2:
                selectedCount = 2;
                answer = myTodayQuestionVO.getAnswerTwo();
                checkButton(btnA2A2, tvA2A2);
                break;
        }

        setAnswer();
    }

    @OnClick({kr.puzi.puzi.R.id.ibtn_question_a1, kr.puzi.puzi.R.id.ibtn_question_a2, kr.puzi.puzi.R.id.ibtn_question_a3, kr.puzi.puzi.R.id.ibtn_question_a4})
    public void checkAnswer2(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.ibtn_question_a1:
                selectedCount = 1;
                answer = myTodayQuestionVO.getAnswerOne();
                checkButton(btnA1, tvA1);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a2:
                selectedCount = 2;
                answer = myTodayQuestionVO.getAnswerTwo();
                checkButton(btnA2, tvA2);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a3:
                selectedCount = 3;
                answer = myTodayQuestionVO.getAnswerThree();
                checkButton(btnA3, tvA3);
                break;
            case kr.puzi.puzi.R.id.ibtn_question_a4:
                selectedCount = 4;
                answer = myTodayQuestionVO.getAnswerFour();
                checkButton(btnA4, tvA4);
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
                Log.i("QuestionActivity", "responseVO : " + responseVO.toString());

                MainActivity.needToUpdateUserVO = true;
                savePoint = responseVO.getInteger("savedPoint");

                PointDialog.load(activity, savePoint, true);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("QuestionActivity", "setAnswer size : " + size + " count : " + QuestionFragment.count);
                        if((size == 0) || (QuestionFragment.count == size - 1)) {
                            QuestionFragment.count++;
                            closeView();
                        } else {
                            QuestionFragment.count++;
                            init();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setComponents();
                                }
                            });
                        }
                    }
                }, 1250);
            }
        });
    }

    public void setEnabled(boolean state) {
        if (selectedCount == 1) {
            btnA2A2.setEnabled(state);
            btnA2.setEnabled(state);
            btnA3.setEnabled(state);
            btnA4.setEnabled(state);
        } else if(selectedCount == 2) {
            btnA2A1.setEnabled(state);
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

    public void checkButton(LinearLayout btn, NotoTextView tv) {
        if(isSelected) {
            isSelected = false;
            setEnabled(true);
            selectedCount = 0;
            btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_off);
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorTextGray));

        } else {
            isSelected = true;
            setEnabled(false);
            btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
        }
    }

    @OnClick(kr.puzi.puzi.R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
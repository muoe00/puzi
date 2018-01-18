package com.puzi.puzi.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import com.puzi.puzi.biz.user.FavoriteType;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.MyServiceNetworkService;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.customview.NotoTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by juhyun on 2018. 1. 14..
 */

public class QuestionActivity extends BaseActivity {

    private Unbinder unbinder;
    private boolean isSelected = false;
    private int selectedCount = 0, savePoint;
    private String answer;
    private MyTodayQuestionVO myTodayQuestionVO = null;

    @BindView(R.id.tv_question_q)
    NotoTextView tvQuestion;
    @BindView(R.id.ibtn_question_a1)
    Button btnA1;
    @BindView(R.id.ibtn_question_a2)
    Button btnA2;
    @BindView(R.id.ibtn_question_a3)
    Button btnA3;
    @BindView(R.id.ibtn_question_a4)
    Button btnA4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);

        unbinder = ButterKnife.bind(this);

        myTodayQuestionVO = (MyTodayQuestionVO) getIntent().getExtras().getSerializable("questionList");

        tvQuestion.setText(myTodayQuestionVO.getQuestion());
        btnA1.setText(myTodayQuestionVO.getAnswerOne());
        btnA2.setText(myTodayQuestionVO.getAnswerTwo());

        if(myTodayQuestionVO.getAnswerCount() == 2) {
            btnA3.setVisibility(View.GONE);
            btnA4.setVisibility(View.GONE);
        } else {
            btnA3.setText(myTodayQuestionVO.getAnswerThree());
            btnA4.setText(myTodayQuestionVO.getAnswerFour());
        }
    }

    @OnClick({R.id.ibtn_question_a1, R.id.ibtn_question_a2, R.id.ibtn_question_a3, R.id.ibtn_question_a4})
    public void checkAnswer(View view) {
        switch (view.getId()) {
            case R.id.ibtn_question_a1:
                selectedCount = 1;
                answer = myTodayQuestionVO.getAnswerOne();
                checkButton(btnA1);
                break;
            case R.id.ibtn_question_a2:
                selectedCount = 2;
                answer = myTodayQuestionVO.getAnswerTwo();
                checkButton(btnA2);
                break;
            case R.id.ibtn_question_a3:
                selectedCount = 3;
                answer = myTodayQuestionVO.getAnswerThree();
                checkButton(btnA3);
                break;
            case R.id.ibtn_question_a4:
                selectedCount = 4;
                answer = myTodayQuestionVO.getAnswerFour();
                checkButton(btnA4);
                break;
        }

        setAnswer();
    }

    public void setAnswer() {
        LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                return myServiceNetworkService.setAnswer(token, myTodayQuestionVO.getMyTodayQuestionId(), answer, selectedCount);
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                savePoint = responseVO.getInteger("savedPoint");
                // 적립 인디케이터
                Toast.makeText(savedActivity, savePoint + "원이 적립되었습니다.", Toast.LENGTH_SHORT).show();
                closeView();
            }
        });
    }

    public void setEnabled(Button btn, boolean state) {
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
            setEnabled(btn, true);
            selectedCount = 0;
            btn.setBackgroundResource(R.drawable.button_question_off);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));

        } else {
            isSelected = true;
            setEnabled(btn, false);
            btn.setBackgroundResource(R.drawable.button_question_on);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
        }
    }

    @OnClick(R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
package com.puzi.puzi.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.biz.myservice.MyWorryAnswerResultDTO;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.MyServiceNetworkService;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.common.PointDialog;
import com.puzi.puzi.ui.customview.NotoTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.puzi.puzi.ui.today.QuestionFragment.updateLike;

/**
 * Created by juhyun on 2018. 1. 20..
 */

public class AnswerActivity extends BaseActivity {

    private Unbinder unbinder;
    private boolean isSelected = false;
    private int selectedCount = 0, savePoint, saveCount;
    private String answer;
    private MyWorryQuestionDTO myWorryQuestionDTO;
    private MyWorryAnswerResultDTO myWorryAnswerResultDTO;

    @BindView(R.id.ibtn_question_report)
    ImageButton ibtnReport;
    @BindView(R.id.tv_answer_count)
    NotoTextView tvCount;
    @BindView(R.id.tv_answer_count_limit)
    NotoTextView tvCountLimit;
    @BindView(R.id.tv_worry_title)
    NotoTextView tvTitle;
    @BindView(R.id.ll_answer_container_2)
    LinearLayout llContainer;
    @BindView(R.id.btn_answer_1)
    Button btnAnswer1;
    @BindView(R.id.btn_answer_2)
    Button btnAnswer2;
    @BindView(R.id.btn_answer_3)
    Button btnAnswer3;
    @BindView(R.id.btn_answer_4)
    Button btnAnswer4;
    @BindView(R.id.ll_answer_like)
    LinearLayout llLike;
    @BindView(R.id.iv_answer_like)
    ImageView ivLike;
    @BindView(R.id.tv_answer_like)
    NotoTextView tvLike;
    @BindView(R.id.btn_answer_ok)
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer);

        unbinder = ButterKnife.bind(this);
        myWorryQuestionDTO = (MyWorryQuestionDTO) getIntent().getExtras().getSerializable("myWorryQuestionDTO");

        Log.i("AnswerActivity", "myWorryQuestionDTO : " + myWorryQuestionDTO.toString());

        initComponents();
    }

    public void initComponents() {
        if(myWorryQuestionDTO.getQuestionCount() == 2) {
            llContainer.setVisibility(View.GONE);
            btnAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
            btnAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
        } else {
            btnAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
            btnAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
            btnAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
            btnAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
        }

        tvTitle.setText(myWorryQuestionDTO.getQuestion());
        tvCount.setText("" + myWorryQuestionDTO.getAnsweredCount());
        tvCountLimit.setText("" + myWorryQuestionDTO.getTotalAnswerCount());

        tvLike.setText("" + myWorryQuestionDTO.getLikedCount());

        if(myWorryQuestionDTO.isLikedByMe()) {
            setLikeOn();
        } else {
            setLikeOff();
        }

        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLikeCount(myWorryQuestionDTO.getMyWorryQuestionId());
            }
        });
    }

    @OnClick({R.id.btn_answer_1, R.id.btn_answer_2, R.id.btn_answer_3, R.id.btn_answer_4})
    public void checkAnswer(View view) {
        switch (view.getId()) {
            case R.id.btn_answer_1:
                selectedCount = 1;
                answer = myWorryQuestionDTO.getAnswerOne();
                checkButton(btnAnswer1);
                break;
            case R.id.btn_answer_2:
                selectedCount = 2;
                answer = myWorryQuestionDTO.getAnswerTwo();
                checkButton(btnAnswer2);
                break;
            case R.id.btn_answer_3:
                selectedCount = 3;
                answer = myWorryQuestionDTO.getAnswerThree();
                checkButton(btnAnswer3);
                break;
            case R.id.btn_answer_4:
                selectedCount = 4;
                answer = myWorryQuestionDTO.getAnswerFour();
                checkButton(btnAnswer4);
                break;
        }
    }

    @OnClick(R.id.btn_answer_ok)
    public void setAnswer() {
        Log.i("AnswerActivity", "setAnswer");

        LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                Log.i("QuestionActivity", "token : " + token);
                return myServiceNetworkService.setWorryAnswer(token, myWorryQuestionDTO.getMyWorryQuestionId(), answer, selectedCount);
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                MainActivity.needToUpdateUserVO = true;

                String count = "";

                savePoint = responseVO.getInteger("savedPoint");
                saveCount = responseVO.getInteger("savedCount");
                myWorryAnswerResultDTO = responseVO.getValue("myWorryAnswerResultDTO", MyWorryAnswerResultDTO.class);
                updateAnswerView();

                Log.i("AnswerActivity", myWorryAnswerResultDTO.toString());

                boolean state = false;

                if(saveCount < 10) {
                    count = "" + saveCount + " / 10";
                    state = false;
                } else if (saveCount == 10) {
                    count = "" + savePoint;
                    state = true;
                }

                PointDialog.load(getActivity(), count, state);
                // Toast.makeText(savedActivity, count, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateAnswerView() {

        int index = 0;
        int maxCount = myWorryAnswerResultDTO.getAnswerLimitCount();
        String context = "";

        btnOk.setVisibility(View.GONE);

        index = myWorryAnswerResultDTO.getAnswerOneCount() * 100 / maxCount;
        context = myWorryQuestionDTO.getAnswerOne() + "\n" + index + "%";
        btnAnswer1.setText(context);

        index = myWorryAnswerResultDTO.getAnswerTwoCount() * 100 / maxCount;
        context = myWorryQuestionDTO.getAnswerTwo() + "\n" + index + "%";
        btnAnswer2.setText(context);

        index = myWorryAnswerResultDTO.getAnswerThreeCount() * 100 / maxCount;
        context = myWorryQuestionDTO.getAnswerThree() + "\n" + index + "%";
        btnAnswer3.setText(context);

        index = myWorryAnswerResultDTO.getAnswerFourCount() * 100 / maxCount;
        context = myWorryQuestionDTO.getAnswerFour() + "\n" + index + "%";
        btnAnswer4.setText(context);
    }

    public void setEnabled(boolean state) {
        if (selectedCount == 1) {
            btnAnswer2.setEnabled(state);
            btnAnswer3.setEnabled(state);
            btnAnswer4.setEnabled(state);
        } else if(selectedCount == 2) {
            btnAnswer1.setEnabled(state);
            btnAnswer3.setEnabled(state);
            btnAnswer4.setEnabled(state);
        } else if(selectedCount == 3) {
            btnAnswer2.setEnabled(state);
            btnAnswer1.setEnabled(state);
            btnAnswer4.setEnabled(state);
        } else if(selectedCount == 4) {
            btnAnswer2.setEnabled(state);
            btnAnswer3.setEnabled(state);
            btnAnswer4.setEnabled(state);
        }
    }

    public void checkButton(Button btn) {
        if(isSelected) {
            isSelected = false;
            setEnabled(true);
            selectedCount = 0;
            btn.setBackgroundResource(R.drawable.button_question_off);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));

        } else {
            isSelected = true;
            setEnabled(false);
            btn.setBackgroundResource(R.drawable.button_question_on);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
        }
    }

    public void setLikeOn() {
        llLike.setBackgroundResource(R.drawable.bg_vote_like_on);
        ivLike.setImageResource(R.drawable.shape);
        tvLike.setTextColor(getResources().getColor(R.color.colorPuzi));
    }

    public void setLikeOff() {
        llLike.setBackgroundResource(R.drawable.bg_vote_like_off);
        ivLike.setImageResource(R.drawable.like);
        tvLike.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    public void addLikeCount(final int id) {

        Log.i("AnswerActivity", "addLikeCount");

        LazyRequestService service = new LazyRequestService(this, MyServiceNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                return myServiceNetworkService.serWorryLike(token, id);
            }
        });
        service.enqueue(new CustomCallback(this) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                int like = 0;

                if(myWorryQuestionDTO.isLikedByMe()) {
                    setLikeOff();
                    like = myWorryQuestionDTO.getLikedCount() - 1;
                    myWorryQuestionDTO.setLikedByMe(false);
                } else {
                    setLikeOn();
                    like = myWorryQuestionDTO.getLikedCount() + 1;
                    myWorryQuestionDTO.setLikedByMe(true);
                }

                tvLike.setText("" + like);
                myWorryQuestionDTO.setLikedCount(like);

                Log.i("AnswerActivity", "id : " + myWorryQuestionDTO.getMyWorryQuestionId());
                Log.i("AnswerActivity", "isLike : " + myWorryQuestionDTO.isLikedByMe());
                Log.i("AnswerActivity", "count : " + myWorryQuestionDTO.getLikedCount());

                updateLike(myWorryQuestionDTO.getMyWorryQuestionId(), myWorryQuestionDTO.isLikedByMe(), myWorryQuestionDTO.getLikedCount());
            }
        });
    }

    @OnClick(R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
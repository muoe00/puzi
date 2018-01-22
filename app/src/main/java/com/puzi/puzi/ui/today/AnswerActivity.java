package com.puzi.puzi.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
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
 * Created by juhyun on 2018. 1. 20..
 */

public class AnswerActivity extends BaseActivity {

    private Unbinder unbinder;
    private MyWorryQuestionDTO myWorryQuestionDTO;

    @BindView(R.id.ibtn_question_report)
    ImageButton ibtnReport;
    @BindView(R.id.tv_answer_count)
    NotoTextView tvCount;
    @BindView(R.id.tv_answer_count_limit)
    NotoTextView tvCountLimit;
    @BindView(R.id.tv_worry_title) NotoTextView tvTitle;
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
    @BindView(R.id.ll_answer_like) LinearLayout llLike;
    @BindView(R.id.iv_answer_like) ImageView ivLike;
    @BindView(R.id.tv_answer_like) NotoTextView tvLike;

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

                if(myWorryQuestionDTO.isLikedByMe()) {
                    setLikeOff();
                    int like = myWorryQuestionDTO.getLikedCount();
                    tvLike.setText("" + like--);
                } else {
                    setLikeOn();
                    int like = myWorryQuestionDTO.getLikedCount();
                    tvLike.setText("" + like++);
                }

            }
        });
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

            }
        });
    }

    @OnClick(R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
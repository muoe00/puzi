package kr.puzi.puzi.ui.today;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.myservice.MyWorryAnswerDTO;
import kr.puzi.puzi.biz.myservice.MyWorryAnswerResultDTO;
import kr.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import kr.puzi.puzi.biz.myservice.MyWorryQuestionDetailDTO;
import kr.puzi.puzi.biz.myservice.PersonalType;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyServiceNetworkService;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.base.BaseActivity;
import kr.puzi.puzi.ui.common.PointDialog;
import kr.puzi.puzi.ui.customview.NotoTextView;
import retrofit2.Call;

/**
 * Created by juhyun on 2018. 1. 20..
 */

public class AnswerActivity extends BaseActivity {

    private Unbinder unbinder;
    private boolean isSelected = false, isTwoAnser = false;
    private int selectedCount = 0, savePoint, saveCount;
    private String answer;
    private PersonalType personalType;
    private MyWorryQuestionDTO myWorryQuestionDTO;
    private MyWorryAnswerDTO myWorryAnswerDTO;
    private MyWorryQuestionDetailDTO myWorryQuestionDetailDTO;
    private MyWorryAnswerResultDTO myWorryAnswerResultDTO;

    @BindView(kr.puzi.puzi.R.id.ibtn_question_report)
    ImageButton ibtnReport;
    @BindView(kr.puzi.puzi.R.id.tv_answer_count)
    NotoTextView tvCount;
    @BindView(kr.puzi.puzi.R.id.tv_answer_count_limit)
    NotoTextView tvCountLimit;
    @BindView(kr.puzi.puzi.R.id.tv_worry_title)
    NotoTextView tvTitle;
    @BindView(kr.puzi.puzi.R.id.ll_answer_container_2)
    LinearLayout llContainer;
    @BindView(kr.puzi.puzi.R.id.btn_answer_1)
    Button btnAnswer1;
    @BindView(kr.puzi.puzi.R.id.btn_answer_2)
    Button btnAnswer2;
    @BindView(kr.puzi.puzi.R.id.btn_answer_3)
    Button btnAnswer3;
    @BindView(kr.puzi.puzi.R.id.btn_answer_4)
    Button btnAnswer4;
    @BindView(kr.puzi.puzi.R.id.ll_answer_like)
    LinearLayout llLike;
    @BindView(kr.puzi.puzi.R.id.iv_answer_like)
    ImageView ivLike;
    @BindView(kr.puzi.puzi.R.id.tv_answer_like)
    NotoTextView tvLike;
    @BindView(kr.puzi.puzi.R.id.btn_answer_ok)
    Button btnOk;
    @BindView(kr.puzi.puzi.R.id.btn_answer_a2_a1)
    Button btnA2A1;
    @BindView(kr.puzi.puzi.R.id.btn_answer_a2_a2)
    Button btnA2A2;
    @BindView(kr.puzi.puzi.R.id.ll_answer_a4)
    LinearLayout llA4;
    @BindView(kr.puzi.puzi.R.id.ll_answer_a2)
    LinearLayout llA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(kr.puzi.puzi.R.layout.activity_answer);

        unbinder = ButterKnife.bind(this);
        myWorryQuestionDTO = (MyWorryQuestionDTO) getIntent().getExtras().getSerializable("myWorryQuestionDTO");

        Log.i("AnswerActivity", "myWorryQuestionDTO : " + myWorryQuestionDTO.toString());

        initComponents();
        getDetail();
    }

    public void getDetail() {
        final LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                return myServiceNetworkService.getWorryDetail(token, myWorryQuestionDTO.getMyWorryQuestionId());
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                Log.i("AnswerActivity", responseVO.toString());

                myWorryQuestionDetailDTO = responseVO.getValue("myWorryQuestionDetailDTO", MyWorryQuestionDetailDTO.class);
                personalType = myWorryQuestionDetailDTO.getPersonalType();

                initAnswer();

                if(!personalType.equals(PersonalType.NOT_ANSWERED)) {
                    if(personalType.equals(PersonalType.ANSWERED)) {
                        myWorryAnswerDTO = myWorryQuestionDetailDTO.getMyWorryAnswerDTO();

                        if(myWorryQuestionDTO.getQuestionCount() == 2) {

                            isTwoAnser = true;

                            Log.i("AnswerActivity", "isTwoAnser : " + isTwoAnser);

                            switch (myWorryAnswerDTO.getAnswerNumber()) {
                                case 1:
                                    btnA2A1.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
                                    btnA2A1.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
                                    break;
                                case 2:
                                    btnA2A2.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
                                    btnA2A2.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
                                    break;
                            }
                        } else {

                            isTwoAnser = false;

                            switch (myWorryAnswerDTO.getAnswerNumber()) {
                                case 1:
                                    btnAnswer1.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
                                    btnAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
                                    break;
                                case 2:
                                    btnAnswer2.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
                                    btnAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
                                    break;
                                case 3:
                                    btnAnswer3.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
                                    btnAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
                                    break;
                                case 4:
                                    btnAnswer4.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
                                    btnAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
                                    break;
                            }
                        }


                    }

                }
                    myWorryAnswerResultDTO = myWorryQuestionDetailDTO.getMyWorryAnswerResultDTO();
                    btnOk.setVisibility(View.INVISIBLE);

                    updateAnswerView();

                    if(myWorryQuestionDTO.getQuestionCount() == 2) {
                        btnA2A1.setClickable(false);
                        btnA2A2.setClickable(false);
                    } else {
                        btnAnswer1.setClickable(false);
                        btnAnswer2.setClickable(false);
                        btnAnswer3.setClickable(false);
                        btnAnswer4.setClickable(false);
                    }

            }
        });
    }

    public void initAnswer() {
        if(myWorryQuestionDTO.getQuestionCount() == 2) {
            llA4.setVisibility(View.GONE);
            llA2.setVisibility(View.VISIBLE);
            btnA2A1.setText(myWorryQuestionDTO.getAnswerOne());
            btnA2A2.setText(myWorryQuestionDTO.getAnswerTwo());
        } else {
            llA4.setVisibility(View.VISIBLE);
            llA2.setVisibility(View.GONE);
            btnAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
            btnAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
            btnAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
            btnAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
        }
    }

    public void initComponents() {
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

    @OnClick({kr.puzi.puzi.R.id.btn_answer_1, kr.puzi.puzi.R.id.btn_answer_2, kr.puzi.puzi.R.id.btn_answer_3, kr.puzi.puzi.R.id.btn_answer_4})
    public void checkAnswer4(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.btn_answer_1:
                checkButton(btnAnswer1, 1);
                break;
            case kr.puzi.puzi.R.id.btn_answer_2:
                checkButton(btnAnswer2, 2);
                break;
            case kr.puzi.puzi.R.id.btn_answer_3:
                checkButton(btnAnswer3, 3);
                break;
            case kr.puzi.puzi.R.id.btn_answer_4:
                checkButton(btnAnswer4, 4);
                break;
        }
    }


    @OnClick({R.id.btn_answer_a2_a1, R.id.btn_answer_a2_a2})
    public void checkAnswer2(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.btn_answer_a2_a1:
                checkButton(btnA2A1, 1);
                break;
            case kr.puzi.puzi.R.id.btn_answer_a2_a2:
                checkButton(btnA2A2, 2);
                break;
        }
    }

    @OnClick(kr.puzi.puzi.R.id.btn_answer_ok)
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

                savePoint = responseVO.getInteger("savedPoint");
                saveCount = responseVO.getInteger("savedCount");

                myWorryAnswerResultDTO = responseVO.getValue("myWorryAnswerResultDTO", MyWorryAnswerResultDTO.class);
                updateAnswerView();

                Log.i("AnswerActivity", myWorryAnswerResultDTO.toString());

                boolean state = false;

                if(saveCount < 10) {
                    state = false;
                    PointDialog.load(getActivity(), saveCount, state);
                } else if (saveCount == 10) {
                    state = true;
                    PointDialog.load(getActivity(), savePoint, state);
                }

                btnAnswer1.setClickable(false);
                btnAnswer2.setClickable(false);
                btnAnswer3.setClickable(false);
                btnAnswer4.setClickable(false);
            }
        });
    }

    public void updateAnswerView() {

        int index = 0;
        int maxCount = myWorryAnswerResultDTO.getAnswerCount();
        String context = "";

        btnOk.setVisibility(View.GONE);

        if(!isTwoAnser) {
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
        } else {
            index = myWorryAnswerResultDTO.getAnswerOneCount() * 100 / maxCount;
            context = myWorryQuestionDTO.getAnswerOne() + "\n" + index + "%";
            btnA2A1.setText(context);

            index = myWorryAnswerResultDTO.getAnswerTwoCount() * 100 / maxCount;
            context = myWorryQuestionDTO.getAnswerTwo() + "\n" + index + "%";
            btnA2A2.setText(context);
        }
    }

    public void checkButton(Button btn, int index) {
        if (isSelected) {
            if(selectedCount == index) {
                isSelected = false;
                selectedCount = 0;
                btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_off);
                btn.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorBlack));
            }
        } else {
            isSelected = true;
            selectedCount = index;
            switch (index) {
                case 1:
                    answer = myWorryQuestionDTO.getAnswerOne();
                    break;
                case 2:
                    answer = myWorryQuestionDTO.getAnswerTwo();
                    break;
                case 3:
                    answer = myWorryQuestionDTO.getAnswerThree();
                    break;
                case 4:
                    answer = myWorryQuestionDTO.getAnswerFour();
                    break;
            }
            btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_question_on);
            btn.setTextColor(ContextCompat.getColor(getApplicationContext(), kr.puzi.puzi.R.color.colorPuzi));
        }
    }

    public void setLikeOn() {
        llLike.setBackgroundResource(kr.puzi.puzi.R.drawable.bg_vote_like_on);
        ivLike.setImageResource(kr.puzi.puzi.R.drawable.shape);
        tvLike.setTextColor(getResources().getColor(kr.puzi.puzi.R.color.colorPuzi));
    }

    public void setLikeOff() {
        llLike.setBackgroundResource(kr.puzi.puzi.R.drawable.bg_vote_like_off);
        ivLike.setImageResource(kr.puzi.puzi.R.drawable.like);
        tvLike.setTextColor(getResources().getColor(kr.puzi.puzi.R.color.colorWhite));
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

                QuestionFragment.updateLike(myWorryQuestionDTO.getMyWorryQuestionId(), myWorryQuestionDTO.isLikedByMe(), myWorryQuestionDTO.getLikedCount());
            }
        });
    }

    @OnClick(kr.puzi.puzi.R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
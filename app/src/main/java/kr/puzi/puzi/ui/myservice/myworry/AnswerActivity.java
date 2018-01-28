package kr.puzi.puzi.ui.myservice.myworry;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.ui.common.PointDialog;
import kr.puzi.puzi.ui.customview.NotoTextView;
import kr.puzi.puzi.ui.myservice.QuestionFragment;
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
    @BindView(R.id.btn_answer_a2_a1)
    Button btnA2A1;
    @BindView(R.id.btn_answer_a2_a2)
    Button btnA2A2;
    @BindView(R.id.ll_answer_a4)
    LinearLayout llA4;
    @BindView(R.id.ll_answer_a2)
    LinearLayout llA2;
    @BindView(R.id.tv_answer_1_t)
    NotoTextView tvAnswer1;
    @BindView(R.id.tv_answer_2_t)
    NotoTextView tvAnswer2;
    @BindView(R.id.tv_answer_3_t)
    NotoTextView tvAnswer3;
    @BindView(R.id.tv_answer_4_t)
    NotoTextView tvAnswer4;
    @BindView(R.id.tv_answer_1_p)
    NotoTextView tvPercent1;
    @BindView(R.id.tv_answer_2_p)
    NotoTextView tvPercent2;
    @BindView(R.id.tv_answer_3_p)
    NotoTextView tvPercent3;
    @BindView(R.id.tv_answer_4_p)
    NotoTextView tvPercent4;
    @BindView(R.id.tv_answer_a2_t1)
    NotoTextView tvA2T1;
    @BindView(R.id.tv_answer_a2_t2)
    NotoTextView tvA2T2;
    @BindView(R.id.tv_answer_a2_p1)
    NotoTextView tvA2P1;
    @BindView(R.id.tv_answer_a2_p2)
    NotoTextView tvA2P2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer);

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

                if(personalType.equals(PersonalType.NOT_ANSWERED)) {
                    if (myWorryQuestionDTO.getQuestionCount() == 2) {
                        tvA2P1.setVisibility(View.GONE);
                        tvA2P2.setVisibility(View.GONE);
                    } else {
                        tvPercent1.setVisibility(View.GONE);
                        tvPercent2.setVisibility(View.GONE);
                        tvPercent3.setVisibility(View.GONE);
                        tvPercent4.setVisibility(View.GONE);
                    }
                } else {
                    myWorryAnswerDTO = myWorryQuestionDetailDTO.getMyWorryAnswerDTO();

                    if (personalType.equals(PersonalType.ANSWERED)) {
                        if (myWorryQuestionDTO.getQuestionCount() == 2) {

                            isTwoAnser = true;

                            switch (myWorryAnswerDTO.getAnswerNumber()) {
                                case 1:
                                    btnA2A1.setBackgroundResource(R.drawable.button_question_on);
                                    btnA2A1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    break;
                                case 2:
                                    btnA2A2.setBackgroundResource(R.drawable.button_question_on);
                                    btnA2A2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    break;
                            }
                        } else {
                            isTwoAnser = false;

                            switch (myWorryAnswerDTO.getAnswerNumber()) {
                                case 1:
                                    btnAnswer1.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    break;
                                case 2:
                                    btnAnswer2.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    break;
                                case 3:
                                    btnAnswer3.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    break;
                                case 4:
                                    btnAnswer4.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    break;
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

            }
        });
    }

    public void initAnswer() {
        if(myWorryQuestionDTO.getQuestionCount() == 2) {
            llA4.setVisibility(View.GONE);
            llA2.setVisibility(View.VISIBLE);
            tvA2T1.setText(myWorryQuestionDTO.getAnswerOne());
            tvA2T2.setText(myWorryQuestionDTO.getAnswerTwo());
        } else {
            llA4.setVisibility(View.VISIBLE);
            llA2.setVisibility(View.GONE);
            tvAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
            tvAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
            tvAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
            tvAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
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

    @OnClick({R.id.btn_answer_1, R.id.btn_answer_2, R.id.btn_answer_3, R.id.btn_answer_4})
    public void checkAnswer(View view) {
        switch (view.getId()) {
            case R.id.btn_answer_1:
                checkButton(btnAnswer1, tvAnswer1, 1);
                break;
            case R.id.btn_answer_2:
                checkButton(btnAnswer2, tvAnswer2,2);
                break;
            case R.id.btn_answer_3:
                checkButton(btnAnswer3, tvAnswer3,3);
                break;
            case R.id.btn_answer_4:
                checkButton(btnAnswer4, tvAnswer4,4);
                break;
        }
    }

    @OnClick({R.id.btn_answer_a2_a1, R.id.btn_answer_a2_a2})
    public void checkAnswer2(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.btn_answer_a2_a1:
                checkButton(btnA2A1, tvA2T1, 1);
                break;
            case kr.puzi.puzi.R.id.btn_answer_a2_a2:
                checkButton(btnA2A2, tvA2T2, 2);
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
                btnA2A1.setClickable(false);
                btnA2A2.setClickable(false);
            }
        });
    }

    public void updateAnswerView() {

        int index = 0;
        int maxCount = myWorryAnswerResultDTO.getAnswerCount();

        btnOk.setVisibility(View.GONE);

        if(maxCount != 0) {
            if(!isTwoAnser) {
                index = myWorryAnswerResultDTO.getAnswerOneCount() * 100 / maxCount;
                tvAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
                tvPercent1.setText(index + "%");

                index = myWorryAnswerResultDTO.getAnswerTwoCount() * 100 / maxCount;
                tvAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvPercent2.setText(index + "%");

                index = myWorryAnswerResultDTO.getAnswerThreeCount() * 100 / maxCount;
                tvAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
                tvPercent3.setText(index + "%");

                index = myWorryAnswerResultDTO.getAnswerFourCount() * 100 / maxCount;
                tvAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
                tvPercent4.setText(index + "%");
            } else {
                index = myWorryAnswerResultDTO.getAnswerOneCount() * 100 / maxCount;
                tvA2T1.setText(myWorryQuestionDTO.getAnswerOne());
                tvA2P1.setText(index + "%");

                index = myWorryAnswerResultDTO.getAnswerTwoCount() * 100 / maxCount;
                tvA2T2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvA2P2.setText(index + "%");
            }
        } else {
            if(!isTwoAnser) {
                tvAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
                tvPercent1.setText(0 + "%");

                tvAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvPercent2.setText(0 + "%");

                tvAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
                tvPercent3.setText(0 + "%");

                tvAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
                tvPercent4.setText(0 + "%");
            } else {
                tvA2T1.setText(myWorryQuestionDTO.getAnswerOne());
                tvA2P1.setText(0 + "%");

                tvA2T2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvA2P2.setText(0 + "%");
            }
        }
    }

    public void checkButton(Button btn, NotoTextView tv, int index) {
        if (isSelected) {
            if(selectedCount == index) {
                isSelected = false;
                selectedCount = 0;
                btn.setBackgroundResource(R.drawable.button_question_off);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
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
            btn.setBackgroundResource(R.drawable.button_question_on);
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
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

                QuestionFragment.updateLike(myWorryQuestionDTO.getMyWorryQuestionId(), myWorryQuestionDTO.isLikedByMe(), myWorryQuestionDTO.getLikedCount());
            }
        });
    }

    @OnClick(R.id.ibtn_question_report)
    public void notifyClick() {
        if(personalType.isMine()) {
            Toast.makeText(getActivity(), "내가 올린 고민을 신고할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(myWorryQuestionDTO.isNotifiedByMe()) {
            Toast.makeText(getActivity(), "이미 신고하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        OneButtonDialog.show(getActivity(), "신고하기", "해당질문을 신고하시겠습니까?", "신고하기", new DialogButtonCallback() {
            @Override
            public void onClick() {
                LazyRequestService service = new LazyRequestService(getActivity(), MyServiceNetworkService.class);
                service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
                    @Override
                    public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                        return myServiceNetworkService.setWorryNotify(token, myWorryQuestionDTO.getMyWorryQuestionId());
                    }
                });
                service.enqueue(new CustomCallback(getActivity()) {
                    @Override
                    public void onSuccess(ResponseVO responseVO) {
                        myWorryQuestionDTO.setNotifiedByMe(true);
                        Toast.makeText(getActivity(), "정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @OnClick(R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
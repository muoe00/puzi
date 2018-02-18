package kr.puzi.puzi.ui.myservice.myworry;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.myservice.MyWorryAnswerDTO;
import kr.puzi.puzi.biz.myservice.MyWorryAnswerResultDTO;
import kr.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import kr.puzi.puzi.biz.myservice.MyWorryQuestionDetailDTO;
import kr.puzi.puzi.biz.myservice.PersonalType;
import kr.puzi.puzi.biz.myworry.MyWorryReplyVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyServiceNetworkService;
import kr.puzi.puzi.network.service.MyWorryReplyNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.ProgressDialog;
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
    private MyWorryReplyAdapter myWorryReplyAdapter;

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
    LinearLayout btnAnswer1;
    @BindView(R.id.btn_answer_2)
    LinearLayout btnAnswer2;
    @BindView(R.id.btn_answer_3)
    LinearLayout btnAnswer3;
    @BindView(R.id.btn_answer_4)
    LinearLayout btnAnswer4;
    @BindView(R.id.ll_answer_like)
    LinearLayout llLike;
    @BindView(R.id.iv_answer_like)
    ImageView ivLike;
    @BindView(R.id.tv_answer_like)
    NotoTextView tvLike;
    @BindView(R.id.btn_answer_ok)
    Button btnOk;
    @BindView(R.id.btn_answer_a2_a1)
    LinearLayout btnA2A1;
    @BindView(R.id.btn_answer_a2_a2)
    LinearLayout btnA2A2;
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
    @BindView(R.id.progress_Bar)
    ProgressBar pb;
    @BindView(R.id.ll_reply_show_container)
    LinearLayout llReplyShowContainer;
    @BindView(R.id.lv_reply_list_container)
    ListView lvReplyListContainer;
    @BindView(R.id.ll_reply_write_container)
    LinearLayout llReplyWriteContainer;
    @BindView(R.id.ll_reply_container)
    LinearLayout llReplyContainer;
    @BindView(R.id.tv_reply_show_count)
    TextView tvReplyCount;
    @BindView(R.id.ll_reply_pedding_container)
    LinearLayout llReplyPeddingContainer;
    @BindView(R.id.ll_reply_list_bar)
    LinearLayout llReplyListBar;
    @BindView(R.id.et_channel_detail_write_reply)
    EditText etWriteReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer);

        unbinder = ButterKnife.bind(this);
        myWorryQuestionDTO = (MyWorryQuestionDTO) getIntent().getExtras().getSerializable("myWorryQuestionDTO");
        if (myWorryQuestionDTO.getQuestionCount() == 2) {
            isTwoAnser = true;
        } else {
            isTwoAnser = false;
        }

        pb.setIndeterminate(true);
        pb.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        pb.setVisibility(View.VISIBLE);

        Log.i("AnswerActivity", "myWorryQuestionDTO : " + myWorryQuestionDTO.toString());

        initComponents();
        getDetail();
        getReply();
    }

    private void getReply() {
        myWorryReplyAdapter = new MyWorryReplyAdapter(getActivity(), lvReplyListContainer, new CustomPagingAdapter.ListHandler() {
            @Override
            public void getList() {
                requestReplyList();
            }
        });
        lvReplyListContainer.setAdapter(myWorryReplyAdapter);
        myWorryReplyAdapter.getList();
    }

    private void requestReplyList() {
        LazyRequestService service = new LazyRequestService(getActivity(), MyWorryReplyNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyWorryReplyNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyWorryReplyNetworkService myWorryReplyNetworkService, String token) {
                return myWorryReplyNetworkService.getReplyList(token, myWorryQuestionDTO.getMyWorryQuestionId(), myWorryReplyAdapter.getPagingIndex());
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {

            @Override
            public void onSuccess(ResponseVO responseVO) {
                myWorryReplyAdapter.stopProgress();

                List<MyWorryReplyVO> list = responseVO.getList("myWorryReplyDTOList", MyWorryReplyVO.class);
                int totalCount = responseVO.getInteger("totalCount");

                tvReplyCount.setText(""+totalCount);
                myWorryReplyAdapter.addListWithTotalCount(list, totalCount);
            }

        });
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

                pb.setVisibility(View.GONE);

                myWorryQuestionDetailDTO = responseVO.getValue("myWorryQuestionDetailDTO", MyWorryQuestionDetailDTO.class);
                personalType = myWorryQuestionDetailDTO.getPersonalType();
                myWorryAnswerResultDTO = myWorryQuestionDetailDTO.getMyWorryAnswerResultDTO();

                initAnswer();

                if(personalType.equals(PersonalType.NOT_ANSWERED)) {
                    btnOk.setVisibility(View.VISIBLE);
                    llReplyShowContainer.setVisibility(View.GONE);
                    // updateAnswerView();

                    if (isTwoAnser) {
                        tvA2P1.setVisibility(View.GONE);
                        tvA2P2.setVisibility(View.GONE);
                    } else {
                        tvPercent1.setVisibility(View.GONE);
                        tvPercent2.setVisibility(View.GONE);
                        tvPercent3.setVisibility(View.GONE);
                        tvPercent4.setVisibility(View.GONE);
                    }

                } else {
                    btnOk.setVisibility(View.INVISIBLE);
                    llReplyShowContainer.setVisibility(View.VISIBLE);
                    myWorryAnswerDTO = myWorryQuestionDetailDTO.getMyWorryAnswerDTO();
                    updateAnswerView();

                    if (personalType.equals(PersonalType.ANSWERED)) {
                        if (isTwoAnser) {
                            switch (myWorryAnswerDTO.getAnswerNumber()) {
                                case 1:
                                    btnA2A1.setBackgroundResource(R.drawable.button_question_on);
                                    btnA2A2.setBackgroundResource(R.drawable.button_question_off);
                                    tvA2T1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    tvA2T1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    tvA2P1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    break;
                                case 2:
                                    btnA2A2.setBackgroundResource(R.drawable.button_question_on);
                                    btnA2A1.setBackgroundResource(R.drawable.button_question_off);
                                    tvA2T2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    tvA2T2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    tvA2P2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    break;
                            }
                        } else {
                            switch (myWorryAnswerDTO.getAnswerNumber()) {
                                case 1:
                                    btnAnswer1.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                                    tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    tvAnswer1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    tvPercent1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    break;
                                case 2:
                                    btnAnswer2.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                                    tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    tvAnswer2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    tvPercent2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    break;
                                case 3:
                                    btnAnswer3.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                                    tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    tvAnswer3.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    tvPercent3.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    break;
                                case 4:
                                    btnAnswer4.setBackgroundResource(R.drawable.button_question_on);
                                    btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                                    btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                                    tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                                    tvAnswer4.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    tvPercent4.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                                    break;
                            }
                        }
                    } else {
                        if(isTwoAnser) {
                            btnA2A1.setBackgroundResource(R.drawable.button_question_off);
                            btnA2A2.setBackgroundResource(R.drawable.button_question_off);
                        } else {
                            btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                            btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                            btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                            btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                        }
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
                checkButton(btnAnswer1, tvAnswer1, tvPercent1, 1);
                break;
            case R.id.btn_answer_2:
                checkButton(btnAnswer2, tvAnswer2, tvPercent2,2);
                break;
            case R.id.btn_answer_3:
                checkButton(btnAnswer3, tvAnswer3, tvPercent3,3);
                break;
            case R.id.btn_answer_4:
                checkButton(btnAnswer4, tvAnswer4, tvPercent4,4);
                break;
        }
    }

    @OnClick({R.id.btn_answer_a2_a1, R.id.btn_answer_a2_a2})
    public void checkAnswer2(View view) {
        switch (view.getId()) {
            case kr.puzi.puzi.R.id.btn_answer_a2_a1:
                checkButton(btnA2A1, tvA2T1, tvA2P1, 1);
                break;
            case kr.puzi.puzi.R.id.btn_answer_a2_a2:
                checkButton(btnA2A2, tvA2T2, tvA2P2, 2);
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
//                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out500);
//                btnOk.setAnimation(animation);
//                Animation animation2= AnimationUtils.loadAnimation(getActivity(), R.anim.alpha500);
//                llReplyShowContainer.setAnimation(animation2);

                savePoint = responseVO.getInteger("savedPoint");
                saveCount = responseVO.getInteger("savedCount");

                myWorryAnswerResultDTO = responseVO.getValue("myWorryAnswerResultDTO", MyWorryAnswerResultDTO.class);

                Log.i("AnswerActivity", myWorryAnswerResultDTO.toString());

                boolean state = false;

                if(savePoint > 0) {
                    state = true;
                    PointDialog.load(getActivity(), savePoint, state);
                } else {
                    state = false;
                    PointDialog.load(getActivity(), saveCount, state);
                }

                int count = myWorryQuestionDTO.getAnsweredCount() + 1;
                updateAnswerView();
                tvCount.setText("" + count);

                QuestionFragment.updateResult(myWorryQuestionDTO.getMyWorryQuestionId(), true);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnOk.setVisibility(View.INVISIBLE);
                                llReplyShowContainer.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }, 1000);
            }
        });
    }

    public void updateAnswerView() {
        int index = 0;
        int maxCount = myWorryAnswerResultDTO.getAnswerCount();

        if(maxCount != 0) {
            if(!isTwoAnser) {
                index = myWorryAnswerResultDTO.getAnswerOneCount() * 100 / maxCount;
                tvAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
                tvPercent1.setVisibility(View.VISIBLE);
                tvPercent1.setText(index + "%");
                btnAnswer1.setEnabled(false);

                index = myWorryAnswerResultDTO.getAnswerTwoCount() * 100 / maxCount;
                tvAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvPercent2.setVisibility(View.VISIBLE);
                tvPercent2.setText(index + "%");
                btnAnswer2.setEnabled(false);

                index = myWorryAnswerResultDTO.getAnswerThreeCount() * 100 / maxCount;
                tvAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
                tvPercent3.setVisibility(View.VISIBLE);
                tvPercent3.setText(index + "%");
                btnAnswer3.setEnabled(false);

                index = myWorryAnswerResultDTO.getAnswerFourCount() * 100 / maxCount;
                tvAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
                tvPercent4.setVisibility(View.VISIBLE);
                tvPercent4.setText(index + "%");
                btnAnswer4.setEnabled(false);
            } else {
                index = myWorryAnswerResultDTO.getAnswerOneCount() * 100 / maxCount;
                tvA2T1.setText(myWorryQuestionDTO.getAnswerOne());
                tvA2P1.setVisibility(View.VISIBLE);
                tvA2P1.setText(index + "%");
                btnA2A1.setEnabled(false);

                index = myWorryAnswerResultDTO.getAnswerTwoCount() * 100 / maxCount;
                tvA2T2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvA2P2.setVisibility(View.VISIBLE);
                tvA2P2.setText(index + "%");
                btnA2A2.setEnabled(false);
            }
        } else {
            if(!isTwoAnser) {
                tvAnswer1.setText(myWorryQuestionDTO.getAnswerOne());
                tvPercent1.setVisibility(View.VISIBLE);
                tvPercent1.setText(0 + "%");
                btnAnswer1.setEnabled(false);

                tvAnswer2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvPercent2.setVisibility(View.VISIBLE);
                tvPercent2.setText(0 + "%");
                btnAnswer2.setEnabled(false);

                tvAnswer3.setText(myWorryQuestionDTO.getAnswerThree());
                tvPercent3.setVisibility(View.VISIBLE);
                tvPercent3.setText(0 + "%");
                btnAnswer3.setEnabled(false);

                tvAnswer4.setText(myWorryQuestionDTO.getAnswerFour());
                tvPercent4.setVisibility(View.VISIBLE);
                tvPercent4.setText(0 + "%");
                btnAnswer4.setEnabled(false);
            } else {
                tvA2T1.setText(myWorryQuestionDTO.getAnswerOne());
                tvA2P1.setVisibility(View.VISIBLE);
                tvA2P1.setText(0 + "%");
                btnA2A1.setEnabled(false);

                tvA2T2.setText(myWorryQuestionDTO.getAnswerTwo());
                tvA2P2.setVisibility(View.VISIBLE);
                tvA2P2.setText(0 + "%");
                btnA2A2.setEnabled(false);
            }
        }
    }

    public void checkButton(LinearLayout btn, NotoTextView tv, NotoTextView tvP, int index) {
        if (isSelected) {
            if(selectedCount == index) {
                isSelected = false;
                selectedCount = 0;
                btn.setBackgroundResource(R.drawable.button_question_off);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
            } else {
                isSelected = true;
                selectedCount = index;

                switch (index) {
                    case 1:
                        answer = myWorryQuestionDTO.getAnswerOne();
                        btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                        tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer3.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer4.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        btnA2A2.setBackgroundResource(R.drawable.button_question_off);
                        tvA2T2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvA2T2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        break;
                    case 2:
                        answer = myWorryQuestionDTO.getAnswerTwo();
                        btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                        tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer3.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer4.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        btnA2A1.setBackgroundResource(R.drawable.button_question_off);
                        tvA2T1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvA2T1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        break;
                    case 3:
                        answer = myWorryQuestionDTO.getAnswerThree();
                        btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                        tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer4.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        break;
                    case 4:
                        answer = myWorryQuestionDTO.getAnswerFour();
                        btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                        btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                        tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tvAnswer2.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer3.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        tvAnswer1.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
                        break;
                }
                btn.setBackgroundResource(R.drawable.button_question_on);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
                tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                tvP.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
            }
        } else {
            isSelected = true;
            selectedCount = index;
            switch (index) {
                case 1:
                    answer = myWorryQuestionDTO.getAnswerOne();
                    btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                    tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    btnA2A2.setBackgroundResource(R.drawable.button_question_off);
                    tvA2T2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    break;
                case 2:
                    answer = myWorryQuestionDTO.getAnswerTwo();
                    btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                    tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    btnA2A1.setBackgroundResource(R.drawable.button_question_off);
                    tvA2T1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    break;
                case 3:
                    answer = myWorryQuestionDTO.getAnswerThree();
                    btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer4.setBackgroundResource(R.drawable.button_question_off);
                    tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    break;
                case 4:
                    answer = myWorryQuestionDTO.getAnswerFour();
                    btnAnswer1.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer3.setBackgroundResource(R.drawable.button_question_off);
                    btnAnswer2.setBackgroundResource(R.drawable.button_question_off);
                    tvAnswer1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tvAnswer2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    break;
            }
            btn.setBackgroundResource(R.drawable.button_question_on);
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPuzi));
            tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
            tvP.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
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

    @OnClick(R.id.btn_reply_show)
    public void clickReplyShowButton() {
        if(lvReplyListContainer.getVisibility() == View.VISIBLE) {
            ViewGroup.LayoutParams params = llReplyContainer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llReplyContainer.setLayoutParams(params);
            lvReplyListContainer.setVisibility(View.GONE);
            llReplyWriteContainer.setVisibility(View.GONE);
            llReplyPeddingContainer.setVisibility(View.GONE);
            llReplyListBar.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams params = llReplyContainer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            llReplyContainer.setLayoutParams(params);
//            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
            lvReplyListContainer.setVisibility(View.VISIBLE);
            llReplyWriteContainer.setVisibility(View.VISIBLE);
//            llReplyShowContainer.startAnimation(animation);
//            lvReplyListContainer.startAnimation(animation);
//            llReplyWriteContainer.startAnimation(animation);
            llReplyPeddingContainer.setVisibility(View.VISIBLE);
            llReplyListBar.setVisibility(View.VISIBLE);
        }
    }

    @OnItemLongClick(R.id.lv_reply_list_container)
    public boolean onItemClick(int position) {
        final int finalPosition = position;
        UserVO myInfo = Preference.getMyInfo(getActivity());
        MyWorryReplyVO replyVO = myWorryReplyAdapter.getItem(position);

        boolean useDelete = myInfo.getUserId().equals(replyVO.getWriter());
        MyWorryReplyLongClickDialog.load(getActivity(), replyVO.getMyWorryReplyId(), useDelete, new DialogButtonCallback() {
            @Override
            public void onClick() {
                myWorryReplyAdapter.removeItem(finalPosition);
                tvReplyCount.setText("" + myWorryReplyAdapter.getCount());
            }
        });
        return false;
    }

    @OnClick(R.id.btn_channel_detail_write_reply)
    public void clickWriteButton() {
        String replyToCheck = etWriteReply.getText().toString();
        if(replyToCheck.replaceAll(" ", "").length() == 0) {
            return;
        }

        final String comment = etWriteReply.getText().toString();
        ProgressDialog.show(getActivity());

        LazyRequestService service = new LazyRequestService(this, MyWorryReplyNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyWorryReplyNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyWorryReplyNetworkService myWorryReplyNetworkService, String token) {
                return myWorryReplyNetworkService.write(token, myWorryQuestionDTO.getMyWorryQuestionId(), comment);
            }
        });
        service.enqueue(new CustomCallback(this) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                MyWorryReplyVO replyVO = responseVO.getValue("myWorryReplyDTO", MyWorryReplyVO.class);
                myWorryReplyAdapter.addFirst(replyVO);
                etWriteReply.setText("");
                tvReplyCount.setText("" + myWorryReplyAdapter.getCount());
                closeInputKeyboard(etWriteReply);
            }
        });
    }

    @OnClick(R.id.ibtn_question_close)
    public void closeView() {
        QuestionFragment.isItemClick = false;
        finish();
    }
}
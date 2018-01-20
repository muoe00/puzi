package com.puzi.puzi.ui.today;

import android.os.Bundle;
import android.widget.ImageButton;

import com.puzi.puzi.R;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.customview.NotoTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by juhyun on 2018. 1. 20..
 */

public class AnswerActivity extends BaseActivity {

    private Unbinder unbinder;

    @BindView(R.id.ibtn_question_report)
    ImageButton ibtnReport;
    @BindView(R.id.tv_answer_count)
    NotoTextView tvCount;
    @BindView(R.id.tv_answer_count_limit)
    NotoTextView tvCountLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer);

        unbinder = ButterKnife.bind(this);


    }

    @OnClick(R.id.ibtn_question_close)
    public void closeView() {
        finish();
    }
}
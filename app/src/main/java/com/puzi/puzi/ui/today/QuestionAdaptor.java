package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.company.CompanyVO;
import com.puzi.puzi.biz.myservice.MyTodayQuestionVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.advertisement.AdvertisementListAdapter;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.customview.NotoTextView;
import com.puzi.puzi.utils.PuziUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by juhyun on 2018. 1. 14..
 */

public class QuestionAdaptor extends BaseAdapter {

    private static final int DEFAULT = 0;
    private static final int BONUS = 1;
    private static final int REMAIN_TIME = 2;
    private static final int CLOSE = 3;

    private FragmentActivity activity;
    private LayoutInflater inflater;
    private List<MyTodayQuestionVO> list = new ArrayList();
    private boolean isBonusTime;
    private boolean isMoreQuestion;

    public QuestionAdaptor(FragmentActivity activity, boolean isBonusTime, boolean isMoreQuestion) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.isBonusTime = isBonusTime;
        this.isMoreQuestion = isMoreQuestion;
    }

    public void addQuestionList(List<MyTodayQuestionVO> myTodayQuestionList) {
        list.addAll(myTodayQuestionList);
    }

    public void clean() {
        list = new ArrayList();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public int getViewTypeCount() {
        return 4;
    }

    public int getItemViewType(int position) {
        if(list.size() > 0) {
            if(isBonusTime) {
                // 보너스 타임
                return BONUS;
            } else {
                // 참여하기
                return DEFAULT;
            }
        } else {
            if(isMoreQuestion) {
                // 남은 시간 표시
                return REMAIN_TIME;
            } else {
                // 질문 끝났다는 멘트 표시
                return CLOSE;
            }
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder = null;
        int viewType = getItemViewType(i);

        if(v == null) {
            switch(viewType) {
                case DEFAULT:
                    v = inflater.inflate(R.layout.item_question_init, null);
                    viewHolder = new ViewHolder1(v);
                    v.setTag(viewHolder);
                    break;

                case BONUS:
                    v = inflater.inflate(R.layout.item_question_bonus, null);
                    viewHolder = new ViewHolder2(v);
                    v.setTag(viewHolder);
                    break;

                case REMAIN_TIME:
                    v = inflater.inflate(R.layout.item_question_time, null);
                    viewHolder = new ViewHolder3(v);
                    v.setTag(viewHolder);
                    break;

                case CLOSE:
                    v = inflater.inflate(R.layout.item_question_time, null);
                    viewHolder = new ViewHolder4(v);
                    v.setTag(viewHolder);
                    break;
            }
        } else {
            viewHolder = (ViewHolder1) v.getTag();
        }

        switch(viewType) {
            case DEFAULT:
                break;

            case BONUS:
                break;

            case REMAIN_TIME:
                break;

            case CLOSE:
                break;
        }

        return v;
    }

    public class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class ViewHolder1 extends ViewHolder{
        @BindView(R.id.tv_question_hour) NotoTextView tvHour;
        @BindView(R.id.tv_question_minute) NotoTextView tvMinute;
        @BindView(R.id.ibtn_today_refresh) ImageButton ibtnRefresh;

        public ViewHolder1(View view) {
            super(view);
        }
    }

    public class ViewHolder2 extends ViewHolder{
        @BindView(R.id.tv_question_hour) NotoTextView tvHour;
        @BindView(R.id.tv_question_minute) NotoTextView tvMinute;
        @BindView(R.id.ibtn_today_refresh) ImageButton ibtnRefresh;

        public ViewHolder2(View view){
            super(view);
        }
    }

    public class ViewHolder3 extends ViewHolder{
        @BindView(R.id.tv_question_hour) NotoTextView tvHour;
        @BindView(R.id.tv_question_minute) NotoTextView tvMinute;
        @BindView(R.id.ibtn_today_refresh) ImageButton ibtnRefresh;

        public ViewHolder3(View view) {
            super(view);
        }
    }

    public class ViewHolder4 extends ViewHolder{
        @BindView(R.id.tv_question_hour) NotoTextView tvHour;
        @BindView(R.id.tv_question_minute) NotoTextView tvMinute;
        @BindView(R.id.ibtn_today_refresh) ImageButton ibtnRefresh;

        public ViewHolder4(View view){
            super(view);
        }
    }
}

package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.channel.ChannelAdapter;
import com.puzi.puzi.ui.company.CompanyActivity;
import com.puzi.puzi.ui.customview.NotoTextView;
import com.puzi.puzi.utils.UIUtils;

import butterknife.BindView;

/**
 * Created by juhyun on 2018. 1. 14..
 */

public class WorryAdaptor extends CustomPagingAdapter<MyWorryQuestionDTO> {

    public WorryAdaptor(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
        super(activity, layoutResource, listView, scrollView, listHandler);
    }

    @Override
    protected void setView(Holder holder, MyWorryQuestionDTO item, int position) {
        ViewHolder view = (ViewHolder) holder;

        final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

        view.tvComment.setText(item.getQuestion());
        view.tvComment.setSelected(true);
    }

    @Override
    protected Holder createHolder(View v) {
        return new WorryAdaptor.ViewHolder(v);
    }

    class ViewHolder extends Holder {

        @BindView(R.id.tv_vote_gender) public NotoTextView tvGender;
        @BindView(R.id.tv_vote_age) public NotoTextView tvAge;
        @BindView(R.id.tv_vote_comment) public NotoTextView tvComment;

        public ViewHolder(View view) {
            super(view);
        }
    }
}

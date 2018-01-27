package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.customview.NotoTextView;

import butterknife.BindView;
/**
 * Created by juhyun on 2018. 1. 14..
 */

public class WorryAdaptor extends CustomPagingAdapter<MyWorryQuestionDTO> {

    private boolean mine = false;
    private Activity activity;
    private Context context;
    private CategoryAdapter categoryAdapter;
    private LinearLayoutManager manager;

    public WorryAdaptor(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
        super(activity, layoutResource, listView, scrollView, listHandler, moreBtn);

        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void setMine(boolean mine) {
        this.mine = mine;
        notifyDataSetChanged();
    }

    @Override
    protected void setView(Holder holder, final MyWorryQuestionDTO item, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

        if(!mine) {
            if(position < 5) {
                viewHolder.llBg.setBackgroundResource(R.drawable.bg_vote_item_pink);
            }
        } else {
            viewHolder.llBg.setBackgroundResource(R.drawable.bg_vote_item);
        }

        viewHolder.tvComment.setText(item.getQuestion());
        viewHolder.tvComment.setSelected(true);
        viewHolder.tvPercent.setText("" + item.getAnsweredCount()*100 / item.getTotalAnswerCount());

        viewHolder.progressBar.setMax(item.getTotalAnswerCount());
        viewHolder.progressBar.setProgress(item.getAnsweredCount());

        viewHolder.tvLike.setText("" + item.getLikedCount());

        categoryAdapter = new CategoryAdapter(activity);
        manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        if(item.getCategoryTypeList().size() > 5) {
            categoryAdapter.addOne();
        } else {
            categoryAdapter.addList(item.getCategoryTypeList());
        }
        viewHolder.lvCategory.setHasFixedSize(true);
        viewHolder.lvCategory.setLayoutManager(manager);
        viewHolder.lvCategory.setAdapter(categoryAdapter);
    }

    public void changedState(QuestionFragment.UpdateLike updateLike) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getMyWorryQuestionId() == updateLike.getId()) {
                list.get(i).setLikedByMe(updateLike.isLike());
                list.get(i).setLikedCount(updateLike.getCount());
            }
        }
        Log.i("WorryAdaptor", "changedState()");
        notifyDataSetChanged();
    }

    @Override
    protected Holder createHolder(View v) {
        return new WorryAdaptor.ViewHolder(v);
    }

    class ViewHolder extends Holder {

        @BindView(R.id.ll_question_bg) public LinearLayout llBg;
        @BindView(R.id.fl_vote_like) public RelativeLayout rlLike;
        @BindView(R.id.tv_vote_comment) public NotoTextView tvComment;
        @BindView(R.id.pg_worry) public ProgressBar progressBar;
        @BindView(R.id.tv_worry_progress) public NotoTextView tvPercent;
        @BindView(R.id.lv_category) public RecyclerView lvCategory;
        @BindView(R.id.iv_vote_like) public ImageView ivLike;
        @BindView(R.id.tv_vote_like) public NotoTextView tvLike;

        public ViewHolder(View view) {
            super(view);
        }
    }
}

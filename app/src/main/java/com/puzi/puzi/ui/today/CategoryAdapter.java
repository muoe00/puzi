package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.CategoryType;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.customview.NotoTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by juhyun on 2018. 1. 20..
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Activity activity;
    private List<CategoryType> list;

    public CategoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addList(List<CategoryType> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCategory.setText(list.get(position).getComment());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_vote_category) NotoTextView tvCategory;

        public ViewHolder(View view) {

            super(view);

            ButterKnife.bind(this, view);
        }
    }
}

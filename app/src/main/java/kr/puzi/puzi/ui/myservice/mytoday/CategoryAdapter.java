package kr.puzi.puzi.ui.myservice.mytoday;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.puzi.puzi.biz.myservice.CategoryType;
import kr.puzi.puzi.ui.customview.NotoTextView;

/**
 * Created by juhyun on 2018. 1. 20..
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Activity activity;
    private List<CategoryType> list;

    public CategoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addOne() {
        this.list = new ArrayList<>();
        this.list.add(CategoryType.ENTIRE);
    }

    public void addList(List<CategoryType> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(kr.puzi.puzi.R.layout.item_question_category, parent, false);

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

        @BindView(kr.puzi.puzi.R.id.tv_vote_category)
        NotoTextView tvCategory;

        public ViewHolder(View view) {

            super(view);

            ButterKnife.bind(this, view);
        }
    }
}

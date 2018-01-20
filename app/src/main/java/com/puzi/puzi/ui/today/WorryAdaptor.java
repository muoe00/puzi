package com.puzi.puzi.ui.today;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.myservice.CategoryType;
import com.puzi.puzi.biz.myservice.MyWorryQuestionDTO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.MyServiceNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.HorizontalListView;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.channel.ChannelAdapter;
import com.puzi.puzi.ui.company.CompanyActivity;
import com.puzi.puzi.ui.customview.NotoTextView;
import com.puzi.puzi.utils.PuziUtils;
import com.puzi.puzi.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;

/**
 * Created by juhyun on 2018. 1. 14..
 */

public class WorryAdaptor extends CustomPagingAdapter<MyWorryQuestionDTO> {

    private Activity activity;
    private Context context;
    private CategoryAdapter categoryAdapter;
    private LinearLayoutManager manager;

    public WorryAdaptor(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
        super(activity, layoutResource, listView, scrollView, listHandler);

        this.activity = activity;
        this.context = activity.getApplicationContext();

    }

    @Override
    protected void setView(Holder holder, final MyWorryQuestionDTO item, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        final BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity) activity;

        viewHolder.tvComment.setText(item.getQuestion());
        viewHolder.tvComment.setSelected(true);
        viewHolder.tvPercent.setText("" + item.getAnsweredCount()*100 / item.getTotalAnswerCount());

        viewHolder.progressBar.setMax(item.getTotalAnswerCount());
        viewHolder.progressBar.setProgress(item.getAnsweredCount());



        viewHolder.tvLike.setText("" + item.getLikedCount());
        viewHolder.rlLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLikeCount(item.getMyWorryQuestionId());

                int like = item.getLikedCount();
                boolean isLike = item.isLikedByMe();

                if(isLike) {
                    like--;
                    isLike = false;
                    viewHolder.ivLike.setImageResource(R.drawable.like);
                } else {
                    like++;
                    isLike = true;
                    viewHolder.ivLike.setImageResource(R.drawable.shape);
                }
                viewHolder.tvLike.setText("" + like);

            }
        });

        categoryAdapter = new CategoryAdapter(activity);
        manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryAdapter.addList(item.getCategoryTypeList());
        viewHolder.lvCategory.setHasFixedSize(true);
        viewHolder.lvCategory.setLayoutManager(manager);
        viewHolder.lvCategory.setAdapter(categoryAdapter);
    }

    @Override
    protected Holder createHolder(View v) {
        return new WorryAdaptor.ViewHolder(v);
    }

    class ViewHolder extends Holder {

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

    public void addLikeCount(final int id) {

        LazyRequestService service = new LazyRequestService(activity, MyServiceNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<MyServiceNetworkService>() {
            @Override
            public Call<ResponseVO> execute(MyServiceNetworkService myServiceNetworkService, String token) {
                return myServiceNetworkService.serWorryLike(token, id);
            }
        });
        service.enqueue(new CustomCallback(activity) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                Intent intent = new Intent();
                ((MainActivity) activity).setResult(Activity.RESULT_OK, intent);
            }
        });
    }
}

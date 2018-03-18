package kr.puzi.puzi.ui.thema;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.joooonho.SelectableRoundedImageView;

import butterknife.BindView;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.thema.ThemaDTO;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.customview.NotoTextView;

/**
 * Created by juhyun on 2018. 3. 18..
 */

public class ThemaAdapter extends CustomPagingAdapter<ThemaDTO> {

    private Activity activity;
    private Context context;
    private LinearLayoutManager manager;

    public ThemaAdapter(Activity activity, int layoutResource, int layoutResource2, int layoutResource3, int layoutResource4, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
        super(activity, layoutResource, layoutResource2, layoutResource3, layoutResource4, 0, listView, scrollView, listHandler, moreBtn);
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if(VIEW_LIST != type) {
            return type;
        }

        ThemaDTO themaDTO = getItem(position);

        switch (themaDTO.getDegreeType()) {
            case MAX:
                return VIEW_LIST;
            case MIN:
                return VIEW_LIST_2;
            case AVERAGE:
                return VIEW_LIST_3;
            case LACK:
                return VIEW_LIST_4;
            default:
                return VIEW_LIST_4;
        }
    }

    @Override
    protected void setView(Holder holder, final ThemaDTO item, int position) {
        final ThemaAdapter.ViewHolder viewHolder = (ThemaAdapter.ViewHolder) holder;


    }

    @Override
    public void setView2(Holder holder, final ThemaDTO item, int position) {
        final ThemaAdapter.ViewHolder viewHolder = (ThemaAdapter.ViewHolder) holder;


    }

    @Override
    public void setView3(Holder holder, final ThemaDTO item, int position) {
        final ThemaAdapter.ViewHolder3 viewHolder = (ThemaAdapter.ViewHolder3) holder;

    }

    @Override
    public void setView4(Holder holder, final ThemaDTO item, int position) {
        final ThemaAdapter.ViewHolder4 viewHolder = (ThemaAdapter.ViewHolder4) holder;

    }


    @Override
    protected Holder createHolder(View v) {
        return new ThemaAdapter.ViewHolder(v);
    }

    @Override
    protected Holder createHolder2(View v) {
        return new ThemaAdapter.ViewHolder2(v);
    }

    @Override
    protected Holder createHolder3(View v) {
        return new ThemaAdapter.ViewHolder3(v);
    }

    @Override
    protected Holder createHolder4(View v) {
        return new ThemaAdapter.ViewHolder4(v);
    }

    class ViewHolder extends Holder {

        @BindView(R.id.iv_thema_background)
        SelectableRoundedImageView ivBack;
        @BindView(R.id.tv_thema_item_percent)
        NotoTextView tvPercent;
        @BindView(R.id.tv_thema_title)
        NotoTextView tvTitle;


        public ViewHolder(View view) {
            super(view);
        }
    }

    class ViewHolder2 extends Holder {

        public ViewHolder2(View view) {
            super(view);
        }
    }

    class ViewHolder3 extends Holder {

        public ViewHolder3(View view) {
            super(view);
        }
    }

    class ViewHolder4 extends Holder {

        public ViewHolder4(View view) {
            super(view);
        }
    }
}

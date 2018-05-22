package kr.puzi.puzi.ui.theme;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.theme.ThemeDTO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.customview.NotoTextView;

/**
 * Created by juhyun on 2018. 3. 18..
 */

public class ThemeAdapter extends CustomPagingAdapter<ThemeDTO> {

    private Activity activity;
    private Context context;
    private LinearLayoutManager manager;

    public ThemeAdapter(Activity activity, int layoutResource, int layoutResource2, int layoutResource3, int layoutResource4, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
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

        ThemeDTO themeDTO = getItem(position);


        if(themeDTO.getDegreeType() != null) {

            switch (themeDTO.getDegreeType()) {
                case MAX:
                    Log.i("ThemeAdapter", "MAX : " + themeDTO.toString());
                    return VIEW_LIST;
                case AVERAGE:
                    Log.i("ThemeAdapter", "AVERAGE : " + themeDTO.toString());
                    return VIEW_LIST_2;
                case MIN:
                    Log.i("ThemeAdapter", "MIN : " + themeDTO.toString());
                    return VIEW_LIST_3;
                case LACK:
                    Log.i("ThemeAdapter", "LACK : " + themeDTO.toString());
                    return VIEW_LIST_4;
                default:
                    Log.i("ThemeAdapter", "DEFAULT : " + themeDTO.toString());
                    return VIEW_LIST_4;
            }
        } else {
            Log.i("ThemeAdapter", "ELSE : " + themeDTO.toString());
            return VIEW_LIST_4;
        }
    }

    @Override
    protected void setView(Holder holder, final ThemeDTO item, int position) {
        final ThemeAdapter.ViewHolder viewHolder = (ThemeAdapter.ViewHolder) holder;

        BitmapUIL.load(item.getThemeBackground(), viewHolder.ivBack);
        viewHolder.tvPercent.setText(String.valueOf(item.getRate()));
        viewHolder.tvTitle.setText(item.getTargetMax());
        viewHolder.tvMax.setText(item.getTargetMax());
        viewHolder.tvMin.setText(item.getTargetMin());
        viewHolder.tvCount.setText(String.valueOf(item.getTotalUserCount()));

    }

    @Override
    public void setView2(Holder holder, final ThemeDTO item, int position) {
        final ThemeAdapter.ViewHolder2 viewHolder = (ThemeAdapter.ViewHolder2) holder;

        BitmapUIL.load(item.getThemeBackground(), viewHolder.ivBack);
        viewHolder.tvPercent.setText(String.valueOf(item.getRate()));
        // viewHolder.tvTitle.setText(item.getTargetMax());
        viewHolder.tvMax.setText(item.getTargetMax());
        viewHolder.tvMin.setText(item.getTargetMin());
        viewHolder.tvCount.setText(String.valueOf(item.getTotalUserCount()));
    }

    @Override
    public void setView3(Holder holder, final ThemeDTO item, int position) {
        final ThemeAdapter.ViewHolder3 viewHolder = (ThemeAdapter.ViewHolder3) holder;

        // BitmapUIL.load(item.getThemeBackground(), viewHolder.ivBack);
        viewHolder.tvMax.setText(item.getTargetMax());
        viewHolder.tvMin.setText(item.getTargetMin());
        viewHolder.tvCount.setText(String.valueOf(item.getTotalUserCount()));

    }

    @Override
    public void setView4(Holder holder, final ThemeDTO item, int position) {
        final ThemeAdapter.ViewHolder4 viewHolder = (ThemeAdapter.ViewHolder4) holder;

        BitmapUIL.load(item.getThemeBackground(), viewHolder.ivBack);
        viewHolder.tvMax.setText(item.getTargetMax());
        viewHolder.tvMin.setText(item.getTargetMin());
        viewHolder.tvCount.setText(String.valueOf(item.getTotalUserCount()));

    }

    @Override
    protected Holder createHolder(View v) {
        return new ThemeAdapter.ViewHolder(v);
    }

    @Override
    protected Holder createHolder2(View v) {
        return new ThemeAdapter.ViewHolder2(v);
    }

    @Override
    protected Holder createHolder3(View v) {
        return new ThemeAdapter.ViewHolder3(v);
    }

    @Override
    protected Holder createHolder4(View v) {
        return new ThemeAdapter.ViewHolder4(v);
    }

    /* MAX */
    class ViewHolder extends Holder {

        @BindView(R.id.iv_theme_max_background)
        SelectableRoundedImageView ivBack;
        @BindView(R.id.tv_theme_max_item_percent)
        NotoTextView tvPercent;
        @BindView(R.id.tv_theme_max_title)
        NotoTextView tvTitle;
        @BindView(R.id.tv_theme_max_versus_max)
        NotoTextView tvMax;
        @BindView(R.id.tv_theme_max_versus_min)
        NotoTextView tvMin;
        @BindView(R.id.tv_theme_max_item_count)
        NotoTextView tvCount;

        public ViewHolder(View view) {
            super(view);
        }
    }

    /* AVERAGE */
    class ViewHolder2 extends Holder {

        @BindView(R.id.iv_theme_average_background)
        SelectableRoundedImageView ivBack;
        @BindView(R.id.tv_theme_average_item_percent)
        NotoTextView tvPercent;
        /*@BindView(R.id.tv_theme_average_title)
        NotoTextView tvTitle;*/
        @BindView(R.id.tv_theme_average_versus_max)
        NotoTextView tvMax;
        @BindView(R.id.tv_theme_average_versus_min)
        NotoTextView tvMin;
        @BindView(R.id.tv_theme_average_item_count)
        NotoTextView tvCount;

        public ViewHolder2(View view) {
            super(view);
        }
    }

    /* MIN */
    class ViewHolder3 extends Holder {

        @BindView(R.id.iv_theme_min_background)
        SelectableRoundedImageView ivBack;
        @BindView(R.id.tv_theme_min_title)
        NotoTextView tvTitle;
        @BindView(R.id.tv_theme_min_versus_max)
        NotoTextView tvMax;
        @BindView(R.id.tv_theme_min_versus_min)
        NotoTextView tvMin;
        @BindView(R.id.tv_theme_min_item_count)
        NotoTextView tvCount;

        public ViewHolder3(View view) {
            super(view);
        }
    }

    /* LACK */
    class ViewHolder4 extends Holder {

        @BindView(R.id.iv_theme_lack_background)
        SelectableRoundedImageView ivBack;
        @BindView(R.id.tv_theme_lack_versus_max)
        NotoTextView tvMax;
        @BindView(R.id.tv_theme_lack_versus_min)
        NotoTextView tvMin;
        @BindView(R.id.tv_theme_lack_item_count)
        NotoTextView tvCount;

        public ViewHolder4(View view) {
            super(view);
        }
    }
}

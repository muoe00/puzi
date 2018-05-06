package kr.puzi.puzi.ui.theme;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.theme.ThemeDTO;
import kr.puzi.puzi.biz.theme.ThemeDetailDTO;
import kr.puzi.puzi.biz.theme.ThemeReplyAdapter;
import kr.puzi.puzi.biz.theme.ThemeReplyVO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.ThemeNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.customview.NotoTextView;
import kr.puzi.puzi.ui.customview.RobotoTextView;
import kr.puzi.puzi.ui.myservice.myworry.MyWorryReplyLongClickDialog;
import retrofit2.Call;

import static kr.puzi.puzi.utils.TextUtils.addComma;

/**
 * Created by juhyun on 2018. 3. 18..
 */

public class ThemeDetailActivity extends BaseActivity {

    Unbinder unbinder;

    @BindView(R.id.chart_theme)
    LineChart chartMine;
    @BindView(R.id.tv_theme_id)
    NotoTextView tvId;
    @BindView(R.id.tv_theme_percent)
    NotoTextView tvPercent;
    @BindView(R.id.tv_theme_count)
    NotoTextView tvCount;
    @BindView(R.id.tv_theme_user_count)
    NotoTextView tvUserCount;
    @BindView(R.id.tv_theme_detail_title)
    RobotoTextView tvTitle;
    @BindView(R.id.ll_reply_show_container)
    LinearLayout llReplyShowContainer;
    @BindView(R.id.lv_reply_list_container)
    ListView lvReplyListContainer;
    @BindView(R.id.ll_reply_write_container)
    LinearLayout llReplyWriteContainer;
    @BindView(R.id.ll_reply_container)
    LinearLayout llReplyContainer;
    @BindView(R.id.ll_reply_list_bar)
    LinearLayout llReplyListBar;
    @BindView(R.id.et_thema_detail_write_reply)
    EditText etWriteReply;
    @BindView(R.id.tv_theme_detail_my_score)
    NotoTextView tvMyScore;
    @BindView(R.id.tv_theme_detail_my_score_count)
    NotoTextView tvMyScoreCount;
    @BindView(R.id.tv_theme_detail_whole_score)
    NotoTextView tvWholeScore;
    @BindView(R.id.tv_theme_detail_whole_score_count)
    NotoTextView tvWholeScoreCount;
    @BindView(R.id.tv_theme_detail_max_score)
    NotoTextView tvMaxScore;
    @BindView(R.id.tv_theme_detail_max_score_count)
    NotoTextView tvMaxScoreCount;
    @BindView(R.id.tv_theme_detail_min_score)
    NotoTextView tvMinScore;
    @BindView(R.id.tv_theme_detail_min_score_count)
    NotoTextView tvMinScoreCount;
    @BindView(R.id.pb_theme)
    ProgressBar pvTheme;
    @BindView(R.id.sv_thema_detail)
    ScrollView svThema;

    private UserVO userVO;
    private ThemeDTO themeDTO;
    private ThemeDetailDTO themeDetailDTO;
    private ThemeReplyAdapter themeReplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detail);

        unbinder = ButterKnife.bind(this);

        themeDTO = (ThemeDTO) getIntent().getExtras().getSerializable("themeDTO");
        tvTitle.setText(themeDTO.getTargetMin() + " vs " + themeDTO.getTargetMax());
        tvPercent.setText("상위 " + themeDTO.getRate() + "%");

        switch (themeDTO.getDegreeType()) {
            case MAX:
                tvCount.setText(themeDTO.getTargetMax() + "입니다.");
            case MIN:
                tvCount.setText(themeDTO.getTargetMin() + "입니다.");
            case AVERAGE:
                tvCount.setText("일반인입니다.");
        }

        userVO = Preference.getMyInfo(getActivity());
        tvId.setText(userVO.getUserId() + "님은");

        tvUserCount.setText("대상인원 " + addComma(themeDTO.getTotalUserCount()) + "명");

        getThemeDetail();
        getReply();
    }

    public void getThemeDetail() {
        LazyRequestService service = new LazyRequestService(getActivity(), ThemeNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<ThemeNetworkService>() {
            @Override
            public Call<ResponseVO> execute(ThemeNetworkService themeNetworkService, String token) {
                return themeNetworkService.getDetail(token, themeDTO.getThemeInfoId());
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                themeDetailDTO = responseVO.getValue("themeDetailDTO", ThemeDetailDTO.class);
                Log.i("ThemeDetailActivity", "themeDetailDTO : " + themeDetailDTO.toString());

                initCount(themeDetailDTO);


            }
        });
    }

    private void getReply() {
        themeReplyAdapter = new ThemeReplyAdapter(getActivity(), R.layout.item_thema_reply, lvReplyListContainer, svThema, new CustomPagingAdapter.ListHandler() {
            @Override
            public void getList() {
                requestReplyList();
            }
        });
        lvReplyListContainer.setAdapter(themeReplyAdapter);
        themeReplyAdapter.getList();
    }

    private void requestReplyList() {
        LazyRequestService service = new LazyRequestService(getActivity(), ThemeNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<ThemeNetworkService>() {
            @Override
            public Call<ResponseVO> execute(ThemeNetworkService themeNetworkService, String token) {
                return themeNetworkService.getReplyList(token, themeDTO.getThemeInfoId(), themeReplyAdapter.getPagingIndex());
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                themeReplyAdapter.stopProgress();

                List<ThemeReplyVO> list = responseVO.getList("themeReplyDTOList", ThemeReplyVO.class);
                int totalCount = responseVO.getInteger("totalCount");

                Log.i("ThemeDetailActivity", "reply totalCount : " + totalCount);

                themeReplyAdapter.addListWithTotalCount(list, totalCount);
            }
        });
    }

    @OnClick(R.id.btn_thema_detail_write_reply)
    public void clickWriteButton() {
        String replyToCheck = etWriteReply.getText().toString();
        if(replyToCheck.replaceAll(" ", "").length() == 0) {
            return;
        }

        final String comment = etWriteReply.getText().toString();
        ProgressDialog.show(getActivity());

        LazyRequestService service = new LazyRequestService(this, ThemeNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<ThemeNetworkService>() {
            @Override
            public Call<ResponseVO> execute(ThemeNetworkService themeNetworkService, String token) {
                return themeNetworkService.write(token, themeDTO.getThemeInfoId(), comment);
            }
        });
        service.enqueue(new CustomCallback(this) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                ThemeReplyVO replyVO = responseVO.getValue("themeReplyDTO", ThemeReplyVO.class);
                themeReplyAdapter.addFirst(replyVO);
                etWriteReply.setText("");
                closeInputKeyboard(etWriteReply);
            }
        });
    }

    @OnItemLongClick(R.id.lv_reply_list_container)
    public boolean onItemClick(int position) {
        final int finalPosition = position;
        UserVO myInfo = Preference.getMyInfo(getActivity());
        ThemeReplyVO replyVO = themeReplyAdapter.getItem(position);

        boolean useDelete = myInfo.getUserId().equals(replyVO.getWriter());
        MyWorryReplyLongClickDialog.load(getActivity(), replyVO.getMyWorryReplyId(), useDelete, new DialogButtonCallback() {
            @Override
            public void onClick() {
                themeReplyAdapter.removeItem(finalPosition);
                // tvReplyCount.setText("" + myWorryReplyAdapter.getCount());
            }
        });
        return false;
    }

    public void initCount(ThemeDetailDTO themeDetailDTO) {
        tvMyScore.setText("나의 " + themeDetailDTO.getTargetMax() + " 지수");

        tvMyScoreCount.setText(String.format("%.1f", themeDetailDTO.getMyAverageScore()));
        tvWholeScoreCount.setText(String.format("%.1f", themeDetailDTO.getTotalAverageScore()));

        tvMaxScore.setText(themeDetailDTO.getTargetMax() + " 평균지수");
        tvMaxScoreCount.setText(String.format("%.1f", themeDetailDTO.getTotalMaxAverageScore()));
        tvMinScore.setText(themeDetailDTO.getTargetMin() + " 평균지수");
        tvMinScoreCount.setText(String.format("%.1f", themeDetailDTO.getTotalMinAverageScore()));
    }

    public void initChart(ThemeDetailDTO themeDetailDTO) {

        ArrayList<Entry> entriesEntry = new ArrayList<>();

        entriesEntry.add(new Entry(4f, 0));
        entriesEntry.add(new Entry(2f, 1));
        entriesEntry.add(new Entry(15f, 2));
        entriesEntry.add(new Entry(9f, 3));
        entriesEntry.add(new Entry(1f, 4));
        entriesEntry.add(new Entry(8f, 5));

        LineDataSet lineDataSet = new LineDataSet(entriesEntry, "");
        LineData lineData = new LineData(lineDataSet);

        chartMine.setData(lineData);
        chartMine.setDrawGridBackground(false);

        chartMine.setTouchEnabled(false);
        chartMine.setDoubleTapToZoomEnabled(false);
        chartMine.getXAxis().setDrawAxisLine(false);
        chartMine.getXAxis().setDrawGridLines(false);
        chartMine.getAxisLeft().setDrawGridLines(false);
        chartMine.getAxisLeft().setDrawAxisLine(false);
        chartMine.getAxisRight().setDrawGridLines(false);
        chartMine.getAxisRight().setDrawAxisLine(false);

        chartMine.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doAnimationGoLeft();
    }
}

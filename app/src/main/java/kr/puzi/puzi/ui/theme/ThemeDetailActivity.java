package kr.puzi.puzi.ui.theme;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.theme.ThemeDTO;
import kr.puzi.puzi.biz.theme.ThemeReplyAdapter;
import kr.puzi.puzi.biz.theme.ThemeReplyVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.ThemeNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseActivity;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juhyun on 2018. 3. 18..
 */

public class ThemeDetailActivity extends BaseActivity {

    Unbinder unbinder;

    @BindView(R.id.chart_theme_mine)
    LineChart chartMine;

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

    private ThemeDTO themeDTO;
    private ThemeReplyAdapter themeReplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detail);

        unbinder = ButterKnife.bind(this);

        themeDTO = (ThemeDTO) getIntent().getExtras().getSerializable("themeDTO");

        getReply();
    }

    private void getReply() {
        themeReplyAdapter = new ThemeReplyAdapter(getActivity(), lvReplyListContainer, new CustomPagingAdapter.ListHandler() {
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

    public void initComponents() {

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

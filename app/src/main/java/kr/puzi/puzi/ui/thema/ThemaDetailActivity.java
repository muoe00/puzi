package kr.puzi.puzi.ui.thema;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.ui.base.BaseActivity;

/**
 * Created by juhyun on 2018. 3. 18..
 */

public class ThemaDetailActivity extends BaseActivity {

    private Unbinder unbinder;

    @BindView(R.id.chart_thema_mine)
    LineChart chartMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thema_detail);

        unbinder = ButterKnife.bind(this);


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

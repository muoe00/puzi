package kr.puzi.puzi.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.theme.ThemeDTO;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.ThemeNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.customview.NotoTextView;
import retrofit2.Call;

import java.util.List;

/**
 * Created by juhyun on 2018. 3. 17..
 */

public class ThemeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    Unbinder unbinder;

    @BindView(R.id.srl_theme_container)
    SwipeRefreshLayout srlContainer;
    @BindView(R.id.sv_theme)
    ScrollView svTheme;
    @BindView(R.id.lv_theme)
    ListView lvTheme;
    @BindView(R.id.tv_theme_id)
    NotoTextView tvId;
    @BindView(R.id.tv_theme_point)
    NotoTextView tvPoint;
    @BindView(R.id.tv_theme_point_title)
    NotoTextView tvPointTitle;
    @BindView(R.id.ibtn_setting)
    ImageButton ibtnSetting;

    private long mLastClickTime = 0;

    private UserVO userVO;
    private List<ThemeDTO> themeList;
    private ThemeAdapter themeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        unbinder = ButterKnife.bind(this, view);

        userVO = Preference.getMyInfo(getActivity());
        tvId.setText(userVO.getUserId());

        initComponents();

        return view;
    }

    public void getThemeList() {
        final LazyRequestService service = new LazyRequestService(getActivity(), ThemeNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<ThemeNetworkService>() {
            @Override
            public Call<ResponseVO> execute(ThemeNetworkService themeNetworkService, String token) {
                return themeNetworkService.getList(token);
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                themeAdapter.stopProgress();

                themeList = responseVO.getList("themeDTOList", ThemeDTO.class);
                int totalCount = responseVO.getInteger("totalCount");

                Log.i("ThemeFragment", themeList.get(0).toString());

                themeAdapter.addList(themeList);

                Log.i("ThemeFragment", "themeList.size() : " + themeList.size());
                Log.i("ThemeFragment", "totalCount : " + totalCount);
            }

            @Override
            public void onFail(ResponseVO responseVO) {
                super.onFail(responseVO);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("ThemeFragment", "1 onItemClick : " + position);

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Log.i("ThemeFragment", "2 onItemClick : " + position);

        ThemeDTO themeDTO = themeAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), ThemeDetailActivity.class);
        intent.putExtra("themeDTO", themeDTO);
        startActivity(intent);
        doAnimationGoRight();
    }

    public void initComponents() {
        themeAdapter = new ThemeAdapter(getActivity(), R.layout.item_theme_list_max,kr.puzi.puzi.R.layout.item_theme_list_average, R.layout.item_theme_list_min, R.layout.item_theme_list_lack,
            lvTheme,
            svTheme, new CustomPagingAdapter.ListHandler() {
            @Override
            public void getList() {
                themeAdapter.startProgressWithScrollDown();
                getThemeList();
            }
        }, true);
        themeAdapter.setMore(false);
        themeAdapter.getList();
        lvTheme.setAdapter(themeAdapter);
        lvTheme.setOnItemClickListener(this);

        srlContainer.setColorSchemeResources(kr.puzi.puzi.R.color.colorPuzi);
        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                themeAdapter.clean();
                themeAdapter.initPagingIndex();
                themeAdapter.increasePagingIndex();
                themeAdapter.startProgress();
                getThemeList();
                srlContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

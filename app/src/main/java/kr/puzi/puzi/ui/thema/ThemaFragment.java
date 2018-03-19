package kr.puzi.puzi.ui.thema;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.thema.ThemaDTO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.ThemaNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.base.BaseFragment;
import kr.puzi.puzi.ui.customview.NotoTextView;
import retrofit2.Call;

/**
 * Created by juhyun on 2018. 3. 17..
 */

public class ThemaFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    Unbinder unbinder;

    @BindView(R.id.sv_thema)
    ScrollView svThema;
    @BindView(R.id.lv_thema)
    ListView lvThema;
    @BindView(R.id.tv_thema_id)
    NotoTextView tvId;
    @BindView(R.id.tv_thema_point)
    NotoTextView tvPoint;
    @BindView(R.id.tv_thema_point_title)
    NotoTextView tvPointTitle;
    @BindView(R.id.ibtn_setting)
    ImageButton ibtnSetting;

    private List<ThemaDTO> themaList;
    private ThemaAdapter themaAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thema, container, false);
        unbinder = ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    public void getThemaList() {
        Log.i("ThemaFragment","getThemaList");
        final LazyRequestService service = new LazyRequestService(getActivity(), ThemaNetworkService.class);
        service.method(new LazyRequestService.RequestMothod<ThemaNetworkService>() {
            @Override
            public Call<ResponseVO> execute(ThemaNetworkService themaNetworkService, String token) {
                return themaNetworkService.getList(token);
            }
        });
        service.enqueue(new CustomCallback(getActivity()) {
            @Override
            public void onSuccess(ResponseVO responseVO) {
                themaAdapter.stopProgress();

                themaList = responseVO.getList("themeDTOList", ThemaDTO.class);
                int totalCount = responseVO.getInteger("totalCount");

                Log.i("ThemaFragment", responseVO.toString());

                themaAdapter.addListWithTotalCount(themaList, totalCount);

                Log.i("ThemaFragment", "myWorryAdaptor.getCount() : " + themaAdapter.getCount());
                Log.i("ThemaFragment", "totalCount : " + totalCount);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void initComponents() {
        themaAdapter = new ThemaAdapter(getActivity(), R.layout.item_thema_list_max, R.layout.item_thema_list_min, kr.puzi.puzi.R.layout.item_thema_list_average, R.layout.item_thema_list_lack, lvThema, svThema, new CustomPagingAdapter.ListHandler() {
            @Override
            public void getList() {
                themaAdapter.startProgressWithScrollDown();
                getThemaList();
            }
        }, true);
        themaAdapter.setMore(false);
        themaAdapter.getList();
        lvThema.setAdapter(themaAdapter);
        lvThema.setOnItemClickListener(this);
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

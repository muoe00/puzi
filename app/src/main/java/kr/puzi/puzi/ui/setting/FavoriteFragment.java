package kr.puzi.puzi.ui.setting;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.puzi.puzi.biz.user.FavoriteType;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.cache.Preference;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.MainActivity;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

import static kr.puzi.puzi.biz.user.FavoriteType.BEAUTY;

/**
 * Created by 170605 on 2017-10-23.
 */

public class FavoriteFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(kr.puzi.puzi.R.id.btn_setting_beauty) public Button btnBeauty;
	@BindView(kr.puzi.puzi.R.id.btn_setting_shopping) public Button btnShop;
	@BindView(kr.puzi.puzi.R.id.btn_setting_game) public Button btnGame;
	@BindView(kr.puzi.puzi.R.id.btn_setting_eat) public Button btnEat;
	@BindView(kr.puzi.puzi.R.id.btn_setting_tour) public Button btnTour;
	@BindView(kr.puzi.puzi.R.id.btn_setting_finance) public Button btnFinance;
	@BindView(kr.puzi.puzi.R.id.btn_setting_culture) public Button btnCulture;

	private List<FavoriteType> favoritesList = new ArrayList<FavoriteType>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(kr.puzi.puzi.R.layout.fragment_setting_favorites, container, false);
		unbinder = ButterKnife.bind(this, view);

		UserVO myInfo = Preference.getMyInfo(getActivity());
		List<String> list = myInfo.getFavoriteTypeList();
		for(String typeName : list) {
			FavoriteType type = FavoriteType.valueOf(typeName);
			favoritesList.add(type);

			switch (type) {
				case BEAUTY:
					btnBeauty.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnBeauty.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				case SHOPPING:
					btnShop.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnShop.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				case GAME:
					btnGame.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnGame.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				case EAT:
					btnEat.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnEat.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				case TOUR:
					btnTour.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnTour.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				case FINANCE:
					btnFinance.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnFinance.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				case CULTURE:
					btnCulture.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
					btnCulture.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
					break;
				default:
					break;
			}
		}

		return view;
	}

	@OnClick({kr.puzi.puzi.R.id.btn_setting_beauty, kr.puzi.puzi.R.id.btn_setting_shopping, kr.puzi.puzi.R.id.btn_setting_game, kr.puzi.puzi.R.id.btn_setting_eat,
		kr.puzi.puzi.R.id.btn_setting_tour, kr.puzi.puzi.R.id.btn_setting_finance, kr.puzi.puzi.R.id.btn_setting_culture})
	public void checkFavorites(View view) {
		switch (view.getId()) {
			case kr.puzi.puzi.R.id.btn_setting_beauty:
				checkList(BEAUTY, btnBeauty);
				break;
			case kr.puzi.puzi.R.id.btn_setting_shopping:
				checkList(FavoriteType.SHOPPING, btnShop);
				break;
			case kr.puzi.puzi.R.id.btn_setting_game:
				checkList(FavoriteType.GAME, btnGame);
				break;
			case kr.puzi.puzi.R.id.btn_setting_eat:
				checkList(FavoriteType.EAT, btnEat);
				break;
			case kr.puzi.puzi.R.id.btn_setting_tour:
				checkList(FavoriteType.TOUR, btnTour);
				break;
			case kr.puzi.puzi.R.id.btn_setting_finance:
				checkList(FavoriteType.FINANCE, btnFinance);
				break;
			case kr.puzi.puzi.R.id.btn_setting_culture:
				checkList(FavoriteType.CULTURE, btnCulture);
				break;
			default:
				break;
		}
	}

	public void checkList(FavoriteType type, Button btn) {
		if(favoritesList.contains(type)) {
			favoritesList.remove(type);
			btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_off);
			btn.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorTextGray));
		} else {
			favoritesList.add(type);
			btn.setBackgroundResource(kr.puzi.puzi.R.drawable.button_favorite_on);
			btn.setTextColor(ContextCompat.getColor(getContext(), kr.puzi.puzi.R.color.colorPuzi));
		}

		Log.i("INFO", "favoritesList : " + favoritesList.toString());
	}

	@OnClick(kr.puzi.puzi.R.id.btn_setting_user_modify)
	public void modifyInformation() {
		if(favoritesList.size() < 3) {
			if(favoritesList.isEmpty()) {
				Toast.makeText(getContext(), "관심분야를 선택하세요", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), "관심분야를 3가지 이상 선택하세요", Toast.LENGTH_SHORT).show();
			}
		} else {
			requestModification();
		}
	}

	public void requestModification() {
		ProgressDialog.show(getActivity());

		LazyRequestService service = new LazyRequestService(getActivity(), SettingNetworkService.class);
		service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
			@Override
			public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
				return settingNetworkService.updateFavorites(token, favoritesList);
			}
		});
		service.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
				MainActivity.needToUpdateUserVO = true;
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

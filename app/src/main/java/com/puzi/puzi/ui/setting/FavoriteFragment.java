package com.puzi.puzi.ui.setting;

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
import com.puzi.puzi.R;
import com.puzi.puzi.biz.user.FavoriteType;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.LazyRequestService;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.MainActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

import static com.puzi.puzi.biz.user.FavoriteType.BEAUTY;

/**
 * Created by 170605 on 2017-10-23.
 */

public class FavoriteFragment extends BaseFragment {

	Unbinder unbinder;

	@BindView(R.id.btn_setting_beauty) public Button btnBeauty;
	@BindView(R.id.btn_setting_shopping) public Button btnShop;
	@BindView(R.id.btn_setting_game) public Button btnGame;
	@BindView(R.id.btn_setting_eat) public Button btnEat;
	@BindView(R.id.btn_setting_tour) public Button btnTour;
	@BindView(R.id.btn_setting_finance) public Button btnFinance;
	@BindView(R.id.btn_setting_culture) public Button btnCulture;

	private List<FavoriteType> favoritesList = new ArrayList<FavoriteType>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_setting_favorites, container, false);
		unbinder = ButterKnife.bind(this, view);

		UserVO myInfo = Preference.getMyInfo(getActivity());
		List<String> list = myInfo.getFavoriteTypeList();
		for(String typeName : list) {
			FavoriteType type = FavoriteType.valueOf(typeName);
			favoritesList.add(type);

			switch (type) {
				case BEAUTY:
					btnBeauty.setBackgroundResource(R.drawable.button_favorite_on);
					btnBeauty.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				case SHOPPING:
					btnShop.setBackgroundResource(R.drawable.button_favorite_on);
					btnShop.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				case GAME:
					btnGame.setBackgroundResource(R.drawable.button_favorite_on);
					btnGame.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				case EAT:
					btnEat.setBackgroundResource(R.drawable.button_favorite_on);
					btnEat.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				case TOUR:
					btnTour.setBackgroundResource(R.drawable.button_favorite_on);
					btnTour.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				case FINANCE:
					btnFinance.setBackgroundResource(R.drawable.button_favorite_on);
					btnFinance.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				case CULTURE:
					btnCulture.setBackgroundResource(R.drawable.button_favorite_on);
					btnCulture.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
					break;
				default:
					break;
			}
		}

		return view;
	}

	@OnClick({R.id.btn_setting_beauty, R.id.btn_setting_shopping, R.id.btn_setting_game, R.id.btn_setting_eat,
		R.id.btn_setting_tour, R.id.btn_setting_finance, R.id.btn_setting_culture})
	public void checkFavorites(View view) {
		switch (view.getId()) {
			case R.id.btn_setting_beauty:
				checkList(BEAUTY, btnBeauty);
				break;
			case R.id.btn_setting_shopping:
				checkList(FavoriteType.SHOPPING, btnShop);
				break;
			case R.id.btn_setting_game:
				checkList(FavoriteType.GAME, btnGame);
				break;
			case R.id.btn_setting_eat:
				checkList(FavoriteType.EAT, btnEat);
				break;
			case R.id.btn_setting_tour:
				checkList(FavoriteType.TOUR, btnTour);
				break;
			case R.id.btn_setting_finance:
				checkList(FavoriteType.FINANCE, btnFinance);
				break;
			case R.id.btn_setting_culture:
				checkList(FavoriteType.CULTURE, btnCulture);
				break;
			default:
				break;
		}
	}

	public void checkList(FavoriteType type, Button btn) {
		if(favoritesList.contains(type)) {
			favoritesList.remove(type);
			btn.setBackgroundResource(R.drawable.button_favorite_off);
			btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextGray));
		} else {
			favoritesList.add(type);
			btn.setBackgroundResource(R.drawable.button_favorite_on);
			btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
		}

		Log.i("INFO", "favoritesList : " + favoritesList.toString());
	}

	@OnClick(R.id.btn_setting_user_modify)
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

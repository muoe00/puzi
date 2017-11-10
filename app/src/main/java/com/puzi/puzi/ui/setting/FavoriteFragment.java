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
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragment;
import retrofit2.Call;

import java.util.ArrayList;

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

	private ArrayList<String> favoritesList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_setting_favorites, container, false);
		unbinder = ButterKnife.bind(this, view);

		return view;
	}

	@OnClick({R.id.btn_setting_beauty, R.id.btn_setting_shopping, R.id.btn_setting_game, R.id.btn_setting_eat,
		R.id.btn_setting_tour, R.id.btn_setting_finance, R.id.btn_setting_culture})
	public void checkFavorites(View view) {
		switch (view.getId()) {
			case R.id.btn_setting_beauty:
				checkList(FavoriteType.BEAUTY.getComment(), btnBeauty);
				break;
			case R.id.btn_setting_shopping:
				checkList(FavoriteType.SHOPPING.getComment(), btnShop);
				break;
			case R.id.btn_setting_game:
				checkList(FavoriteType.GAME.getComment(), btnGame);
				break;
			case R.id.btn_setting_eat:
				checkList(FavoriteType.EAT.getComment(), btnEat);
				break;
			case R.id.btn_setting_tour:
				checkList(FavoriteType.TOUR.getComment(), btnTour);
				break;
			case R.id.btn_setting_finance:
				checkList(FavoriteType.FINANCE.getComment(), btnFinance);
				break;
			case R.id.btn_setting_culture:
				checkList(FavoriteType.CULTURE.getComment(), btnCulture);
				break;
			default:
				break;
		}
	}

	public void checkList(String category, Button btn) {
		if(isFavorites(category)) {
			favoritesList.remove(category);
			btn.setBackgroundResource(R.drawable.button_favorite_off);
			btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextGray));
		} else {
			favoritesList.add(String.valueOf(category));
			btn.setBackgroundResource(R.drawable.button_favorite_on);
			btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPuzi));
		}

		Log.i("INFO", "favoritesList : " + favoritesList.toString());
	}

	public boolean isFavorites(String item) {
		if(favoritesList.isEmpty()) {
			return false;
		} else {
			for (String favorite : favoritesList) {
				if (favorite.equals(String.valueOf(item))) {
					return true;
				}
			}
			return false;
		}
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

		String token = Preference.getProperty(getActivity(), "token");

		ProgressDialog.show(getActivity());
		SettingNetworkService settingNetworkService  = RetrofitManager.create(SettingNetworkService.class);
		Call<ResponseVO> call = settingNetworkService.updateFavorites(token, favoritesList);
		call.enqueue(new CustomCallback(getActivity()) {
			@Override
			public void onSuccess(ResponseVO responseVO) {
				ProgressDialog.dismiss();

			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

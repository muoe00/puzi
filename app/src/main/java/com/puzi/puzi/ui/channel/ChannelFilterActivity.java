package com.puzi.puzi.ui.channel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.utils.SerializeUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class ChannelFilterActivity extends Activity {

	private Unbinder unbinder;

	@BindView(R.id.ibtn_channel_filter_back)
	ImageButton ibtnBack;
	@BindView(R.id.ibtn_channel_filter_male)
	ImageButton ibtnMale;
	@BindView(R.id.ibtn_channel_filter_female)
	ImageButton ibtnFemale;
	@BindView(R.id.ibtn_channel_filter_10)
	ImageButton ibtn10;
	@BindView(R.id.ibtn_channel_filter_20)
	ImageButton ibtn20;
	@BindView(R.id.ibtn_channel_filter_30)
	ImageButton ibtn30;
	@BindView(R.id.ibtn_channel_filter_40)
	ImageButton ibtn40;
	@BindView(R.id.btn_channel_filter_beauty)
	Button btnBeauty;
	@BindView(R.id.btn_channel_filter_shopping)
	Button btnShopping;
	@BindView(R.id.btn_channel_filter_eat)
	Button btnEat;
	@BindView(R.id.btn_channel_filter_tour)
	Button btnTour;
	@BindView(R.id.btn_channel_filter_culture)
	Button btnCulture;
	@BindView(R.id.btn_channel_filter_game)
	Button btnGame;
	@BindView(R.id.btn_channel_filter_finance)
	Button btnFinance;

	private List<ChannelCategoryType> categoryTypeList = newArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_filter);

		unbinder = ButterKnife.bind(this);

		List<ChannelCategoryType> categoryTypeList = SerializeUtils.convertToType((List<String>) getIntent().getSerializableExtra("categoryTypeList"));
		if(categoryTypeList != null && categoryTypeList.size() != 0) {
			for(ChannelCategoryType categoryType : categoryTypeList) {
				setByCategoryType(categoryType);
			}
		}
	}

	@OnClick(R.id.ibtn_channel_filter_back)
	public void backPress() {
		super.onBackPressed();
	}

	@OnClick(R.id.ibtn_channel_filter_male)
	public void filterMale(View v) {
		setCategoryType(ChannelCategoryType.MALE, v);
	}

	@OnClick(R.id.ibtn_channel_filter_female)
	public void filterFemale(View v) {
		setCategoryType(ChannelCategoryType.FEMALE, v);
	}

	@OnClick(R.id.ibtn_channel_filter_10)
	public void filter10(View v) {
		setCategoryType(ChannelCategoryType.TEN, v);
	}

	@OnClick(R.id.ibtn_channel_filter_20)
	public void filter20(View v) {
		setCategoryType(ChannelCategoryType.TWENTY, v);
	}

	@OnClick(R.id.ibtn_channel_filter_30)
	public void filter30(View v) {
		setCategoryType(ChannelCategoryType.THIRTY, v);
	}

	@OnClick(R.id.ibtn_channel_filter_40)
	public void filter40(View v) {
		setCategoryType(ChannelCategoryType.FOURTY, v);
	}

	@OnClick(R.id.btn_channel_filter_beauty)
	public void filterBeauty(View v) {
		setCategoryType(ChannelCategoryType.BEAUTY, v);
	}

	@OnClick(R.id.btn_channel_filter_shopping)
	public void filterShopping(View v) {
		setCategoryType(ChannelCategoryType.SHOPPING, v);
	}

	@OnClick(R.id.btn_channel_filter_eat)
	public void filterEat(View v) {
		setCategoryType(ChannelCategoryType.EAT, v);
	}

	@OnClick(R.id.btn_channel_filter_tour)
	public void filterTour(View v) {
		setCategoryType(ChannelCategoryType.TOUR, v);
	}

	@OnClick(R.id.btn_channel_filter_culture)
	public void filterCulture(View v) {
		setCategoryType(ChannelCategoryType.CULTURE, v);
	}

	@OnClick(R.id.btn_channel_filter_game)
	public void filterGame(View v) {
		setCategoryType(ChannelCategoryType.GAME, v);
	}

	@OnClick(R.id.btn_channel_filter_finance)
	public void filterFinance(View v) {
		setCategoryType(ChannelCategoryType.FINANCE, v);
	}

	@OnClick(R.id.btn_channel_filter_all)
	public void filterAll() {
		categoryTypeList.clear();
		for(ChannelCategoryType categoryType : ChannelCategoryType.values()) {
			setByCategoryType(categoryType);
		}
	}

	@OnClick(R.id.btn_channel_filter_confirm)
	public void filterConfirm() {
		if(categoryTypeList.size() == 0) {
			Toast.makeText(this, "한개 이상 카테고리를 선택해야 합니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent();
		intent.putStringArrayListExtra("categoryTypeList", SerializeUtils.convertToString(categoryTypeList));
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void setByCategoryType(ChannelCategoryType categoryType) {
		switch(categoryType) {
			case BEAUTY:
				filterBeauty(btnBeauty);
				break;
			case SHOPPING:
				filterShopping(btnShopping);
				break;
			case EAT:
				filterEat(btnEat);
				break;
			case TOUR:
				filterTour(btnTour);
				break;
			case CULTURE:
				filterCulture(btnCulture);
				break;
			case GAME:
				filterGame(btnGame);
				break;
			case FINANCE:
				filterFinance(btnFinance);
				break;
			case MALE:
				filterMale(ibtnMale);
				break;
			case FEMALE:
				filterFemale(ibtnFemale);
				break;
			case TEN:
				filter10(ibtn10);
				break;
			case TWENTY:
				filter20(ibtn20);
				break;
			case THIRTY:
				filter30(ibtn30);
				break;
			case FOURTY:
				filter40(ibtn40);
				break;
		}
	}

	private void setCategoryType(ChannelCategoryType categoryType, View view) {
		if(categoryTypeList.contains(categoryType)) {
			categoryTypeList.remove(categoryType);
			if(categoryType.isButtonType()) {
				offButton(view);
			} else {
				offCircleIcon((ImageButton) view);
			}
		} else {
			categoryTypeList.add(categoryType);
			if(categoryType.isButtonType()) {
				onButton(view);
			} else {
				onCircleIcon((ImageButton) view);
			}
		}
	}

	private void onButton(View view) {
		view.setBackgroundColor(Color.parseColor("#ff2470"));
	}

	private void offButton(View view) {
		view.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	private void onCircleIcon(ImageButton view) {
		view.setImageResource(R.drawable.oval_4);
	}

	private void offCircleIcon(ImageButton view) {
		view.setImageResource(R.drawable.oval_4_copy_2);
	}

}
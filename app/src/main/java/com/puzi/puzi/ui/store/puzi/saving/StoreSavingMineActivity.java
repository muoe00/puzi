package com.puzi.puzi.ui.store.puzi.saving;

import android.os.Bundle;
import android.widget.*;
import butterknife.*;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.puzi.UserSavingVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.CustomArrayAdapter;
import com.puzi.puzi.ui.base.BaseActivity;
import com.puzi.puzi.utils.TextUtils;

import static com.puzi.puzi.biz.PuziStaticValue.DAILY_POINT_LIST;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class StoreSavingMineActivity extends BaseActivity {

	Unbinder unbinder;

	@BindView(R.id.iv_store_saving_mine_preview)
	ImageView ivPreview;
	@BindView(R.id.tv_store_saving_mine_name)
	TextView tvName;
	@BindView(R.id.pb_store_saving_mine_point)
	ProgressBar pbPoint;
	@BindView(R.id.tv_store_saving_mine_point_text)
	TextView tvPointText;
	@BindView(R.id.tv_store_saving_mine_point_total_text)
	TextView tvPointTotalText;
	@BindView(R.id.pb_store_saving_mine_count)
	ProgressBar pbCount;
	@BindView(R.id.tv_store_saving_mine_count_text)
	TextView tvCountText;
	@BindView(R.id.tv_store_saving_mine_count_total_text)
	TextView tvCountTotalText;
	@BindView(R.id.sp_store_saving_mine_daily_point)
	Spinner spDailyPoint;
	@BindView(R.id.ll_profile_channel_dropdown_container)
	LinearLayout llContainer;

	private int selectedDailyPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_saving_detail_mine);

		unbinder = ButterKnife.bind(this);

		initComponent();
	}

	private void initComponent() {
		UserVO userVO = Preference.getMyInfo(getActivity());
		UserSavingVO userSavingVO = userVO.getUserSavingDTO();

		BitmapUIL.load(userSavingVO.getStoreItemDTO().getPictureUrl(), ivPreview);
		tvName.setText(userSavingVO.getStoreSavingItemDTO().getName());
		pbPoint.setMax(userSavingVO.getStoreSavingItemDTO().getTargetPoint());
		pbPoint.setProgress(userSavingVO.getSavedPoint());
		tvPointText.setText(TextUtils.addComma(userSavingVO.getSavedPoint()) + "P ");
		tvPointTotalText.setText("/ " + TextUtils.addComma(userSavingVO.getStoreSavingItemDTO().getTargetPoint()) + "P");
		pbCount.setMax(userSavingVO.getStoreSavingItemDTO().getTargetMyToday());
		pbCount.setProgress(userSavingVO.getSavedMyToday());
		tvCountText.setText(TextUtils.addComma(userSavingVO.getSavedMyToday()) + "P ");
		tvCountTotalText.setText("/ " + TextUtils.addComma(userSavingVO.getStoreSavingItemDTO().getTargetMyToday()) + "P");
		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), DAILY_POINT_LIST);
		spDailyPoint.setAdapter(adapter);
		int count = 0;
		for(String dailyPointString : DAILY_POINT_LIST) {
			int point = Integer.parseInt(dailyPointString.substring(0, dailyPointString.length() - 1));
			if(point == userSavingVO.getDailyPoint()) {
				spDailyPoint.setSelection(count);
			}
			++count;
		}
	}

	@OnItemSelected(R.id.sp_store_saving_mine_daily_point)
	public void dailyPointSelected(int position) {
		String selected = DAILY_POINT_LIST.get(position);
		this.selectedDailyPoint = Integer.parseInt(selected.substring(0, selected.length() - 1));
	}

	@OnClick(R.id.btn_profile_channel_dropdown_block)
	public void blockOnClick() {
	}

	@OnClick(R.id.btn_store_saving_mine_daily_point_update)
	public void changeRequestOnClick() {
	}

	@OnClick(R.id.btn_store_saving_mine_block)
	public void more() {
	}

	@OnClick(R.id.btn_store_saving_mine_back)
	public void back() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

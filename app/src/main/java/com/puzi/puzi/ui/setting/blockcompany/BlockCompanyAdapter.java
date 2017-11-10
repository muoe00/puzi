package com.puzi.puzi.ui.setting.blockcompany;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.setting.RejectCompanyVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.SettingNetworkService;
import com.puzi.puzi.ui.CustomPagingAdapter;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import com.puzi.puzi.ui.common.DialogButtonCallback;
import com.puzi.puzi.ui.common.OneButtonDialog;
import com.puzi.puzi.ui.company.CompanyActivity;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 11. 4..
 */
public class BlockCompanyAdapter extends CustomPagingAdapter<RejectCompanyVO> {

	private ListHandler listHandler;

	public BlockCompanyAdapter(Activity activity, ListView listView, ListHandler listHandler) {
		super(activity, R.layout.item_block_company, listView, listHandler);
	}

	@Override
	protected void setView(Holder viewHolder, final RejectCompanyVO companyVO, final int position) {
		ViewHolder holder = (ViewHolder) viewHolder;

		BitmapUIL.load(companyVO.getCompanyInfoDTO().getPictureUrl(), holder.ibtnPreview);
		holder.ibtnPreview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseFragmentActivity baseActivity = (BaseFragmentActivity) activity;
				Intent intent = new Intent(baseActivity, CompanyActivity.class);
				intent.putExtra("company", companyVO.getCompanyInfoDTO());
				baseActivity.startActivity(intent);
				baseActivity.doAnimationGoRight();

			}
		});
		holder.tvName.setText(companyVO.getCompanyInfoDTO().getCompanyAlias());
		holder.ibtnRemove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				OneButtonDialog.show(activity, "차단해제", companyVO.getCompanyInfoDTO().getCompanyAlias() + "을 차단해제하시겠습니까?", "확인",
					new DialogButtonCallback() {

						@Override
						public void onClick() {
							final SettingNetworkService settingNetworkService = RetrofitManager.create(SettingNetworkService.class);
							String token = Preference.getProperty(activity, "token");

							ProgressDialog.show(activity);

							Call<ResponseVO> call = settingNetworkService.blockCompany(token, false, companyVO.getCompanyInfoDTO().getCompanyId());
							call.enqueue(new CustomCallback(activity) {

								@Override
								public void onSuccess(ResponseVO responseVO) {

									ProgressDialog.dismiss();

									if (responseVO.getResultType().isSuccess()) {
										Toast.makeText(activity, "성공적으로 해제되었습니다.", Toast.LENGTH_SHORT).show();
										removeItem(position);
									}
								}
							});
						}
					});
			}
		});
	}

	@Override
	protected Holder createHolder(View v) {
		return new ViewHolder(v);
	}

	public class ViewHolder extends Holder {

		@BindView(R.id.ibtn_block_company_preview) public SelectableRoundedImageView ibtnPreview;
		@BindView(R.id.tv_block_company_name) public TextView tvName;
		@BindView(R.id.ibtn_block_company_remove) public ImageButton ibtnRemove;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

package kr.puzi.puzi.ui.setting.blockcompany;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.joooonho.SelectableRoundedImageView;

import kr.puzi.puzi.biz.setting.RejectCompanyVO;
import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.base.BaseFragmentActivity;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import kr.puzi.puzi.ui.company.CompanyActivity;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 11. 4..
 */
public class BlockCompanyAdapter extends CustomPagingAdapter<RejectCompanyVO> {

	private ListHandler listHandler;

	public BlockCompanyAdapter(Activity activity, ListView listView, ListHandler listHandler) {
		super(activity, kr.puzi.puzi.R.layout.item_block_company, listView, listHandler);
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
							ProgressDialog.show(activity);

							LazyRequestService service = new LazyRequestService(activity, SettingNetworkService.class);
							service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
								@Override
								public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
									return settingNetworkService.blockCompany(token, false, companyVO.getCompanyInfoDTO().getCompanyId());
								}
							});
							service.enqueue(new CustomCallback(activity) {

								@Override
								public void onSuccess(ResponseVO responseVO) {
									Toast.makeText(activity, "성공적으로 해제되었습니다.", Toast.LENGTH_SHORT).show();
									removeItem(position);
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

		@BindView(kr.puzi.puzi.R.id.ibtn_block_company_preview) public SelectableRoundedImageView ibtnPreview;
		@BindView(kr.puzi.puzi.R.id.tv_block_company_name) public TextView tvName;
		@BindView(kr.puzi.puzi.R.id.ibtn_block_company_remove) public ImageButton ibtnRemove;

		public ViewHolder(View view) {
			super(view);
		}
	}

}

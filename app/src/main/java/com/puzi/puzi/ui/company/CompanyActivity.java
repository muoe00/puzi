package com.puzi.puzi.ui.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.company.CompanyVO;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.network.CustomCallback;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import com.puzi.puzi.network.service.CompanyNetworkService;
import retrofit2.Call;

public class CompanyActivity extends Activity {

	Unbinder unbinder;

	@BindView(R.id.iv_companyProfile_picture) public SelectableRoundedImageView companyPicture;
	@BindView(R.id.tv_companyName) public TextView companyName;
	@BindView(R.id.tv_companyComment) public TextView companyComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		unbinder = ButterKnife.bind(this);

		Intent intent = getIntent();
		CompanyVO companyVO = (CompanyVO) intent.getSerializableExtra("company");
		// int companyId = intent.getIntExtra("companyId", 0);
		int companyId = companyVO.getCompanyId();

		if(companyId != 0) {
			getCompany(companyId);
		}
	}

	public void getCompany(int companyId) {

		CompanyNetworkService companyNetworkService = RetrofitManager.create(CompanyNetworkService.class);
		String token = Preference.getProperty(CompanyActivity.this, "token");

		Call<ResponseVO> call = companyNetworkService.profile(token, companyId);
		call.enqueue(new CustomCallback<ResponseVO>(CompanyActivity.this) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				switch(responseVO.getResultType()){
					case SUCCESS:
						CompanyVO companyVO = responseVO.getValue("companyInfoDTO", CompanyVO.class);

						BitmapUIL.load(companyVO.getPictureUrl(), companyPicture);
						companyName.setText(companyVO.getCompanyAlias());
						companyComment.setText(companyVO.getComment());
						break;

					default:
						Log.i("INFO", "company profile failed.");
						Toast.makeText(getBaseContext(), responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@OnClick(R.id.btn_block)
	public void blockCompany() {

	}

	@OnClick(R.id.btn_back_profile)
	public void back() {
		finish();
	}

}

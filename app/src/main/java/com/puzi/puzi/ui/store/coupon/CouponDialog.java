package com.puzi.puzi.ui.store.coupon;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.puzi.puzi.R;
import com.puzi.puzi.image.BitmapUIL;

/**
 * Created by muoe0 on 2017-11-12.
 */

public class CouponDialog extends Dialog {

	@BindView(R.id.iv_coupon_url)
	ImageView ivCoupon;

	private String couponUrl;

	public CouponDialog(Context context, String couponImageUrl) {
		super(context);

		this.couponUrl = couponImageUrl;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_coupon);
		ButterKnife.bind(this);

		BitmapUIL.load(couponUrl, ivCoupon);
	}

	@OnClick(R.id.ibtn_coupon_cancel)
	public void back() {
		cancel();
	}

}

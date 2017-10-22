package com.puzi.puzi.ui.store;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.store.StoreItemVO;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.ui.base.BaseActivity;

import butterknife.*;


public class PurchaseActivity extends BaseActivity {

    @BindView(R.id.ibtn_back_point)
    public ImageButton backBtn;
    @BindView(R.id.brandTv)
    public TextView brandNameTv;
    @BindView(R.id.brandIv)
    public SelectableRoundedImageView brandImageView;
    @BindView(R.id.brandTv2)
    public TextView brandNameTv2;
    @BindView(R.id.goodsIv)
    public ImageView goodsIv;
    @BindView(R.id.expiryDayTv)
    public TextView expiryDayTv;
    @BindView(R.id.commentTv)
    public TextView commentTv;
    @BindView(R.id.goodsNameTv)
    public TextView goodsNameTv;
    @BindView(R.id.goodsPriceTv)
    public TextView goodsPriceTv;
    @BindView(R.id.purchaseBtn)
    public Button purchaseBtn;
    @BindView(R.id.infoView)
    public ScrollView infoView;

    public StoreItemVO storeItemVO;
    public String brandName;
    public String brandImage;
    public int purchaseBtnClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        ButterKnife.bind(this);
        setup();
    }

    private void setup() {
        storeItemVO = (StoreItemVO) getIntent().getSerializableExtra("storeItem");
        brandName = getIntent().getStringExtra("brandName");
        brandImage = getIntent().getStringExtra("brandImage");
        brandNameTv.setText(brandName);
        brandNameTv2.setText(brandName);
        BitmapUIL.load(brandImage, brandImageView);
        BitmapUIL.load(storeItemVO.getPictureUrl(), goodsIv);
        goodsNameTv.setText(storeItemVO.getName());
        goodsPriceTv.setText(storeItemVO.getPrice() + "");
        expiryDayTv.setText(storeItemVO.getExpiryDay() + "일");
        commentTv.setText(storeItemVO.getComment());
    }

    @OnClick(R.id.ibtn_back_point)
    public void dismiss() {
        finish();
    }

    @OnClick(R.id.purchaseBtn)
    public void purchaseAction() {

    }
}

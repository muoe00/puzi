package com.puzi.puzi.ui.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.puzi.puzi.image.BitmapUIL;
import com.puzi.puzi.R;

/**
 * Created by muoe0 on 2017-07-16.
 */

public class CompanyDialog extends Activity {

	private Intent intent;
	private String name, url;
	private Button btnOk, btnBlock;
	private ImageView ivComp;
	private TextView tvCategory, tvComp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_company_detail);
		this.setFinishOnTouchOutside(false);

		initComponents();

		intent = getIntent();
		name = intent.getStringExtra("name");
		url = intent.getStringExtra("url");

		tvComp.setText(name);
		BitmapUIL.load(url, ivComp);

	}

	public void getCompany() {

	}

	public void initComponents() {

		btnOk = (Button) findViewById(R.id.btn_ok);
		btnBlock = (Button) findViewById(R.id.btn_block);
		ivComp = (ImageView) findViewById(R.id.iv_companydialog_image);
		tvCategory = (TextView) findViewById(R.id.tv_dialog_category);
		tvComp = (TextView) findViewById(R.id.tv_dialog_company);

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnBlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "차단되었습니다.", Toast.LENGTH_SHORT).show();
			}
		});
	}
}

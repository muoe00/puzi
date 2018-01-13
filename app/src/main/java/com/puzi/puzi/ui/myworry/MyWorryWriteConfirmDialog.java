package com.puzi.puzi.ui.myworry;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.puzi.puzi.R;
import com.puzi.puzi.utils.TextUtils;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class MyWorryWriteConfirmDialog {

	private static Dialog dlg = null;

	public static void load(final Activity activity, final int point, final ConfirmListener listener) {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}

		if (dlg == null) {
			dlg = new Dialog(activity, R.style.FullHeightDialog);
			dlg.setContentView(R.layout.dialog_myworry_write_confirm);

			TextView tvTitlePoint = (TextView) dlg.findViewById(R.id.tv_dialog_title_point);
			tvTitlePoint.setText(TextUtils.addComma(point) + "P가 차감됩니다.");

			Button btnClose = (Button) dlg.findViewById(R.id.btn_dialog_close);
			btnClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
				}
			});

			Button btnConfirm = (Button) dlg.findViewById(R.id.btn_dialog_confirm);
			btnConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
					listener.onConfirm();
				}
			});

		}

		dlg.show();
	}

	private static void close() {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}
	}

	public interface ConfirmListener {
		void onConfirm();
	}

}

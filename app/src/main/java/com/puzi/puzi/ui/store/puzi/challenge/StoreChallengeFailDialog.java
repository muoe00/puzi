package com.puzi.puzi.ui.store.puzi.challenge;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import com.puzi.puzi.R;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class StoreChallengeFailDialog {

	private static Dialog dlg = null;

	public static void load(final Activity activity) {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}

		if (dlg == null) {
			dlg = new Dialog(activity, R.style.FullHeightDialog);
			dlg.setContentView(R.layout.dialog_store_challenge_fail);

			Button btnClose = (Button) dlg.findViewById(R.id.btn_dialog_close);
			btnClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
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

}

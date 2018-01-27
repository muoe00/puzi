package kr.puzi.puzi.ui.store.puzi.saving;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import kr.puzi.puzi.image.BitmapUIL;
import kr.puzi.puzi.ui.store.puzi.challenge.StoreChallengeDetailActivity;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class StoreChallengeCompletedDialog {

	private static Dialog dlg = null;

	public static void load(final Activity activity, String pictureUrl, final StoreChallengeDetailActivity.ChallengeSuccessListener listener) {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}

		if (dlg == null) {
			dlg = new Dialog(activity, kr.puzi.puzi.R.style.FullHeightDialog);
			dlg.setContentView(kr.puzi.puzi.R.layout.dialog_store_saving_completed);

			ImageView ivSuccess = (ImageView) dlg.findViewById(kr.puzi.puzi.R.id.iv_store_saving_completed);
			BitmapUIL.load(pictureUrl, ivSuccess);

			Button btnClose = (Button) dlg.findViewById(kr.puzi.puzi.R.id.btn_dialog_close);
			btnClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
				}
			});
			Button btnMove = (Button) dlg.findViewById(kr.puzi.puzi.R.id.btn_dialog_move);
			btnMove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
					listener.onSuccess();
				}
			});
		}

		dlg.show();
	}

	public static void close() {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}
	}

}

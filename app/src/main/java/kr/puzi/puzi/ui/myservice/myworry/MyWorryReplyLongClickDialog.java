package kr.puzi.puzi.ui.myservice.myworry;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import kr.puzi.puzi.R;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.MyWorryReplyNetworkService;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 12. 31..
 */
public class MyWorryReplyLongClickDialog {

	private static Dialog dlg = null;

	public static void load(final Activity activity, final int myWorryReplyId, boolean useDelete, final DialogButtonCallback deleteCallback) {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}

		if (dlg == null) {
			dlg = new Dialog(activity, R.style.FullHeightDialog);
			dlg.setContentView(R.layout.dialog_myworry_reply_longclick);

			Button btnNotify = (Button) dlg.findViewById(R.id.btn_reply_longclick_notify);
			Button btnDelete = (Button) dlg.findViewById(R.id.btn_reply_longclick_delete);
			LinearLayout llBar = (LinearLayout) dlg.findViewById(R.id.ll_reply_longclick_bar);

			if(!useDelete) {
				btnDelete.setVisibility(View.GONE);
				llBar.setVisibility(View.GONE);
			}

			btnNotify.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					OneButtonDialog.show(activity, "신고하기", "해당댓글을 신고하시겠습니까?", "신고하기", new DialogButtonCallback() {
						@Override
						public void onClick() {
							ProgressDialog.show(activity);

							LazyRequestService service = new LazyRequestService(activity, MyWorryReplyNetworkService.class);
							service.method(new LazyRequestService.RequestMothod<MyWorryReplyNetworkService>() {
								@Override
								public Call<ResponseVO> execute(MyWorryReplyNetworkService myWorryReplyNetworkService, String token) {
									return myWorryReplyNetworkService.notify(token, myWorryReplyId);
								}
							});
							service.enqueue(new CustomCallback(activity) {
								@Override
								public void onSuccess(ResponseVO responseVO) {
									Toast.makeText(activity, "신고가 등록되었습니다.", Toast.LENGTH_SHORT).show();
									close();
								}
							});
						}
					});
				}
			});

			btnDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					OneButtonDialog.show(activity, "삭제하기", "해당댓글을 삭제하시겠습니까?", "삭제하기", new DialogButtonCallback() {
						@Override
						public void onClick() {
							ProgressDialog.show(activity);

							LazyRequestService service = new LazyRequestService(activity, MyWorryReplyNetworkService.class);
							service.method(new LazyRequestService.RequestMothod<MyWorryReplyNetworkService>() {
								@Override
								public Call<ResponseVO> execute(MyWorryReplyNetworkService myWorryReplyNetworkService, String token) {
									return myWorryReplyNetworkService.delete(token, myWorryReplyId);
								}
							});
							service.enqueue(new CustomCallback(activity) {
								@Override
								public void onSuccess(ResponseVO responseVO) {
									Toast.makeText(activity, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
									deleteCallback.onClick();
									close();
								}
							});
						}
					});
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

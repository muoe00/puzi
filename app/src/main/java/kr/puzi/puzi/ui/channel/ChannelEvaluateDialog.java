package kr.puzi.puzi.ui.channel;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.ChannelNetworkService;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.utils.UIUtils;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class ChannelEvaluateDialog {

	private static Dialog dlg = null;
	private static int score = 0;

	public static void load(final Activity activity, final int channelId, final ChannelDetailActivity.EvaluateListener listener) {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}

		if (dlg == null) {

			score = 0;
			dlg = new Dialog(activity, kr.puzi.puzi.R.style.FullHeightDialog);
			dlg.setContentView(kr.puzi.puzi.R.layout.dialog_channel_evaluate);

			final ImageButton ibtnStar1 = (ImageButton) dlg.findViewById(kr.puzi.puzi.R.id.dialog_channel_evaluate_star_1);
			final ImageButton ibtnStar2 = (ImageButton) dlg.findViewById(kr.puzi.puzi.R.id.dialog_channel_evaluate_star_2);
			final ImageButton ibtnStar3 = (ImageButton) dlg.findViewById(kr.puzi.puzi.R.id.dialog_channel_evaluate_star_3);
			final ImageButton ibtnStar4 = (ImageButton) dlg.findViewById(kr.puzi.puzi.R.id.dialog_channel_evaluate_star_4);
			final ImageButton ibtnStar5 = (ImageButton) dlg.findViewById(kr.puzi.puzi.R.id.dialog_channel_evaluate_star_5);
			ibtnStar1.setOnClickListener(evaluate(1, ibtnStar1, ibtnStar2, ibtnStar3, ibtnStar4, ibtnStar5));
			ibtnStar2.setOnClickListener(evaluate(2, ibtnStar1, ibtnStar2, ibtnStar3, ibtnStar4, ibtnStar5));
			ibtnStar3.setOnClickListener(evaluate(3, ibtnStar1, ibtnStar2, ibtnStar3, ibtnStar4, ibtnStar5));
			ibtnStar4.setOnClickListener(evaluate(4, ibtnStar1, ibtnStar2, ibtnStar3, ibtnStar4, ibtnStar5));
			ibtnStar5.setOnClickListener(evaluate(5, ibtnStar1, ibtnStar2, ibtnStar3, ibtnStar4, ibtnStar5));

			final EditText etComment = (EditText) dlg.findViewById(kr.puzi.puzi.R.id.et_dialog_channel_evaluate_comment);

			Button btnCancel = (Button) dlg.findViewById(kr.puzi.puzi.R.id.dialog_edittext_btnCancel);
			btnCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
				}
			});

			Button btnConfirm = (Button) dlg.findViewById(kr.puzi.puzi.R.id.btn_dialog_channel_evaluate_confirm);
			btnConfirm.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(ChannelEvaluateDialog.score == 0) {
						Toast.makeText(activity, "점수를 선택해주세요.", Toast.LENGTH_SHORT).show();
						return;
					}
					final String comment = etComment.getText().toString();
					if(comment == null || comment.length() < 5){
						Toast.makeText(activity, "평가를 5자이상 작성해주세요.", Toast.LENGTH_SHORT).show();
						return;
					}

					ProgressDialog.show(activity);

					LazyRequestService service = new LazyRequestService(activity, ChannelNetworkService.class);
					service.method(new LazyRequestService.RequestMothod<ChannelNetworkService>() {
						@Override
						public Call<ResponseVO> execute(ChannelNetworkService channelNetworkService, String token) {
							return channelNetworkService.evaludate(token, channelId, score, comment);
						}
					});
					service.enqueue(new CustomCallback(activity) {

						@Override
						public void onSuccess(ResponseVO responseVO) {
							Toast.makeText(activity, "평가를 작성해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
							listener.success(score);
							close();
						}
					});
				}
			});

			dlg.show();
		}
	}

	private static void close() {
		if (dlg != null) {
			dlg.dismiss();
			dlg = null;
		}
	}

	private static View.OnClickListener evaluate(final int score, final ImageButton ibtnStar1, final ImageButton ibtnStar2,
		final ImageButton ibtnStar3, final ImageButton ibtnStar4, final ImageButton ibtnStar5) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ChannelEvaluateDialog.score = score;
				UIUtils.setEvaluateStarScoreImage(score, ibtnStar1, ibtnStar2, ibtnStar3, ibtnStar4, ibtnStar5,
					kr.puzi.puzi.R.drawable.star_big, kr.puzi.puzi.R.drawable.star_big_copy_4);
			}
		};
	}
}

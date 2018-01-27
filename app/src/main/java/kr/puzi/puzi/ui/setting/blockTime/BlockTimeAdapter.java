package kr.puzi.puzi.ui.setting.blockTime;

import android.app.Activity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import kr.puzi.puzi.biz.setting.RejectTimeVO;
import kr.puzi.puzi.network.CustomCallback;
import kr.puzi.puzi.network.LazyRequestService;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.service.SettingNetworkService;
import kr.puzi.puzi.ui.CustomPagingAdapter;
import kr.puzi.puzi.ui.ProgressDialog;
import kr.puzi.puzi.ui.common.DialogButtonCallback;
import kr.puzi.puzi.ui.common.OneButtonDialog;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 11. 4..
 */

public class BlockTimeAdapter extends CustomPagingAdapter<RejectTimeVO> {

	public BlockTimeAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		super(activity, layoutResource, listView, scrollView, listHandler);
	}

	@Override
	protected void setView(Holder holder, final RejectTimeVO item, final int position) {
		ViewHolder viewHolder = (ViewHolder) holder;
		viewHolder.tvTime.setText(item.getStartTime() + "시 ~ " + item.getEndTime() + "시");
		viewHolder.ibtnRemove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OneButtonDialog.show(activity, "제한 해제", "해제하시겠습니까?", "확인", new DialogButtonCallback() {
					@Override
					public void onClick() {

						ProgressDialog.show(activity);

						LazyRequestService service = new LazyRequestService(activity, SettingNetworkService.class);
						service.method(new LazyRequestService.RequestMothod<SettingNetworkService>() {
							@Override
							public Call<ResponseVO> execute(SettingNetworkService settingNetworkService, String token) {
								return settingNetworkService.blockTime(token, false, item.getStartTime(), item.getEndTime());
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

	class ViewHolder extends Holder {
		@BindView(kr.puzi.puzi.R.id.tv_block_time) public TextView tvTime;
		@BindView(kr.puzi.puzi.R.id.ibtn_block_time_remove) public ImageButton ibtnRemove;

		public ViewHolder(View view) {
			super(view);
		}
	}
}

package kr.puzi.puzi.ui.setting;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kr.puzi.puzi.R;
import kr.puzi.puzi.biz.setting.NoticeVO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 170605 on 2017-10-23.
 */

public class NoticeListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

	private static final int VIEW_NOTICE = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private Activity activity = null;
	private LayoutInflater inflater;
	private List<NoticeVO> list = new ArrayList();
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public NoticeListAdapter(Activity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void addNoticeList(List<NoticeVO> noticeVOs) {
		if(empty && noticeVOs.size() > 0) {
			empty = false;
		}
		list.addAll(noticeVOs);
	}

	public void empty() {
		if(!empty){
			empty = true;
		}
	}

	public int getViewTypeCount() {
		return 3;
	}

	public int getItemViewType(int position) {
		if(empty) {
			return VIEW_EMPTY;
		}
		if(progressed) {
			if(getGroupCount() - 1 == position) {
				return VIEW_PROGRESS;
			}
		}
		return VIEW_NOTICE;
	}

	public void startProgress() {
		if(!progressed) {
			progressed = true;
			notifyDataSetChanged();
		}
	}

	public void stopProgress() {
		if(progressed) {
			progressed = false;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getGroupCount() {
		if(empty) {
			return 1;
		}
		if(progressed) {
			return list.size() + 1;
		}
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		View v = null;
		ParentViewHolder parentViewHolder = null;
		int viewType = getItemViewType(groupPosition);
		Log.i("INFO", "groupPosition" + groupPosition);

		if(v == null) {
			v = convertView;

			switch(viewType) {
				case VIEW_NOTICE:
					Log.i("INFO", "parentViewHolder v == null");
					v = inflater.inflate(R.layout.fragment_setting_notice_item_parent, null);
					parentViewHolder = new ParentViewHolder(v);
					v.setTag(parentViewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_notice, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			parentViewHolder = (ParentViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_NOTICE:
				final NoticeVO noticeVO = (NoticeVO) getGroup(groupPosition);

				Log.i("INFO", "noticeVO.getTitle() : " + noticeVO.getTitle());

				if(parentViewHolder == null) {
					Log.i("INFO", "parentViewHolder");
				} else if (parentViewHolder.tvNotice == null) {
					Log.i("INFO", "parentViewHolder.tvNotice");
				}
				parentViewHolder.tvNotice.setText(noticeVO.getTitle().toString());

				if(isExpanded) {
					parentViewHolder.ivNotice.setImageResource(R.drawable.back_chevron_copy_click);
				} else {
					parentViewHolder.ivNotice.setImageResource(R.drawable.back_chevron_copy);
				}
				break;
		}

		return v;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View v = convertView;
		ChildViewHolder childViewHolder = null;
		int viewType = getItemViewType(groupPosition);

		if(v == null) {
			switch(viewType) {
				case VIEW_NOTICE:
					Log.i("INFO", "getRealChildView");
					v = inflater.inflate(R.layout.fragment_setting_notice_item_child, null);
					childViewHolder = new ChildViewHolder(v);
					final NoticeVO noticeVO = (NoticeVO) getGroup(groupPosition);
					childViewHolder.tvContent.setText(noticeVO.getComment());
					v.setTag(childViewHolder);
					break;
			}
		} else {
			childViewHolder = (ChildViewHolder) v.getTag();
		}

		return v;
	}

	public class ParentViewHolder {

		@BindView(R.id.iv_setting_notice_parent) public ImageView ivNotice;
		@BindView(R.id.tv_setting_notice_parent) public TextView tvNotice;

		public ParentViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public class ChildViewHolder {

		@BindView(R.id.tv_setting_notice_content) public TextView tvContent;

		public ChildViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}

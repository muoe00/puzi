package com.puzi.puzi.ui.setting;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import com.puzi.puzi.biz.notice.NoticeVO;
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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View v = convertView;
		ParentViewHolder viewHolder = null;
		int viewType = getItemViewType(groupPosition);

		if(v == null) {
			switch(viewType) {
				case VIEW_NOTICE:
					v = inflater.inflate(R.layout.fragment_setting_notice_item_parent, null);
					viewHolder = new ParentViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_notice, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ParentViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_NOTICE:
				final NoticeVO noticeVO = (NoticeVO) getGroup(groupPosition);

				Log.i("INFO", "noticeVO.getTitle() : " + noticeVO.getTitle());

				viewHolder.tvNotice.setText(noticeVO.getTitle());

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
		ChildViewHolder viewHolder = null;
		int viewType = getItemViewType(groupPosition);

		if(v == null) {
			switch(viewType) {
				case VIEW_NOTICE:
					v = inflater.inflate(R.layout.fragment_setting_notice_item_child, null);
					viewHolder = new ChildViewHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_list_empty_notice, null);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_list_progressbar, null);
					break;
			}
		} else {
			viewHolder = (ChildViewHolder) v.getTag();
		}

		switch(viewType) {
			case VIEW_NOTICE:
				final NoticeVO noticeVO = (NoticeVO) getGroup(groupPosition);

				viewHolder.tvContent.setText(noticeVO.getComment());

				break;
		}

		return v;
	}

	public class ParentViewHolder {

		@BindView(R.id.iv_setting_notice) public ImageView ivNotice;
		@BindView(R.id.tv_setting_notice) public TextView tvNotice;

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

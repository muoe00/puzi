package com.puzi.puzi.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.puzi.puzi.R;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 11. 4..
 *
 * 커스텀리스트뷰 기능
 * 1. 자동 프로그래스 하단에 띄어주고 화면 최적화
 * 2. 리스트가 비워져있을 때 안내메시지 띄어줌
 */
public abstract class CustomBaseAdapter extends BaseAdapter {

	protected static final int VIEW_LIST = 0;
	protected static final int VIEW_EMPTY = 1;
	protected static final int VIEW_PROGRESS = 2;

	protected LayoutInflater inflater;
	protected Activity activity;
	protected int layoutResource;
	protected ListView listView;
	protected ScrollView scrollView;
	protected ListHandler listHandler;
	private boolean lastestScrollFlag = false;

	protected List<Object> list = newArrayList();
	@Getter
	private int pagingIndex = 0; //시작할 때 +1하고 시작함, 즉 처음 pagingIndex값은 1임

	@Getter
	private boolean progressed = false;
	@Setter @Getter
	private boolean empty = false;
	@Setter
	private String emptyMessage = "표시할 내용이 없습니다.";
	@Setter @Getter
	private boolean more = false;

	public CustomBaseAdapter(Activity activity, int layoutResource, ListView listView, ListHandler listHandler) {
		this(activity, layoutResource, listView, null, listHandler);
	}

	public CustomBaseAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.layoutResource = layoutResource;
		this.listView = listView;
		this.scrollView = scrollView;
		this.listHandler =listHandler;
		init();
	}

	protected void init() {
		if(scrollView != null) {
			scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
				@Override
				public void onScrollChanged() {
					int scrollViewPos = scrollView.getScrollY();
					int TextView_lines = scrollView.getChildAt(0).getBottom() - scrollView.getHeight();
					if(TextView_lines == scrollViewPos){
						if(more && !progressed) {
							getList();
						}
					}
				}
			});
		} else {
			listView.setOnScrollListener(new AbsListView.OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
						if(more && !progressed) {
							getList();
						}
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					lastestScrollFlag = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
				}
			});
		}
	}

	public void addList(List list) {
		if(empty && list.size() > 0) {
			empty = false;
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addListWithTotalCount(List list, int totalCount) {
		addList(list);
		checkMore(totalCount);
	}

	protected void checkMore(int totalCount) {
		if(list.size() >= totalCount) {
			more = false;
		} else {
			more = true;
		}
	}

	public void addOne(Object obj) {
		empty = false;
		list.add(obj);
		notifyDataSetChanged();
	}

	public void addFirst(Object obj) {
		empty = false;
		list.add(0, obj);
		notifyDataSetChanged();
	}

	public void increasePagingIndex() {
		pagingIndex = pagingIndex + 1;
	}

	public void initPagingIndex() {
		pagingIndex = 1;
	}

	public void getList() {
		increasePagingIndex();
		listHandler.getList();
	}

	public void startProgress() {
		if(!progressed) {
			progressed = true;
			notifyDataSetChanged();
		}
	}

	public void startProgressWithScrollDown() {
		startProgress();
		if(scrollView != null) {
			scrollView.post(new Runnable() {
				@Override
				public void run() {
					scrollView.fullScroll(View.FOCUS_DOWN);
				}
			});
		} else {
			listView.setSelection(getCount() - 1);
		}
	}

	public void stopProgress() {
		if(progressed) {
			progressed = false;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if(empty) {
			return 1;
		}
		if(progressed) {
			return list.size() + 1;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if(empty) {
			return VIEW_EMPTY;
		}
		if(progressed) {
			if(getCount() - 1 == position) {
				return VIEW_PROGRESS;
			}
		}
		return VIEW_LIST;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder viewHolder = null;
		EmptyHolder emptyHolder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case VIEW_LIST:
					v = inflater.inflate(layoutResource, null);
					viewHolder = createHolder(v);
					v.setTag(viewHolder);
					break;

				case VIEW_EMPTY:
					v = inflater.inflate(R.layout.item_custom_adapter_empty, null);
					emptyHolder = new EmptyHolder(v);
					v.setTag(emptyHolder);
					break;

				case VIEW_PROGRESS:
					v = inflater.inflate(R.layout.item_custom_adapter_progress, null);
					break;
			}
		} else {
			switch(viewType) {
				case VIEW_LIST:
					viewHolder = (Holder) v.getTag();
					break;

				case VIEW_EMPTY:
					emptyHolder = (EmptyHolder) v.getTag();
					break;
			}
		}

		switch(viewType) {
			case VIEW_LIST:
				setView(viewHolder, getItem(position), position);
				break;

			case VIEW_EMPTY:
				emptyHolder.tvEmptyMessage.setText(emptyMessage);
				break;
		}

		return v;
	}

	protected abstract void setView(Holder viewHolder, Object item, int position);

	protected abstract Holder createHolder(View v);

	protected abstract class Holder {
		public Holder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	class EmptyHolder {
		@BindView(R.id.tv_custom_adapter_message)
		public TextView tvEmptyMessage;

		public EmptyHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if(scrollView != null) {
			setListViewHeightBasedOnChildren();
		}
	}

	/**
	 * 스크롤 뷰 안에 리스트뷰를 넣을 때 height문제로 인해서
	 * notifyDataSetChanged() 를 호출 할 때 이 메소드를 같이 호출해 줘야 한다.
	 */
	private void setListViewHeightBasedOnChildren() {
		ListAdapter listAdapter = this;

		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public interface ListHandler {
		void getList();
	}

}

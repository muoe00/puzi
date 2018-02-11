package kr.puzi.puzi.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 11. 4..
 *
 * 커스텀리스트뷰 기능
 * 1. 자동 프로그래스 하단에 띄어주고 화면 최적화
 * 2. 리스트가 비워져있을 때 안내메시지 띄어줌
 * 3. 페이징인덱스 관리
 */
public abstract class CustomPagingAdapter<T> extends BaseAdapter {

	protected static final int VIEW_LIST = 0;
	protected static final int VIEW_EMPTY = 1;
	protected static final int VIEW_PROGRESS = 2;
	protected static final int VIEW_LIST_2 = 3;
	protected static final int VIEW_LIST_3 = 4;
	protected static final int VIEW_LIST_4 = 5;

	protected LayoutInflater inflater;
	protected Activity activity;
	protected int layoutResource;
	protected int layoutResource2;
	protected int layoutResource3;
	protected int layoutResource4;
	protected int layoutResource5;
	protected ListView listView;
	protected GridView gridView;
	protected ScrollView scrollView;
	protected ListHandler listHandler;
	private boolean lastestScrollFlag = false;
	private boolean moreBtn = false;

	protected List<T> list = newArrayList();
	@Getter
	private int pagingIndex = 0; // 시작할 때 +1하고 시작함, 즉 처음 pagingIndex 값은 1임

	@Getter
	private boolean progressed = false;
	@Setter @Getter
	private boolean empty = false;
	@Setter
	private String emptyMessage = "표시할 내용이 없습니다.";
	@Setter @Getter
	private boolean more = false;

	public CustomPagingAdapter(Activity activity, int layoutResource, ListView listView, ListHandler listHandler) {
		this(activity, layoutResource, 0, listView, null, listHandler);
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, int layoutResource2, ListView listView, ListHandler listHandler) {
		this(activity, layoutResource, layoutResource2, listView, null, listHandler);
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, int layoutResource2, int layoutResource3, int layoutResource4, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
		this(activity, layoutResource, layoutResource2, layoutResource3, layoutResource4, 0, listView, scrollView, listHandler, moreBtn);
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		this(activity, layoutResource, 0, listView, scrollView, listHandler);
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
		this(activity, layoutResource, 0, listView, scrollView, listHandler, moreBtn);
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, int layoutResource2, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.layoutResource = layoutResource;
		this.layoutResource2 = layoutResource2;
		this.listView = listView;
		this.scrollView = scrollView;
		this.listHandler =listHandler;
		this.moreBtn = moreBtn;
		init();
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, int layoutResource2, int layoutResource3, int layoutResource4, int layoutResource5, ListView listView, ScrollView scrollView, ListHandler listHandler, boolean moreBtn) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.layoutResource = layoutResource;
		this.layoutResource2 = layoutResource2;
		this.layoutResource3 = layoutResource3;
		this.layoutResource4 = layoutResource4;
		this.layoutResource5 = layoutResource5;
		this.listView = listView;
		this.scrollView = scrollView;
		this.listHandler =listHandler;
		init();
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, int layoutResource2, ListView listView, ScrollView scrollView, ListHandler listHandler) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.layoutResource = layoutResource;
		this.layoutResource2 = layoutResource2;
		this.listView = listView;
		this.scrollView = scrollView;
		this.listHandler =listHandler;
		init();
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, GridView gridView, ScrollView scrollView, ListHandler listHandler) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.layoutResource = layoutResource;
		this.gridView = gridView;
		this.scrollView = scrollView;
		this.listHandler =listHandler;
		init();
	}

	public CustomPagingAdapter(Activity activity, int layoutResource, GridView gridView, ListHandler listHandler) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		this.layoutResource = layoutResource;
		this.gridView = gridView;
		this.listHandler =listHandler;
		init();
	}

	protected void init() {
		if(this.listView != null) {
			this.listView.setAdapter(this);
		} else {
			this.gridView.setAdapter(this);
		}

		if(!moreBtn) {
			if (scrollView != null) {
				scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						int scrollViewPos = scrollView.getScrollY();
						int TextView_lines = scrollView.getChildAt(0).getBottom() - scrollView.getHeight();
						if (TextView_lines == scrollViewPos) {
							if (more && !progressed) {
								getList();
							}
						}
					}
				});
			} else if (listView != null) {
				listView.setOnScrollListener(new AbsListView.OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
							if (more && !progressed) {
								getList();
							}
						}
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						lastestScrollFlag = (totalItemCount > 0) && firstVisibleItem + visibleItemCount >= totalItemCount;
					}
				});
			} else {
				gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastestScrollFlag) {
							if (more && !progressed) {
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
	}

	public void addList(List<T> list) {
		if((list == null || list.size() == 0) && this.list.size() == 0) {
			empty = true;
			notifyDataSetChanged();
			return;
		}
		if(empty && list.size() > 0) {
			empty = false;
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addListWithTotalCount(List<T> list, int totalCount) {
		addList(list);
		checkMore(totalCount);
	}

	public void removeItem(int position) {
		list.remove(position);
		if(list.size() == 0) {
			empty = true;
		}
		notifyDataSetChanged();
	}

	protected void checkMore(int totalCount) {
		if(list.size() >= totalCount) {
			more = false;
		} else {
			more = true;
		}
	}

	public void addOne(T obj) {
		empty = false;
		list.add(obj);
		notifyDataSetChanged();
	}

	public void addFirst(T obj) {
		empty = false;
		list.add(0, obj);
		notifyDataSetChanged();
	}

	public void increasePagingIndex() {
		pagingIndex = pagingIndex + 1;
	}

	public void initPagingIndex() {
		pagingIndex = 0;
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
		} else if(listView != null) {
			listView.setSelection(getCount() - 1);
		} else {
			gridView.setSelection(getCount() - 1);
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
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getViewTypeCount() {
		return 5;
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
					v = inflater.inflate(kr.puzi.puzi.R.layout.item_custom_adapter_empty, null);
					emptyHolder = new EmptyHolder(v);
					v.setTag(emptyHolder);
					break;

				case VIEW_PROGRESS:
					if(listView != null) {
						v = inflater.inflate(kr.puzi.puzi.R.layout.item_custom_adapter_progress, null);
					} else {
						v = inflater.inflate(kr.puzi.puzi.R.layout.item_empty, null);
					}
					break;
				case VIEW_LIST_2:
					v = inflater.inflate(layoutResource2, null);
					viewHolder = createHolder2(v);
					v.setTag(viewHolder);
					break;

				case VIEW_LIST_3:
					v = inflater.inflate(layoutResource3, null);
					viewHolder = createHolder3(v);
					v.setTag(viewHolder);
					break;

				case VIEW_LIST_4:
					v = inflater.inflate(layoutResource4, null);
					viewHolder = createHolder4(v);
					v.setTag(viewHolder);
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

				case VIEW_LIST_2:
					viewHolder = (Holder) v.getTag();
					break;

				case VIEW_LIST_3:
					viewHolder = (Holder) v.getTag();
					break;

				case VIEW_LIST_4:
					viewHolder = (Holder) v.getTag();
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

			case VIEW_LIST_2:
				setView2(viewHolder, getItem(position), position);
				break;

			case VIEW_LIST_3:
				setView3(viewHolder, getItem(position), position);
				break;

			case VIEW_LIST_4:
				setView4(viewHolder, getItem(position), position);
				break;
		}

		return v;
	}

	public void setView2(Holder viewHolder, T item, int position) {
		// @Override if use
	}

	public void setView3(Holder viewHolder, T item, int position) {
		// @Override if use
	}

	public void setView4(Holder viewHolder, T item, int position) {
		// @Override if use
	}

	protected abstract void setView(Holder viewHolder, T item, int position);

	protected abstract Holder createHolder(View v);

	protected Holder createHolder2(View v) {
		// @Override if use
		return null;
	}

	protected Holder createHolder3(View v) {
		// @Override if use
		return null;
	}

	protected Holder createHolder4(View v) {
		// @Override if use
		return null;
	}

	public void clean() {
		initPagingIndex();
		list = newArrayList();
		notifyDataSetChanged();
	}

	protected abstract class Holder {
		public Holder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	class EmptyHolder {
		@BindView(kr.puzi.puzi.R.id.tv_custom_adapter_message)
		public TextView tvEmptyMessage;

		public EmptyHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if(scrollView != null && listView != null) {
			setListViewHeightBasedOnChildren();
			return;
		}
		if(scrollView != null && gridView != null) {
			setGridViewHeightBasedOnChildren();
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

	private void setGridViewHeightBasedOnChildren() {
		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.AT_MOST);

		for (int i = 0; i < this.getCount(); i++) {
			View listItem = this.getView(i, null, gridView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			if(i%2 == 0) {
				totalHeight += listItem.getMeasuredHeight();
			}
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
		gridView.requestLayout();
	}

	public interface ListHandler {
		void getList();
	}

}

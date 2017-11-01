package com.puzi.puzi.ui.store.coupon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import lombok.Getter;

import java.util.List;

/**
 * Created by 170605 on 2017-10-27.
 */

public class CouponListAdapter extends BaseAdapter {

	private static final int VIEW_COUPON = 0;
	private static final int VIEW_EMPTY = 1;
	private static final int VIEW_PROGRESS = 2;

	private Activity activity;
	private LayoutInflater inflater;
	@Getter
	private boolean progressed = false;
	private boolean empty = false;

	public CouponListAdapter(Activity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	public void addAdvertiseList(List<ReceivedAdvertiseVO> advertiseVOList) {
		if(empty && advertiseVOList.size() > 0) {
			empty = false;
		}
		// list.addAll(advertiseVOList);
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
			if(getCount() - 1 == position) {
				return VIEW_PROGRESS;
			}
		}
		return VIEW_COUPON;
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
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}

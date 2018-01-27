package kr.puzi.puzi.ui.myworry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kr.puzi.puzi.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */

public class MyWorryWriteAdapter extends BaseAdapter {

	private static final int NORMAL = 0;
	private static final int SELECTED = 1;

	private LayoutInflater inflater;
	private List<Integer> priceList;

	@Getter @Setter
	private int selectedType = 0;

	public int getPrice() {
		return priceList.get(selectedType);
	}

	public MyWorryWriteAdapter(Activity activity, List<Integer> priceList) {
		this.inflater = activity.getLayoutInflater();
		this.priceList = priceList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		int viewType = getItemViewType(position);

		if(v == null) {
			switch(viewType) {
				case NORMAL:
					v = inflater.inflate(kr.puzi.puzi.R.layout.item_myworry_write_price, null);
					break;

				case SELECTED:
					v = inflater.inflate(kr.puzi.puzi.R.layout.item_myworry_write_price_selected, null);
					break;
			}
			holder = new ViewHolder(v);
			v.setTag(holder);
		} else {
			switch(viewType) {
				case NORMAL:
					holder = (ViewHolder) v.getTag();
					break;

				case SELECTED:
					holder = (ViewHolder) v.getTag();
					break;
			}
		}

		String titleName = getTitleCount(position);
		holder.tvtitle.setText(titleName);

		int price = priceList.get(position);
		String answerCount = getAnswerCount(position);
		holder.tvPrice.setText(TextUtils.addComma(price) + "p(최대답변수:" + answerCount + "명)");

		return v;
	}

	public class ViewHolder {

		@BindView(kr.puzi.puzi.R.id.tv_item_myworry_write_title) public TextView tvtitle;
		@BindView(kr.puzi.puzi.R.id.tv_item_myworry_write_price) public TextView tvPrice;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	@Override
	public int getCount() {
		return 4;
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
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if(position == selectedType) {
			return SELECTED;
		}
		return NORMAL;
	}

	private String getAnswerCount(int position) {
		if(position == 0) {
			return "21";
		} else if(position == 1) {
			return "201";
		} else if(position == 2) {
			return "801";
		} else {
			return "무제한";
		}
	}

	private String getTitleCount(int position) {
		if(position == 0) {
			return "초저가형";
		} else if(position == 1) {
			return "일반형";
		} else if(position == 2) {
			return "고급형";
		} else {
			return "프리미엄";
		}
	}

}

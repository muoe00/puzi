package kr.puzi.puzi.ui.myservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by juhyun on 2018. 1. 24..
 */

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    List<String> data;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflater.inflate(kr.puzi.puzi.R.layout.item_spinner_normal, parent, false);
        }

        if(data!=null){
            //데이터세팅
            String text = data.get(position);
            ((TextView)convertView.findViewById(kr.puzi.puzi.R.id.tv_spinner_normal)).setText(text);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(kr.puzi.puzi.R.layout.item_spinner_down, parent, false);
        }

        String text = data.get(position);
        ((TextView)convertView.findViewById(kr.puzi.puzi.R.id.tv_spinner_down)).setText(text);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

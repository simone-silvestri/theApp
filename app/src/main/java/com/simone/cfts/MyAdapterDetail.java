package com.simone.cfts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapterDetail extends BaseAdapter {
    Context context;
    List<RowDataDetail> rowData;
    public MyAdapterDetail(Context context, List<RowDataDetail> items) {
        this.context = context;
        this.rowData = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtTime;
        TextView txtPause;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_exercises, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txttitle);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txttime);
            holder.txtPause = (TextView) convertView.findViewById(R.id.txtpause);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        RowDataDetail rowItem = (RowDataDetail) getItem(position);
        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtTime.setText(rowItem.getTime());
        holder.txtPause.setText(rowItem.getPause());
        return convertView;
    }
    @Override
    public int getCount() {
        return rowData.size();
    }
    @Override
    public Object getItem(int position) {
        return rowData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return rowData.indexOf(getItem(position));
    }
}
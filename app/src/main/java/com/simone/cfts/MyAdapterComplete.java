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

public class MyAdapterComplete extends BaseAdapter {

    Context context;
    List<RowDataComplete> rowData;
    public MyAdapterComplete(Context context, List<RowDataComplete> items) {
        this.context = context;
        this.rowData = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        ImageView imageType;
        TextView txtTitle;
        TextView txtDesc;
        TextView txtType;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        MyAdapterComplete.ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_workout, null);
            holder = new MyAdapterComplete.ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.subtitle);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.txtType = (TextView) convertView.findViewById(R.id.typetext);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.imageType = (ImageView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        }
        else {
            holder = (MyAdapterComplete.ViewHolder) convertView.getTag();
        }
        RowDataComplete rowItem = (RowDataComplete) getItem(position);
        holder.txtDesc.setText(rowItem.getSubtitle());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtType.setText(rowItem.getType());
        holder.imageView.setImageResource(rowItem.getImageId());
        holder.imageType.setImageResource(rowItem.getImageType());
        return convertView;
    }

    public List<RowDataComplete> getRowData() {
        return rowData;
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

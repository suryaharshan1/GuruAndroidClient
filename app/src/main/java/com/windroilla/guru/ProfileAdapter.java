package com.windroilla.guru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Surya Harsha Nunnaguppala on 29/6/15.
 */
public class ProfileAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> highText;
    private ArrayList<String> lowText;

    public ProfileAdapter(Context context, ArrayList<String> highText, ArrayList<String> lowText) {
        this.context = context;
        this.highText = highText;
        this.lowText = lowText;
    }

    @Override
    public int getCount() {
        return highText.size();
    }

    @Override
    public Object getItem(int position) {
        return lowText.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_profile, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);
        viewHolder.highView.setText(highText.get(position));
        viewHolder.lowView.setText(lowText.get(position));
        return view;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView highView;
        public final TextView lowView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_profile_icon);
            highView = (TextView) view.findViewById(R.id.list_item_profile_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_profile_low_textview);
        }
    }
}

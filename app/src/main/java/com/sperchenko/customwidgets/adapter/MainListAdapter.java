package com.sperchenko.customwidgets.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sperchenko.customwidgets.R;
import com.sperchenko.customwidgets.model.ChildActivityItem;

import java.util.List;

/**
 * Created by Stas on 24.11.2015.
 */
public class MainListAdapter extends BaseExpandableListAdapter {
    public interface OnStartActivityListener {
        void onStartActivity(Intent intentToStart);
    }


    private List<ChildActivityItem> mData;
    private String mPackageName;
    private LayoutInflater mInflater;
    private OnStartActivityListener mListener;

    public MainListAdapter(Context context, List<ChildActivityItem> data, OnStartActivityListener l) {
        mData = data;
        mPackageName = context.getPackageName();
        mInflater = LayoutInflater.from(context);
        mListener = l;
    }

    @Override
    public int getGroupCount() {
        return (mData != null) ? mData.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (mData != null) ? 1 : 0;
    }

    @Override
    public  ChildActivityItem getGroup(int groupPosition) {
        return (mData != null && groupPosition < mData.size()) ? mData.get(groupPosition) : null;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        if (mData != null && groupPosition < mData.size()) {
            return mData.get(groupPosition).getItemDescription();
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        ChildActivityItem group = getGroup(groupPosition);
        return group != null ? group.hashCode() : 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        String child = getChild(groupPosition, childPosition);
        return (child != null) ? child.hashCode() : 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView row = (TextView)((convertView != null) ? convertView : mInflater.inflate(R.layout.main_list_group_item, parent, false));
        row.setText(getGroup(groupPosition).getItemName());
        row.setSelected(isExpanded);
        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row != null) {
            holder = (ViewHolder) row.getTag();
        } else {
            row = mInflater.inflate(R.layout.main_list_subitem, parent, false);
            holder = new ViewHolder(row);
            holder.getButton().setOnClickListener(mBtnListener);
            row.setTag(holder);
        }
        holder.getText().setText(getChild(groupPosition, childPosition));
        holder.getButton().setTag(getGroup(groupPosition).getItemName());
        return row;
    }

    private View.OnClickListener mBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                Intent intent = new Intent();
                String activityFullName = String.format("%s.activity.%s", mPackageName, v.getTag());
                intent.setComponent(new ComponentName(mPackageName, activityFullName));
                mListener.onStartActivity(intent);
            }
        }
    };

    private class ViewHolder {
        private View base;
        private TextView text;
        private Button button;

        public ViewHolder(View v) {
            base = v;
        }

        public TextView getText() {
            if (text == null) {
                text = (TextView) base.findViewById(R.id.text);
            }
            return text;
        }

        public Button getButton() {
            if (button == null) {
                button = (Button) base.findViewById(R.id.button);
            }
            return button;
        }
    }




    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }
}

package com.sperchenko.customwidgets.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import com.sperchenko.customwidgets.R;

/**
 * Created by Stas on 23.11.2015.
 */
public class MainActivity extends Activity {

    private class ChildActivityItem {
        private String itemName;
        private List<String> items;

        public ChildActivityItem(String itemName, String itemDescription) {
            if (TextUtils.isEmpty(itemName)) throw new IllegalArgumentException("Name of the item must not be empty of null");
            if (TextUtils.isEmpty(itemDescription)) throw new IllegalArgumentException("Description of the item must not be empty of null");
            this.itemName = itemName;
            this.items = new ArrayList<String>(1);
            this.items.add(itemDescription);
        }

        public String getItemName() {
            return itemName;
        }

        public List<String> getItems() {
            return items;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] names = getResources().getStringArray(R.array.children_activity_names);
        String[] descriptions = getResources().getStringArray(R.array.children_activity_description);
        if (names.length != descriptions.length) {
            showError();
            return;
        }

        List<ChildActivityItem> data = new ArrayList<>(names.length);
        for (int i=0; i<names.length; i++) {
            data.add(new ChildActivityItem(names[i], descriptions[i]));
        }

        ((ExpandableListView) findViewById(android.R.id.list)).setAdapter(new MainListAdapter(data));
    }

    private void showError() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(R.string.err_items_count_title)
                .setMessage(R.string.err_items_count_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.err_items_count_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MainActivity.this.finish();
            }
        }).create().show();
    }

    private class MainListAdapter extends BaseExpandableListAdapter {
        private List<ChildActivityItem> mData;

        public MainListAdapter(List<ChildActivityItem> data) {
            mData = data;
        }

        @Override
        public int getGroupCount() {
            return (mData != null) ? mData.size() : 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return (mData != null) ? ((groupPosition < mData.size()) ? mData.get(groupPosition).getItems().size() : 0) : 0;
        }

        @Override
        public  ChildActivityItem getGroup(int groupPosition) {
            return (mData != null && groupPosition < mData.size()) ? mData.get(groupPosition) : null;
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            if (mData != null && groupPosition < mData.size()) {
                ChildActivityItem group = mData.get(groupPosition);
                return (childPosition < group.getItems().size()) ? group.getItems().get(childPosition) : null;
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
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
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
}

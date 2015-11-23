package com.sperchenko.customwidgets.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.sperchenko.customwidgets.R;
import com.sperchenko.customwidgets.adapter.MainListAdapter;
import com.sperchenko.customwidgets.model.ChildActivityItem;

/**
 * Created by Stas on 23.11.2015.
 */
public class MainActivity extends Activity {

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

        final ExpandableListView list = (ExpandableListView) findViewById(android.R.id.list);

        list.setAdapter(new MainListAdapter(this, data, new MainListAdapter.OnStartActivityListener() {
            @Override
            public void onStartActivity(Intent intentToStart) {
                startActivity(intentToStart);
            }
        }));

        list.expandGroup(0);

        list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < list.getExpandableListAdapter().getGroupCount(); i++) {
                    if (i != groupPosition) {
                        list.collapseGroup(i);
                    }
                }
            }
        });
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

}

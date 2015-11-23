package com.sperchenko.customwidgets.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stas on 24.11.2015.
 */
public class ChildActivityItem {
    private String itemName;
    private String itemDescription;

    public ChildActivityItem(String itemName, String itemDescription) {
        if (TextUtils.isEmpty(itemName)) throw new IllegalArgumentException("Name of the item must not be empty of null");
        if (TextUtils.isEmpty(itemDescription)) throw new IllegalArgumentException("Description of the item must not be empty of null");
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }
}

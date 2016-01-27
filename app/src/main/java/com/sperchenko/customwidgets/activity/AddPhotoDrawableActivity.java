package com.sperchenko.customwidgets.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.sperchenko.customwidgets.R;
import com.sperchenko.widgets.AddPhotoDrawable;

/**
 * Created by stanislav.perchenko on 1/27/2016.
 */
public class AddPhotoDrawableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_drawable);

        int colorWhite = Color.parseColor("#FFFFFF");

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        for(int i=0; i<container.getChildCount(); i++) {
            Drawable dr1 = (Build.VERSION.SDK_INT >= 21) ? getResources().getDrawable(R.drawable.bg_add_photo, null) : getResources().getDrawable(R.drawable.bg_add_photo);
            Drawable dr2 = new AddPhotoDrawable(getString(R.string.add_photo), getResources().getDimensionPixelSize(R.dimen.add_phcoto_padding), colorWhite);
            container.getChildAt(i).setBackground(new LayerDrawable(new Drawable[]{dr1, dr2}));
            container.getChildAt(i).setClickable(true);
        }
    }
}

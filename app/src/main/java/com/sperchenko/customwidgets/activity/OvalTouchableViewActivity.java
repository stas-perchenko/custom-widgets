package com.sperchenko.customwidgets.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.sperchenko.customwidgets.R;
import com.sperchenko.widgets.OvalTouchableView;

/**
 * Created by Stas on 23.11.2015.
 */
public class OvalTouchableViewActivity extends AppCompatActivity {


    private OvalTouchableView vBtn1;
    private OvalTouchableView vBtn2;
    private OvalTouchableView vBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oval_touchable_view);
        vBtn1 = (OvalTouchableView) findViewById(R.id.view_horizontal);
        vBtn2 = (OvalTouchableView) findViewById(R.id.view_square);
        vBtn3 = (OvalTouchableView) findViewById(R.id.view_vertical);
        final View squareContainer = findViewById(R.id.square_container);


        getSupportActionBar().setTitle(R.string.activity_oval_touchable_view_title);

        /**
         * Setting the middle View to be square
         */
        squareContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                squareContainer.getLayoutParams().height = squareContainer.getMeasuredWidth();
                ((ViewGroup) squareContainer.getParent()).requestLayout();
                if (Build.VERSION.SDK_INT >= 16) {
                    squareContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    squareContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        vBtn1.setOnClickListener(mBtnClickListener);
        vBtn2.setOnClickListener(mBtnClickListener);
        vBtn3.setOnClickListener(mBtnClickListener);
    }

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            vBtn1.setSelected(false);
            vBtn2.setSelected(false);
            vBtn3.setSelected(false);
            v.setSelected(true);
        }
    };

}

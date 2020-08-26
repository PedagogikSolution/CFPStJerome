package com.pedagogiksolution.CFPStJerome.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class ProportionnalView extends AppCompatImageView {




        public ProportionnalView(Context context) {
            super(context);
        }

        public ProportionnalView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ProportionnalView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            Drawable d = getDrawable();
            if (d != null) {
                int w = MeasureSpec.getSize(widthMeasureSpec);
                int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
                setMeasuredDimension(w, h);
            }
            else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


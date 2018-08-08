package com.artproficiencyapp.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.artproficiencyapp.R;


@SuppressLint("AppCompatCustomView")
public class RegularTextview extends TextView {

    public RegularTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public RegularTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public RegularTextview(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getResources().getString(R.string.font_sink_sans_regular));
        setTypeface(myTypeface);

    }

}
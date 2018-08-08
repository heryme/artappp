package com.artproficiencyapp.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.artproficiencyapp.R;


@SuppressLint("AppCompatCustomView")
public class MediumEditext extends EditText {

    public MediumEditext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MediumEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MediumEditext(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getResources().getString(R.string.font_sink_sans_medium));
        setTypeface(myTypeface);

    }

}
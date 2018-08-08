package com.artproficiencyapp.test.expandable_list_view.utils;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by admin on 14-Mar-18.
 */

public class CustomExpandableListview extends ExpandableListView {


    int intGroupPosition, intChildPosition, intGroupid;


    public CustomExpandableListview(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //999999 is radio_button_selected_state size in pixels. ExpandableListView requires radio_button_selected_state maximum height in order to do measurement calculations.
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
package com.artproficiencyapp.model;

/**
 * Created by admin on 10-Oct-16.
 * Use this class to interact between two activity
 * using interface
 * Create listener and implement this listener
 * in activity where you want result back
 * and
 */
public class FingerScanModel {

    private TargetValueChagned targetValueChagned;
    private static FingerScanModel mInstance;
    private boolean mState;

    public interface TargetValueChagned {
        void targetChanged();
    }

    public static FingerScanModel getInstance() {
        if(mInstance == null) {
            mInstance = new FingerScanModel();
        }
        return mInstance;
    }

    /****
     *** set this listener in activity where
     *** you want result back
     ****/
    public void setListener(TargetValueChagned listener) {
        targetValueChagned = listener;
    }

    public void changeState(boolean state) {
        if(targetValueChagned != null) {
            mState = state;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return mState;
    }

    private void notifyStateChange() {
        targetValueChagned.targetChanged();
    }
}
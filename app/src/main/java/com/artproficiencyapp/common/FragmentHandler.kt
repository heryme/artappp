package com.artproficiencyapp.common

import android.app.Activity
import android.app.Fragment
import android.os.Bundle


class FragmentHandler {

    enum class ANIMATION_TYPE {
        SLIDE_UP_TO_DOWN, SLIDE_DOWN_TO_UP, SLIDE_LEFT_TO_RIGHT, SLIDE_RIGHT_TO_LEFT, NONE, SLIDE_LEFT_RIGHT
    }

    /**
     * Add Fragment
     *
     * @param fragment
     * @param fragmentToTarget
     * @param bundle
     * @param isAddToBackStack
     * @param tag
     * @param requestcode
     * @param animationType
     */
    fun addFragment(activity: Activity, frameId: Int, fragment: Fragment, fragmentToTarget: Fragment?, bundle: Bundle?, isAddToBackStack: Boolean, tag: String, requestcode: Int, animationType: ANIMATION_TYPE) {

        //If Already fragment is open so not call again

        if (isOpenFragment(activity, tag)) {
            val fragTrans = activity.fragmentManager.beginTransaction()


            //Pass data between fragment
            if (bundle != null) {
                fragment.arguments = bundle
            }

            //Set Target fragment
            if (fragmentToTarget != null) {
                fragment.setTargetFragment(fragmentToTarget, requestcode)
            }

            //If you need to add back stack so put here
            if (isAddToBackStack) {
                fragTrans.addToBackStack(tag)
            }

            fragTrans.add(frameId, fragment, tag)
            fragTrans.commit()
        }
    }

    /**
     * Replace Fragment
     *
     * @param fragment
     * @param fragmentToTarget
     * @param bundle
     * @param isAddToBackStack
     * @param tag
     * @param requestCode      for get value from previous fragment
     * @param animationType
     */
    fun replaceFragment(activity: Activity, frameId: Int, fragment: Fragment, fragmentToTarget: Fragment?, bundle: Bundle?, isAddToBackStack: Boolean, tag: String, requestCode: Int, animationType: ANIMATION_TYPE) {

        //If Already fragment is open so not call again
        if (isOpenFragment(activity, tag)) {
            val fragTrans = activity.fragmentManager.beginTransaction()

            //Pass data between fragment
            if (bundle != null) {
                fragment.arguments = bundle
            }

            //Set Target fragment
            if (fragmentToTarget != null) {
                fragment.setTargetFragment(fragmentToTarget, requestCode)
            }

            //If you need to add back stack so put here
            if (isAddToBackStack) {
                fragTrans.addToBackStack(tag)
            }

            fragTrans.replace(frameId, fragment, tag)
            fragTrans.commit()
        }
    }

    //Check fragment is already opened or not
    fun isOpenFragment(activity: Activity, fragmentName: String): Boolean {
        try {
            if (!activity.fragmentManager.findFragmentByTag(fragmentName).isVisible) {
                return true
            }
        } catch (e: Exception) {
            return true
        }

        return false
    }

}

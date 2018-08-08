package com.artproficiencyapp.common

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.artproficiencyapp.R

class SessionManager(private val context: Context) {

    private val resources: Resources

    companion object {

        private lateinit var pref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        lateinit var instance: SessionManager

        // Shared pref mode
        private val PRIVATE_MODE = 0
        var REQUIRED_UPDATE_FAVOURITE_LIST = false
        var UPDATE_FAVOURITE_BABYSITTER = false
    }

    enum class FavouriteBabysitterType {
        NEAR_BY, FEATURED, HISTORY, ACCEPTED, REVIEW_UPDATED, NONE, DELETED
    }

    init {
        instance = this
        resources = context.resources
        pref = context.getSharedPreferences(resources.getString(R.string.app_shared_pref), PRIVATE_MODE)
        editor = pref.edit()
    }

    fun isLogin(isLogin: Boolean) {
        editor.putBoolean(resources.getString(R.string.is_login), isLogin)
        editor.commit()
    }

    val isLogin: Boolean
        get() = pref.getBoolean(resources.getString(R.string.is_login), false)

    fun put(key: String, value: String): String {
        editor.putString(key, value)
        editor.commit()
        return key
    }

    fun put(key: String, value: Boolean): String {
        editor.putBoolean(key, value)
        editor.commit()
        return key
    }

    fun put(key: String, value: Int): String {
        editor.putInt(key, value)
        editor.commit()
        return key
    }
    fun remove(key: String)
    {
        editor.remove(key)
    }

    operator fun get(key: String, value: String): String = pref.getString(key, value)

    operator fun get(key: String, value: Boolean): Boolean = pref.getBoolean(key, value)
    /*  public boolean get(String key) {
        return pref.getBoolean(key, false);
    }*/

    operator fun get(key: String, value: Int): Int = pref.getInt(key, value)

    fun logout() {
        editor.clear()
        editor.commit()
    }


}
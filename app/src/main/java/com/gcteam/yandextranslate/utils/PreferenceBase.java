package com.gcteam.yandextranslate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Helper (base class) for access to Key/Value storage of Android (Preferences)
 *
 * Created by turist on 07.04.2017.
 */
public class PreferenceBase {

    protected final Context context;

    protected PreferenceBase(Context context) {
        this.context = context;
    }

    protected boolean contains(String valueKey) {
        return getPreferences().contains(valueKey);
    }

    protected String getString(String valueKey, String defaultValue) {
        SharedPreferences preferences = getPreferences();
        return preferences.getString(valueKey, defaultValue);
    }

    protected void putString(String valueKey, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(valueKey, value);
        editor.commit();
    }

    protected int getInt(String valueKey, int defaultValue) {
        SharedPreferences preferences = getPreferences();
        return preferences.getInt(valueKey, defaultValue);
    }

    protected long getLong(String valueKey, long defaultValue) {
        SharedPreferences preferences = getPreferences();
        return preferences.getLong(valueKey, defaultValue);
    }

    protected void putLong(String valueKey, long value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(valueKey, value);
        editor.commit();
    }

    protected void putInt(String valueKey, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(valueKey, value);
        editor.commit();
    }

    protected void remove(String valueKey) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(valueKey);
        editor.commit();
    }

    protected boolean getBoolean(String valueKey, boolean defaultValue) {
        SharedPreferences preferences = getPreferences();
        return preferences.getBoolean(valueKey, defaultValue);
    }

    protected void putBoolean(String valueKey, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(valueKey, value);
        editor.commit();
    }

    protected SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getPreferences();
        return preferences.edit();
    }

    protected SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}

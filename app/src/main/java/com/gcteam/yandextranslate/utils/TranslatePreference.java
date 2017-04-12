package com.gcteam.yandextranslate.utils;

import android.content.Context;
import android.util.Pair;

import com.gcteam.yandextranslate.domain.Direction;

/**
 * Created by turist on 07.04.2017.
 */

public class TranslatePreference extends PreferenceBase {

    static final String SOURCE_KEY = "last_source_language";
    static final String TARGET_KEY = "last_target_language";

    protected TranslatePreference(Context context) {
        super(context);
    }

    public static TranslatePreference get(Context context) {
        return new TranslatePreference(context);
    }

    public String sourceLanguage() {
        return getString(SOURCE_KEY, "");
    }

    public String targetLanguage() {
        return getString(TARGET_KEY, "");
    }

    public Pair<String, String> lastDirection() {
        return new Pair<>(sourceLanguage(), targetLanguage());
    }

    public void save(Direction direction) {
        if(direction == null) {
            return;
        }

        if(direction.from != null) {
            putString(SOURCE_KEY, direction.from.code);
        }

        if(direction.to != null) {
            putString(TARGET_KEY, direction.to.code);
        }
    }
}

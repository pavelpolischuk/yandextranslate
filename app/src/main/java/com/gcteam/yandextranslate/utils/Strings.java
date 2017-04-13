package com.gcteam.yandextranslate.utils;

import android.support.annotation.Nullable;

/**
 * Created by turist on 12.04.2017.
 */

public class Strings {

    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }

    public static boolean areEqual(@Nullable String str1, @Nullable String str2) {
        if(str1 == str2) {
            return true;
        }

        if(str1 == null || str2 == null) {
            return false;
        }

        return str1.equals(str2);
    }
}

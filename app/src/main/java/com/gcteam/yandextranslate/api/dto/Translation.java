package com.gcteam.yandextranslate.api.dto;

import android.support.annotation.Nullable;

import com.gcteam.yandextranslate.utils.Strings;

/**
 * Response content on translate request of {@link com.gcteam.yandextranslate.api.YandexTranslateApi}
 *
 * Created by turist on 07.04.2017.
 */
public class Translation {

    public Translation(String lang, String text) {
        this.lang = lang;

        if(text.isEmpty()) {
            this.text = new String[0];
            return;
        }

        this.text = new String[1];
        this.text[0] = text;
    }

    public String lang;

    /** Source of translation (it is not response of server - it will be set while response processing) */
    @Nullable
    public String source;

    /** Translations field */
    public String[] text;

    public static final Translation EMPTY = new Translation("", "");

    /** Empty if {@link Translation#source} or {@link Translation#text} is empty */
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(source)
            || text.length == 0
            || Strings.isNullOrEmpty(text[0]);
    }

    /** Compile {@link Translation#text} array to one string with '\n' delimiter (as "\n".join(source)) */
    @Override
    public String toString() {
        if(text.length == 0) {
            return "";
        }
        if(text.length == 1) {
            return text[0];
        }

        StringBuilder sb = new StringBuilder(text[0]);
        for(int i = 1; i < text.length; ++i) {
            sb.append('\n');
            sb.append(text[i]);
        }

        return sb.toString();
    }
}

package com.gcteam.yandextranslate.api.dto;

/**
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

    public String[] text;

    public static final Translation EMPTY = new Translation("", "");
}

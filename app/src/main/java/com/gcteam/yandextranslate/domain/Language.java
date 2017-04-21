package com.gcteam.yandextranslate.domain;

/**
 * Created by turist on 07.04.2017.
 */
public class Language {

    final public String code;
    final public String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
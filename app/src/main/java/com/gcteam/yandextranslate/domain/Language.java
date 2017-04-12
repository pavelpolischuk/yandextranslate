package com.gcteam.yandextranslate.domain;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by turist on 07.04.2017.
 */

@Table(name = "language")
public class Language extends Model {

    @Column(name = "lang_code", index = true)
    public String code;

    @Column(name = "lang_name")
    public String name;

    public Language() {
        super();
    }

    public Language(String code, String name) {
        super();
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
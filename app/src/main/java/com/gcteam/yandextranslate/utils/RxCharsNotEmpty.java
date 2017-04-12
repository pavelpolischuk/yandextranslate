package com.gcteam.yandextranslate.utils;

import io.reactivex.functions.Function;

/**
 * Created by turist on 10.04.2017.
 */

public class RxCharsNotEmpty implements Function<CharSequence, Boolean> {

    public static final RxCharsNotEmpty Instance = new RxCharsNotEmpty();

    @Override
    public Boolean apply(CharSequence charSequence) throws Exception {
        return charSequence.length() != 0;
    }
}
package com.gcteam.yandextranslate.utils;

import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.History;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by turist on 12.04.2017.
 */

public class RxHelpers {

    public static final Predicate<Translation> TranslationNotEmpty = new Predicate<Translation>() {
        @Override
        public boolean test(Translation translation) throws Exception {
            return !translation.isEmpty();
        }
    };

    public static final Function<CharSequence, Boolean> CharsNotEmpty = new Function<CharSequence, Boolean>() {
        @Override
        public Boolean apply(CharSequence charSequence) throws Exception {
            return charSequence.length() != 0;
        }
    };

    public static final Function<Translation, History> TranslationToHistory = new Function<Translation, History>() {
        @Override
        public History apply(Translation translation) throws Exception {
            return new History(translation.source, translation.toString(), translation.lang, false);
        }
    };

    public static final Function<History, History.SourceKey> HistorySource = new Function<History, History.SourceKey>() {
        @Override
        public History.SourceKey apply(History history) throws Exception {
            return new History.SourceKey(history);
        }
    };
}

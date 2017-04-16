package com.gcteam.yandextranslate.utils;

import android.view.View;

import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.History;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turist on 12.04.2017.
 */

public class RxHelpers {

    public static final Predicate<Translation> TranslationNotEmpty = new Predicate<Translation>() {
        @Override
        public boolean test(Translation translation) throws Exception {
            return translation != null && !translation.isEmpty();
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

    public static final BiFunction<Translation, Object, Translation> SelectTranslation = new BiFunction<Translation, Object, Translation>() {
        @Override
        public Translation apply(Translation translation, Object o) throws Exception {
            return translation;
        }
    };

    public static Disposable subscribeViewVisibleOnNotEmptyText(Observable<CharSequence> source, View view) {
        return source
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(RxHelpers.CharsNotEmpty)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(RxView.visibility(view));
    }
}
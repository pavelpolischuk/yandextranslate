package com.gcteam.yandextranslate.services;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.gcteam.yandextranslate.api.YandexService;
import com.gcteam.yandextranslate.api.dto.AvailableLanguages;
import com.gcteam.yandextranslate.domain.Direction;
import com.gcteam.yandextranslate.domain.Language;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Service with available languages.
 */
public class LanguagesService implements Serializable {

    private static LanguagesService instance;

    private Language[] languages;
    private HashMap<String, Language> mapCodeToName;


    /**
     * @return existing singleton instance of {@link LanguagesService}
     */
    @Nullable
    public static LanguagesService get() {
        return instance;
    }


    /**
     * @return Observable with singleton instance of {@link LanguagesService}, beforehand (if need) create it.
     *
     * To create instance, request languages from server (waiting response released with Observable)
     */
    public static Observable<LanguagesService> get(Context context) {
        if(instance != null) {
            return Observable.just(instance);
        }

        Function<AvailableLanguages, LanguagesService> createInstance = new Function<AvailableLanguages, LanguagesService>() {
            @Override
            public LanguagesService apply(AvailableLanguages availableLanguages) throws Exception {
                instance = new LanguagesService(availableLanguages);
                return instance;
            }
        };

        return YandexService.get()
                .getLangs(Locale.getDefault().getLanguage())
                .map(createInstance);
    }

    private LanguagesService(AvailableLanguages availableLanguages) {
        this.languages = new Language[availableLanguages.langs.size()];
        this.mapCodeToName = new HashMap<>();

        int i = 0;
        for(Map.Entry<String, String> entry : availableLanguages.langs.entrySet()) {
            Language current = new Language(entry.getKey(), entry.getValue());
            this.languages[i++] = current;
            this.mapCodeToName.put(current.code, current);
        }

        Arrays.sort(this.languages, new Comparator<Language>() {
            @Override
            public int compare(Language o1, Language o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }


    public Language[] sortedLanguages() {
        return languages;
    }

    @Nullable
    public Language language(String code) {
        if(mapCodeToName.containsKey(code)) {
            return mapCodeToName.get(code);
        }

        return null;
    }

    @Nullable
    public Direction firstDirection() {
        if(languages.length >= 2) {
            return new Direction(languages[0], languages[1]);
        }

        if(languages.length == 1) {
            return new Direction(languages[0], languages[0]);
        }

        return null;
    }

    public static Observable<Direction> direction(Pair<String, String> pair) {
        if(instance == null) {
            return Observable.empty();
        }

        return Observable.just(instance.direction(pair.first, pair.second));
    }

    @Nullable
    public Direction direction(String from, String to) {

        Language first = language(from);
        Language second = language(to);

        if(first == null || second == null) {
            return firstDirection();
        }

        return new Direction(first, second);
    }
}
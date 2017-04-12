package com.gcteam.yandextranslate.services;

import android.util.Pair;

import com.gcteam.yandextranslate.api.dto.AvailableLanguages;
import com.gcteam.yandextranslate.domain.Direction;
import com.gcteam.yandextranslate.domain.Language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by turist on 07.04.2017.
 */

public class LanguagesService implements Consumer<AvailableLanguages>, Serializable {

    private Language[] languages;
    private HashMap<String, Language> mapCodeToName;

    @Override
    public void accept(AvailableLanguages availableLanguages) throws Exception {
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

    public Language language(String code) {
        if(mapCodeToName.containsKey(code)) {
            return mapCodeToName.get(code);
        }

        return null;
    }


    public ArrayList<Direction> directions() {
        ArrayList<Direction> dirs = new ArrayList<>();

        for(int i = 0; i < languages.length; ++i) {
            for(int j = 0; j < i; ++j) {
                dirs.add(new Direction(languages[i], languages[j]));
            }
        }

        return dirs;
    }

    public Direction firstDirection() {
        if(languages.length >= 2) {
            return new Direction(languages[0], languages[1]);
        }

        if(languages.length == 1) {
            return new Direction(languages[0], languages[0]);
        }

        return null;
    }

    public Direction direction(String from, String to) {

        Language first = language(from);
        Language second = language(to);

        if(first == null || second == null) {
            return firstDirection();
        }

        return new Direction(first, second);
    }


    public Function<AvailableLanguages, Direction> acceptAndGiveDirection(final Pair<String, String> codes) {
        return new Function<AvailableLanguages, Direction>() {
            @Override
            public Direction apply(AvailableLanguages availableLanguages) throws Exception {
                accept(availableLanguages);
                return direction(codes.first, codes.second);
            }
        };
    }
}
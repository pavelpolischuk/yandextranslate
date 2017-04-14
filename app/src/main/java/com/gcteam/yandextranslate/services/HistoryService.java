package com.gcteam.yandextranslate.services;

import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.gcteam.yandextranslate.domain.History;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by turist on 10.04.2017.
 */

public class HistoryService implements Consumer<History> {

    private static final String ADDED_ORDER = Table.DEFAULT_ID_NAME + " DESC";
    private static final String SOURCE_ORDER = History.SOURCE_FIELD + " ASC";

    private static final String SOURCE_EQUAL = History.SOURCE_FIELD + " = ?";
    private static final String TRANSLATION_EQUAL = History.TRANSLATION_FIELD + " = ?";
    private static final String DIRECTION_EQUAL = History.DIRECTION_FIELD + " = ?";
    private static final String BOOKMARK_EQUAL = History.BOOKMARK_FIELD + " = ?";

    private static final String SOURCE_LIKE = History.SOURCE_FIELD + " LIKE ?";
    private static final String TRANSLATION_LIKE = History.TRANSLATION_FIELD + " LIKE ?";

    private static HistoryService Instance;

    private PublishSubject<Integer> updateSubject = PublishSubject.create();

    public static HistoryService get() {
        if(Instance == null) {
            Instance = new HistoryService();
        }

        return Instance;
    }

    public void addNew(History history) {
        if(like(history) == null) {
            history.save();
            notifyChanges();
        }
    }

    public void save(History history) {
        History old = like(history);

        if(old == null) {
            history.save();
        } else {
            old.isBookmark = history.isBookmark;
            old.save();
        }

        notifyChanges();
    }

    private History like(History history) {
        return new Select().from(History.class)
                .where(SOURCE_EQUAL, history.sourceText)
                .and(TRANSLATION_EQUAL, history.translation)
                .and(DIRECTION_EQUAL, history.direction)
                .executeSingle();
    }

    public List<History> get(boolean onlyBookmark) {
        return execute(new Select().from(History.class), onlyBookmark);
    }

    public List<History> get(String template, boolean onlyBookmark) {
        if(template.isEmpty()) {
            return get(onlyBookmark);
        }

        From from = new Select().from(History.class)
                .where("(" + SOURCE_LIKE, "%"+template+"%")
                .or(TRANSLATION_LIKE + ")", "%"+template+"%");

        return execute(from, onlyBookmark);
    }

    private List<History> execute(From from, boolean onlyBookmark) {
        if(onlyBookmark) {
            return from.and(BOOKMARK_EQUAL, true)
                    .orderBy(SOURCE_ORDER)
                    .execute();
        }

        return from.orderBy(ADDED_ORDER)
                .execute();
    }

    private void notifyChanges() {
        updateSubject.onNext(0);
    }

    public Observable<Integer> updates() {
        return updateSubject;
    }

    @Override
    public void accept(History history) throws Exception {
        addNew(history);
    }
}
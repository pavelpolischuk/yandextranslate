package com.gcteam.yandextranslate.services;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.gcteam.yandextranslate.domain.History;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by turist on 10.04.2017.
 */

public class HistoryService implements Consumer<History> {

    public static HistoryService get() {
        return new HistoryService();
    }

    public void addNew(History history) {
        History old = new Select().from(History.class)
                .where("source_text = ?", history.sourceText)
                .and("translation = ?", history.translation)
                .and("direction = ?", history.direction)
                .executeSingle();

        if(old == null) {
            history.save();
        }
    }

    public void save(History history) {
        History old = new Select().from(History.class)
                .where("source_text = ?", history.sourceText)
                .and("translation = ?", history.translation)
                .and("direction = ?", history.direction)
                .executeSingle();

        if(old == null) {
            history.save();
        } else {
            old.isBookmark = history.isBookmark;
            old.save();
        }
    }


    public List<History> all() {
        return new Select().from(History.class).execute();
    }

    public List<History> all(boolean onlyBookmark) {
        if(onlyBookmark) {
            return new Select().from(History.class).where("is_bookmark = ?", true).execute();
        }

        return all();
    }

    public List<History> get(String template, boolean onlyBookmark) {
        if(template.isEmpty()) {
            return all(onlyBookmark);
        }

        From from = new Select().from(History.class)
                .where("(source_text LIKE ?", "%"+template+"%")
                .or("translation LIKE ?)", "%"+template+"%");

        if(onlyBookmark) {
            return from.and("is_bookmark = ?", true).execute();
        }

        return from.execute();
    }


    @Override
    public void accept(History history) throws Exception {
        addNew(history);
    }
}

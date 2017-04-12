package com.gcteam.yandextranslate.services;

import com.gcteam.yandextranslate.domain.History;

import io.reactivex.functions.Consumer;

/**
 * Created by turist on 10.04.2017.
 */

public class HistorySaver implements Consumer<History> {

    public static final HistorySaver Instance = new HistorySaver();

    public void save(History history) {
      //  history.save();
    }

    @Override
    public void accept(History history) throws Exception {
        save(history);
    }
}

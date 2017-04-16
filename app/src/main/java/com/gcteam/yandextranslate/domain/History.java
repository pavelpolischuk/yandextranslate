package com.gcteam.yandextranslate.domain;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;

/**
 * Created by turist on 10.04.2017.
 */

@Table(name = "history")
public class History extends Model {

    public static final String SOURCE_FIELD = "source_text";
    public static final String TRANSLATION_FIELD = "translation";
    public static final String DIRECTION_FIELD = "direction";
    public static final String BOOKMARK_FIELD = "is_bookmark";
    public static final String LAST_TRANSLATED_FIELD = "last_translated";

    @Column(name = SOURCE_FIELD, index = true)
    public String sourceText;

    @Column(name = TRANSLATION_FIELD)
    public String translation;

    @Column(name = DIRECTION_FIELD)
    public String direction;

    @Column(name = BOOKMARK_FIELD)
    public boolean isBookmark;

    @Column(name = LAST_TRANSLATED_FIELD)
    public long timestamp;

    public History() {
        super();
    }

    public History(String sourceText, String translation, String direction, boolean isBookmark) {
        super();
        this.sourceText = sourceText;
        this.translation = translation;
        this.direction = direction;
        this.isBookmark = isBookmark;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
    }

    public static class SourceKey {

        private String sourceText;
        private String direction;

        public SourceKey(History history) {
            this.sourceText = history.sourceText;
            this.direction = history.direction;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof SourceKey && equals((SourceKey)obj);
        }

        public boolean equals(SourceKey obj) {
            return sourceText.equals(obj.sourceText)
                    && direction.equals(obj.direction);
        }
    }
}

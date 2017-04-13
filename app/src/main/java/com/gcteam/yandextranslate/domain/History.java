package com.gcteam.yandextranslate.domain;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by turist on 10.04.2017.
 */

@Table(name = "history")
public class History extends Model {

    @Column(name = "source_text", index = true)
    public String sourceText;

    @Column
    public String translation;

    @Column
    public String direction;

    @Column(name = "is_bookmark")
    public boolean isBookmark;

    public History() {
        super();
    }

    public History(String sourceText, String translation, String direction, boolean isBookmark) {
        super();
        this.sourceText = sourceText;
        this.translation = translation;
        this.direction = direction;
        this.isBookmark = isBookmark;
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

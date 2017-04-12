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

    @Column(name = "source_language")
    public Language sourceLanguage;

    @Column(name = "target_language")
    public Language targetLanguage;

    @Column(name = "is_bookmark")
    public boolean isBookmark;

    public History() {
        super();
    }

    public History(String sourceText, String translation, Direction direction, boolean isBookmark) {
        super();
        this.sourceText = sourceText;
        this.translation = translation;
        this.sourceLanguage = direction.from;
        this.targetLanguage = direction.to;
        this.isBookmark = isBookmark;
    }
}

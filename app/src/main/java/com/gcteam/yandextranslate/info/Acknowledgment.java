package com.gcteam.yandextranslate.info;

/**
 * Created by turist on 16.04.2017.
 */

class Acknowledgment {

    final private String title;
    final private String content;

    Acknowledgment(String title, String content) {
        this.title = title;
        this.content = content;
    }

    String getTitle() {
        return title;
    }

    String getContent() {
        return content;
    }
}

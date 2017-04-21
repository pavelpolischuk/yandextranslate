package com.gcteam.yandextranslate.api.dto;

import java.util.HashMap;

/**
 * Response content on getLangs request of {@link com.gcteam.yandextranslate.api.YandexTranslateApi}
 *
 * Created by turist on 07.04.2017.
 */
public class AvailableLanguages {

    @Deprecated
    public String[] dirs;

    /** Map language codes to title (sample: en->English) */
    public HashMap<String, String> langs;
}

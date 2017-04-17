package com.gcteam.yandextranslate;

import com.gcteam.yandextranslate.api.YandexService;
import com.gcteam.yandextranslate.api.dto.AvailableLanguages;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test that Yandex Service get correct response for getLangs request (contains English language).
 */
public class YandexServiceTests {
    @Test
    public void getLangs_return_containsEnglish() throws Exception {

        YandexService service = YandexService.get();

        AvailableLanguages languages = service.getLangs("en").blockingFirst();

        assertEquals(languages.langs.get("en"), "English");
    }
}
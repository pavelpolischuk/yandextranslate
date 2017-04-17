package com.gcteam.yandextranslate;


import com.gcteam.yandextranslate.api.dto.Translation;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by turist on 17.04.2017.
 */

public class TranslationTests {
    @Test
    public void emptyTranslation_isEmpty() throws Exception {
        assertTrue(Translation.EMPTY.isEmpty());
    }

    @Test
    public void notEmptyTranslation_isNotEmpty() throws Exception {
        Translation translation = new Translation("en", "Lorem Ipsum");
        translation.source = "Lorem Ipsum";
        assertFalse(translation.isEmpty());
    }

    @Test
    public void emptyTranslation_toString_returnEmptyString() throws Exception {
        assertEquals(Translation.EMPTY.toString(), "");
    }

    @Test
    public void arrayTranslation_toString_returnCorrectString() throws Exception {
        Translation translation = Translation.EMPTY;
        translation.text = (String[]) Arrays.asList("Lorem", "Ipsum").toArray();
        assertEquals(translation.toString(), "Lorem\nIpsum");
    }
}

package com.gcteam.yandextranslate;

import com.gcteam.yandextranslate.domain.Direction;
import com.gcteam.yandextranslate.domain.Language;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by turist on 17.04.2017.
 */

public class DirectionTests {

    private Direction direction;

    @Before
    public void setUp() throws Exception {
        direction = new Direction(new Language("en", "Eng"), new Language("ru", "Rus"));
    }

    @Test
    public void direction_field() throws Exception {
        assertEquals(direction.from.code, "en");
        assertEquals(direction.to.code, "ru");
    }

    @Test
    public void direction_toString() throws Exception {
        assertEquals(direction.toString(), "en-ru");
    }

    @Test
    public void direction_inverse() throws Exception {
        Direction inv = direction.inverse();
        assertEquals(inv.from.code, "ru");
        assertEquals(inv.to.code, "en");
    }

    @Test
    public void direction_changeFrom() throws Exception {
        Direction inv = direction.changeFrom(new Language("fr", "Fr"));
        assertEquals(inv.from.code, "fr");
        assertEquals(inv.to.code, "ru");
    }

    @Test
    public void direction_changeTo() throws Exception {
        Direction inv = direction.changeTo(new Language("fr", "Fr"));
        assertEquals(inv.to.code, "fr");
        assertEquals(inv.from.code, "en");
    }
}

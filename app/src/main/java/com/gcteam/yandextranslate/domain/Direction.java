package com.gcteam.yandextranslate.domain;

/**
 * Created by turist on 07.04.2017.
 */

public class Direction {

    final public Language to;

    final public Language from;

    public Direction(Language from, Language to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return this.from.code + "-" + this.to.code;
    }

    public Direction inverse() {
        return new Direction(to, from);
    }

    public Direction changeFrom(Language from) {
        return new Direction(from, this.to);
    }

    public Direction changeTo(Language to) {
        return new Direction(this.from, to);
    }
}

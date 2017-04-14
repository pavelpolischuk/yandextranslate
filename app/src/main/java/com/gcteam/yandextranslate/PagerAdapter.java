package com.gcteam.yandextranslate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gcteam.yandextranslate.bookmarks.BookmarksFragment;
import com.gcteam.yandextranslate.info.InfoFragment;
import com.gcteam.yandextranslate.translate.TranslateRootFragment;

/**
 * Created by turist on 05.04.2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final static int PAGE_COUNT = 3;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TranslateRootFragment();
            case 1:
                return new BookmarksFragment();
            case 2:
                return new InfoFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

package com.gcteam.yandextranslate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
            ViewPager.OnPageChangeListener {

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.navigation) BottomNavigationView bottomNavigation;

    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActiveAndroid.initialize(this);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_translate:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_bookmarks:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_info:
                viewPager.setCurrentItem(2);
                return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                bottomNavigation.setSelectedItemId(R.id.navigation_translate);
                break;
            case 1:
                bottomNavigation.setSelectedItemId(R.id.navigation_bookmarks);
                break;
            case 2:
                bottomNavigation.setSelectedItemId(R.id.navigation_info);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
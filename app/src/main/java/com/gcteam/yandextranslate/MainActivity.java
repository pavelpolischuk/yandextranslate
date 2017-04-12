package com.gcteam.yandextranslate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.gcteam.yandextranslate.api.YandexTranslateApi;
import com.gcteam.yandextranslate.api.YandexTranslateApiProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
            ViewPager.OnPageChangeListener, IYandexTranslateApiProvider {

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigation;

    PagerAdapter pagerAdapter;

    YandexTranslateApi translateApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActiveAndroid.initialize(this);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);

        translateApi = YandexTranslateApiProvider.get(this).api();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_dashboard:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_notifications:
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
                bottomNavigation.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                bottomNavigation.setSelectedItemId(R.id.navigation_dashboard);
                break;
            case 2:
                bottomNavigation.setSelectedItemId(R.id.navigation_notifications);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public YandexTranslateApi api() {
        return translateApi;
    }
}

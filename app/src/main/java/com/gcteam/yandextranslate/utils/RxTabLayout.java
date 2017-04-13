package com.gcteam.yandextranslate.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by turist on 13.04.2017.
 */

public class RxTabLayout {

    public static Observable<Integer> selectedChanges(TabLayout tabLayout) {
        return new RxTabSelectedObservable(tabLayout);
    }

    private static class RxTabSelectedObservable extends Observable<Integer>
            implements Disposable, TabLayout.OnTabSelectedListener {

        TabLayout tabLayout;
        Observer<? super Integer> observer;

        RxTabSelectedObservable(@NonNull TabLayout tabLayout) {
            this.tabLayout = tabLayout;
            this.tabLayout.addOnTabSelectedListener(this);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (observer != null) {
                observer.onNext(tab.getPosition());
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }

        @Override
        protected void subscribeActual(Observer<? super Integer> observer) {
            this.observer = observer;
            this.observer.onSubscribe(this);
            this.observer.onNext(this.tabLayout.getSelectedTabPosition());
        }

        @Override
        public void dispose() {
            if(tabLayout != null) {
                tabLayout.removeOnTabSelectedListener(this);
                tabLayout = null;
            }

            if(observer != null) {
                observer = null;
            }
        }

        @Override
        public boolean isDisposed() {
            return tabLayout == null;
        }
    }
}
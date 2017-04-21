package com.gcteam.yandextranslate.utils;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory method for creating {@linkplain Observable observable} for {@link TabLayout}.
 *
 * Created by turist on 13.04.2017.
 */
public class RxTabLayout {

    /**
     * Create an observable which emits the selected tab position on {@code tabLayout}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code tabLayout}.
     * Unsubscribe to free this reference.
     */
    @CheckResult @NonNull
    public static Observable<Integer> selectedChanges(@NonNull TabLayout tabLayout) {
        checkNotNull(tabLayout, "view == null");
        return new RxTabSelectedObservable(tabLayout);
    }

    private static class RxTabSelectedObservable extends InitialValueObservable<Integer> {

        private final TabLayout tabLayout;

        RxTabSelectedObservable(@NonNull TabLayout tabLayout) {
            this.tabLayout = tabLayout;
        }

        @Override
        protected void subscribeListener(Observer<? super Integer> observer) {
            Listener listener = new Listener(tabLayout, observer);
            observer.onSubscribe(listener);
            tabLayout.addOnTabSelectedListener(listener);
        }

        @Override
        protected Integer getInitialValue() {
            return tabLayout.getSelectedTabPosition();
        }
    }

    final static class Listener extends MainThreadDisposable implements TabLayout.OnTabSelectedListener {

        private final TabLayout tabLayout;
        private final Observer<? super Integer> observer;

        Listener(TabLayout tabLayout, Observer<? super Integer> observer) {
            this.tabLayout = tabLayout;
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            tabLayout.removeOnTabSelectedListener(this);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (!isDisposed()) {
                observer.onNext(tab.getPosition());
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }
}
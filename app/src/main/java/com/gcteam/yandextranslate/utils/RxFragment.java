package com.gcteam.yandextranslate.utils;

import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by turist on 05.04.2017.
 */

public class RxFragment extends Fragment {

    private CompositeDisposable disposables = new CompositeDisposable();

    protected void save(Disposable subscription) {
        this.disposables.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(!disposables.isDisposed()) {
            this.disposables.dispose();
        }
    }
}
package com.gcteam.yandextranslate.utils;

import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by turist on 05.04.2017.
 */

public class RxKnifeFragment extends Fragment {

    private CompositeDisposable disposables = new CompositeDisposable();
    private Unbinder unbinder;

    protected void save(Disposable subscription) {
        this.disposables.add(subscription);
    }

    protected View bind(View view) {
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(!disposables.isDisposed()) {
            this.disposables.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
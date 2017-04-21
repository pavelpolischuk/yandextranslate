package com.gcteam.yandextranslate.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base fragment, containing utility functional for work with {@link io.reactivex} and {@link ButterKnife} frameworks
 *
 * Save subscriptions on {@link io.reactivex.Observable} and unsubscribe its when fragment destroyed
 * Bind views on ids (set with annotation), save bindings and unbind its when view destroyed
 *
 * Created by turist on 05.04.2017.
 */
public class RxKnifeFragment extends Fragment {

    private CompositeDisposable disposables = new CompositeDisposable();
    private Unbinder unbinder;

    /**
     * Any subscription must be saved with this method
     */
    protected void save(Disposable subscription) {
        this.disposables.add(subscription);
    }

    /**
     * Need call from {{@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}}
     */
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
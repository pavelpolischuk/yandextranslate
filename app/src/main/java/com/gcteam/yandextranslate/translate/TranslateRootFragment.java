package com.gcteam.yandextranslate.translate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.services.LanguagesService;
import com.gcteam.yandextranslate.utils.RxKnifeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turist on 12.04.2017.
 */

public class TranslateRootFragment extends RxKnifeFragment
        implements Observer<LanguagesService> {

    @BindView(R.id.progress) ProgressBar progress;
    @BindViews({ R.id.placeholder, R.id.reconnect}) List<View> when_error;

    @OnClick(R.id.reconnect)
    void reconnect() {
        LanguagesService.get(getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this);
        showProgress();
    }

    static final ButterKnife.Setter<View, Integer> VISIBILITY = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            view.setVisibility(value);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = bind(inflater.inflate(R.layout.fragment_translate_root, container, false));

        ButterKnife.apply(when_error, VISIBILITY, View.GONE);
        progress.setVisibility(View.GONE);

        reconnect();

        return view;
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        ButterKnife.apply(when_error, VISIBILITY, View.GONE);
    }

    private void showMessage() {
        progress.setVisibility(View.GONE);
        ButterKnife.apply(when_error, VISIBILITY, View.VISIBLE);
    }

    private void showFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_frame, new TranslateFragment());
        transaction.commit();
    }

    @Override
    public void onSubscribe(Disposable d) {
        save(d);
    }

    @Override
    public void onNext(LanguagesService value) {
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TranslateRootFragment.class.getSimpleName(), "error connect to api", e);
        showMessage();
    }

    @Override
    public void onComplete() {
        showFragment();
    }
}

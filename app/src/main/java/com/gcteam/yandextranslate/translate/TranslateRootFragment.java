package com.gcteam.yandextranslate.translate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.services.LanguagesService;
import com.gcteam.yandextranslate.utils.RxKnifeFragment;

import butterknife.BindView;
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

    @BindView(R.id.placeholder) TextView placeholder;
    @BindView(R.id.progress) ProgressBar progress;

    @OnClick(R.id.placeholder)
    void reconnect() {
        LanguagesService.get(getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this);
        showProgress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = bind(inflater.inflate(R.layout.fragment_translate_root, container, false));

        reconnect();

        return view;
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        placeholder.setVisibility(View.GONE);
    }

    private void showMessage() {
        progress.setVisibility(View.GONE);
        placeholder.setVisibility(View.VISIBLE);
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

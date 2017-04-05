package com.gcteam.yandextranslate.translate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.utils.RxFragment;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turist on 05.04.2017.
 */

public class TranslateFragment extends RxFragment {

    @BindView(R.id.source_language)
    AppCompatButton source_language;

    @OnClick(R.id.swap_languages)
    void swapLanguages() {
        CharSequence t = source_language.getText();
        source_language.setText(target_language.getText());
        target_language.setText(t);
    }

    @OnClick(R.id.clear_source)
    void clearSource() {
        source_text.setText("");
    }

    @BindView(R.id.target_language)
    AppCompatButton target_language;

    @BindView(R.id.source_text)
    EditText source_text;

    @BindView(R.id.translation)
    TextView translation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        ButterKnife.bind(this, view);

        source_language.setText("ru");
        target_language.setText("en");

        save(RxTextView.textChanges(source_text)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        translation.setText(charSequence);
                    }
                }));

        return view;
    }
}

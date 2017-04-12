package com.gcteam.yandextranslate.translate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gcteam.yandextranslate.IYandexTranslateApiProvider;
import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.api.YandexTranslateApi;
import com.gcteam.yandextranslate.api.YandexTranslateApiProvider;
import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.Direction;
import com.gcteam.yandextranslate.domain.History;
import com.gcteam.yandextranslate.domain.Language;
import com.gcteam.yandextranslate.services.HistorySaver;
import com.gcteam.yandextranslate.services.LanguagesService;
import com.gcteam.yandextranslate.services.TranslateService;
import com.gcteam.yandextranslate.utils.RxCharsNotEmpty;
import com.gcteam.yandextranslate.utils.RxFragment;
import com.gcteam.yandextranslate.utils.TranslatePreference;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by turist on 05.04.2017.
 */

public class TranslateFragment extends RxFragment
        implements Consumer<Direction>, TranslateService.TranslationProvider,
            SelectLanguageDialog.Callback, Callback<Translation> {

    private static final int SOURCE_CODE = 0;
    private static final int TARGET_CODE = 1;

    @BindView(R.id.source_language)
    AppCompatButton source_language;

    @OnClick(R.id.source_language)
    void selectSource() {
        SelectLanguageDialog
                .create(languagesService.sortedLanguages(), this, SOURCE_CODE)
                .show(getFragmentManager(), SelectLanguageDialog.class.getSimpleName());
    }

    @OnClick(R.id.swap_languages)
    void swapLanguages() {
        if(currentDirection == null) {
            return;
        }

        set(currentDirection.inverse());

        String newSource = translation_text.getText().toString();
        source_text.setText(newSource);
    }

    @BindView(R.id.target_language)
    AppCompatButton target_language;

    @OnClick(R.id.target_language)
    void selectTarget() {
        SelectLanguageDialog
                .create(languagesService.sortedLanguages(), this, TARGET_CODE)
                .show(getFragmentManager(), SelectLanguageDialog.class.getSimpleName());
    }

    @OnClick(R.id.clear_source)
    void clearSource() {
        translateService.dispose();
        source_text.setText("");
    }

    @OnClick(R.id.add_bookmark)
    void bookmarkAdd() {
        String source = source_text.getText().toString();
        String translation = translation_text.getText().toString();

        if(source.isEmpty() || translation.isEmpty()) {
            showMessage(R.string.nothing_to_save);
            return;
        }

        HistorySaver.Instance.save(new History(source, translation, currentDirection, true));
        showMessage(R.string.bookmark_added);
    }

    @BindView(R.id.clear_source)
    AppCompatImageButton clear_source;

    @BindView(R.id.source_text)
    EditText source_text;

    @BindView(R.id.translation)
    TextView translation_text;

    LanguagesService languagesService = new LanguagesService();
    TranslateService translateService = new TranslateService(this, this);
    Direction currentDirection;
    Subject<History> historySubject = PublishSubject.create();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        ButterKnife.bind(this, view);

        save(translateService);

        Observable<CharSequence> textChanges = RxTextView.textChanges(source_text)
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        save(textChanges
                .subscribe(translateService));

        save(textChanges
                .map(RxCharsNotEmpty.Instance)
                .distinctUntilChanged()
                .subscribe(RxView.visibility(clear_source)));

        save(historySubject
                .debounce(1600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(HistorySaver.Instance));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        save(getApi().getLangs(apiKey(), getString(R.string.ui_code))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(this.languagesService.acceptAndGiveDirection(TranslatePreference.get(getContext()).lastDirection()))
                .subscribe(this));
    }

    @Override
    public YandexTranslateApi getApi() {
        Context context = getContext();
        if(context instanceof IYandexTranslateApiProvider) {
            IYandexTranslateApiProvider provider = (IYandexTranslateApiProvider) context;
            return provider.api();
        }

        return YandexTranslateApiProvider.get(getContext()).api();
    }

    private void set(Direction direction) {
        currentDirection = direction;

        TranslatePreference.get(getContext()).save(direction);

        if(currentDirection != null) {
            if(currentDirection.from != null) {
                source_language.setText(currentDirection.from.name);
            }
            if(currentDirection.to != null) {
                target_language.setText(currentDirection.to.name);
            }
        }
    }

    @Override
    public void accept(Direction direction) throws Exception {
        set(direction);
    }

    @Override
    public void onSelect(Language language, int code) {
        switch (code) {
            case SOURCE_CODE:
                set(currentDirection.changeFrom(language));
                break;
            case TARGET_CODE:
                set(currentDirection.changeTo(language));
                break;
        }

        String text = source_text.getText().toString();
        if(!text.isEmpty()) {
            translateService.start(text);
        }
    }

    @Override
    public void onResponse(Call<Translation> call, Response<Translation> response) {
        if(!response.isSuccessful()) {
            showMessage(R.string.translate_error);
            return;
        }

        String[] strings = response.body().text;
        if(strings.length == 0) {
            translation_text.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder(strings[0]);
        for(int i = 1; i < strings.length; ++i) {
            sb.append('\n');
            sb.append(strings[i]);
        }

        String translation = sb.toString();
        String source = source_text.getText().toString();

        if(source.isEmpty()) {
            return;
        }

        translation_text.setText(translation);
        historySubject.onNext(new History(source, translation, currentDirection, false));
    }

    @Override
    public void onFailure(Call<Translation> call, Throwable t) {
        showMessage(R.string.translate_error);
    }

    private void showMessage(@StringRes int messageId) {
        Snackbar.make(translation_text, messageId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public String apiKey() {
        return getString(R.string.api_key);
    }

    @Override
    public Direction direction() {
        return currentDirection;
    }
}

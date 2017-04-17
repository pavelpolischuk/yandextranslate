package com.gcteam.yandextranslate.translate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.Direction;
import com.gcteam.yandextranslate.domain.History;
import com.gcteam.yandextranslate.domain.Language;
import com.gcteam.yandextranslate.services.HistoryService;
import com.gcteam.yandextranslate.services.LanguagesService;
import com.gcteam.yandextranslate.services.TranslateService;
import com.gcteam.yandextranslate.utils.RxKnifeFragment;
import com.gcteam.yandextranslate.utils.RxHelpers;
import com.gcteam.yandextranslate.utils.TranslatePreference;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turist on 05.04.2017.
 */

public class TranslateFragment extends RxKnifeFragment
        implements TranslateService.DirectionProvider, SelectLanguageDialog.Callback,
                Consumer<Throwable> {

    private static final int SOURCE_CODE = 0;
    private static final int TARGET_CODE = 1;

    @BindView(R.id.source_language) AppCompatButton source_language;
    @BindView(R.id.target_language) AppCompatButton target_language;
    @BindView(R.id.clear_source) CircleButton clear_source;
    @BindView(R.id.source_text) EditText source_text;
    @BindView(R.id.translation) TextView translation_text;


    @OnClick({ R.id.source_language, R.id.target_language })
    void selectLanguage(View view) {
        int code = view.getId() == R.id.source_language ? SOURCE_CODE : TARGET_CODE;
        SelectLanguageDialog.create(this, code)
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

    @OnClick(R.id.clear_source)
    void clearSource() {
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

        History history = new History(source, translation, currentDirection.toString(), true);
        HistoryService.get().addOrSave(history, true);
        showMessage(R.string.bookmark_added);
    }

    TranslateService translateService = new TranslateService(this);

    Direction currentDirection;
    Consumer<Direction> directionConsumer = new Consumer<Direction>() {
        @Override
        public void accept(Direction direction) throws Exception {
            set(direction);
        }
    };

    Consumer<Translation> translationConsumer = new Consumer<Translation>() {
        @Override
        public void accept(Translation translation) throws Exception {
            set(translation);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = bind(inflater.inflate(R.layout.fragment_translate, container, false));

        clear_source.setVisibility(View.GONE);

        save(LanguagesService.direction(TranslatePreference.get(getContext()).lastDirection())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(directionConsumer));

        Observable<CharSequence> textChanges = RxTextView.textChanges(source_text)
                .skipInitialValue();


        save(RxHelpers.subscribeViewVisibleOnNotEmptyText(textChanges, clear_source));

        Observable<Translation> translations = Observable.switchOnNextDelayError(
                textChanges
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.io())
                    .map(translateService.translate()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        save(translations
                .subscribe(translationConsumer, this));

        save(Observable.combineLatest(translations, textChanges, RxHelpers.SelectTranslation)
                .debounce(2, TimeUnit.SECONDS)
                .filter(RxHelpers.TranslationNotEmpty)
                .map(RxHelpers.TranslationToHistory)
                .distinct(RxHelpers.HistorySource)
                .subscribe(HistoryService.get(), this));

        return view;
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
            source_text.setText(text);
        }
    }

    @Override
    public Direction direction() {
        return currentDirection;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        Log.e(TranslateFragment.class.getSimpleName(), "translation consumer error", throwable);
        showMessage(R.string.translate_error);
    }


    private void set(Translation translation) {
        translation_text.setText(translation.toString());
    }

    private void set(@Nullable Direction direction) {
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

    private void showMessage(@StringRes int messageId) {
        Toast.makeText(getContext(), messageId, Toast.LENGTH_LONG).show();
    }
}
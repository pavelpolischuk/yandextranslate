package com.gcteam.yandextranslate.services;

import com.gcteam.yandextranslate.api.YandexService;
import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.Direction;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by turist on 09.04.2017.
 */

public class TranslateService {

    public interface DirectionProvider {
        Direction direction();
    }

    private DirectionProvider provider;

    public TranslateService(DirectionProvider provider) {
        this.provider = provider;
    }

    public Function<CharSequence, Observable<Translation>> translate() {
        return new Function<CharSequence, Observable<Translation>>() {
            @Override
            public Observable<Translation> apply(CharSequence charSequence) throws Exception {
                return translate(charSequence);
            }
        };
    }

    public Observable<Translation> translate(CharSequence charSequence) {
        if(charSequence.length() == 0) {
            return Observable.just(Translation.EMPTY);
        }

        final String source = String.valueOf(charSequence);
        return YandexService.get()
                .translate(
                        source,
                        provider.direction().toString())
                .doOnNext(new Consumer<Translation>() {
                    @Override
                    public void accept(Translation translation) throws Exception {
                        translation.source = source;
                    }
                });
    }
}
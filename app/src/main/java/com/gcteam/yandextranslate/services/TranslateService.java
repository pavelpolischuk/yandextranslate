package com.gcteam.yandextranslate.services;

import com.gcteam.yandextranslate.api.YandexService;
import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.Direction;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Service for text translating, use {@link YandexService}
 *
 * Created by turist on 09.04.2017.
 */
public class TranslateService {

    /**
     * Need to take actual language direction (everyone, who using {@link TranslateService}, must implement
     * this interface to Service know direction)
     */
    public interface DirectionProvider {
        Direction direction();
    }

    private DirectionProvider provider;

    public TranslateService(DirectionProvider provider) {
        this.provider = provider;
    }

    /**
     * @return mapping source text to Translate Observable (for {@link Observable#map(Function)})
     */
    public Function<CharSequence, Observable<Translation>> translate() {
        return new Function<CharSequence, Observable<Translation>>() {
            @Override
            public Observable<Translation> apply(CharSequence charSequence) throws Exception {
                return translate(charSequence);
            }
        };
    }

    /**
     * Translate text
     * @return Observable with translation result (Observable for async, because request to server)
     */
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
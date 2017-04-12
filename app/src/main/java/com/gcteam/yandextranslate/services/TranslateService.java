package com.gcteam.yandextranslate.services;

import com.gcteam.yandextranslate.api.YandexTranslateApi;
import com.gcteam.yandextranslate.api.dto.Translation;
import com.gcteam.yandextranslate.domain.Direction;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by turist on 09.04.2017.
 */

public class TranslateService implements Consumer<CharSequence>, Disposable {

    @Override
    public void dispose() {
        if(request != null) {
            if(!request.isCanceled() && !request.isExecuted()) {
                request.cancel();
            }

            request = null;
        }
    }

    @Override
    public boolean isDisposed() {
        return request == null || request.isCanceled() || request.isExecuted();
    }

    public interface TranslationProvider {
        String apiKey();
        YandexTranslateApi getApi();
        Direction direction();
    }

    private Call<Translation> request;
    private TranslationProvider provider;
    private Callback<Translation> callback;

    public TranslateService(TranslationProvider provider, Callback<Translation> callback) {
        this.provider = provider;
        this.callback = callback;
    }

    public void start(CharSequence charSequence) {
        dispose();

        if(charSequence.length() == 0) {
            callback.onResponse(null, Response.success(Translation.EMPTY));
            return;
        }

        Map<String, String> options = new HashMap<>();
        options.put("format", "html");

        request = provider.getApi()
                .translate(provider.apiKey(),
                        String.valueOf(charSequence),
                        provider.direction().toString(), options);

        request.enqueue(callback);
    }

    @Override
    public void accept(CharSequence charSequence) throws Exception {
        start(charSequence);
    }
}

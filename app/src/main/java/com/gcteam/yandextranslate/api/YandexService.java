package com.gcteam.yandextranslate.api;

import com.gcteam.yandextranslate.api.dto.AvailableLanguages;
import com.gcteam.yandextranslate.api.dto.Translation;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service for request Yandex.Translate api
 *
 * Using Retrofit2 create instance of interface {@link YandexTranslateApi} and call its methods
 * Each method return Observable<T> (need subscribe to get response object, when response come from server)
 *
 * Created by turist on 07.04.2017.
 */

public class YandexService {

    private static final String API_BASE_URL = "https://translate.yandex.net/api/";
    private static final String API_KEY = "trnsl.1.1.20170405T161139Z.bdeadca16f808a8d.ab838d768232c3737ff17ec095f314752bb02532";

    private static YandexService instance;

    private YandexTranslateApi api;

    public static YandexService get() {
        if(instance == null) {
            instance = new YandexService();
            instance.api = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(YandexTranslateApi.class);
        }

        return instance;
    }

    public Observable<Translation> translate(String text, String lang) {
        return api.translate(API_KEY, text, lang, new HashMap<String, String>());
    }

    public Observable<AvailableLanguages> getLangs(String uiLangCode) {
        return api.getLangs(API_KEY, uiLangCode);
    }
}

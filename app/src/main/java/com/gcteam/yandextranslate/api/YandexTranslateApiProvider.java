package com.gcteam.yandextranslate.api;

import android.content.Context;

import com.gcteam.yandextranslate.IYandexTranslateApiProvider;
import com.gcteam.yandextranslate.R;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by turist on 07.04.2017.
 */

public class YandexTranslateApiProvider implements IYandexTranslateApiProvider {

    private Context context;

    public static YandexTranslateApiProvider get(final Context context) {
        YandexTranslateApiProvider provider = new YandexTranslateApiProvider();
        provider.context = context;
        return provider;
    }

    public YandexTranslateApi api() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(YandexTranslateApi.class);
    }
}

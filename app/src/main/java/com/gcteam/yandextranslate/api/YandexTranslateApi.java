package com.gcteam.yandextranslate.api;

import com.gcteam.yandextranslate.api.dto.AvailableLanguages;
import com.gcteam.yandextranslate.api.dto.Detection;
import com.gcteam.yandextranslate.api.dto.Translation;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by turist on 07.04.2017.
 */

public interface YandexTranslateApi {

    @FormUrlEncoded
    @POST("v1.5/tr.json/translate")
    Observable<Translation> translate(@Query("key") String apiKey,
                                      @Field("text") String text,
                                      @Query("lang") String lang,
                                      @QueryMap Map<String, String> options);

    @FormUrlEncoded
    @GET("v1.5/tr.json/detect")
    Observable<Detection> detectLang(@Query("key") String apiKey,
                                     @Field("text") String text,
                                     @QueryMap Map<String, String> options);

    @GET("v1.5/tr.json/getLangs")
    Observable<AvailableLanguages> getLangs(@Query("key") String apiKey,
                                            @Query("ui") String uiLangCode);
}

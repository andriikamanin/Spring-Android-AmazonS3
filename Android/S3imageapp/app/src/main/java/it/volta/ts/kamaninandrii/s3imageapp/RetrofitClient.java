package it.volta.ts.kamaninandrii.s3imageapp;

import com.google.gson.GsonBuilder;

import it.volta.ts.kamaninandrii.s3imageapp.api.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://16.171.236.200:8090/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Настроим Gson с lenient режимом
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()  // Включаем lenient режим
                    .create()))
            .build();

    public static ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}

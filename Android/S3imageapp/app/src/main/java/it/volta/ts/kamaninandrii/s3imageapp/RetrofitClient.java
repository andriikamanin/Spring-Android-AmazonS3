package it.volta.ts.kamaninandrii.s3imageapp;
import it.volta.ts.kamaninandrii.s3imageapp.api.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://s3imaggeapp.projecthub.click:8090/";

    // Метод для создания логгера
    private static OkHttpClient getLoggingClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Полный лог
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getLoggingClient()) // Добавляем логгер
            .build();

    public static ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
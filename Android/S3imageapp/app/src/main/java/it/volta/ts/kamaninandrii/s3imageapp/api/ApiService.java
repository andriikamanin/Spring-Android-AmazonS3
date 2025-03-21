package it.volta.ts.kamaninandrii.s3imageapp.api;

import retrofit2.Call;
import retrofit2.http.GET;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("s3/random-file")
    Call<ResponseBody> getRandomFile();
}

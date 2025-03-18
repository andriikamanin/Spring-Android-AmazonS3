package it.volta.ts.kamaninandrii.s3imageapp.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("s3/random-file")
    Call<String> getRandomFileUrl();
}

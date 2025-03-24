package it.volta.ts.kamaninandrii.s3imageapp.api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @GET("s3/random-file")
    Call<ResponseBody> getRandomFile();

    @Multipart
    @POST("s3/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
}

package it.volta.ts.kamaninandrii.s3imageapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        updateButton = findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomImage();
            }
        });

        loadRandomImage(); // Загружаем картинку при запуске
    }

    private void loadRandomImage() {
        RetrofitClient.getApiService().getRandomFile().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Получаем поток байтов
                    InputStream inputStream = response.body().byteStream();
                    // Декодируем в Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    // Устанавливаем картинку в ImageView
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
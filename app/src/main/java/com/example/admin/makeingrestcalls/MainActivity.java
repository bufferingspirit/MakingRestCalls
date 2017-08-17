package com.example.admin.makeingrestcalls;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.makeingrestcalls.model.WeatherData;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://samples.openweathermap.org/data/2.5/forecast?zip=94040&appid=b1b15e88fa797225412429c1c50c122a1";
    private static final String TAG = "MainActivity" ;

    WeatherData weatherData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    String resultResponse = "";
    public void makingRestCalls(View view) throws IOException {



        Intent intentNative = new Intent(this, HttpIntentService.class);
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL)
                .build();
        switch(view.getId()){

            case R.id.btnNative:
                startService(intentNative);
                break;
            case R.id.btnOkHttp:

                //make synchronous calls using okhttp
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = client.newCall(request).execute().body().string();
                            Log.d(TAG, "run: " + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, "Check your logs", Toast.LENGTH_SHORT).show();

                break;

            //make async calls using okhttp
            case R.id.btnHttpAsync:

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        resultResponse = response.body().string();

                        Gson gson = new Gson();
                        weatherData = gson.fromJson(resultResponse, WeatherData.class);

                        Log.d(TAG, "onResponse: "  + weatherData.getCity().getName());

                    }
                });

                break;

            case R.id.btnRetrofit:

                break;
        }
    }

    //http://www.mocky.io/v2/599495951100009403723127
}

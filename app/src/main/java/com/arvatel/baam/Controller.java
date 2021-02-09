package com.arvatel.baam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Controller {

    private static final String BASE_URL = "https://baam.duckdns.org";
    int CODE_REDIRECT = 300;
    int CODE_OK = 200;
    int CODE_ERROR = 400;

    private BaamAPI baamAPI;
    private Retrofit retrofit;
    private OkHttpClient okClient;
    private HttpLoggingInterceptor logging;
    private Gson gson;

    Controller(){
        gson = new GsonBuilder()
                .setLenient()
                .create();

        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        baamAPI = retrofit.create(BaamAPI.class);
    }

    public int submitChallenge(String cookie, String session, String secretCode) {
        Call<Void> call = baamAPI.submitChallenge(cookie, session, secretCode);
        int[] code = new int[1];

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 300 && response.code() < 400) {
                    code[0] = CODE_REDIRECT;
                    System.out.println("Redirect");
                }
                if (response.isSuccessful()) {
                    code[0] = CODE_OK;

                    System.out.println("Success");
                } else {
                    code[0] = CODE_ERROR;
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                code[0] = CODE_ERROR;
                t.printStackTrace();
            }

        });
        return code[0];
    }

}

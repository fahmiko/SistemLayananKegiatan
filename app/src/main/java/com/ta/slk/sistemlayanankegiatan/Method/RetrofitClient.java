package com.ta.slk.sistemlayanankegiatan.Method;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

        public static String BASE_URL = "http://192.168.43.102/android_api/";
        private static Retrofit retrofit = null;

        public static Retrofit getRetroftInstance() {
            if (retrofit == null) {

                OkHttpClient cl = new OkHttpClient().newBuilder().addInterceptor(new Authentication()).build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        // .client(client)
                        .build();
            }

            return retrofit;
        }
}
